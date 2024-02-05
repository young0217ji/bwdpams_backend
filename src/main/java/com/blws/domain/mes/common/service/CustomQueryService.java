package com.blws.domain.mes.common.service;

import com.blws.domain.mes.common.dao.CustomQueryDao;
import com.blws.domain.mes.common.vo.CustomQueryRequestVO;
import com.blws.domain.mes.common.vo.Pagable;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomQueryService {

  private final CustomQueryDao customQueryDao;
  private final QueryAndParam queryAndParam;

  public Map<String, Object> bindSelectQuery(CustomQueryRequestVO params) {
    Map<String, Object> paramMap = queryAndParam.toMap(params);

    return Map.of("returncode", "0",
        "body", customQueryDao.bindCustomQuery(paramMap));
  }

  public Map<String, Object> pagingSelectQuery(CustomQueryRequestVO params) {
    Map<String, Object> paramMap = queryAndParam.toMap(params);

    int total = customQueryDao.totalCustomQuery(paramMap);
    List<Map<String, Object>> data = customQueryDao.pagingCustomQuery(paramMap);

    return Map.of("returncode", "0",
        "body", Map.of("list", data,
            "paging", Pagable.builder()
                .skip(Integer.parseInt(paramMap.get("SKIP").toString()))
                .take(Integer.parseInt(paramMap.get("TAKE").toString()))
                .total(total)
                .build()));

  }
}
