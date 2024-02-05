package com.blws.domain.mes.common.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
public class CustomQuery {

  private String plantid;
  private String queryid;
  private String queryversion;
  private String querystring;
  private String querytype;
  private String description;
  private String programid;
  private String formid;
  private String menuid;
  private Long querycount;
  private LocalDateTime createtime;
  private String createuserid;
}
