package org.example.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Error {

    private List<Map<String, Object>> error = new ArrayList<>();

    public Error(String description) {

        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", LocalDateTime.now());
        map.put("code", 400);
        map.put("Description", description);
        error.add(map);
    }

}
