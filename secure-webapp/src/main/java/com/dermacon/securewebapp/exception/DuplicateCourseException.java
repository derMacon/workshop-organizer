package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.DUPLICATE_COURSE;

public class DuplicateCourseException extends Exception {

    private final static ErrorCode ERROR_CODE = DUPLICATE_COURSE;

    public DuplicateCourseException() {
        super(ERROR_CODE.toString());
    }

    public static ErrorCode getErrorCode() {
        return ERROR_CODE;
    }
}
