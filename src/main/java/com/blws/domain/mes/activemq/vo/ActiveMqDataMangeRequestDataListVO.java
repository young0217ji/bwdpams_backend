package com.blws.domain.mes.activemq.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;


@Data
@Builder
@JsonRootName("DATALIST")
@Schema(title="",description="")
public class ActiveMqDataMangeRequestDataListVO {

    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String ID;
    @JsonProperty("DATAINFO")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Set<Map<String,Object>> datainfo;
}
