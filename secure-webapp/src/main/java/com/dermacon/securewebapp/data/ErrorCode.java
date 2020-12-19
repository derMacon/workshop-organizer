package com.dermacon.securewebapp.data;

public enum ErrorCode {

    ACCESS_DENIED("Access Denied", "User does not have the permission to access this resource");

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
}
