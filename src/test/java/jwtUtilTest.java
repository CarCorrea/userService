
import org.example.service.IUserService;
import org.example.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class jwtUtilTest {

    private JwtUtil jwtUtil;
    private final String rawSecret = "mySuperSecretKeyForJwt1234567890";
    private final String base64Key = Base64.getEncoder().encodeToString(rawSecret.getBytes());

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();

        Field keyField = JwtUtil.class.getDeclaredField("key");
        keyField.setAccessible(true);
        keyField.set(jwtUtil, base64Key);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("test@example.com");

        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
    }

    @Test
    void testExtractUsername() {
        String subject = "test@example.com";
        String token = jwtUtil.generateToken(subject);

        String extracted = jwtUtil.extractUsername(token);

        assertEquals(subject, extracted);
    }

    @Test
    void testInvalidKeyThrowsException() throws Exception {
        JwtUtil brokenJwtUtil = new JwtUtil();

        Field keyField = JwtUtil.class.getDeclaredField("key");
        keyField.setAccessible(true);
        keyField.set(brokenJwtUtil, "");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> brokenJwtUtil.generateToken("test@example.com"));

        assertTrue(exception.getMessage().contains("clave secreta"));
    }

    @Test
    void testGetSigningKeyThrowsExceptionWhenKeyIsBlank() throws NoSuchFieldException, IllegalAccessException {
        JwtUtil jwtUtil = new JwtUtil();

        Field field = JwtUtil.class.getDeclaredField("key");
        field.setAccessible(true);
        field.set(jwtUtil, "");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> jwtUtil.generateToken("test@example.com"));
        assertEquals("La clave secreta (jwt.secret) no puede estar vacía.", exception.getMessage());
    }
}
