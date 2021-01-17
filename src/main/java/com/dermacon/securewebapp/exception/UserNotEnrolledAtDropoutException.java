package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.USER_NOT_ENROLLED_AT_DROPOUT;

public class UserNotEnrolledAtDropoutException extends ErrorCodeException {

    public UserNotEnrolledAtDropoutException() {
        super(USER_NOT_ENROLLED_AT_DROPOUT);
    }

}
