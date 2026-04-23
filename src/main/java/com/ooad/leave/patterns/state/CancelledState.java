package com.ooad.leave.patterns.state;

import com.ooad.leave.model.LeaveRequest;

public class CancelledState implements LeaveState {
    @Override
    public void approve(LeaveRequest request) { throw new IllegalStateException("Cannot approve cancelled leave"); }
    @Override
    public void reject(LeaveRequest request) { throw new IllegalStateException("Cannot reject cancelled leave"); }
    @Override
    public void cancel(LeaveRequest request) { throw new IllegalStateException("Already cancelled"); }
    @Override
    public String getStatusString() { return "CANCELLED"; }
}
