package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * DateTimeTestController - Cloud-ready implementation
 * Uses UTC timezone explicitly to ensure consistency across distributed cloud environments.
 * All date/time operations are timezone-agnostic and suitable for Azure cloud deployment.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use Instant for UTC-based timestamp (cloud-ready)
        Instant now = Instant.now();
        
        // Convert to Date using UTC timezone
        model.addAttribute("standardDate", Date.from(now));
        
        // Use UTC timezone explicitly instead of system default
        model.addAttribute("localDateTime", LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.ofInstant(now, ZoneOffset.UTC));
        model.addAttribute("timestamp", now);
        
        return "date/test";
    }

}
