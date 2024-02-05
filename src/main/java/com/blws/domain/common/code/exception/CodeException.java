package com.blws.domain.common.code.exception;

import com.blws.global.error.exception.BusinessException;
import com.blws.global.error.exception.ErrorCode;

public class CodeException extends BusinessException {

  public CodeException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }

  public CodeException(String message) {
    super(message, ErrorCode.INTERNAL_SERVER_ERROR);
  }

  public CodeException(ErrorCode errorCode) {
    super(errorCode);
  }
}
