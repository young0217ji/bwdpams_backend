package com.lsitc.domain.mes.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Pagable {

  private long skip;
  private long take;
  private long total;
}
