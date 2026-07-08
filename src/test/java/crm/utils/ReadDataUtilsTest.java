package crm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadDataUtilsTest {

    @Test
    void testReadFile_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
                ReadDataUtils.ReadFile("Select CSV file", null, "Only CSV Files", "csv")
        );
    }

    @Test
    void testReadFile_exceptionMessageContainsNotSupported() {
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class, () ->
                ReadDataUtils.ReadFile("message", null, "description", "txt")
        );
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("not supported"));
    }

    @Test
    void testReadFile_withMultipleExtensions_throwsException() {
        assertThrows(UnsupportedOperationException.class, () ->
                ReadDataUtils.ReadFile("Select file", null, "Files", "csv", "txt", "xls")
        );
    }

    @Test
    void testReadFile_withNullParent_throwsException() {
        assertThrows(UnsupportedOperationException.class, () ->
                ReadDataUtils.ReadFile("msg", null, "desc", "pdf")
        );
    }

    @Test
    void testReadFile_withEmptyExtension_throwsException() {
        assertThrows(UnsupportedOperationException.class, () ->
                ReadDataUtils.ReadFile("msg", null, "desc")
        );
    }
}
