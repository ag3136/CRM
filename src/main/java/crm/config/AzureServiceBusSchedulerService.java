package crm.config;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class AzureServiceBusSchedulerService {

    private final String connectionString;
    private final String queueName;

    public AzureServiceBusSchedulerService(@Value("${azure.servicebus.connection-string}") String connectionString,
                                           @Value("${azure.servicebus.queue-name}") String queueName) {
        this.connectionString = connectionString;
        this.queueName = queueName;
    }

    public OffsetDateTime scheduleMessage(String payload, Duration delay) {
        OffsetDateTime scheduledTime = OffsetDateTime.now(ZoneOffset.UTC).plus(delay);
        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();
        try {
            ServiceBusMessage message = new ServiceBusMessage(payload);
            senderClient.scheduleMessage(message, scheduledTime);
            return scheduledTime;
        } finally {
            senderClient.close();
        }
    }
}
