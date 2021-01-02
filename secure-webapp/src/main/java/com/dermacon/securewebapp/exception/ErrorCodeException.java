package com.dermacon.securewebapp.exception;

import com.dermacon.securewebapp.SecureWebappApplication;
import org.apache.log4j.Logger;

public abstract class ErrorCodeException extends Exception {

    private static Logger log = Logger.getLogger(ErrorCodeException.class.getName());

    protected final ErrorCode error_code;

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.error_code = errorCode;
        log.fatal(errorCode);
    }

    public ErrorCode getErrorCode() {
        return error_code;
    }

}
