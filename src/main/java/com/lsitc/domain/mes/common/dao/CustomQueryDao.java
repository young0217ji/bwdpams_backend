package com.lsitc.domain.mes.common.dao;

import com.lsitc.domain.mes.common.entity.CustomQuery;
import com.lsitc.domain.mes.common.vo.CustomQueryRequestVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface CustomQueryDao {

  @SelectProvider(type = CustomQueryBuilder.class, method = "selectCustomQuery")
  CustomQuery selectCustomQuery(CustomQueryRequestVO params);

  @SelectProvider(type = CustomQueryBuilder.class, method = "selectCustomQuery")
  CustomQuery selectByCustomQuery(CustomQuery params);

  @SelectProvider(type = CustomQueryBuilder.class, method = "bindCustomQuery")
  List<Map<String, Object>> bindCustomQuery(Map<String, Object> params);

  @SelectProvider(type = CustomQueryBuilder.class, method = "pagingCustomQuery")
  List<Map<String, Object>> pagingCustomQuery(Map<String, Object> params);

  @SelectProvider(type = CustomQueryBuilder.class, method = "totalCustomQuery")
  int totalCustomQuery(Map<String, Object> params);

}
