package com.lsitc.domain.mes.activemq.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonRootName("message")
public class ActiveMqGetQueryResultVO<T> {

  @JsonProperty("header")
  private Object header;
  @JsonProperty("body")
  private ActiveMqGetQueryResultDataVO<T> body;
  @JsonProperty("return")
  private Map<String,Object> Return;
}