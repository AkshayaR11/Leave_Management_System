package com.ooad.leave.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MANAGER")
public class Manager extends User {
    private String managedDepartment;

    public String getManagedDepartment() { return managedDepartment; }
    public void setManagedDepartment(String managedDepartment) { this.managedDepartment = managedDepartment; }
}
