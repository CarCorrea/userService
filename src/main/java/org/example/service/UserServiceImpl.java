package org.example.service;

import org.example.dto.UserDTO;
import org.example.dto.UserResponse;
import org.example.entity.User;
import org.example.exceptions.Error;
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

    @Override
    public ResponseEntity<?> register(UserDTO userDTO){
        Map<Predicate<String>, String> validate = Map.of(
                email -> email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"), "Ingrese un correo electronico",
                password -> password.matches("^(?=.*[A-Z])(?=(?:.*\\d){2})(?=.*[a-z]).{8,12}$"), "La contraseña debe tener solo una mayuscula, un maximo de 2 numeros y entre 8 a 12 caracteres"
        );

        Optional<String> error = validate.entrySet().stream()
                .filter(entry -> !entry.getKey().test(entry.getKey() == validate.keySet().toArray()[0] ? userDTO.getEmail() : userDTO.getPassword()))
                .map(Map.Entry::getValue)
                .findFirst();

        Optional<ResponseEntity<Error>> errorResponse = Optional.ofNullable(error.orElse(null))
                .map(msge -> ResponseEntity.badRequest().body(new Error(msge)));

        errorResponse = errorResponse.isEmpty() && userRepository.existByEmail(userDTO.getEmail())
                ? Optional.of(ResponseEntity.status(HttpStatus.CONFLICT).body(new Error("El usuario ya existe en el sistema")))
                : errorResponse;

        if(errorResponse.isPresent()) return errorResponse.get();

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setDateCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);
        user.setUserPhones(userDTO.getUserPhones());

        String token = JwtUtil.generateToken(user.getEmail());
        user.setToken(token);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new UserResponse(savedUser));

    }

    @Override
    public ResponseEntity<?> login(String token) {
        return Optional.ofNullable(token)
                .map(tkn -> tkn.replace("Bearer", ""))
                .map(JwtUtil::extractUserName)
                .flatMap(email -> userRepository.findByEmail(email).map(user -> {
                    user.setLastLogin(LocalDateTime.now());
                    String newToken = (JwtUtil.generateToken(email));
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
                .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Usuario no existe")));
    }
}
