package org.example.service;

import org.example.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IUserService {

    ResponseEntity<?> register(UserDTO userDTO);

    ResponseEntity<?> login(String  token);

    ResponseEntity<?> getUserById(UUID userId);
}
