package com.blws.domain.mes.activemq.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(title = "ActiveMqGetQueryRequestBody 파라미터 VO", description = "파라미터 종류 추가 시 작업 필요")
public class ActiveMqGetQueryRequestBodyVO {

  @JsonProperty(value = "LANGUAGE")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String language;

  @JsonProperty(value = "QUERYID")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String queryid;

  @JsonProperty(value = "PLANTID")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String plantid;

  @JsonProperty(value = "EVENTUSER")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String eventuser;

  @JsonProperty(value = "QUERYVERSION")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String queryversion;

  @JsonProperty(value = "BINDV")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private Object bindv;
}

