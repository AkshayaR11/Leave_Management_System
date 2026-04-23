package com.ooad.leave.patterns.state;

import com.ooad.leave.model.LeaveRequest;

// Behavioral Pattern: State Pattern
public interface LeaveState {
    void approve(LeaveRequest request);
    void reject(LeaveRequest request);
    void cancel(LeaveRequest request);
    String getStatusString();
}
