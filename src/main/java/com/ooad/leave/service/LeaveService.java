// Author: Member 1 & 2
package com.ooad.leave.service;

import com.ooad.leave.model.Employee;
import com.ooad.leave.model.LeaveRequest;
import com.ooad.leave.repository.LeaveRequestRepository;
import com.ooad.leave.repository.UserRepository;
import com.ooad.leave.patterns.observer.LeaveStatusChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public LeaveRequest applyLeave(LeaveRequest request) {
        // Validate dates
        LocalDate today = LocalDate.now();
        if (request.getStartDate().isBefore(today) || request.getEndDate().isBefore(today)) {
            throw new IllegalArgumentException("Leave dates cannot be in the past");
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        // Calculate requested days
        long days = java.time.temporal.ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

        // Check leave balance for the type
        Employee emp = request.getEmployee();
        String typeName = request.getLeaveType().getName();
        int availableBalance = getBalanceForType(emp, typeName);
        if (days > availableBalance) {
            throw new IllegalArgumentException("Insufficient leave balance for " + typeName + ". Available: " + availableBalance + ", Requested: " + days);
        }

        request.setStatus("PENDING");
        request.setAppliedOn(today);
        LeaveRequest saved = leaveRepository.save(request);
        // Observer Pattern Trigger
        eventPublisher.publishEvent(new LeaveStatusChangedEvent(this, saved));
        return saved;
    }
    
    public LeaveRequest cancelLeave(Long leaveId) {
        LeaveRequest leave = leaveRepository.findById(leaveId).orElseThrow();
        leave.cancel(); // Behavioral Pattern: State
        LeaveRequest saved = leaveRepository.save(leave);
        eventPublisher.publishEvent(new LeaveStatusChangedEvent(this, saved));
        return saved;
    }

    public List<LeaveRequest> getEmployeeLeaves(Employee emp) {
        return leaveRepository.findByEmployee(emp);
    }
    
    public List<LeaveRequest> getAllPendingLeaves() {
        return leaveRepository.findByStatus("PENDING");
    }

    public LeaveRequest approveLeave(Long leaveId) {
        LeaveRequest leave = leaveRepository.findById(leaveId).orElseThrow();
        leave.approve(); // State Transition

        // Deduct leave balance
        Employee emp = leave.getEmployee();
        long days = java.time.temporal.ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
        deductBalanceForType(emp, leave.getLeaveType().getName(), (int) days);

        LeaveRequest saved = leaveRepository.save(leave);
        eventPublisher.publishEvent(new LeaveStatusChangedEvent(this, saved));
        return saved;
    }

    public LeaveRequest rejectLeave(Long leaveId) {
        LeaveRequest leave = leaveRepository.findById(leaveId).orElseThrow();
        leave.reject(); // State Transition
        LeaveRequest saved = leaveRepository.save(leave);
        eventPublisher.publishEvent(new LeaveStatusChangedEvent(this, saved));
        return saved;
    }

    private int getBalanceForType(Employee emp, String typeName) {
        switch (typeName) {
            case "Sick Leave": return emp.getSickLeaveBalance();
            case "Casual Leave": return emp.getCasualLeaveBalance();
            case "Earned Leave": return emp.getEarnedLeaveBalance();
            case "Maternity Leave": return emp.getMaternityLeaveBalance();
            default: return 0;
        }
    }

    private void deductBalanceForType(Employee emp, String typeName, int days) {
        switch (typeName) {
            case "Sick Leave": emp.setSickLeaveBalance(emp.getSickLeaveBalance() - days); break;
            case "Casual Leave": emp.setCasualLeaveBalance(emp.getCasualLeaveBalance() - days); break;
            case "Earned Leave": emp.setEarnedLeaveBalance(emp.getEarnedLeaveBalance() - days); break;
            case "Maternity Leave": emp.setMaternityLeaveBalance(emp.getMaternityLeaveBalance() - days); break;
        }
        userRepository.save(emp);
    }
}
