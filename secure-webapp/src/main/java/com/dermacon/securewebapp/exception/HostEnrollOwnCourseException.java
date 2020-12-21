package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.HOST_ENROLL_OWN_COURSE;

public class HostEnrollOwnCourseException extends ErrorCodeException {

    public HostEnrollOwnCourseException() {
        super(HOST_ENROLL_OWN_COURSE);
    }

}
