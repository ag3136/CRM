package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        assertNotNull(pdf);
    }

    @Test
    void testAllArgsConstructor() {
        Pdf p = new Pdf(1L, "report.pdf", "PDF content here");
        assertEquals(1L, p.getId());
        assertEquals("report.pdf", p.getName());
        assertEquals("PDF content here", p.getContent());
    }

    @Test
    void testBuilder() {
        Pdf p = Pdf.builder()
                .id(2L)
                .name("invoice.pdf")
                .content("Invoice content")
                .build();

        assertEquals(2L, p.getId());
        assertEquals("invoice.pdf", p.getName());
        assertEquals("Invoice content", p.getContent());
    }

    @Test
    void testSetAndGetId() {
        pdf.setId(10L);
        assertEquals(10L, pdf.getId());
    }

    @Test
    void testSetAndGetName() {
        pdf.setName("document.pdf");
        assertEquals("document.pdf", pdf.getName());
    }

    @Test
    void testSetAndGetContent() {
        pdf.setContent("Some PDF content");
        assertEquals("Some PDF content", pdf.getContent());
    }

    @Test
    void testSetId_null() {
        pdf.setId(null);
        assertNull(pdf.getId());
    }

    @Test
    void testSetName_null() {
        pdf.setName(null);
        assertNull(pdf.getName());
    }

    @Test
    void testSetContent_null() {
        pdf.setContent(null);
        assertNull(pdf.getContent());
    }

    @Test
    void testSetName_withExtension() {
        pdf.setName("report");
        assertEquals("report", pdf.getName());
    }

    @Test
    void testEquals_sameObject() {
        pdf.setId(1L);
        pdf.setName("test.pdf");
        assertEquals(pdf, pdf);
    }

    @Test
    void testEquals_equalObjects() {
        Pdf p1 = Pdf.builder().id(1L).name("test.pdf").build();
        Pdf p2 = Pdf.builder().id(1L).name("test.pdf").build();
        assertEquals(p1, p2);
    }

    @Test
    void testHashCode_equalObjects() {
        Pdf p1 = Pdf.builder().id(1L).name("test.pdf").build();
        Pdf p2 = Pdf.builder().id(1L).name("test.pdf").build();
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testToString_notNull() {
        pdf.setId(1L);
        pdf.setName("test.pdf");
        assertNotNull(pdf.toString());
    }

    @Test
    void testSetId_largeValue() {
        pdf.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, pdf.getId());
    }
}
