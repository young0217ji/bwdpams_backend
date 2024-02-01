package com.lsitc.domain.mes.activemq.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(title="ActiveMqDataManageRequestBodyVO 파라미터 VO",description="파라미터 종류 추가 시 작업 필요")
public class ActiveMqDataManageRequestBodyVO {

    @JsonProperty(value ="MESSAGENAME")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String messagename;

    @JsonProperty(value ="PLANTID")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String plantid;
    
    @JsonProperty(value ="PROCESSROUTEID")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String processrouteid;

    @JsonProperty(value ="PARAMV")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private Object paramv;

    @JsonProperty("DATALIST")
    private ActiveMqDataMangeRequestDataListVO datalist;

}

