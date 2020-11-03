package com.dermacon.securewebapp.data;

public enum UserRole {
    ROLE_USER("user"),
    ROLE_ADMIN("admin"),
    ROLE_MANAGER("manager");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
