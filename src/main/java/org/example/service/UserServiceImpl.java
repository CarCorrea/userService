package org.example.service;

import org.example.dto.UserDTO;
import org.example.dto.UserResponse;
import org.example.entity.User;
import org.example.exceptions.CustomError;
import org.example.repository.UserRepository;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseEntity<?> register(UserDTO userDTO){

        Optional<String> error = validateCredentials(userDTO);

        Optional<ResponseEntity<CustomError>> errorResponse = error
                .map(msge -> ResponseEntity.badRequest().body(new CustomError(msge)));

        errorResponse = errorResponse.isEmpty() && userRepository.existsByEmail(userDTO.getEmail())
                ? Optional.of(ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomError("El usuario ya existe en el sistema")))
                : errorResponse;

        if(errorResponse.isPresent()){
            return errorResponse.get();
        }
        else{
            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setDateCreated(LocalDateTime.now());
            user.setLastLogin(LocalDateTime.now());
            user.setActive(true);
            user.setUserPhones(userDTO.getUserPhones());

            String token = jwtUtil.generateToken(user.getEmail());
            user.setToken(token);
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(new UserResponse(savedUser));
        }
    }

    @Override
    public ResponseEntity<?> login(String token) {
        return Optional.ofNullable(token)
                .map(tkn -> tkn.replace("Bearer", ""))
                .map(token1 -> {
                    return jwtUtil.extractUsername(token1);
                })
                .flatMap(email -> userRepository.findByEmail(email).map(user -> {
                    user.setLastLogin(LocalDateTime.now());
                    String newToken = (jwtUtil.generateToken(email));
                    user.setToken(newToken);
                    userRepository.save(user);
                    return ResponseEntity.ok((Object) new UserResponse(user));
                }))
                .orElseGet(()-> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error("Token invalido")));
    }

    @Override
    public ResponseEntity<?> getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok((Object) new UserResponse(user)))
                .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomError("Usuario no existe")));
    }

    private static Optional<String> validateCredentials(UserDTO userDTO) {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        Map<String, Predicate<String>> validate = Map.of(
                "Email inválido", e -> e.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"),
                "La contraseña debe tener solo una mayúscula, un máximo de 2 números y entre 8 a 12 caracteres",
                p -> p.matches("^(?=(?:[^A-Z]*[A-Z]){1}[^A-Z]*$)(?=(?:\\D*\\d){0,2}\\D*$)[A-Za-z\\d]{8,12}$")
        );

        return validate.entrySet().stream()
                .filter(e -> {
                    if (e.getKey().startsWith("Email")) return !e.getValue().test(email);
                    return !e.getValue().test(password);
                })
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
