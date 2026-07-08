package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver = new CsvViewResolver();

    @Test
    void testResolveViewName_returnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(CsvView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentLocale_returnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName("test", Locale.FRENCH);
        assertNotNull(view);
        assertInstanceOf(CsvView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(CsvView.class, view);
    }

    @Test
    void testResolveViewName_calledMultipleTimes_returnsNewInstanceEachTime() throws Exception {
        View view1 = csvViewResolver.resolveViewName("view1", Locale.ENGLISH);
        View view2 = csvViewResolver.resolveViewName("view2", Locale.ENGLISH);
        assertNotNull(view1);
        assertNotNull(view2);
    }
}
