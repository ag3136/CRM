package crm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CrmApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void main_shouldStartApplication() {
        assertDoesNotThrow(() -> {
            // Verify main method exists
            CrmApplication.class.getMethod("main", String[].class);
        });
    }

    @Test
    void applicationContext_shouldContainBeans() {
        assertNotNull(applicationContext);
        assertTrue(applicationContext.getBeanDefinitionCount() > 0);
    }

    @Test
    void crmApplication_shouldBeSpringBootApplication() {
        assertTrue(CrmApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }
}
