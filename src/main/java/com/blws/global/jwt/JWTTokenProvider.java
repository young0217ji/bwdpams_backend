package com.blws.global.jwt;

import com.blws.domain.common.auth.vo.AuthFailureGetRequestVO;
import com.blws.domain.common.auth.vo.AuthSuccessGetResponseVO;
import com.blws.domain.common.user.dao.UserDAO;
import com.blws.domain.common.user.entity.UserEntity;
import com.blws.domain.common.user.service.UserService;
import com.blws.domain.common.user.vo.UserInfoGetRequestVO;
import com.blws.global.jwt.management.SecurityMetersService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JWTTokenProvider {

  private static final String AUTHORITIES_KEY = "auth";

  private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";

  public static final String AUTHORIZATION_ACCESS_HEADER = "authorization";

  public static final String AUTHORIZATION_ACCESS_TOKEN = "accesTkn";

  public static final String AUTHORIZATION_REFRESH_TOKEN = "refreshTkn";


  private final Key key;

  private final JwtParser jwtParser;

  private final long tokenAccessValidityInSeconds;
  private final long tokenRefreshValidityInSeconds;
  private final int tokenAccessCookieMaxAge;
  private final int tokenRefreshCookieMaxAge;

  private final SecurityMetersService securityMetersService;

  private final UserService userService;
  private final UserDAO userDAO;

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  public JWTTokenProvider(@Value("${spring.jwt.secret}") String secret,
      @Value("${spring.jwt.token-access-validity-in-seconds}") long tokenAccessValidityInSeconds,
      @Value("${spring.jwt.token-refresh-validity-in-seconds}") long tokenRefreshValidityInSeconds,
      SecurityMetersService securityMetersService, UserService userService, UserDAO userDAO,
      AuthenticationManagerBuilder authenticationManagerBuilder) {
    this.userService = userService;
    this.userDAO = userDAO;
    this.securityMetersService = securityMetersService;

    this.authenticationManagerBuilder = authenticationManagerBuilder;

    byte[] keyBytes = Decoders.BASE64.decode(secret);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

    this.tokenAccessValidityInSeconds = 1000 * tokenAccessValidityInSeconds;
    this.tokenRefreshValidityInSeconds = 1000 * tokenRefreshValidityInSeconds;

    this.tokenAccessCookieMaxAge = (int) tokenAccessValidityInSeconds;
    this.tokenRefreshCookieMaxAge = (int) tokenRefreshValidityInSeconds;
  }

  public String createaccesTkn(Authentication authentication) {

    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

    long now = (new Date()).getTime();
    Date validity = new Date(now + this.tokenAccessValidityInSeconds);

    return Jwts
        .builder()
        .setId(authentication.getName())
        .setSubject(authentication.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .signWith(key, SignatureAlgorithm.HS512)
        .setExpiration(validity)
        .compact();
  }

  public String createrefreshTkn(Authentication authentication) {

    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

    long now = (new Date()).getTime();
    Date validity = new Date(now + this.tokenRefreshValidityInSeconds);

    return Jwts
        .builder()
        .setId(authentication.getName())
        .setSubject(authentication.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .signWith(key, SignatureAlgorithm.HS512)
        .setExpiration(validity)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = jwtParser.parseClaimsJws(token).getBody();

    Collection<? extends GrantedAuthority> authorities = Arrays
        .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
        .filter(auth -> !auth.trim().isEmpty())
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    User principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  public boolean validateToken(String authToken) {
    try {

      jwtParser.parseClaimsJws(authToken);

      return true;
    }
//        catch (ExpiredJwtException e) {
//            this.securityMetersService.trackTokenExpired();
//
//            log.trace(INVALID_JWT_TOKEN, e);
//        }
    catch (UnsupportedJwtException e) {
      this.securityMetersService.trackTokenUnsupported();

      log.trace(INVALID_JWT_TOKEN, e);
    } catch (MalformedJwtException e) {
      this.securityMetersService.trackTokenMalformed();

      log.trace(INVALID_JWT_TOKEN, e);
    } catch (SignatureException e) {
      this.securityMetersService.trackTokenInvalidSignature();

      log.trace(INVALID_JWT_TOKEN, e);
    } catch (IllegalArgumentException e) { // TODO: should we let it bubble (no catch), to avoid defensive programming and follow the fail-fast principle?
      log.error("Token validation error {}", e.getMessage());
    }

    return false;
  }

  public String resolveaccesTkn(HttpServletRequest request) {

    String bearerToken = request.getHeader(AUTHORIZATION_ACCESS_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    String jwt = request.getParameter(AUTHORIZATION_ACCESS_TOKEN);
    if (StringUtils.hasText(jwt)) {
      return jwt;
    }

    return Arrays.stream(request.getCookies())
        .filter(cookie -> this.AUTHORIZATION_ACCESS_TOKEN.equals(cookie.getName()))
        .findFirst()
        .orElse(null)
        .getValue();
  }


  public String resolverefreshTkn(HttpServletRequest request) {
    return Arrays.stream(request.getCookies())
        .filter(cookie -> this.AUTHORIZATION_REFRESH_TOKEN.equals(cookie.getName()))
        .findFirst()
        .orElse(null)
        .getValue();
  }


  public Boolean checkaccesTkn(ServletRequest servletRequest, ServletResponse servletResponse) {

    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String accesTkn = this.resolveaccesTkn(httpServletRequest);
    String refreshTkn = this.resolverefreshTkn(httpServletRequest);

    String userId = jwtParser.parseClaimsJws(accesTkn).getBody().getId();

    UserEntity userEntity = userDAO.selectUserByUserId(UserInfoGetRequestVO.builder()
        .userId(userId)
        .build().toEntity());

    if (!(userEntity.getAccesTkn().equals(accesTkn) && userEntity.getRefreshTkn()
        .equals(refreshTkn))) {
      return false;
    }

    if (StringUtils.hasText(accesTkn) && this.validateToken(accesTkn)
        && StringUtils.hasText(refreshTkn) && this.validateToken(refreshTkn)) {

      Date expirationDate = jwtParser.parseClaimsJws(accesTkn).getBody().getExpiration();

      if (expirationDate.before(new Date())) {
        this.setCookieAndSetToken(this.getAuthentication(accesTkn),
            (HttpServletResponse) servletResponse);
      } else {
        Authentication authentication = this.getAuthentication(accesTkn);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      return true;

    }

    return false;
  }

  public Boolean checkrefreshTkn(ServletRequest servletRequest, ServletResponse servletResponse) {

    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String accesTkn = this.resolveaccesTkn(httpServletRequest);
    String refreshTkn = this.resolverefreshTkn(httpServletRequest);

    String userId = jwtParser.parseClaimsJws(accesTkn).getBody().getId();

    UserEntity userEntity = userDAO.selectUserByUserId(UserInfoGetRequestVO.builder()
        .userId(userId)
        .build().toEntity());

    if (!(userEntity.getAccesTkn().equals(accesTkn) && userEntity.getRefreshTkn()
        .equals(refreshTkn))) {
      return false;
    }

    if (StringUtils.hasText(accesTkn) && this.validateToken(accesTkn)
        && StringUtils.hasText(refreshTkn) && this.validateToken(refreshTkn)) {

      Date expirationDate = jwtParser.parseClaimsJws(refreshTkn).getBody().getExpiration();

      if (expirationDate.before(new Date())) {
        this.setCookieAndSetToken(this.getAuthentication(accesTkn),
            (HttpServletResponse) servletResponse);
      } else {
        Authentication authentication = this.getAuthentication(accesTkn);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      return true;

    }

    return false;
  }

  public AuthSuccessGetResponseVO signIn(AuthFailureGetRequestVO loginVM,
      HttpServletResponse httpServletResponse) {

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginVM.getUserId(),
        loginVM.getPwd()
    );

    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserEntity userEntity = this.setCookieAndSetToken(authentication, httpServletResponse);

    return AuthSuccessGetResponseVO.of(userEntity, this.tokenAccessValidityInSeconds);

  }

  public void signOut(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) throws IOException {

    this.removeJwtCookies(httpServletResponse);

    try {
      String accesTkn = this.resolveaccesTkn(httpServletRequest);
      String refreshTkn = this.resolveaccesTkn(httpServletRequest);
      String userId = "";

      if (StringUtils.hasText(accesTkn) || StringUtils.hasText(refreshTkn)) {
        userId = jwtParser.parseClaimsJws(
            StringUtils.hasText(accesTkn) ? accesTkn : refreshTkn).getBody().getId();
      }

      Authentication authentication = this.getAuthentication(accesTkn);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      userService.updateUserToken(UserEntity.builder()
          .userId(userId)
          .accesTkn(null)
          .refreshTkn(null)
          .build());

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public AuthSuccessGetResponseVO refresh(String token,
      HttpServletResponse httpServletResponse) throws UnsupportedEncodingException {

    String userId = jwtParser.parseClaimsJws(token).getBody().getSubject();
    UserEntity user = userDAO.selectUserByUserId(UserEntity.builder().userId(userId).build());

    if (user == null) {
      throw new UsernameNotFoundException(userId);
    }

    if (!token.equals(user.getRefreshTkn())) {
      throw new UsernameNotFoundException(userId);
    }

    String authorities = user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

    long now = (new Date()).getTime();
    String accesTkn = Jwts
        .builder()
        .setId(user.getUserId())
        .setSubject(user.getUserId())
        .claim(AUTHORITIES_KEY, authorities)
        .signWith(key, SignatureAlgorithm.HS512)
        .setExpiration(new Date(now + this.tokenAccessValidityInSeconds))
        .compact();

    String refreshTkn = Jwts
        .builder()
        .setId(user.getUserId())
        .setSubject(user.getUserId())
        .claim(AUTHORITIES_KEY, authorities)
        .signWith(key, SignatureAlgorithm.HS512)
        .setExpiration(new Date(now + this.tokenRefreshValidityInSeconds))
        .compact();

    userService.updateUserToken(UserEntity.builder()
        .userId(user.getUserId())
        .accesTkn(accesTkn)
        .refreshTkn(refreshTkn)
        .build());

    UserEntity userEntity = UserEntity.builder()
        .accesTkn(accesTkn)
        .refreshTkn(refreshTkn)
        .userId(user.getUserId())
        .name(user.getName())
        .eml(user.getEml())
        .factoryId(user.getFactoryId())
        .phoneNo(user.getPhoneNo())
        .roleId(user.getRoleId())
        .build();

    setJwtCookies(accesTkn, refreshTkn, httpServletResponse);

    return AuthSuccessGetResponseVO.of(userEntity, this.tokenAccessValidityInSeconds, true);

  }

  private UserEntity setCookieAndSetToken(Authentication authentication,
      HttpServletResponse httpServletResponse) {

    try {
      String accesTkn = this.createaccesTkn(authentication);
      String refreshTkn = this.createrefreshTkn(authentication);

      this.setJwtCookies(accesTkn, refreshTkn, httpServletResponse);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserEntity authenticationUser = (UserEntity) authentication.getPrincipal();

      userService.updateUserToken(UserEntity.builder()
          .userId(authenticationUser.getUserId())
          .accesTkn(accesTkn)
          .refreshTkn(refreshTkn)
          .build());

      return UserEntity.builder()
          .accesTkn(accesTkn)
          .refreshTkn(refreshTkn)
          .userId(authenticationUser.getUserId())
          .name(authenticationUser.getName())
          .eml(authenticationUser.getEml())
          .factoryId(authenticationUser.getFactoryId())
          .phoneNo(authenticationUser.getPhoneNo())
          .roleId(authenticationUser.getRoleId())
          .build();

    } catch (Exception e) {
      throw new SignatureException(e.getMessage());
    }

  }

  private void setJwtCookies(String accesTkn, String refreshTkn,
      HttpServletResponse httpServletResponse) throws UnsupportedEncodingException {

    httpServletResponse.addHeader(this.AUTHORIZATION_ACCESS_HEADER, "Bearer " + accesTkn);

    Cookie accesTknCookie = new Cookie(AUTHORIZATION_ACCESS_TOKEN,
        URLEncoder.encode(accesTkn, "UTF-8"));
    accesTknCookie.setPath("/");
    accesTknCookie.setMaxAge(this.tokenAccessCookieMaxAge);

    ResponseCookie refreshTknCookie = ResponseCookie.from(AUTHORIZATION_REFRESH_TOKEN,
            URLEncoder.encode(refreshTkn, "UTF-8"))
        .path("/")
//                .sameSite("None")
        .httpOnly(false)
        .secure(false)
        .maxAge(this.tokenAccessCookieMaxAge)
        .build();

    httpServletResponse.addHeader("Set-Cookie", refreshTknCookie.toString());
    httpServletResponse.addCookie(accesTknCookie);

  }

  private void removeJwtCookies(HttpServletResponse httpServletResponse) {
    try {
      httpServletResponse.addHeader(this.AUTHORIZATION_ACCESS_HEADER, null);

      Cookie refreshTknCookie = new Cookie(AUTHORIZATION_ACCESS_TOKEN, null);
      refreshTknCookie.setPath("/");
      refreshTknCookie.setMaxAge(0);

      Cookie accesTknCookie = new Cookie(AUTHORIZATION_REFRESH_TOKEN, null);
      accesTknCookie.setPath("/");
      accesTknCookie.setMaxAge(0);

      httpServletResponse.addCookie(refreshTknCookie);
      httpServletResponse.addCookie(accesTknCookie);
    } catch (Exception e) {
      throw new SignatureException(e.getMessage());
    }

  }
}