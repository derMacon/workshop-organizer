package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.NON_EXISTENT_COURSE;

public class NonExistentCourseException extends ErrorCodeException {

    public NonExistentCourseException() {
        super(NON_EXISTENT_COURSE);
    }

}
