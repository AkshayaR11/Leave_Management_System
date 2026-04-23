package com.ooad.leave.patterns.state;

import com.ooad.leave.model.LeaveRequest;

public class RejectedState implements LeaveState {
    @Override
    public void approve(LeaveRequest request) { throw new IllegalStateException("Cannot approve rejected leave"); }
    @Override
    public void reject(LeaveRequest request) { throw new IllegalStateException("Already rejected"); }
    @Override
    public void cancel(LeaveRequest request) { throw new IllegalStateException("Cannot cancel rejected leave"); }
    @Override
    public String getStatusString() { return "REJECTED"; }
}
