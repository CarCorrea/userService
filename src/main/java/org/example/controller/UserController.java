package org.example.controller;

import org.example.dto.UserDTO;
import org.example.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/userService")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/user-registration")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO userDTO){
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String token){
        return userService.login(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id){
        return userService.getUserById(id);
    }

}
