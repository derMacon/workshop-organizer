package com.dermacon.securewebapp.data;

import com.dermacon.securewebapp.security.user.ApplicationUserPermission;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.dermacon.securewebapp.security.user.ApplicationUserPermission.*;

public enum UserRole {
    ROLE_USER(COURSE_READ),
    ROLE_MANAGER(COURSE_READ, COURSE_WRITE),
    ROLE_ADMIN(COURSE_READ, COURSE_WRITE, PERSON_READ, PERSON_WRITE);

    private final Set<ApplicationUserPermission> permissions;

    UserRole(ApplicationUserPermission... permissions) {
        this.permissions = new HashSet<>(Arrays.asList(permissions));
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
}
