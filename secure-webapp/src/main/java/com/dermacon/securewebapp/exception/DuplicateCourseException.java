package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.DUPLICATE_COURSE;

public class DuplicateCourseException extends Exception {

    public DuplicateCourseException() {
        super(DUPLICATE_COURSE.toString());
    }
}
