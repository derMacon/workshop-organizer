package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.USER_CANT_CREATE_COURSE;

public class UserCantCreateCourseException extends ErrorCodeException {
    public UserCantCreateCourseException() {
        super(USER_CANT_CREATE_COURSE);
    }
}
