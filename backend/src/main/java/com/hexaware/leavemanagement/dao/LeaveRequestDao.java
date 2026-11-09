package com.hexaware.leavemanagement.dao;

import com.hexaware.leavemanagement.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestDao extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeIdOrderByIdDesc(Long employeeId);
    List<LeaveRequest> findAllByOrderByIdDesc();
}
