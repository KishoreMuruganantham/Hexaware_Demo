package com.hexaware.leavemanagement.utility;

import com.hexaware.leavemanagement.model.Employee;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class TokenUtility {
    private final Map<String, Long> sessions = new ConcurrentHashMap<>();

    public String createToken(Employee employee) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, employee.getId());
        return token;
    }

    public Long readToken(String token) {
        Long id = sessions.get(token);
        if (id == null) throw new IllegalArgumentException("Invalid login token");
        return id;
    }
}
