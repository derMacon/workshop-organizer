package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.USERNAME_ALREADY_EXISTS;

public class UsernameAlreadyExistsException extends ErrorCodeException {
    public UsernameAlreadyExistsException() {
        super(USERNAME_ALREADY_EXISTS);
    }
}
