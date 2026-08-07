package crm.controller;

import crm.config.AzureServiceBusSchedulerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    private final AzureServiceBusSchedulerService azureServiceBusSchedulerService;
    private final long scheduleDelaySeconds;

    public DateTimeTestController(AzureServiceBusSchedulerService azureServiceBusSchedulerService,
                                  @Value("${azure.servicebus.schedule-delay-seconds:300}") long scheduleDelaySeconds) {
        this.azureServiceBusSchedulerService = azureServiceBusSchedulerService;
        this.scheduleDelaySeconds = scheduleDelaySeconds;
    }

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        OffsetDateTime utcNow = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime scheduledExecution = azureServiceBusSchedulerService.scheduleMessage(
                "date-time-test",
                Duration.ofSeconds(scheduleDelaySeconds));
        model.addAttribute("standardDate", Date.from(utcNow.toInstant()));
        model.addAttribute("localDateTime", LocalDateTime.ofInstant(utcNow.toInstant(), ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.ofInstant(utcNow.toInstant(), ZoneOffset.UTC));
        model.addAttribute("timestamp", Instant.now());
        model.addAttribute("scheduledExecution", scheduledExecution);
        return "date/test";
    }
}
