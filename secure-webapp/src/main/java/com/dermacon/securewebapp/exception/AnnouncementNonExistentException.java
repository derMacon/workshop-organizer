package com.dermacon.securewebapp.exception;

import static com.dermacon.securewebapp.exception.ErrorCode.ANNOUNCEMENT_NONEXISTENT;

public class AnnouncementNonExistentException extends ErrorCodeException {
    public AnnouncementNonExistentException() {
        super(ANNOUNCEMENT_NONEXISTENT);
    }
}
