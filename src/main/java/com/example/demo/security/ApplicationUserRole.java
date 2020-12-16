package com.example.demo.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationUserRole {
    EMPLOYEE(Sets.newHashSet(
            ApplicationUserPermission.EMPLOYEES_READ,
            ApplicationUserPermission.LAPTOPS_READ,
            ApplicationUserPermission.DEPARTMENTS_READ
    )),
    ADMIN(Sets.newHashSet(
            ApplicationUserPermission.EMPLOYEES_READ,
            ApplicationUserPermission.EMPLOYEES_DELETE,
            ApplicationUserPermission.EMPLOYEES_UPDATE,
            ApplicationUserPermission.EMPLOYEES_CREATE,
            ApplicationUserPermission.LAPTOPS_CREATE,
            ApplicationUserPermission.LAPTOPS_READ,
            ApplicationUserPermission.LAPTOPS_UPDATE,
            ApplicationUserPermission.LAPTOPS_DELETE,
            ApplicationUserPermission.DEPARTMENTS_CREATE,
            ApplicationUserPermission.DEPARTMENTS_UPDATE,
            ApplicationUserPermission.DEPARTMENTS_DELETE,
            ApplicationUserPermission.DEPARTMENTS_READ
    )),
    ADMINTRAINEE(Sets.newHashSet(
            ApplicationUserPermission.EMPLOYEES_READ,
            ApplicationUserPermission.LAPTOPS_READ,
            ApplicationUserPermission.DEPARTMENTS_READ,
            ApplicationUserPermission.EMPLOYEES_CREATE,
            ApplicationUserPermission.DEPARTMENTS_CREATE,
            ApplicationUserPermission.LAPTOPS_CREATE
    ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
