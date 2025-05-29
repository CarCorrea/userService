package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.User;

import java.util.UUID;

@Getter
@Setter
public class UserResponse {

    private UUID userId;

    private String email;

    private String dateCreated;

    private String lastLogin;

    private String token;

    private boolean isActive;

    public UserResponse(User user){
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.dateCreated = user.getDateCreated().toString();
        this.lastLogin = user.getLastLogin().toString();
        this.token = user.getToken();
        this.isActive = user.isActive();
    }
}
