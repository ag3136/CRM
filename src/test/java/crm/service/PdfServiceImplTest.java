package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
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
                .content("Sample content")
                .build();
    }

    @Test
    void testFindByName_existingName_returnsPdf() {
        when(pdfRepository.findByName("test-document")).thenReturn(pdf);
        Pdf result = pdfService.findByName("test-document");
        assertNotNull(result);
        assertEquals("test-document", result.getName());
        verify(pdfRepository).findByName("test-document");
    }

    @Test
    void testFindByName_nonExistingName_returnsNull() {
        when(pdfRepository.findByName("nonexistent")).thenReturn(null);
        Pdf result = pdfService.findByName("nonexistent");
        assertNull(result);
        verify(pdfRepository).findByName("nonexistent");
    }

    @Test
    void testSavePdf_callsRepositorySave() {
        pdfService.savePdf(pdf);
        verify(pdfRepository).save(pdf);
    }

    @Test
    void testSavePdf_withNewPdf_savesSuccessfully() {
        Pdf newPdf = Pdf.builder()
                .id(2L)
                .name("new-document")
                .content("New content")
                .build();
        pdfService.savePdf(newPdf);
        verify(pdfRepository).save(newPdf);
    }

    @Test
    void testConstructor_withRepository_createsInstance() {
        PdfServiceImpl service = new PdfServiceImpl(pdfRepository);
        assertNotNull(service);
    }

    @Test
    void testFindByName_withNullName_callsRepository() {
        when(pdfRepository.findByName(null)).thenReturn(null);
        Pdf result = pdfService.findByName(null);
        assertNull(result);
        verify(pdfRepository).findByName(null);
    }
}
