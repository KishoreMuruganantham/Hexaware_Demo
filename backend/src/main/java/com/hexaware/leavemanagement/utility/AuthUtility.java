package com.hexaware.leavemanagement.utility;

import com.hexaware.leavemanagement.model.Employee;
import com.hexaware.leavemanagement.model.Role;
import com.hexaware.leavemanagement.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthUtility {
    private final TokenUtility tokenUtility;
    private final EmployeeService employeeService;

    public AuthUtility(TokenUtility tokenUtility, EmployeeService employeeService) {
        this.tokenUtility = tokenUtility;
        this.employeeService = employeeService;
    }

    public Employee getLoggedInEmployee(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Please log in to continue");
        }
        return employeeService.getEmployeeById(tokenUtility.readToken(header.substring(7)));
    }

    public Employee requireAdmin(HttpServletRequest request) {
        Employee employee = getLoggedInEmployee(request);
        if (employee.getRole() != Role.ADMIN) {
            throw new SecurityException("Only administrators can perform this action");
        }
        return employee;
    }
}
