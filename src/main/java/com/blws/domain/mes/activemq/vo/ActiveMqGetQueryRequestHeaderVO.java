package com.blws.domain.mes.activemq.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@Data
@Builder
@AllArgsConstructor
@Schema(title="ActiveMqGetQueryRequestHeader 파라미터 VO",description="UI 사용안함")
public class ActiveMqGetQueryRequestHeaderVO {

  
  private String messagename;
  private String replysubject;
  private String sourcesubject;
  private String targetsubject;
  private String transactionid;
}