package crm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ViewResolver;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebAppConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void webAppConfig_shouldBeConfigurationClass() {
        assertTrue(WebAppConfig.class.isAnnotationPresent(
            org.springframework.context.annotation.Configuration.class));
    }

    @Test
    void applicationContext_shouldContainViewResolvers() {
        assertNotNull(applicationContext);
        assertTrue(applicationContext.getBeanNamesForType(ViewResolver.class).length > 0);
    }

    @Test
    void webAppConfig_shouldImplementWebMvcConfigurer() {
        assertTrue(org.springframework.web.servlet.config.annotation.WebMvcConfigurer.class
                .isAssignableFrom(WebAppConfig.class));
    }
}
