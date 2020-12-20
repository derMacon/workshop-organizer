package com.dermacon.securewebapp.exception;

public abstract class ErrorCodeException extends Exception {

    protected final ErrorCode error_code;

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.error_code = errorCode;
    }

    public ErrorCode getErrorCode() {
        return error_code;
    }

}
