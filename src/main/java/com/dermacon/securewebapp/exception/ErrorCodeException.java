package com.dermacon.securewebapp.exception;

import org.apache.log4j.Logger;

public abstract class ErrorCodeException extends Exception {

    private static final Logger log = Logger.getLogger(ErrorCodeException.class.getName());

    protected final ErrorCode errorCode;

    protected ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
        log.fatal(errorCode);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
