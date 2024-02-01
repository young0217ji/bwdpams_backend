package com.lsitc.domain.mes.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsitc.domain.mes.common.dao.CustomQueryDao;
import com.lsitc.domain.mes.common.entity.CustomQuery;
import com.lsitc.domain.mes.common.vo.CustomQueryRequestVO;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueryAndParam {

  private final CustomQueryDao customQueryDao;
  private final ObjectMapper objectMapper;


  public Map<String, Object> toMap(CustomQueryRequestVO params) {
    Map<String, Object> paramMap = voToMap(params);
    CustomQuery findQuery = selectQuery(params);
    return convertFormat(findQuery.getQuerystring(), paramMap);

  }

  @SneakyThrows
  public Map<String, Object> voToMap(CustomQueryRequestVO params) {
    Map<String, Object> paramMap = objectMapper.convertValue(params, Map.class);
    Map<String, Object> bindvMap = objectMapper.readValue(paramMap.get("BINDV").toString(),
        Map.class);
    bindvMap = bindvMap.entrySet().stream().collect(
        Collectors.toMap(v1 -> v1.getKey().toUpperCase(Locale.ROOT),
            v2 -> v2.getValue().toString()));

    Map<String, Object> paging = bindvMap.entrySet().stream()
        .filter(
            v -> "SKIP".equalsIgnoreCase(v.getKey()) || "TAKE".equalsIgnoreCase(v.getKey()))
        .collect(Collectors.toMap(v1 -> v1.getKey().toUpperCase(Locale.ROOT),
            v2 -> Integer.parseInt(v2.getValue().toString())));

    paramMap.putAll(bindvMap);
    paramMap.putAll(paging);
    return paramMap;
  }

  public CustomQuery selectQuery(CustomQueryRequestVO params) {
    return Optional.ofNullable(customQueryDao.selectCustomQuery(params))
        .orElseThrow(RuntimeException::new);
  }

  public Map<String, Object> convertFormat(String query, Map<String, Object> params) {

    final String REGEXP = "\\:(?<word>[\\w]*)";

    String formatting = Pattern.compile(REGEXP).matcher(query).replaceAll((matchResult) -> {
      String key = matchResult.group().replaceAll(REGEXP, "${word}");
      if (params.containsKey(key)) {
        // mybatis 바인딩 포맷으로 변경
        // :파라미터명 => #{파라미터명}
        return String.format("#{%s}", key);
      } else {
        // 파라미터에 없는 바인딩 데이터는 빈문자열로 치환
        return "\'\'";
      }

    });
    params.put("query", formatting);

    return params;
  }

}
