package com.example.demo.security;

public enum ApplicationUserPermission {
    EMPLOYEES_READ("EMPLOYEES:READ"),
    EMPLOYEES_CREATE("EMPLOYEES:CREATE"),
    EMPLOYEES_DELETE("EMPLOYEES:DELETE"),
    EMPLOYEES_UPDATE("EMPLOYEES:UPDATE"),
    LAPTOPS_READ("LAPTOPS:READ"),
    LAPTOPS_UPDATE("LAPTOPS:UPDATE"),
    LAPTOPS_DELETE("LAPTOPS:DELETE"),
    LAPTOPS_CREATE("LAPTOPS:CREATE"),
    DEPARTMENTS_CREATE("DEPARTMENTS:CREATE"),
    DEPARTMENTS_UPDATE("DEPARTMENTS:UPDATE"),
    DEPARTMENTS_READ("DEPARTMENTS:READ"),
    DEPARTMENTS_DELETE("DEPARTMENTS:DELETE");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    String getPermission() {
        return this.permission;
    }
}
