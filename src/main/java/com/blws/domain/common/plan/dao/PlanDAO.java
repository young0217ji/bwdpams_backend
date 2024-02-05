package com.blws.domain.common.plan.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.blws.domain.common.plan.entity.PlanEntity;

@Mapper
public interface PlanDAO {

  PlanEntity selectPlanById(PlanEntity planEntity);
  
  List<PlanEntity> selectPlanByConditions(PlanEntity planEntity);
  
  int insertPlanList(List<PlanEntity> calendarEntityList);

  int updatePlanById(List<PlanEntity> calendarEntityList);

  int insertPlanWithId(List<PlanEntity> planEntityList);

  int deletePlanById(List<PlanEntity> planEntityList);
}
