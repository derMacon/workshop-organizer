package com.dermacon.securewebapp.data;

import com.dermacon.securewebapp.security.user.ApplicationUserPermission;
import org.hibernate.annotations.Cascade;
import org.thymeleaf.expression.Sets;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.dermacon.securewebapp.security.user.ApplicationUserPermission.*;

public enum UserRole {
    ROLE_USER(ITEM_READ, ITEM_WRITE),
    ROLE_ADMIN(ITEM_READ, ITEM_WRITE, FLATMATE_READ, FLATMATE_WRITE);

    private final Set<ApplicationUserPermission> permissions;

    UserRole(ApplicationUserPermission... permissions) {
        this.permissions = new HashSet<>(Arrays.asList(permissions));
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
}
