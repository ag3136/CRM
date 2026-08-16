package crm.service;

import crm.entity.Customer;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .enabled(1)
                .build();
    }

    @Test
    void getMaxId_shouldReturnMaxId() {
        // Arrange
        when(customerRepository.getMaxId()).thenReturn(10L);

        // Act
        Long result = customerService.getMaxId();

        // Assert
        assertEquals(10L, result);
        verify(customerRepository).getMaxId();
    }

    @Test
    void listAllCustomers_shouldReturnAllCustomers() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        // Act
        Iterable<Customer> result = customerService.listAllCustomers();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAll();
    }

    @Test
    void showCustomer_withValidId_shouldReturnCustomer() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        Customer result = customerService.showCustomer(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void showCustomer_withInvalidId_shouldReturnNull() {
        // Arrange
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Customer result = customerService.showCustomer(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void findAllByEnabledTrue_shouldReturnEnabledCustomers() {
        // Arrange
        when(customerRepository.findAllByEnabled(1)).thenReturn(Arrays.asList(customer));

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledTrue();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(1);
    }

    @Test
    void findAllByEnabledFalse_shouldReturnDisabledCustomers() {
        // Arrange
        when(customerRepository.findAllByEnabled(0)).thenReturn(Arrays.asList());

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledFalse();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(0);
    }

    @Test
    void findOneByEnabledTrueAndName_shouldReturnCustomer() {
        // Arrange
        when(customerRepository.findOneByEnabledAndName(1, "Test Customer")).thenReturn(customer);

        // Act
        Customer result = customerService.findOneByEnabledTrueAndName("Test Customer");

        // Assert
        assertNotNull(result);
        assertEquals("Test Customer", result.getName());
    }

    @Test
    void findByEnabledTrueAndEmail_shouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndEmail(1, "test@example.com"))
                .thenReturn(Arrays.asList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("test@example.com");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(1, "test@example.com");
    }

    @Test
    void findByEnabledTrueAndPhone_shouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndPhone(1, 123456789))
                .thenReturn(Arrays.asList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(123456789);

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndPhone(1, 123456789);
    }

    @Test
    void saveCustomer_shouldSetEnabledAndSave() {
        // Arrange
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        customerService.saveCustomer(customer);

        // Assert
        verify(customerRepository).save(argThat(c -> c.getEnabled() == 1));
    }

    @Test
    void findByEnabledTrueAndCity_shouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndCity(1, "New York"))
                .thenReturn(Arrays.asList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCity("New York");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCity(1, "New York");
    }

    @Test
    void findByFirstName_shouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByFirstName("John")).thenReturn(Arrays.asList(customer));

        // Act
        Iterable<Customer> result = customerService.findByFirstName("John");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByFirstName("John");
    }

    @Test
    void findByLastName_shouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByLastName("Doe")).thenReturn(Arrays.asList(customer));

        // Act
        Iterable<Customer> result = customerService.findByLastName("Doe");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByLastName("Doe");
    }
}
