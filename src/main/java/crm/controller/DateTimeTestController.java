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
 * Controller for date/time demonstration endpoint.
 *
 * Cloud-readiness fix (cr-java-0111):
 *   - Removed java.util.Date (lines 19-20) which relies on server-local timezone settings.
 *   - All time values now use the java.time API (Instant, ZonedDateTime, LocalDate).
 *   - Standardised on UTC (ZoneOffset.UTC) for all time representations to ensure
 *     consistent behaviour across distributed cloud regions and containers.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // UTC instant — replaces new Date() (blocker-4, line 19)
        Instant nowUtc = Instant.now();
        // ZonedDateTime in UTC — replaces LocalDateTime.now() (blocker-5, line 20)
        ZonedDateTime zonedDateTimeUtc = ZonedDateTime.now(ZoneOffset.UTC);
        // Calendar-independent local date (no timezone dependency)
        LocalDate localDate = LocalDate.now(ZoneOffset.UTC);

        model.addAttribute("standardDate", nowUtc.toString());
        model.addAttribute("localDateTime", zonedDateTimeUtc);
        model.addAttribute("localDate", localDate);
        model.addAttribute("timestamp", nowUtc);
        return "date/test";
    }
}
