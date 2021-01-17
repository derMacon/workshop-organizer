package com.dermacon.securewebapp.exception;

public enum ErrorCode {

    ACCESS_DENIED(
            "Access Denied",
            "User does not have the permission to access this resource"
    ),
    EMAIL_ALREADY_EXISTS(
            "Email already in user",
            "User cannot register twice with the same email"
    ),
    USERNAME_ALREADY_EXISTS(
            "Username already in use",
            "The given username at the registration is already in use"
    ),
    DUPLICATE_COURSE(
            "Duplicate Course",
            "Manager tried to create a course with a name that already exists"
    ),
    USER_ALREADY_ENROLLED(
            "User already enrolled",
            "User is already enrolled in the course and cannot be added a second time"
    ),
    USER_NOT_ENROLLED_AT_DROPOUT(
            "User not enrolled at dropout",
            "The user tries to dropout out of a course which in which he never enrolled"
    ),
    HOST_ENROLL_OWN_COURSE(
            "Host tried to enroll in own course",
            "A user who created a course cannot enroll in the same course"
    ),
    ANNOUNCEMENT_NONEXISTENT(
            "Delete non existent Announcement",
            "User tried to delete an announcement which does not exist"
    ),
    USER_CANT_CREATE_COURSE(
        "User with wrong privileges",
            "The logged in User is a standard user who cannot create a course"
    ),
    NON_EXISTENT_COURSE(
        "Non existent course",
            "Course does not exist"
    );

    private final String title;
    private final String description;

    ErrorCode(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return title + " - " + description;
    }
}
