package org.example.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue
    private UUID userId;

    private String name;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @OneToMany
    private List<Phone> userPhones;

    private LocalDateTime dateCreated;

    private LocalDateTime lastLogin;

    private boolean isActive;

    private String token;


}
