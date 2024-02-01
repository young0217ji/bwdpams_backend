package com.lsitc.domain.mes.activemq.vo;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonRootName("message")
public class ActiveMqGetQueryRequestVO<T> {

  private ActiveMqGetQueryRequestHeaderVO header;
  private T body;

}