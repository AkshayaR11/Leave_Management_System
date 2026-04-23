package com.ooad.leave.config;

import com.ooad.leave.model.*;
import com.ooad.leave.patterns.factory.UserFactory;
import com.ooad.leave.repository.LeaveTypeRepository;
import com.ooad.leave.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize Admin
        Admin admin = (Admin) UserFactory.createUser("ADMIN");
        admin.setUsername("admin");
        admin.setName("System Admin");
        userRepository.save(admin);

        // Initialize Manager
        Manager manager = (Manager) UserFactory.createUser("MANAGER");
        manager.setUsername("manager");
        manager.setName("John Manager");
        manager.setManagedDepartment("Engineering");
        userRepository.save(manager);

        // Initialize Employee
        Employee employee = (Employee) UserFactory.createUser("EMPLOYEE");
        employee.setUsername("employee");
        employee.setName("Alice Employee");
        employee.setDepartment("Engineering");
        employee.setLeaveBalance(20);
        employee.setSickLeaveBalance(10);
        employee.setCasualLeaveBalance(10);
        employee.setEarnedLeaveBalance(10);
        employee.setMaternityLeaveBalance(10);
        userRepository.save(employee);

        // Initialize another Employee (John)
        Employee johnEmp = (Employee) UserFactory.createUser("EMPLOYEE");
        johnEmp.setUsername("john");
        johnEmp.setName("John Employee");
        johnEmp.setDepartment("Engineering");
        johnEmp.setLeaveBalance(20);
        johnEmp.setSickLeaveBalance(10);
        johnEmp.setCasualLeaveBalance(10);
        johnEmp.setEarnedLeaveBalance(10);
        johnEmp.setMaternityLeaveBalance(10);
        userRepository.save(johnEmp);

        // Initialize Leave Types
        LeaveType sick = new LeaveType();
        sick.setName("Sick Leave");
        sick.setMaxDays(10);
        sick.setDescription("For medical emergencies");
        leaveTypeRepository.save(sick);

        LeaveType casual = new LeaveType();
        casual.setName("Casual Leave");
        casual.setMaxDays(10);
        casual.setDescription("For personal reasons");
        leaveTypeRepository.save(casual);

        LeaveType earned = new LeaveType();
        earned.setName("Earned Leave");
        earned.setMaxDays(15);
        earned.setDescription("Accrued leave based on service");
        leaveTypeRepository.save(earned);

        LeaveType maternity = new LeaveType();
        maternity.setName("Maternity Leave");
        maternity.setMaxDays(90);
        maternity.setDescription("For maternity purposes");
        leaveTypeRepository.save(maternity);
    }
}
