package com.lsitc.domain.common.auth.vo;

import com.lsitc.domain.common.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public class AuthSuccessGetResponseVO {

  private final String userId;
  private final String name;
  private final String email;
  private final String phoneNumber;
  private final String plantId;
  private final String accessToken;
  private final String refreshToken;
  private final boolean isRefreshing;
  private final long maxAge;

  @Builder
  private AuthSuccessGetResponseVO(String userId, String name, String email,
      String phoneNumber, String plantId, String accessToken, String refreshToken,
      boolean isRefreshing, long maxAge) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.plantId = plantId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.isRefreshing = isRefreshing;
    this.maxAge = maxAge;
  }

  public static AuthSuccessGetResponseVO of(UserEntity userEntity) {
    return builder()
        .userId(userEntity.getUserId())
        .name(userEntity.getName())
        .email(userEntity.getEmail())
        .phoneNumber(userEntity.getPhoneNumber())
        .plantId(userEntity.getPlantId())
        .accessToken(userEntity.getAccessToken())
        .refreshToken(userEntity.getRefreshToken())
        .isRefreshing(false)
        .build();
  }

  public static AuthSuccessGetResponseVO of(UserEntity userEntity, long maxAge) {
    return builder()
        .userId(userEntity.getUserId())
        .name(userEntity.getName())
        .email(userEntity.getEmail())
        .phoneNumber(userEntity.getPhoneNumber())
        .plantId(userEntity.getPlantId())
        .accessToken(userEntity.getAccessToken())
        .refreshToken(userEntity.getRefreshToken())
        .maxAge(maxAge)
        .isRefreshing(false)
        .build();
  }

  public static AuthSuccessGetResponseVO of(UserEntity userEntity, long maxAge,
      boolean isRefreshing) {
    return builder()
        .userId(userEntity.getUserId())
        .name(userEntity.getName())
        .email(userEntity.getEmail())
        .phoneNumber(userEntity.getPhoneNumber())
        .plantId(userEntity.getPlantId())
        .accessToken(userEntity.getAccessToken())
        .refreshToken(userEntity.getRefreshToken())
        .maxAge(maxAge)
        .isRefreshing(isRefreshing)
        .build();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
