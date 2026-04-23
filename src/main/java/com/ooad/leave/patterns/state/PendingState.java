package com.ooad.leave.patterns.state;

import com.ooad.leave.model.LeaveRequest;

public class PendingState implements LeaveState {

    @Override
    public void approve(LeaveRequest request) {
        request.setState(new ApprovedState());
    }

    @Override
    public void reject(LeaveRequest request) {
        request.setState(new RejectedState());
    }

    @Override
    public void cancel(LeaveRequest request) {
        request.setState(new CancelledState());
    }

    @Override
    public String getStatusString() {
        return "PENDING";
    }
}
