package com.blws.domain.mes.excel.controller;

import com.blws.domain.mes.common.vo.CustomQueryRequestVO;
import com.blws.domain.mes.excel.service.ExcelService;
import io.swagger.annotations.Api;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mes/excel")
@RestController
@RequiredArgsConstructor
@Api
public class MesExcelController {

  private final ExcelService excelService;

  @GetMapping("/customquery")
  public void customQueryExcel(HttpServletResponse res, CustomQueryRequestVO param)
      throws IOException {
    excelService.toExcel(res, param);
  }

  @GetMapping("/customquerypaging")
  public void customQueryPagingExcel(HttpServletResponse res, CustomQueryRequestVO param)
      throws IOException {
    excelService.pagingToExcel(res, param);
  }

}
