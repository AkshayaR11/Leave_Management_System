package com.ooad.leave.patterns.factory;

import com.ooad.leave.model.Admin;
import com.ooad.leave.model.Employee;
import com.ooad.leave.model.Manager;
import com.ooad.leave.model.User;

// Creational Pattern: Factory Method
public class UserFactory {
    public static User createUser(String role) {
        if (role == null) return null;
        if (role.equalsIgnoreCase("EMPLOYEE")) {
            return new Employee();
        } else if (role.equalsIgnoreCase("MANAGER")) {
            return new Manager();
        } else if (role.equalsIgnoreCase("ADMIN")) {
            return new Admin();
        }
        throw new IllegalArgumentException("Unknown role " + role);
    }
}
