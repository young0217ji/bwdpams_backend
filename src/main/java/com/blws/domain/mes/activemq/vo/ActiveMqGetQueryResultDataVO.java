package com.blws.domain.mes.activemq.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Schema(title="ActiveMq GetQuery result BINDV 파라미터 VO",description="UI 작업")
public class ActiveMqGetQueryResultDataVO<T> {

  @JsonProperty("DATALIST")
  private List<T> data;
}