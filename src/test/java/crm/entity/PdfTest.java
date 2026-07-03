package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = Pdf.builder()
                .id(1L)
                .name("test-document")
                .content("Sample PDF content")
                .build();
    }

    @Test
    void testBuilder_createsPdfWithAllFields() {
        assertNotNull(pdf);
        assertEquals(1L, pdf.getId());
        assertEquals("test-document", pdf.getName());
        assertEquals("Sample PDF content", pdf.getContent());
    }

    @Test
    void testNoArgsConstructor_createsEmptyPdf() {
        Pdf emptyPdf = new Pdf();
        assertNotNull(emptyPdf);
        assertNull(emptyPdf.getId());
        assertNull(emptyPdf.getName());
    }

    @Test
    void testAllArgsConstructor_createsPdfWithAllFields() {
        Pdf allArgsPdf = new Pdf(2L, "document2", "Content 2");
        assertNotNull(allArgsPdf);
        assertEquals(2L, allArgsPdf.getId());
        assertEquals("document2", allArgsPdf.getName());
        assertEquals("Content 2", allArgsPdf.getContent());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        pdf.setId(5L);
        assertEquals(5L, pdf.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        pdf.setName("new-document");
        assertEquals("new-document", pdf.getName());
    }

    @Test
    void testSetAndGetContent_returnsCorrectContent() {
        pdf.setContent("New content");
        assertEquals("New content", pdf.getContent());
    }

    @Test
    void testSetName_withNull_returnsNull() {
        pdf.setName(null);
        assertNull(pdf.getName());
    }

    @Test
    void testSetContent_withNull_returnsNull() {
        pdf.setContent(null);
        assertNull(pdf.getContent());
    }

    @Test
    void testEquals_equalPdfs_returnsTrue() {
        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("test-document")
                .content("Sample PDF content")
                .build();
        assertEquals(pdf, pdf2);
    }

    @Test
    void testEquals_differentPdfs_returnsFalse() {
        Pdf pdf2 = Pdf.builder()
                .id(2L)
                .name("other-document")
                .build();
        assertNotEquals(pdf, pdf2);
    }

    @Test
    void testHashCode_equalPdfs_sameHashCode() {
        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("test-document")
                .content("Sample PDF content")
                .build();
        assertEquals(pdf.hashCode(), pdf2.hashCode());
    }

    @Test
    void testToString_containsName() {
        String toString = pdf.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("test-document"));
    }

    @Test
    void testSetId_withNull_returnsNull() {
        pdf.setId(null);
        assertNull(pdf.getId());
    }
}
