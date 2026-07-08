package crm.viewResolver;

import crm.view.PdfView;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewResolverTest {

    private PdfViewResolver pdfViewResolver = new PdfViewResolver();

    @Test
    void testResolveViewName_returnsPdfView() throws Exception {
        View view = pdfViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(PdfView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentLocale_returnsPdfView() throws Exception {
        View view = pdfViewResolver.resolveViewName("test", Locale.ITALIAN);
        assertNotNull(view);
        assertInstanceOf(PdfView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsPdfView() throws Exception {
        View view = pdfViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(PdfView.class, view);
    }

    @Test
    void testResolveViewName_calledMultipleTimes_returnsNewInstances() throws Exception {
        View view1 = pdfViewResolver.resolveViewName("v1", Locale.ENGLISH);
        View view2 = pdfViewResolver.resolveViewName("v2", Locale.ENGLISH);
        assertNotNull(view1);
        assertNotNull(view2);
    }
}
