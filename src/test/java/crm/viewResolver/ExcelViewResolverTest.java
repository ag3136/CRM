package crm.viewResolver;

import crm.view.ExcelView;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ExcelViewResolverTest {

    private ExcelViewResolver excelViewResolver = new ExcelViewResolver();

    @Test
    void testResolveViewName_returnsNonNullView() throws Exception {
        View view = excelViewResolver.resolveViewName("anyView", Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testResolveViewName_returnsExcelViewInstance() throws Exception {
        View view = excelViewResolver.resolveViewName("anyView", Locale.getDefault());
        assertInstanceOf(ExcelView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentViewName_returnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName("users", Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(ExcelView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName(null, Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        ExcelViewResolver resolver = new ExcelViewResolver();
        assertNotNull(resolver);
    }
}
