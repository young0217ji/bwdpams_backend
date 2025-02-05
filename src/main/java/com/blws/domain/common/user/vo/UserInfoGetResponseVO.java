package com.blws.domain.common.user.vo;

import com.blws.domain.common.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public class UserInfoGetResponseVO {

  private final String userId;
  private final String name;
  private final String eml;
  private final String phoneNo;
  private final String roleId;
  private final String factoryId;

  @Builder
  private UserInfoGetResponseVO(String userId, String name, String eml,
      String phoneNo, String roleId, String factoryId) {
    super();
    this.userId = userId;
    this.name = name;
    this.eml = eml;
    this.phoneNo = phoneNo;
    this.roleId = roleId;
    this.factoryId = factoryId;
  }

  public static UserInfoGetResponseVO of(UserEntity resultEntity) {
    return builder()
        .userId(resultEntity.getUserId())
        .name(resultEntity.getName())
        .eml(resultEntity.getEml())
        .phoneNo(resultEntity.getPhoneNo())
        .roleId(resultEntity.getRoleId())
        .factoryId(resultEntity.getFactoryId())
        .build();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
