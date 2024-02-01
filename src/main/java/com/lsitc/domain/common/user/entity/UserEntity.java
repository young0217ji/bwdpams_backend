package com.lsitc.domain.common.user.entity;

import com.lsitc.global.common.BaseAbstractEntity;
import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseAbstractEntity
    implements UserDetails {

  private static final long serialVersionUID = 7428290075155619330L;

  private String userId;
  private String password;
  private String name;
  private String email;
  private String phoneNumber;
  private String accessToken;
  private String refreshToken;
  private String roleId;
  private String plantId;

  @Builder
  private UserEntity(String userId, String password, String name, String email,
      String phoneNumber, String accessToken, String refreshToken, String roleId, String plantId) {
    this.userId = userId;
    this.password = password;
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.roleId = roleId;
    this.plantId = plantId;
  }

  // Guest 사용 로직 사용불가
  public static UserEntity AnonymousUser() {
    return builder().userId("anonymous").name("anonymous").build();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    String roleId = this.roleId == null ? "" : this.roleId.toString();

    // FIXME 권한 관련 로직이 들어가는 곳
    return Collections.singletonList(new SimpleGrantedAuthority(roleId));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.userId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !isDeleted();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
