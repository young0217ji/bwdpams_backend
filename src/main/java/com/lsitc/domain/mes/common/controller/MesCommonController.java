package com.lsitc.domain.mes.common.controller;

import com.lsitc.domain.common.file.service.FileService;
import com.lsitc.domain.common.file.vo.FileListVO;
import com.lsitc.domain.mes.activemq.service.ActiveMqService;
import com.lsitc.domain.mes.activemq.vo.ActiveMqDataManageRequestBodyVO;
import com.lsitc.domain.mes.activemq.vo.ActiveMqGetQueryRequestBodyVO;
import com.lsitc.domain.mes.common.service.CustomQueryService;
import com.lsitc.domain.mes.common.vo.CustomQueryRequestVO;
import com.lsitc.domain.sample.exception.SampleException;
import com.lsitc.global.error.exception.ErrorCode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.util.Map;
import javax.jms.JMSException;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mes/common")
@RestController
@RequiredArgsConstructor
@Api
public class MesCommonController {

  private final ActiveMqService activeMqService;
  private final CustomQueryService customQueryService;
  private final FileService fileService;

  @GetMapping("/getqueryresult")
  @ApiOperation(value = "messagename GetQueryResult", notes = "GetQueryResult 리스트")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "500", description = "에러")
  })
  public ResponseEntity<Map<String, Object>> getQueryResult(ActiveMqGetQueryRequestBodyVO param)
      throws SampleException, IOException, JMSException {
    log.info("param = {}", param);

    return ResponseEntity.ok(activeMqService.getQueryResult(param));
  }

  @GetMapping("/customquery")
  @ApiOperation(value = "", notes = "customquery 리스트")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "500", description = "에러")
  })
  public ResponseEntity<Map<String, Object>> getCustomQuery(CustomQueryRequestVO param)
   {
    log.info("param = {}", param);

    return ResponseEntity.ok(customQueryService.bindSelectQuery(param));
  }

  @GetMapping("/customquerypaging")
  @ApiOperation(value = "", notes = "customquery 리스트 페이징")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "500", description = "에러")
  })
  public ResponseEntity<Map<String, Object>> getCustomQueryPaging(
      CustomQueryRequestVO param) {
    log.info("param = {}", param);

    return ResponseEntity.ok(customQueryService.pagingSelectQuery(param));
  }


  @PostMapping("/manage")
  //모델링 -> 공장기본설정
  public ResponseEntity<Map<String, Object>> dataSendAndRecive(
      @RequestBody ActiveMqDataManageRequestBodyVO param)
      throws IOException, JMSException {

    log.error("body = {}, message = {}", param);
    Map<String, Object> result = activeMqService.dataSendAndRecive(param).getReturn();
    log.info("result = {}", result);
    return ResponseEntity.ok(result);
  }
  
  @PostMapping("/fileupload")
  public ResponseEntity<Map<String,Object>> fileUpload(@ModelAttribute FileListVO fileList){
	  
	  Map<String,Object> result = fileService.uploadFile(fileList);
	  
	  return ResponseEntity.ok(result);
  }
  
  @GetMapping("/fileDownload")
  public String fileDownload(HttpServletResponse response,
		  @RequestParam(value="fileName", required=true) String fileName,
		  @RequestParam(value="fileLocation", required=true) String fileLocation){
	  try {
		fileService.downloadFile(response, fileName, fileLocation);
	} catch (IOException e) {
		return ErrorCode.FILE_NOT_FOUND.getMessage();
	}
	return ErrorCode.FILE_NOT_FOUND.getMessage();
	  
  }

}