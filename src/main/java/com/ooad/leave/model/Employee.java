package com.ooad.leave.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {
    private String department;
    private int leaveBalance; // Total balance
    private int sickLeaveBalance;
    private int casualLeaveBalance;
    private int earnedLeaveBalance;
    private int maternityLeaveBalance;

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public int getLeaveBalance() { return leaveBalance; }
    public void setLeaveBalance(int leaveBalance) { this.leaveBalance = leaveBalance; }

    public int getSickLeaveBalance() { return sickLeaveBalance; }
    public void setSickLeaveBalance(int sickLeaveBalance) { this.sickLeaveBalance = sickLeaveBalance; }

    public int getCasualLeaveBalance() { return casualLeaveBalance; }
    public void setCasualLeaveBalance(int casualLeaveBalance) { this.casualLeaveBalance = casualLeaveBalance; }

    public int getEarnedLeaveBalance() { return earnedLeaveBalance; }
    public void setEarnedLeaveBalance(int earnedLeaveBalance) { this.earnedLeaveBalance = earnedLeaveBalance; }

    public int getMaternityLeaveBalance() { return maternityLeaveBalance; }
    public void setMaternityLeaveBalance(int maternityLeaveBalance) { this.maternityLeaveBalance = maternityLeaveBalance; }
}
