package crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * CrmApplication - Cloud-ready Spring Boot application
 * Configured for Azure cloud deployment with UTC timezone for consistency
 */
@EntityScan(
        basePackageClasses = {CrmApplication.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
public class CrmApplication {

    /**
     * Initialize application with UTC timezone to ensure consistent behavior
     * across distributed cloud environments (Azure regions, containers, etc.)
     */
    @PostConstruct
    public void init() {
        // Set default timezone to UTC for cloud-ready deployment
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }

}
