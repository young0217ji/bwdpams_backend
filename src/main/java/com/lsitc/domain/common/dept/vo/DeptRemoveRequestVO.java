package com.lsitc.domain.common.dept.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.lsitc.domain.common.dept.entity.DeptEntity;
import lombok.Getter;

@Getter
public class DeptRemoveRequestVO {

  @NotNull
  @PositiveOrZero(message = "음수의 ID는 사용할 수 없습니다.")
  private final Long id;

  @JsonCreator
  public DeptRemoveRequestVO(Long id) {
    this.id = id;
  }

  public DeptEntity toEntity() {
    return DeptEntity.builder().id(id).build();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
