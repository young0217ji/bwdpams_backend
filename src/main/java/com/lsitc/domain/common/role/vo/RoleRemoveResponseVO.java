package com.lsitc.domain.common.role.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoleRemoveResponseVO {

  private final String result;

  @Builder
  private RoleRemoveResponseVO(String result) {
    this.result = result;
  }

  public static RoleRemoveResponseVO of(final int deleteRows) {
    String result = 0 < deleteRows ? "success" : "failure";
    return builder().result(result).build();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
