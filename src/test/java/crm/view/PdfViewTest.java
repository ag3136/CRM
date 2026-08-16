package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewTest {

    private PdfView pdfView;

    @BeforeEach
    void setUp() {
        pdfView = new PdfView();
    }

    @Test
    void pdfView_shouldCreateInstance() {
        assertNotNull(pdfView);
    }

    @Test
    void pdfView_shouldExtendAbstractPdfView() {
        assertTrue(pdfView instanceof AbstractPdfView);
    }

    @Test
    void getContentType_shouldReturnApplicationPdf() {
        // Act
        String contentType = pdfView.getContentType();

        // Assert
        assertEquals("application/pdf", contentType);
    }
}
