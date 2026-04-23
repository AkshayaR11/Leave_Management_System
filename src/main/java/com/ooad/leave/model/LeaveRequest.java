package com.ooad.leave.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ooad.leave.patterns.state.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Employee employee;
    
    @ManyToOne
    private LeaveType leaveType;
    
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    
    private String status;
    private LocalDate appliedOn;

    @Transient
    @JsonIgnore
    private LeaveState state;

    @PostLoad
    @PostPersist
    public void loadState() {
        if ("PENDING".equals(status)) state = new PendingState();
        else if ("APPROVED".equals(status)) state = new ApprovedState();
        else if ("REJECTED".equals(status)) state = new RejectedState();
        else if ("CANCELLED".equals(status)) state = new CancelledState();
        else state = new PendingState();
    }

    public void approve() {
        if (state == null) loadState();
        state.approve(this);
        this.status = state.getStatusString();
    }

    public void reject() {
        if (state == null) loadState();
        state.reject(this);
        this.status = state.getStatusString();
    }

    public void cancel() {
        if (state == null) loadState();
        state.cancel(this);
        this.status = state.getStatusString();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public LeaveType getLeaveType() { return leaveType; }
    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getAppliedOn() { return appliedOn; }
    public void setAppliedOn(LocalDate appliedOn) { this.appliedOn = appliedOn; }
    
    public LeaveState getState() { return state; }
    public void setState(LeaveState state) { this.state = state; }
}
