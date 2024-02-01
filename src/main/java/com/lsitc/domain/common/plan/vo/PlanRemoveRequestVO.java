package com.lsitc.domain.common.plan.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.lsitc.domain.common.plan.entity.PlanEntity;
import lombok.Getter;

@Getter
public class PlanRemoveRequestVO {

  private final String planSeq;

  @JsonCreator
  public PlanRemoveRequestVO(String planSeq) {
    this.planSeq = planSeq;
  }

  public PlanEntity toEntity() {
    return PlanEntity.builder()
        .id(Long.valueOf(planSeq))
        .build();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
