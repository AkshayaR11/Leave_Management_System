package com.ooad.leave.patterns.facade;

import com.ooad.leave.model.LeaveRequest;
import com.ooad.leave.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Structural Pattern: Facade Pattern
// This facade abstracts complex repository/service calls and data transformations
// to provide a perfectly formatted array of events for the UI calendar.
@Service
public class TeamCalendarFacade {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    public List<CalendarEventDTO> getTeamCalendarEvents() {
        // We only want to show APPROVED or PENDING leaves on the team calendar
        List<LeaveRequest> allLeaves = leaveRequestRepository.findAll();
        
        return allLeaves.stream()
                .filter(l -> l.getStatus().equals("APPROVED") || l.getStatus().equals("PENDING"))
                .map(l -> {
                    String color = l.getStatus().equals("APPROVED") ? "#10b981" : "#f59e0b"; // Green for approved, Yellow for pending
                    String title = l.getEmployee().getName() + " (" + l.getLeaveType().getName() + ")";
                    return new CalendarEventDTO(title, l.getStartDate(), l.getEndDate(), color, l.getStatus());
                })
                .collect(Collectors.toList());
    }
}
