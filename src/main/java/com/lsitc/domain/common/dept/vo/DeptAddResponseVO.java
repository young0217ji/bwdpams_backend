package com.lsitc.domain.common.dept.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeptAddResponseVO {

  private final String result;

  @Builder
  private DeptAddResponseVO(String result) {
    this.result = result;
  }

  public static DeptAddResponseVO of(final int addRows) {
    String result = 0 < addRows ? "success" : "failure";
    return builder().result(result).build();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
