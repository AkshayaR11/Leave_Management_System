// Author: Member 4
package com.ooad.leave.service;

import com.ooad.leave.model.Notification;
import com.ooad.leave.repository.NotificationRepository;
import com.ooad.leave.patterns.observer.LeaveStatusChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

// Behavioral Pattern: Observer Pattern (Event Listener)
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @EventListener
    public void handleLeaveStatusChange(LeaveStatusChangedEvent event) {
        Notification notification = new Notification();
        notification.setUser(event.getLeaveRequest().getEmployee());
        notification.setMessage("Your leave request from " + event.getLeaveRequest().getStartDate() + 
            " to " + event.getLeaveRequest().getEndDate() + " has been marked as: " + 
            event.getLeaveRequest().getStatus());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notificationRepository.save(notification);
    }
}
