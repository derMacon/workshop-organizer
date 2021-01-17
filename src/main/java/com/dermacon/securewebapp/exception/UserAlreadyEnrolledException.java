package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.USER_ALREADY_ENROLLED;

public class UserAlreadyEnrolledException extends ErrorCodeException {

    public UserAlreadyEnrolledException() {
        super(USER_ALREADY_ENROLLED);
    }

}
