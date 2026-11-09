package com.hexaware.leavemanagement.service;

import com.hexaware.leavemanagement.dao.LeaveRequestDao;
import com.hexaware.leavemanagement.model.Employee;
import com.hexaware.leavemanagement.model.LeaveRequest;
import com.hexaware.leavemanagement.model.LeaveStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveRequestService {
    private final LeaveRequestDao leaveRequestDao;

    public LeaveRequestService(LeaveRequestDao leaveRequestDao) {
        this.leaveRequestDao = leaveRequestDao;
    }

    public LeaveRequest applyLeave(Employee employee, LocalDate startDate,
                                   LocalDate endDate, String reason) {
        if (startDate == null || endDate == null || reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Start date, end date and reason are required");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Leave start date cannot be in the past");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setStartDate(startDate);
        leaveRequest.setEndDate(endDate);
        leaveRequest.setReason(reason.trim());
        leaveRequest.setStatus(LeaveStatus.PENDING);
        return leaveRequestDao.save(leaveRequest);
    }

    public List<LeaveRequest> getMyLeaveRequests(Long employeeId) {
        return leaveRequestDao.findByEmployeeIdOrderByIdDesc(employeeId);
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestDao.findAllByOrderByIdDesc();
    }

    @Transactional
    public LeaveRequest updateLeaveStatus(Long leaveId, LeaveStatus status) {
        LeaveRequest leaveRequest = leaveRequestDao.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));
        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Only pending leave requests can be processed");
        }
        leaveRequest.setStatus(status);
        return leaveRequestDao.save(leaveRequest);
    }
}
