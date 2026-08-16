package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfServiceImplTest {

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private PdfServiceImpl pdfService;

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = Pdf.builder()
                .id(1L)
                .name("test-document")
                .content("Test content")
                .build();
    }

    @Test
    void findByName_withValidName_shouldReturnPdf() {
        // Arrange
        when(pdfRepository.findByName("test-document")).thenReturn(pdf);

        // Act
        Pdf result = pdfService.findByName("test-document");

        // Assert
        assertNotNull(result);
        assertEquals("test-document", result.getName());
        verify(pdfRepository).findByName("test-document");
    }

    @Test
    void findByName_withInvalidName_shouldReturnNull() {
        // Arrange
        when(pdfRepository.findByName("invalid")).thenReturn(null);

        // Act
        Pdf result = pdfService.findByName("invalid");

        // Assert
        assertNull(result);
    }

    @Test
    void savePdf_withValidPdf_shouldSavePdf() {
        // Arrange
        when(pdfRepository.save(any(Pdf.class))).thenReturn(pdf);

        // Act
        pdfService.savePdf(pdf);

        // Assert
        verify(pdfRepository).save(pdf);
    }

    @Test
    void savePdf_withNullPdf_shouldCallRepository() {
        // Arrange
        when(pdfRepository.save(null)).thenReturn(null);

        // Act
        pdfService.savePdf(null);

        // Assert
        verify(pdfRepository).save(null);
    }
}
