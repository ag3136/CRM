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
 * Blockers 4 & 5 (cr-java-0111): Replaced java.util.Date (line 19) and
 * LocalDateTime.now() without timezone (line 20) with java.time API
 * standardized on UTC to eliminate timezone inconsistencies in distributed
 * cloud environments.
 *
 * - java.util.Date replaced with ZonedDateTime.now(ZoneOffset.UTC)
 * - LocalDateTime.now() replaced with ZonedDateTime.now(ZoneOffset.UTC)
 * - LocalDate.now() replaced with LocalDate.now(ZoneOffset.UTC) for UTC consistency
 * - Instant.now() retained as it is already UTC-based
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Replaced new Date() (blocker-4, line 19) with ZonedDateTime in UTC
        model.addAttribute("standardDate", ZonedDateTime.now(ZoneOffset.UTC));
        // Replaced LocalDateTime.now() (blocker-5, line 20) with ZonedDateTime in UTC
        model.addAttribute("localDateTime", ZonedDateTime.now(ZoneOffset.UTC));
        // LocalDate standardized to UTC zone
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        // Instant.now() is already UTC-based — retained as-is
        model.addAttribute("timestamp", Instant.now());
        return "date/test";
    }

}
