package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.NON_EXISTENT_COURSE;

public class NonExistentCourseException extends Exception {

    private final static ErrorCode ERROR_CODE = NON_EXISTENT_COURSE;

    public NonExistentCourseException() {
        super(ERROR_CODE.toString());
    }

    public static ErrorCode getErrorCode() {
        return ERROR_CODE;
    }
}
