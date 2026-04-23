// Author: Member 1, 2, 3, 4 Combined API
package com.ooad.leave.controller;

import com.ooad.leave.model.*;
import com.ooad.leave.patterns.proxy.AdminAccessProxy;
import com.ooad.leave.patterns.facade.TeamCalendarFacade;
import com.ooad.leave.patterns.facade.CalendarEventDTO;
import com.ooad.leave.repository.UserRepository;
import com.ooad.leave.repository.LeaveTypeRepository;
import com.ooad.leave.repository.NotificationRepository;
import com.ooad.leave.service.LeaveService;
import com.ooad.leave.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*") // Allow frontend easy access
public class MainController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LeaveService leaveService;
    
    @Autowired
    private AdminAccessProxy adminProxy;
    
    @Autowired
    private TeamCalendarFacade calendarFacade;
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private NotificationRepository notificationRepo;
    
    @Autowired
    private LeaveTypeRepository leaveTypeRepo;

    // ----- AUTH MOCK -----
    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        User user = userRepository.findByUsername(username);
        if (user == null) throw new RuntimeException("User not found");
        return user;
    }

    // ----- MEMBER 1: EMPLOYEE -----
    @PostMapping("/leave")
    public LeaveRequest applyLeave(@RequestBody Map<String, String> payload) {
        Employee emp = (Employee) userRepository.findByUsername(payload.get("username"));
        LeaveType type = leaveTypeRepo.findById(Long.parseLong(payload.get("leaveTypeId"))).orElseThrow();
        
        LeaveRequest req = new LeaveRequest();
        req.setEmployee(emp);
        req.setLeaveType(type);
        req.setStartDate(java.time.LocalDate.parse(payload.get("startDate")));
        req.setEndDate(java.time.LocalDate.parse(payload.get("endDate")));
        req.setReason(payload.get("reason"));
        return leaveService.applyLeave(req);
    }

    @GetMapping("/leaves/{username}")
    public List<LeaveRequest> getMyLeaves(@PathVariable String username) {
        Employee emp = (Employee) userRepository.findByUsername(username);
        return leaveService.getEmployeeLeaves(emp);
    }
    
    @PostMapping("/leave/{id}/cancel")
    public LeaveRequest cancelLeave(@PathVariable Long id) {
        return leaveService.cancelLeave(id);
    }

    @GetMapping("/calendar")
    public List<CalendarEventDTO> getTeamCalendar() {
        return calendarFacade.getTeamCalendarEvents();
    }

    // ----- MEMBER 2: MANAGER -----
    @GetMapping("/leaves/pending")
    public List<LeaveRequest> getPendingLeaves() {
        return leaveService.getAllPendingLeaves();
    }

    @PostMapping("/leave/{id}/approve")
    public LeaveRequest approveLeave(@PathVariable Long id) {
        return leaveService.approveLeave(id);
    }

    @PostMapping("/leave/{id}/reject")
    public LeaveRequest rejectLeave(@PathVariable Long id) {
        return leaveService.rejectLeave(id);
    }

    // ----- MEMBER 3: ADMIN CONFIG -----
    @GetMapping("/leavetypes")
    public List<LeaveType> getLeaveTypes() {
        return adminProxy.getAllLeaveTypes();
    }
    
    @PostMapping("/leavetypes")
    public LeaveType addLeaveType(@RequestHeader("username") String username, @RequestBody LeaveType type) {
        User admin = userRepository.findByUsername(username);
        return adminProxy.addLeaveType(admin, type);
    }

    // ----- MEMBER 4: REPORTS & NOTIFICATIONS -----
    @PostMapping("/reports/generate")
    public Report generateReport(@RequestHeader("username") String username) {
        Admin admin = (Admin) userRepository.findByUsername(username);
        return reportService.generateSystemReport(admin);
    }
    
    @GetMapping("/reports")
    public List<Report> getReports() {
        return reportService.getAllReports();
    }
    
    @GetMapping("/notifications/{username}")
    public List<Notification> getNotifications(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return notificationRepo.findByUserOrderByCreatedAtDesc(user);
    }
}
