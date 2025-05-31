package org.example.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CustomError {

    private final List<Map<String, Object>> error = new ArrayList<>();

    public CustomError(String description) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", LocalDateTime.now());
        map.put("code", 400);
        map.put("description", description);
        error.add(map);
    }
}

