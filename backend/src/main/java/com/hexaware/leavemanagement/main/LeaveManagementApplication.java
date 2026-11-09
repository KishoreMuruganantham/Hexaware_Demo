package com.hexaware.leavemanagement.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hexaware.leavemanagement")
public class LeaveManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeaveManagementApplication.class, args);
    }
}
