package com.dermacon.securewebapp.security.user;

public enum ApplicationUserPermission {
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write"),
    PERSON_READ("person:read"),
    PERSON_WRITE("person:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
