package com.dermacon.securewebapp.security.user;

public enum ApplicationUserPermission {
    ITEM_READ("item:read"),
    ITEM_WRITE("item:write"),
    FLATMATE_READ("flatmate:read"),
    FLATMATE_WRITE("flatmate:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
