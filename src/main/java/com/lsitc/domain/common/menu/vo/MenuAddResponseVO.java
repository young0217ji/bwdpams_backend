package com.lsitc.domain.common.menu.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuAddResponseVO {

//  @JsonInclude(Include.NON_EMPTY)
//  private final String result;
//
//  @Builder
//  private MenuAddResponseVO(String result) {
//    this.result = result;
//  }
//
//  public static MenuAddResponseVO of(final int addRows) {
//    String result = 0 < addRows ? "success" : "failure";
//    return builder().result(result).build();
//  }
//
//  @Override
//  public String toString() {
//    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
//  }
}