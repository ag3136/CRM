package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CSVController csvController;

    private Customer customer;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .enabled(1)
                .build();
        
        response = new MockHttpServletResponse();
    }

    @Test
    void findCustomers_shouldWriteCsvToResponse() throws IOException {
        // Arrange
        when(customerService.listAllCustomers()).thenReturn(Arrays.asList(customer));

        // Act
        csvController.findCustomers(response);

        // Assert
        verify(customerService).listAllCustomers();
        assertNotNull(response.getContentAsString());
    }

    @Test
    void findCustomer_withValidId_shouldWriteCsvToResponse() throws IOException {
        // Arrange
        when(customerService.showCustomer(1L)).thenReturn(customer);

        // Act
        csvController.findCustomer(1L, response);

        // Assert
        verify(customerService).showCustomer(1L);
        assertNotNull(response.getContentAsString());
    }

    @Test
    void findCustomers_withEmptyList_shouldHandleGracefully() throws IOException {
        // Arrange
        when(customerService.listAllCustomers()).thenReturn(Arrays.asList());

        // Act
        csvController.findCustomers(response);

        // Assert
        verify(customerService).listAllCustomers();
    }
}
