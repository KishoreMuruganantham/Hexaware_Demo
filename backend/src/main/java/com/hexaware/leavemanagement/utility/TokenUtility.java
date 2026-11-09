package com.hexaware.leavemanagement.utility;

import com.hexaware.leavemanagement.model.Employee;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TokenUtility {
    public String createToken(Employee employee) {
        String value = employee.getId() + ":" + employee.getEmail() + ":" + employee.getRole();
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public TokenUser readToken(String token) {
        try {
            String value = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
            String[] parts = value.split(":", 3);
            if (parts.length != 3) throw new IllegalArgumentException("Invalid token");
            return new TokenUser(Long.parseLong(parts[0]), parts[1], parts[2]);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("Invalid or expired login token");
        }
    }

    public record TokenUser(Long employeeId, String email, String role) {
    }
}
