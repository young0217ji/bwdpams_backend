package com.blws.domain.common.plan.exception;

import com.blws.global.error.exception.BusinessException;
import com.blws.global.error.exception.ErrorCode;

public class PlanException extends BusinessException {

  public PlanException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }

  public PlanException(String message) {
    super(message, ErrorCode.INTERNAL_SERVER_ERROR);
  }

  public PlanException(ErrorCode errorCode) {
    super(errorCode);
  }

}
