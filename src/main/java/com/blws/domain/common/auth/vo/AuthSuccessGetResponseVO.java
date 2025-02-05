package com.blws.domain.common.auth.vo;

import com.blws.domain.common.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public class AuthSuccessGetResponseVO {

  private final String userId;
  private final String name;
  private final String eml;
  private final String phoneNo;
  private final String factoryId;
  private final String accesTkn;
  private final String refreshTkn;
  private final boolean isRefreshing;
  private final long maxAge;

  @Builder
  private AuthSuccessGetResponseVO(String userId, String name, String eml,
      String phoneNo, String factoryId, String accesTkn, String refreshTkn,
      boolean isRefreshing, long maxAge) {
    this.userId = userId;
    this.name = name;
    this.eml = eml;
    this.phoneNo = phoneNo;
    this.factoryId = factoryId;
    this.accesTkn = accesTkn;
    this.refreshTkn = refreshTkn;
    this.isRefreshing = isRefreshing;
    this.maxAge = maxAge;
  }

  public static AuthSuccessGetResponseVO of(UserEntity userEntity) {
    return builder()
        .userId(userEntity.getUserId())
        .name(userEntity.getName())
        .eml(userEntity.getEml())
        .phoneNo(userEntity.getPhoneNo())
        .factoryId(userEntity.getFactoryId())
        .accesTkn(userEntity.getAccesTkn())
        .refreshTkn(userEntity.getRefreshTkn())
        .isRefreshing(false)
        .build();
  }

  public static AuthSuccessGetResponseVO of(UserEntity userEntity, long maxAge) {
    return builder()
        .userId(userEntity.getUserId())
        .name(userEntity.getName())
        .eml(userEntity.getEml())
        .phoneNo(userEntity.getPhoneNo())
        .factoryId(userEntity.getFactoryId())
        .accesTkn(userEntity.getAccesTkn())
        .refreshTkn(userEntity.getRefreshTkn())
        .maxAge(maxAge)
        .isRefreshing(false)
        .build();
  }

  public static AuthSuccessGetResponseVO of(UserEntity userEntity, long maxAge,
      boolean isRefreshing) {
    return builder()
        .userId(userEntity.getUserId())
        .name(userEntity.getName())
        .eml(userEntity.getEml())
        .phoneNo(userEntity.getPhoneNo())
        .factoryId(userEntity.getFactoryId())
        .accesTkn(userEntity.getAccesTkn())
        .refreshTkn(userEntity.getRefreshTkn())
        .maxAge(maxAge)
        .isRefreshing(isRefreshing)
        .build();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
