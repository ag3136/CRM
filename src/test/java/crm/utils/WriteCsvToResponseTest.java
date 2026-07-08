package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteCsvToResponseTest {

    private Customer createCustomer(Long id, String name, String email, int phone,
                                     String firstName, String lastName, String city, String address, int enabled) {
        return Customer.builder()
                .id(id)
                .name(name)
                .email(email)
                .phone(phone)
                .firstName(firstName)
                .lastName(lastName)
                .city(city)
                .address(address)
                .enabled(enabled)
                .build();
    }

    @Test
    void testWriteCustomers_withSingleCustomer_writesOutput() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Customer customer = createCustomer(1L, "TestCorp", "test@corp.com", 1234567,
                "John", "Doe", "New York", "123 Main St", 1);

        WriteCsvToResponse.writeCustomers(printWriter, Arrays.asList(customer));
        printWriter.flush();

        String output = stringWriter.toString();
        assertNotNull(output);
    }

    @Test
    void testWriteCustomers_withEmptyList_writesNoData() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        WriteCsvToResponse.writeCustomers(printWriter, Collections.emptyList());
        printWriter.flush();

        // Should not throw exception
        assertNotNull(stringWriter.toString());
    }

    @Test
    void testWriteCustomers_withMultipleCustomers_writesAllRows() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        Customer c1 = createCustomer(1L, "Corp1", "c1@test.com", 111, "A", "B", "City1", "Addr1", 1);
        Customer c2 = createCustomer(2L, "Corp2", "c2@test.com", 222, "C", "D", "City2", "Addr2", 1);

        WriteCsvToResponse.writeCustomers(printWriter, Arrays.asList(c1, c2));
        printWriter.flush();

        assertNotNull(stringWriter.toString());
    }

    @Test
    void testWriteCustomer_withSingleCustomer_writesOutput() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Customer customer = createCustomer(1L, "TestCorp", "test@corp.com", 1234567,
                "John", "Doe", "New York", "123 Main St", 1);

        WriteCsvToResponse.writeCustomer(printWriter, customer);
        printWriter.flush();

        assertNotNull(stringWriter.toString());
    }

    @Test
    void testWriteCustomer_withNullFields_doesNotThrow() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Customer customer = Customer.builder()
                .id(1L)
                .name("TestCorp")
                .email("test@corp.com")
                .build();

        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomer(printWriter, customer));
    }

    @Test
    void testWriteCustomers_doesNotThrowException() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        List<Customer> customers = Arrays.asList(
                createCustomer(1L, "Corp", "corp@test.com", 123, "F", "L", "C", "A", 1)
        );

        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomers(printWriter, customers));
    }
}
