package com.hexaware.leavemanagement.service;

import com.hexaware.leavemanagement.model.Employee;
import com.hexaware.leavemanagement.utility.TokenUtility;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final EmployeeService employeeService;
    private final TokenUtility tokenUtility;

    public AuthService(EmployeeService employeeService, TokenUtility tokenUtility) {
        this.employeeService = employeeService;
        this.tokenUtility = tokenUtility;
    }

    public LoginResponse login(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
        Employee employee = employeeService.getEmployeeByEmail(email);
        if (!employeeService.checkPassword(password, employee.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return new LoginResponse(tokenUtility.createToken(employee),
                employee.getId(), employee.getName(), employee.getEmail(), employee.getRole().name());
    }

    public record LoginResponse(String token, Long id, String name, String email, String role) {
    }
}
