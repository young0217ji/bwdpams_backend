package com.lsitc.domain.common.user.vo;

import com.lsitc.domain.common.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public class UserInfoGetResponseVO {

  private final String userId;
  private final String name;
  private final String email;
  private final String phoneNumber;
  private final String roleId;
  private final String plantId;

  @Builder
  private UserInfoGetResponseVO(String userId, String name, String email,
      String phoneNumber, String roleId, String plantId) {
    super();
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.roleId = roleId;
    this.plantId = plantId;
  }

  public static UserInfoGetResponseVO of(UserEntity resultEntity) {
    return builder()
        .userId(resultEntity.getUserId())
        .name(resultEntity.getName())
        .email(resultEntity.getEmail())
        .phoneNumber(resultEntity.getPhoneNumber())
        .roleId(resultEntity.getRoleId())
        .plantId(resultEntity.getPlantId())
        .build();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
