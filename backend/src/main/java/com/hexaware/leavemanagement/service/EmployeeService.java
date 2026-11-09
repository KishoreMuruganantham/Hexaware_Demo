package com.hexaware.leavemanagement.service;

import com.hexaware.leavemanagement.dao.EmployeeDao;
import com.hexaware.leavemanagement.model.Employee;
import com.hexaware.leavemanagement.model.Role;
import com.hexaware.leavemanagement.utility.PasswordUtility;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeDao employeeDao;
    private final PasswordUtility passwordUtility;

    public EmployeeService(EmployeeDao employeeDao, PasswordUtility passwordUtility) {
        this.employeeDao = employeeDao;
        this.passwordUtility = passwordUtility;
    }

    public Employee registerEmployee(String name, String email, String password) {
        if (name == null || name.isBlank() || email == null || email.isBlank()
                || password == null || password.length() < 8) {
            throw new IllegalArgumentException("Name, email and a password of at least 8 characters are required");
        }
        String cleanEmail = email.trim().toLowerCase();
        if (employeeDao.existsByEmail(cleanEmail)) {
            throw new IllegalArgumentException("An account with this email already exists");
        }
        Employee employee = new Employee();
        employee.setName(name.trim());
        employee.setEmail(cleanEmail);
        employee.setPassword(passwordUtility.encodePassword(password));
        employee.setRole(Role.EMPLOYEE);
        return employeeDao.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeDao.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }

    public boolean checkPassword(String password, String encodedPassword) {
        return passwordUtility.matchPassword(password, encodedPassword);
    }
}
