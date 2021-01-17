package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.EMAIL_ALREADY_EXISTS;

public class EmailAlreadyExistsException extends ErrorCodeException {
    public EmailAlreadyExistsException() {
        super(EMAIL_ALREADY_EXISTS);
    }
}
