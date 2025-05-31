import org.example.exceptions.CustomError;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomErrorTest {

    @Test
    void testErrorConstructor() {
        String description = "Email ya registrado";
        CustomError customError = new CustomError(description);

        List<Map<String, Object>> errorList = customError.getError();
        assertEquals(1, errorList.size());

        Map<String, Object> errorMap = errorList.get(0);
        assertTrue(errorMap.containsKey("timestamp"));
        assertTrue(errorMap.containsKey("code"));
        assertTrue(errorMap.containsKey("description"));

        assertEquals(400, errorMap.get("code"));
        assertEquals(description, errorMap.get("description"));
        assertNotNull(errorMap.get("timestamp"));
        assertTrue(errorMap.get("timestamp") instanceof java.time.LocalDateTime);

    }
}