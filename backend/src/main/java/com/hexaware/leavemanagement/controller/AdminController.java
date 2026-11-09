package com.hexaware.leavemanagement.controller;

import com.hexaware.leavemanagement.model.LeaveRequest;
import com.hexaware.leavemanagement.model.LeaveStatus;
import com.hexaware.leavemanagement.service.LeaveRequestService;
import com.hexaware.leavemanagement.utility.AuthUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    private final LeaveRequestService leaveRequestService;
    private final AuthUtility authUtility;

    public AdminController(LeaveRequestService leaveRequestService, AuthUtility authUtility) {
        this.leaveRequestService = leaveRequestService;
        this.authUtility = authUtility;
    }

    @GetMapping("/leaves")
    public List<LeaveController.LeaveResponse> getAllLeaveRequests(HttpServletRequest request) {
        authUtility.requireAdmin(request);
        return leaveRequestService.getAllLeaveRequests().stream().map(this::toResponse).toList();
    }

    @PutMapping("/leaves/{id}/approve")
    public LeaveController.LeaveResponse approveLeave(@PathVariable Long id, HttpServletRequest request) {
        authUtility.requireAdmin(request);
        return toResponse(leaveRequestService.updateLeaveStatus(id, LeaveStatus.APPROVED));
    }

    @PutMapping("/leaves/{id}/reject")
    public LeaveController.LeaveResponse rejectLeave(@PathVariable Long id, HttpServletRequest request) {
        authUtility.requireAdmin(request);
        return toResponse(leaveRequestService.updateLeaveStatus(id, LeaveStatus.REJECTED));
    }

    private LeaveController.LeaveResponse toResponse(LeaveRequest leaveRequest) {
        return new LeaveController.LeaveResponse(leaveRequest.getId(),
                leaveRequest.getEmployee().getId(), leaveRequest.getEmployee().getName(),
                leaveRequest.getEmployee().getEmail(), leaveRequest.getStartDate(),
                leaveRequest.getEndDate(), leaveRequest.getReason(),
                leaveRequest.getStatus().name());
    }
}
