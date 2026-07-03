package crm.viewResolver;

import crm.view.PdfView;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewResolverTest {

    private PdfViewResolver pdfViewResolver = new PdfViewResolver();

    @Test
    void testResolveViewName_returnsNonNullView() throws Exception {
        View view = pdfViewResolver.resolveViewName("anyView", Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testResolveViewName_returnsPdfViewInstance() throws Exception {
        View view = pdfViewResolver.resolveViewName("anyView", Locale.getDefault());
        assertInstanceOf(PdfView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentViewName_returnsPdfView() throws Exception {
        View view = pdfViewResolver.resolveViewName("users", Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(PdfView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsPdfView() throws Exception {
        View view = pdfViewResolver.resolveViewName(null, Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        PdfViewResolver resolver = new PdfViewResolver();
        assertNotNull(resolver);
    }
}
