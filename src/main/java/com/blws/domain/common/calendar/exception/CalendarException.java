package com.blws.domain.common.calendar.exception;

import com.blws.global.error.exception.BusinessException;
import com.blws.global.error.exception.ErrorCode;

public class CalendarException extends BusinessException {

  public CalendarException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }

  public CalendarException(String message) {
    super(message, ErrorCode.INTERNAL_SERVER_ERROR);
  }

  public CalendarException(ErrorCode errorCode) {
    super(errorCode);
  }

}
