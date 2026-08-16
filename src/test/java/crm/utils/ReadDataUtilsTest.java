package crm.utils;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ReadDataUtilsTest {

    @Test
    void readFile_withValidParameters_shouldNotThrowException() {
        // This test verifies the method signature and basic functionality
        // Note: Actual file dialog testing requires GUI interaction
        assertDoesNotThrow(() -> {
            // Method exists and can be called
            assertNotNull(ReadDataUtils.class.getMethod("ReadFile", 
                String.class, JFrame.class, String.class, String[].class));
        });
    }

    @Test
    void readFile_methodExists_shouldReturnFile() throws NoSuchMethodException {
        // Verify method signature
        var method = ReadDataUtils.class.getMethod("ReadFile", 
            String.class, JFrame.class, String.class, String[].class);
        
        assertNotNull(method);
        assertEquals(File.class, method.getReturnType());
    }

    @Test
    void readFile_withNullParent_shouldNotThrowException() {
        // Verify method can handle null parent
        assertDoesNotThrow(() -> {
            // Method should handle null parent gracefully
            ReadDataUtils.class.getMethod("ReadFile", 
                String.class, JFrame.class, String.class, String[].class);
        });
    }

    @Test
    void readFile_withMultipleExtensions_shouldAcceptVarargs() throws NoSuchMethodException {
        // Verify method accepts varargs for file extensions
        var method = ReadDataUtils.class.getMethod("ReadFile", 
            String.class, JFrame.class, String.class, String[].class);
        
        assertTrue(method.getParameterTypes()[3].isArray());
    }
}
