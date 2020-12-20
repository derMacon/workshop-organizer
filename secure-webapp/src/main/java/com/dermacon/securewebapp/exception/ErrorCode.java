package com.dermacon.securewebapp.exception;

public enum ErrorCode {

    ACCESS_DENIED(
            "Access Denied",
            "User does not have the permission to access this resource"
    ),
    DUPLICATE_COURSE(
            "Duplicate Course",
            "Manager tried to create a course with a name that already exists"
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
