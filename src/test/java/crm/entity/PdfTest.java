package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = Pdf.builder()
                .id(1L)
                .name("test-document")
                .content("This is test content")
                .build();
    }

    @Test
    void pdf_shouldCreateInstance() {
        assertNotNull(pdf);
    }

    @Test
    void builder_shouldCreatePdfWithAllFields() {
        assertEquals(1L, pdf.getId());
        assertEquals("test-document", pdf.getName());
        assertEquals("This is test content", pdf.getContent());
    }

    @Test
    void setName_withValidName_shouldSetName() {
        // Arrange
        String newName = "updated-document";
        
        // Act
        pdf.setName(newName);
        
        // Assert
        assertEquals(newName, pdf.getName());
    }

    @Test
    void setContent_withValidContent_shouldSetContent() {
        // Arrange
        String newContent = "Updated content";
        
        // Act
        pdf.setContent(newContent);
        
        // Assert
        assertEquals(newContent, pdf.getContent());
    }

    @Test
    void setId_withValidId_shouldSetId() {
        // Arrange
        Long newId = 2L;
        
        // Act
        pdf.setId(newId);
        
        // Assert
        assertEquals(newId, pdf.getId());
    }

    @Test
    void pdf_shouldHaveEntityAnnotation() {
        assertTrue(Pdf.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyPdf() {
        // Act
        Pdf emptyPdf = new Pdf();
        
        // Assert
        assertNotNull(emptyPdf);
    }

    @Test
    void allArgsConstructor_shouldCreatePdfWithAllFields() {
        // Act
        Pdf newPdf = new Pdf(2L, "document2", "Content 2");
        
        // Assert
        assertEquals(2L, newPdf.getId());
        assertEquals("document2", newPdf.getName());
        assertEquals("Content 2", newPdf.getContent());
    }

    @Test
    void setName_withNullName_shouldSetNull() {
        // Act
        pdf.setName(null);
        
        // Assert
        assertNull(pdf.getName());
    }

    @Test
    void setContent_withNullContent_shouldSetNull() {
        // Act
        pdf.setContent(null);
        
        // Assert
        assertNull(pdf.getContent());
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        // Arrange
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("doc")
                .content("content")
                .build();
        
        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("doc")
                .content("content")
                .build();
        
        // Assert
        assertEquals(pdf1, pdf2);
    }

    @Test
    void hashCode_withSameValues_shouldReturnSameHashCode() {
        // Arrange
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("doc")
                .build();
        
        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("doc")
                .build();
        
        // Assert
        assertEquals(pdf1.hashCode(), pdf2.hashCode());
    }
}
