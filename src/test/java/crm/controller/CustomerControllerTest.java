package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private Customer customer;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        
        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .enabled(1)
                .build();
    }

    @Test
    void showAllCustomers_shouldReturnCustomerListView() throws Exception {
        // Arrange
        when(customerService.listAllCustomers()).thenReturn(Arrays.asList(customer));

        // Act & Assert
        mockMvc.perform(get("/customer/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attributeExists("customers"));
        
        verify(customerService).listAllCustomers();
    }

    @Test
    void showFormAddCustomer_shouldReturnAddCustomerView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/customer/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/add"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void showFormEditCustomer_shouldReturnEditCustomerView() throws Exception {
        // Arrange
        when(customerService.showCustomer(1L)).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(get("/customer/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/edit"))
                .andExpect(model().attributeExists("customer"));
        
        verify(customerService).showCustomer(1L);
    }

    @Test
    void showNameSearchForm_shouldReturnNameSearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/customer/name-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/name-search"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void showEmailSearchForm_shouldReturnEmailSearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/customer/email-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/email-search"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void showPhoneSearchForm_shouldReturnPhoneSearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/customer/phone-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/phone-search"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void showCitySearchForm_shouldReturnCitySearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/customer/city-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/city-search"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void showFirstNameSearchForm_shouldReturnFirstNameSearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/customer/first-name-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/first-name-search"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void showLastNameSearchForm_shouldReturnLastNameSearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/customer/last-name-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/last-name-search"))
                .andExpect(model().attributeExists("customer"));
    }
}
