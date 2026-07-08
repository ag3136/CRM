package crm.viewResolver;

import crm.view.ExcelView;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ExcelViewResolverTest {

    private ExcelViewResolver excelViewResolver = new ExcelViewResolver();

    @Test
    void testResolveViewName_returnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(ExcelView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentLocale_returnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName("test", Locale.GERMAN);
        assertNotNull(view);
        assertInstanceOf(ExcelView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(ExcelView.class, view);
    }

    @Test
    void testResolveViewName_calledMultipleTimes_returnsNewInstances() throws Exception {
        View view1 = excelViewResolver.resolveViewName("v1", Locale.ENGLISH);
        View view2 = excelViewResolver.resolveViewName("v2", Locale.ENGLISH);
        assertNotNull(view1);
        assertNotNull(view2);
    }
}
