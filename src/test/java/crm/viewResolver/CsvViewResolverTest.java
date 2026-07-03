package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver = new CsvViewResolver();

    @Test
    void testResolveViewName_returnsNonNullView() throws Exception {
        View view = csvViewResolver.resolveViewName("anyView", Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testResolveViewName_returnsCsvViewInstance() throws Exception {
        View view = csvViewResolver.resolveViewName("anyView", Locale.getDefault());
        assertInstanceOf(CsvView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentViewName_returnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName("users", Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(CsvView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName(null, Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        CsvViewResolver resolver = new CsvViewResolver();
        assertNotNull(resolver);
    }
}
