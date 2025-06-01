import org.example.controller.UserController;
import org.example.dto.UserDTO;
import org.example.dto.UserResponse;
import org.example.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    UserServiceImpl userService;

    private UserDTO validUser;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        UserDTO validUser = new UserDTO();
        validUser.setName("John Doe");
        validUser.setEmail("john@example.com");
        validUser.setPassword("password123");

        userResponse = new UserResponse();
        userResponse.setUserId(UUID.randomUUID());
        userResponse.setToken("fake-jwt-token");
        userResponse.setActive(true);
    }


    @Test
    void testRegisterUser_ShouldReturnResponseEntity() {
        UserDTO userDTO = new UserDTO();
        ResponseEntity<?> expectedResponse = ResponseEntity.ok("User Registered");

        when(userService.register(userDTO)).thenReturn((ResponseEntity) expectedResponse);

        ResponseEntity<?> actualResponse = userController.registerUser(userDTO);

        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).register(userDTO);
    }

    @Test
    void testLogin_ShouldReturnResponseEntity() {
        String token = "Bearer dummyToken";
        ResponseEntity<?> expectedResponse = ResponseEntity.ok("User Logged In");

        when(userService.login(token)).thenReturn((ResponseEntity) expectedResponse);

        ResponseEntity<?> actualResponse = userController.login(token);

        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).login(token);
    }

    @Test
    void testGetUserById_ShouldReturnResponseEntity() {
        UUID userId = UUID.randomUUID();
        ResponseEntity<?> expectedResponse = ResponseEntity.ok("User Found");

        when(userService.getUserById(userId)).thenReturn((ResponseEntity) expectedResponse);

        ResponseEntity<?> actualResponse = userController.getUserById(userId);

        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).getUserById(userId);
    }
}
