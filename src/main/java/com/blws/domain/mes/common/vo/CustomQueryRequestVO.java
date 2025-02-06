package com.blws.domain.mes.common.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonAutoDetect
public class CustomQueryRequestVO {

  @JsonProperty(value = "LANGUAGE")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String language;

  @JsonProperty(value = "QUERYID")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String queryid;

  @JsonProperty(value = "FACTORYID")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String factoryid;

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

