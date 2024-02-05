package com.blws.domain.common.usermenu.exception;

import com.blws.global.error.exception.BusinessException;
import com.blws.global.error.exception.ErrorCode;

public class UserMenuException extends BusinessException{

	public UserMenuException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}
	
	public UserMenuException(String message) {
		super(message, ErrorCode.INTERNAL_SERVER_ERROR);
	}
	
	public UserMenuException(ErrorCode errorCode) {
		super(errorCode);
	}
	
	

}
