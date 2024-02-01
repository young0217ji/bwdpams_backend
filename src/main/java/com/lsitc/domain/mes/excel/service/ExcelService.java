package com.lsitc.domain.mes.excel.service;

import com.lsitc.domain.mes.common.dao.CustomQueryDao;
import com.lsitc.domain.mes.common.entity.CustomQuery;
import com.lsitc.domain.mes.common.service.QueryAndParam;
import com.lsitc.domain.mes.common.vo.CustomQueryRequestVO;
import com.lsitc.global.error.exception.BusinessException;
import com.lsitc.global.error.exception.ErrorCode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {

  private static final String GRID_DETAIL_QUERY_ID = "GetGridDetailList";

  private final CustomQueryDao customQueryDao;
  private final QueryAndParam queryAndParam;

  public void toExcel(HttpServletResponse res, CustomQueryRequestVO requestVO) throws IOException {
    toExcel(res, getGridDetail(requestVO), getContents(requestVO));

  }

  public void pagingToExcel(HttpServletResponse res, CustomQueryRequestVO requestVO)
      throws IOException {
    toExcel(res, getGridDetail(requestVO), getPagingContents(requestVO));

  }

  private List<Map<String, Object>> getGridDetail(CustomQueryRequestVO requestVO) {
    Map<String, Object> params = queryAndParam.voToMap(requestVO);

    CustomQuery findQuery = customQueryDao.selectByCustomQuery(
        CustomQuery.builder()
            .plantid(requestVO.getPlantid())
            .queryid(GRID_DETAIL_QUERY_ID)
            .queryversion(params.getOrDefault("GRIDQUERYVERSION", "00001").toString())
            .build());

    if (findQuery == null) {
      return null;
    }

    return customQueryDao.bindCustomQuery(
        queryAndParam.convertFormat(findQuery.getQuerystring(), params));

  }

  private List<Map<String, Object>> getContents(CustomQueryRequestVO requestVO) {
    Map<String, Object> params = queryAndParam.toMap(requestVO);

    return customQueryDao.bindCustomQuery(params);
  }

  private List<Map<String, Object>> getPagingContents(CustomQueryRequestVO requestVO) {
    Map<String, Object> params = queryAndParam.toMap(requestVO);

    return customQueryDao.pagingCustomQuery(params);
  }


  public void toExcel(HttpServletResponse res, List<Map<String, Object>> headers,
      List<Map<String, Object>> contents) throws IOException {

    if (contents.isEmpty()) {
      throw new BusinessException(ErrorCode.INVALID_TYPE_VALUE);
    }

    /**
     * excel sheet 생성
     */
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Sheet1"); // 엑셀 sheet 이름
    sheet.setDefaultColumnWidth(28); // 디폴트 너비 설정

    /**
     * header data
     */

    if (headers.isEmpty()) {
      headers = new ArrayList<>();
      for (Entry<String, Object> v : contents.stream().findFirst().get().entrySet()) {
        headers.add(Map.of("GRIDCOLUMNNAME", v.getKey()
            , "GRIDCOLUMNID", v.getKey()));
      }

    }

    int rowCount = 0; // 데이터가 저장될 행
    Row headerRow = sheet.createRow(rowCount++);

    for (Map<String, Object> item : headers) {
      headerRow.createCell(headers.indexOf(item))
          .setCellValue(
              item.getOrDefault("GRIDCOLUMNNAME", item.getOrDefault("GRIDCOLUMNID", ""))
                  .toString());
    }

    /**
     * body data
     */
    List<String> headerColumnId = headers.stream().map(v -> v.get("GRIDCOLUMNID").toString())
        .collect(Collectors.toList());
    for (Map<String, Object> row : contents) {
      Row bodyRow = sheet.createRow(rowCount++);
      int cellCount = 0;

      for (String key : headerColumnId) {
        if (row.containsKey(key)) {
          bodyRow.createCell(cellCount++).setCellValue(row.getOrDefault(key, "").toString());
        } else {
          bodyRow.createCell(cellCount++);
        }
      }
    }

    /**
     * download
     */
    String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

    res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    res.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
    ServletOutputStream servletOutputStream = res.getOutputStream();

    workbook.write(servletOutputStream);
    workbook.close();
    servletOutputStream.flush();
    servletOutputStream.close();
  }

}
