// Author: Member 4
package com.ooad.leave.service;

import com.ooad.leave.model.Admin;
import com.ooad.leave.model.Report;
import com.ooad.leave.model.LeaveRequest;
import com.ooad.leave.repository.LeaveRequestRepository;
import com.ooad.leave.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;
    
    @Autowired
    private LeaveRequestRepository leaveRepository;

    public Report generateSystemReport(Admin admin) {
        List<LeaveRequest> allLeaves = leaveRepository.findAll();
        long pending = allLeaves.stream().filter(l -> l.getStatus().equals("PENDING")).count();
        long approved = allLeaves.stream().filter(l -> l.getStatus().equals("APPROVED")).count();
        long rejected = allLeaves.stream().filter(l -> l.getStatus().equals("REJECTED")).count();
        
        Report report = new Report();
        report.setTitle("Monthly Leave Report");
        report.setContent("Total Leaves: " + allLeaves.size() + ", Pending: " + pending + ", Approved: " + approved + ", Rejected: " + rejected);
        report.setGeneratedOn(LocalDateTime.now());
        report.setGeneratedBy(admin);
        
        return reportRepository.save(report);
    }
    
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
}
