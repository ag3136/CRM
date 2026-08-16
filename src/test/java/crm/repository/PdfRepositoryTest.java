package crm.repository;

import crm.entity.Pdf;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PdfRepositoryTest {

    @Autowired
    private PdfRepository pdfRepository;

    @Test
    void pdfRepository_shouldBeInjected() {
        assertNotNull(pdfRepository);
    }

    @Test
    void findByName_withValidName_shouldReturnPdf() {
        // Arrange
        Pdf pdf = Pdf.builder()
                .name("test-document")
                .content("Test content")
                .build();
        pdfRepository.save(pdf);

        // Act
        Pdf result = pdfRepository.findByName("test-document");

        // Assert
        assertNotNull(result);
        assertEquals("test-document", result.getName());
    }

    @Test
    void findByName_withInvalidName_shouldReturnNull() {
        // Act
        Pdf result = pdfRepository.findByName("invalid");

        // Assert
        assertNull(result);
    }

    @Test
    void save_withValidPdf_shouldPersistPdf() {
        // Arrange
        Pdf pdf = Pdf.builder()
                .name("document")
                .content("Content")
                .build();

        // Act
        Pdf saved = pdfRepository.save(pdf);

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("document", saved.getName());
    }
}
