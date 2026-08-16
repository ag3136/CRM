package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class ExcelViewTest {

    private ExcelView excelView;

    @BeforeEach
    void setUp() {
        excelView = new ExcelView();
    }

    @Test
    void excelView_shouldCreateInstance() {
        assertNotNull(excelView);
    }

    @Test
    void excelView_shouldExtendAbstractXlsView() {
        assertTrue(excelView instanceof org.springframework.web.servlet.view.document.AbstractXlsView);
    }
}
