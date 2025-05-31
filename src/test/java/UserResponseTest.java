import org.example.dto.UserResponse;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserResponseTest {

    private User user;
    private UUID userId;
    private LocalDateTime created;
    private LocalDateTime lastLogin;

    @BeforeEach
    void setUp() {
        user = new User();
        userId = UUID.randomUUID();
        created = LocalDateTime.now().minusDays(1);
        lastLogin = LocalDateTime.now();

        user.setUserId(userId);
        user.setEmail("test@example.com");
        user.setDateCreated(created);
        user.setLastLogin(lastLogin);
        user.setToken("token123");
        user.setActive(true);
    }

    @Test
    void testUserResponseConstructor() {
        UserResponse response = new UserResponse(user);

        assertEquals(userId, response.getUserId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(created.toString(), response.getDateCreated());
        assertEquals(lastLogin.toString(), response.getLastLogin());
        assertEquals("token123", response.getToken());
        assertTrue(response.isActive());
    }
}
