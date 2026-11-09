package com.hexaware.leavemanagement.controller;

import com.hexaware.leavemanagement.model.Employee;
import com.hexaware.leavemanagement.service.AuthService;
import com.hexaware.leavemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final EmployeeService employeeService;
    private final AuthService authService;

    public AuthController(EmployeeService employeeService, AuthService authService) {
        this.employeeService = employeeService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse register(@Valid @RequestBody RegisterRequest request) {
        Employee employee = employeeService.registerEmployee(request.name(), request.email(), request.password());
        return new EmployeeResponse(employee.getId(), employee.getName(),
                employee.getEmail(), employee.getRole().name());
    }

    @PostMapping("/login")
    public AuthService.LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request.email(), request.password());
    }

    public record RegisterRequest(@NotBlank String name,
                                  @NotBlank @Email String email,
                                  @NotBlank @Size(min = 8) String password) {
    }
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {
    }
    public record EmployeeResponse(Long id, String name, String email, String role) {
    }
}
