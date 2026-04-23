package com.ooad.leave.patterns.state;

import com.ooad.leave.model.LeaveRequest;

public class ApprovedState implements LeaveState {
    @Override
    public void approve(LeaveRequest request) { throw new IllegalStateException("Already approved"); }
    @Override
    public void reject(LeaveRequest request) { throw new IllegalStateException("Cannot reject approved leave"); }
    @Override
    public void cancel(LeaveRequest request) { throw new IllegalStateException("Cannot cancel approved leave"); }
    @Override
    public String getStatusString() { return "APPROVED"; }
}
