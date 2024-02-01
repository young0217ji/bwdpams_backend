package com.lsitc.domain.mes.common.dao;

import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class CustomQueryBuilder {

  /**
   * table customquery 조회 쿼리 생성
   *
   * @return String
   */
  public String selectCustomQuery() {
    return new SQL().SELECT("*")
        .FROM("customquery")
        .WHERE("PLANTID=#{plantid}")
        .WHERE("QUERYID=#{queryid}")
        .WHERE("QUERYVERSION=#{queryversion}")
        .toString();
  }

  /**
   * 조회된 customquery 바인딩
   *
   * @param params
   * @return String
   */
  public String bindCustomQuery(Map<String, Object> params) {
    return params.get("query").toString();
  }

  /**
   * 조회된 customquery 바인딩 및 페이징 추가
   *
   * @param params`
   * @return
   */
  public String pagingCustomQuery(Map<String, Object> params) {
    return new StringBuffer()
        .append(params.get("query").toString())
        .append(" OFFSET #{SKIP} ROW FETCH NEXT #{TAKE} ROW ONLY ")
        .toString();
  }

  /**
   * 조회된 customquery 전체 rows 조회
   *
   * @param params
   * @return
   */
  public String totalCustomQuery(Map<String, Object> params) {
    String query = params.get("query").toString();
    if (query.contains("ORDER BY")) {
      query = query.substring(0, query.indexOf("ORDER BY"));
    }

    return new StringBuffer()
        .append("SELECT COUNT(*) AS CNT")
        .append(" FROM (")
        .append(query)
        .append(") A").toString();
  }


}
