import org.example.dto.UserDTO;
import org.example.dto.UserResponse;
import org.example.entity.User;
import org.example.exceptions.CustomError;
import org.example.repository.UserRepository;
import org.example.service.UserServiceImpl;
import org.example.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setName("Dummy");
        userDTO.setEmail("dummy@test.com");
        userDTO.setPassword("Password34");


        user = new User();
        user.setUserId(UUID.randomUUID());
        user.setName("Dummy");
        user.setEmail("dummy@test.com");
        user.setPassword("Password34");
        user.setDateCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);

    }

    @Test
    void testRegisterSuccess() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(jwtUtil.generateToken(anyString())).thenReturn("mockedToken");
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userService.register(userDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof UserResponse);
    }

    @Test
    void testRegisterExistingUser() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        ResponseEntity<?> response = userService.register(userDTO);

        assertEquals(409, response.getStatusCodeValue());
    }

    @Test
    void testLoginSuccess() {
        String token = "Bearer mocked.jwt.token";
        when(jwtUtil.extractUsername(anyString())).thenReturn("test@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(anyString())).thenReturn("newToken");
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userService.login(token);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof UserResponse);
    }

    @Test
    void testLoginInvalidToken() {
        ResponseEntity<?> response = userService.login(null);
        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void testGetUserByIdSuccess() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userService.getUserById(user.getUserId());

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof UserResponse);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.getUserById(UUID.randomUUID());

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testRegisterInvalidEmail() {
        userDTO.setEmail("invalid-email");

        ResponseEntity<?> response = userService.register(userDTO);

        assertEquals(400, response.getStatusCodeValue());

        CustomError customError = (CustomError) response.getBody();
        String description = (String) customError.getError().get(0).get("description");
        assertEquals("Email inválido", description);
    }

    @Test
    void testRegisterInvalidPassword() {
        userDTO.setPassword("abc"); // demasiado simple

        ResponseEntity<?> response = userService.register(userDTO);

        CustomError customError = (CustomError) response.getBody();
        String description = (String) customError.getError().get(0).get("description");
        assertEquals("La contraseña debe tener solo una mayúscula, un máximo de 2 números y entre 8 a 12 caracteres", description);
    }

    @Test
    void testRegisterValidWithoutPhones() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(jwtUtil.generateToken(anyString())).thenReturn("mockedToken");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0)); // echo back

        userDTO.setUserPhones(null);

        ResponseEntity<?> response = userService.register(userDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof UserResponse);
    }

}
