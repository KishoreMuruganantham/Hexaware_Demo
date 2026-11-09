package com.hexaware.leavemanagement.controller;

import com.hexaware.leavemanagement.model.Employee;
import com.hexaware.leavemanagement.model.LeaveRequest;
import com.hexaware.leavemanagement.service.LeaveRequestService;
import com.hexaware.leavemanagement.utility.AuthUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "http://localhost:5173")
public class LeaveController {
    private final LeaveRequestService leaveRequestService;
    private final AuthUtility authUtility;

    public LeaveController(LeaveRequestService leaveRequestService, AuthUtility authUtility) {
        this.leaveRequestService = leaveRequestService;
        this.authUtility = authUtility;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeaveResponse applyLeave(@Valid @RequestBody ApplyLeaveRequest request,
                                    HttpServletRequest httpRequest) {
        Employee employee = authUtility.getLoggedInEmployee(httpRequest);
        return toResponse(leaveRequestService.applyLeave(employee,
                request.startDate(), request.endDate(), request.reason()));
    }

    @GetMapping("/my")
    public List<LeaveResponse> getMyRequests(HttpServletRequest request) {
        Employee employee = authUtility.getLoggedInEmployee(request);
        return leaveRequestService.getMyLeaveRequests(employee.getId())
                .stream().map(this::toResponse).toList();
    }

    private LeaveResponse toResponse(LeaveRequest leaveRequest) {
        return new LeaveResponse(leaveRequest.getId(), leaveRequest.getEmployee().getId(),
                leaveRequest.getEmployee().getName(), leaveRequest.getEmployee().getEmail(),
                leaveRequest.getStartDate(), leaveRequest.getEndDate(),
                leaveRequest.getReason(), leaveRequest.getStatus().name());
    }

    public record ApplyLeaveRequest(@NotNull LocalDate startDate,
                                    @NotNull LocalDate endDate,
                                    @NotBlank String reason) {
    }
    public record LeaveResponse(Long id, Long employeeId, String employeeName,
                                String employeeEmail, LocalDate startDate,
                                LocalDate endDate, String reason, String status) {
    }
}
