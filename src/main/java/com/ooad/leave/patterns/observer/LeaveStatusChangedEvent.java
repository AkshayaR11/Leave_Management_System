package com.ooad.leave.patterns.observer;

import com.ooad.leave.model.LeaveRequest;
import org.springframework.context.ApplicationEvent;

// Behavioral Pattern: Observer Pattern (Event Subject)
public class LeaveStatusChangedEvent extends ApplicationEvent {
    private final LeaveRequest leaveRequest;

    public LeaveStatusChangedEvent(Object source, LeaveRequest leaveRequest) {
        super(source);
        this.leaveRequest = leaveRequest;
    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }
}
