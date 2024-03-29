package com.blws.domain.common.dept.exception;

import com.blws.global.error.exception.BusinessException;
import com.blws.global.error.exception.ErrorCode;

public class DeptException extends BusinessException {

  public DeptException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }

  public DeptException(String message) {
    super(message, ErrorCode.INTERNAL_SERVER_ERROR);
  }
  
  public DeptException(ErrorCode errorCode) {
    super(errorCode);
  }
}
