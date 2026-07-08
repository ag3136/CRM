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
                .name("report.pdf")
                .content("PDF content")
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        PdfServiceImpl service = new PdfServiceImpl(pdfRepository);
        assertNotNull(service);
    }

    @Test
    void testFindByName_returnsPdf_whenFound() {
        when(pdfRepository.findByName("report.pdf")).thenReturn(pdf);
        Pdf result = pdfService.findByName("report.pdf");
        assertNotNull(result);
        assertEquals("report.pdf", result.getName());
        verify(pdfRepository).findByName("report.pdf");
    }

    @Test
    void testFindByName_returnsNull_whenNotFound() {
        when(pdfRepository.findByName("nonexistent.pdf")).thenReturn(null);
        Pdf result = pdfService.findByName("nonexistent.pdf");
        assertNull(result);
        verify(pdfRepository).findByName("nonexistent.pdf");
    }

    @Test
    void testFindByName_withEmptyString() {
        when(pdfRepository.findByName("")).thenReturn(null);
        Pdf result = pdfService.findByName("");
        assertNull(result);
        verify(pdfRepository).findByName("");
    }

    @Test
    void testSavePdf_callsRepository() {
        pdfService.savePdf(pdf);
        verify(pdfRepository).save(pdf);
    }

    @Test
    void testSavePdf_withNewPdf() {
        Pdf newPdf = Pdf.builder().name("invoice.pdf").content("Invoice content").build();
        pdfService.savePdf(newPdf);
        verify(pdfRepository).save(newPdf);
    }

    @Test
    void testSavePdf_withNullContent() {
        Pdf pdfNoContent = Pdf.builder().id(2L).name("empty.pdf").build();
        pdfService.savePdf(pdfNoContent);
        verify(pdfRepository).save(pdfNoContent);
    }

    @Test
    void testFindByName_withSpecialCharacters() {
        String specialName = "report-2023_final.pdf";
        when(pdfRepository.findByName(specialName)).thenReturn(pdf);
        Pdf result = pdfService.findByName(specialName);
        assertNotNull(result);
        verify(pdfRepository).findByName(specialName);
    }
}
