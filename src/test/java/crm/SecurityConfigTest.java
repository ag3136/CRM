package crm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void securityConfig_shouldBeConfigurationClass() {
        assertTrue(SecurityConfig.class.isAnnotationPresent(
            org.springframework.context.annotation.Configuration.class));
    }

    @Test
    void securityConfig_shouldHaveEnableWebSecurityAnnotation() {
        assertTrue(SecurityConfig.class.isAnnotationPresent(
            org.springframework.security.config.annotation.web.configuration.EnableWebSecurity.class));
    }

    @Test
    void applicationContext_shouldContainPasswordEncoder() {
        assertNotNull(applicationContext);
        BCryptPasswordEncoder encoder = applicationContext.getBean(BCryptPasswordEncoder.class);
        assertNotNull(encoder);
    }

    @Test
    void passwordEncoder_shouldEncodePasswords() {
        BCryptPasswordEncoder encoder = applicationContext.getBean(BCryptPasswordEncoder.class);
        String encoded = encoder.encode("password");
        assertNotNull(encoded);
        assertNotEquals("password", encoded);
        assertTrue(encoder.matches("password", encoded));
    }
}
