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
        TokenUtility.TokenUser tokenUser = tokenUtility.readToken(header.substring(7));
        Employee employee = employeeService.getEmployeeById(tokenUser.employeeId());
        if (!employee.getEmail().equals(tokenUser.email()) ||
                !employee.getRole().name().equals(tokenUser.role())) {
            throw new IllegalArgumentException("Invalid login token");
        }
        return employee;
    }

    public Employee requireAdmin(HttpServletRequest request) {
        Employee employee = getLoggedInEmployee(request);
        if (employee.getRole() != Role.ADMIN) {
            throw new SecurityException("Only administrators can perform this action");
        }
        return employee;
    }
}
