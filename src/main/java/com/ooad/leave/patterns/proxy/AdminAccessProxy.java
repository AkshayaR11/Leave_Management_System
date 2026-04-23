// Author: Member 3
package com.ooad.leave.patterns.proxy;

import com.ooad.leave.model.LeaveType;
import com.ooad.leave.model.User;
import com.ooad.leave.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// Structural Pattern: Proxy Pattern
// Acts as a proxy to the LeaveTypeRepository to enforce admin-only access
@Service
public class AdminAccessProxy {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    public LeaveType addLeaveType(User user, LeaveType leaveType) {
        if (!"ADMIN".equals(user.getClass().getAnnotation(jakarta.persistence.DiscriminatorValue.class).value())) {
            throw new SecurityException("Access Denied: Only Admins can modify Leave Types.");
        }
        return leaveTypeRepository.save(leaveType);
    }
    
    public void deleteLeaveType(User user, Long id) {
        if (!"ADMIN".equals(user.getClass().getAnnotation(jakarta.persistence.DiscriminatorValue.class).value())) {
            throw new SecurityException("Access Denied: Only Admins can delete Leave Types.");
        }
        leaveTypeRepository.deleteById(id);
    }
}
