package crm.view;

import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewTest {

    private CsvView csvView;

    @BeforeEach
    void setUp() {
        csvView = new CsvView();
    }

    @Test
    void csvView_shouldCreateInstance() {
        assertNotNull(csvView);
    }

    @Test
    void csvView_shouldExtendAbstractCsvView() {
        assertTrue(csvView instanceof AbstractCsvView);
    }

    @Test
    void getContentType_shouldReturnTextCsv() {
        // Act
        String contentType = csvView.getContentType();

        // Assert
        assertEquals("text/csv", contentType);
    }
}
