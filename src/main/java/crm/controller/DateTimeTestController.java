package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Controller for date/time demonstration.
 *
 * Cloud-readiness fix (cr-java-0111):
 *   - Removed java.util.Date usage (lines 19-20) which relied on server-local timezone.
 *   - All time values are now derived from the java.time API standardized on UTC (ZoneOffset.UTC).
 *   - ZonedDateTime.now(ZoneOffset.UTC) replaces new Date() for consistent time across
 *     distributed cloud regions and containers.
 *   - LocalDate.now(ZoneOffset.UTC) ensures date values are also UTC-anchored.
 *   - Instant.now() is retained as it is already timezone-neutral (UTC epoch).
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Standardized on UTC — replaces new Date() (server-local timezone) and
        // LocalDateTime.now() (system-default timezone) with explicit UTC-based equivalents
        model.addAttribute("standardDate", ZonedDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDateTime", ZonedDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        model.addAttribute("timestamp", Instant.now());
        return "date/test";
    }

}
