package com.blws.domain.common.auth.controller;

import com.blws.domain.common.auth.vo.AuthFailureGetRequestVO;
import com.blws.domain.common.auth.vo.AuthRefreshRequestVO;
import com.blws.domain.common.auth.vo.AuthSuccessGetResponseVO;
import com.blws.global.jwt.JWTTokenProvider;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/common/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final JWTTokenProvider jWTTokenProvider;

  @PostMapping("/signin")
  public AuthSuccessGetResponseVO authorize(@Valid @RequestBody AuthFailureGetRequestVO loginVM,
      HttpServletResponse httpServletResponse) {
    return jWTTokenProvider.signIn(loginVM, httpServletResponse);
  }

  @GetMapping("/signout")
  public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) throws IOException {
    jWTTokenProvider.signOut(httpServletRequest, httpServletResponse);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  public AuthSuccessGetResponseVO refresh(@Valid @RequestBody AuthRefreshRequestVO refreshToken,
      HttpServletResponse httpServletResponse)
      throws UnsupportedEncodingException {
    return jWTTokenProvider.refresh(refreshToken.getRefreshTkn(), httpServletResponse);
  }

}
