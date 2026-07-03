package crm.service;

import crm.entity.Category;
import crm.entity.Customer;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        categories = new HashSet<>();
        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Tech");
        categories.add(cat);

        customer = Customer.builder()
                .id(1L)
                .name("Acme Corp")
                .email("acme@example.com")
                .phone(1234567890)
                .categories(categories)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();
    }

    @Test
    void testGetMaxId_returnsMaxId() {
        when(customerRepository.getMaxId()).thenReturn(5L);
        Long result = customerService.getMaxId();
        assertEquals(5L, result);
        verify(customerRepository).getMaxId();
    }

    @Test
    void testListAllCustomers_returnsAllCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAll()).thenReturn(customers);
        Iterable<Customer> result = customerService.listAllCustomers();
        assertNotNull(result);
        verify(customerRepository).findAll();
    }

    @Test
    void testShowCustomer_existingId_returnsCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Customer result = customerService.showCustomer(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository).findById(1L);
    }

    @Test
    void testShowCustomer_nonExistingId_returnsNull() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        Customer result = customerService.showCustomer(99L);
        assertNull(result);
    }

    @Test
    void testFindAllByEnabledTrue_returnsEnabledCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);
        Iterable<Customer> result = customerService.findAllByEnabledTrue();
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(1);
    }

    @Test
    void testFindAllByEnabledFalse_returnsDisabledCustomers() {
        Customer disabledCustomer = Customer.builder().id(2L).name("Disabled Corp").enabled(0).build();
        List<Customer> customers = Arrays.asList(disabledCustomer);
        when(customerRepository.findAllByEnabled(0)).thenReturn(customers);
        Iterable<Customer> result = customerService.findAllByEnabledFalse();
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(0);
    }

    @Test
    void testFindOneByEnabledTrueAndName_returnsCustomer() {
        when(customerRepository.findOneByEnabledAndName(1, "Acme Corp")).thenReturn(customer);
        Customer result = customerService.findOneByEnabledTrueAndName("Acme Corp");
        assertNotNull(result);
        assertEquals("Acme Corp", result.getName());
        verify(customerRepository).findOneByEnabledAndName(1, "Acme Corp");
    }

    @Test
    void testFindOneByEnabledFalseAndName_returnsCustomer() {
        Customer disabledCustomer = Customer.builder().id(2L).name("Disabled Corp").enabled(0).build();
        when(customerRepository.findOneByEnabledAndName(0, "Disabled Corp")).thenReturn(disabledCustomer);
        Customer result = customerService.findOneByEnabledFalseAndName("Disabled Corp");
        assertNotNull(result);
        verify(customerRepository).findOneByEnabledAndName(0, "Disabled Corp");
    }

    @Test
    void testFindOneByName_returnsCustomer() {
        when(customerRepository.findOneByName("Acme Corp")).thenReturn(customer);
        Customer result = customerService.findOneByName("Acme Corp");
        assertNotNull(result);
        verify(customerRepository).findOneByName("Acme Corp");
    }

    @Test
    void testFindByEnabledTrueAndEmail_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndEmail(1, "acme@example.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("acme@example.com");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(1, "acme@example.com");
    }

    @Test
    void testFindByEnabledFalseAndEmail_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndEmail(0, "acme@example.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndEmail("acme@example.com");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(0, "acme@example.com");
    }

    @Test
    void testFindByEmail_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEmail("acme@example.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEmail("acme@example.com");
        assertNotNull(result);
        verify(customerRepository).findByEmail("acme@example.com");
    }

    @Test
    void testFindByEnabledTrueAndPhone_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndPhone(1, 1234567890)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(1234567890);
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndPhone(1, 1234567890);
    }

    @Test
    void testFindByEnabledFalseAndPhone_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndPhone(0, 1234567890)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndPhone(1234567890);
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndPhone(0, 1234567890);
    }

    @Test
    void testFindByPhone_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByPhone(1234567890)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByPhone(1234567890);
        assertNotNull(result);
        verify(customerRepository).findByPhone(1234567890);
    }

    @Test
    void testFindByEnabledTrueAndCategories_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCategories(1, categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(categories);
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCategories(1, categories);
    }

    @Test
    void testFindByEnabledFalseAndCategories_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCategories(0, categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndCategories(categories);
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCategories(0, categories);
    }

    @Test
    void testFindByCategories_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCategories(categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCategories(categories);
        assertNotNull(result);
        verify(customerRepository).findByCategories(categories);
    }

    @Test
    void testFindByEnabledTrueAndFirstName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstName(1, "John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName("John");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstName(1, "John");
    }

    @Test
    void testFindByEnabledFalseAndFirstName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstName(0, "John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstName("John");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstName(0, "John");
    }

    @Test
    void testFindByFirstName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstName("John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByFirstName("John");
        assertNotNull(result);
        verify(customerRepository).findByFirstName("John");
    }

    @Test
    void testFindByEnabledTrueAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndLastName(1, "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndLastName("Doe");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndLastName(1, "Doe");
    }

    @Test
    void testFindByEnabledFalseAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndLastName(0, "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndLastName("Doe");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndLastName(0, "Doe");
    }

    @Test
    void testFindByLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByLastName("Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByLastName("Doe");
        assertNotNull(result);
        verify(customerRepository).findByLastName("Doe");
    }

    @Test
    void testFindByEnabledTrueAndFirstNameAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, "John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstNameAndLastName(1, "John", "Doe");
    }

    @Test
    void testFindByEnabledFalseAndFirstNameAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstNameAndLastName(0, "John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstNameAndLastName(0, "John", "Doe");
    }

    @Test
    void testFindByFirstNameAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
        verify(customerRepository).findByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testFindByEnabledTrueAndCity_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCity(1, "New York")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCity("New York");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCity(1, "New York");
    }

    @Test
    void testFindByEnabledFalseAndCity_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCity(0, "New York")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndCity("New York");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCity(0, "New York");
    }

    @Test
    void testFindByCity_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCity("New York")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCity("New York");
        assertNotNull(result);
        verify(customerRepository).findByCity("New York");
    }

    @Test
    void testFindByEnabledTrueAndCityAndAddress_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCityAndAddress(1, "New York", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCityAndAddress("New York", "123 Main St");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCityAndAddress(1, "New York", "123 Main St");
    }

    @Test
    void testFindByEnabledFalseAndCityAndAddress_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCityAndAddress(0, "New York", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndCityAndAddress("New York", "123 Main St");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCityAndAddress(0, "New York", "123 Main St");
    }

    @Test
    void testFindByCityAndAddress_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCityAndAddress("New York", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCityAndAddress("New York", "123 Main St");
        assertNotNull(result);
        verify(customerRepository).findByCityAndAddress("New York", "123 Main St");
    }

    @Test
    void testSaveCustomer_setsEnabledAndSaves() {
        Customer newCustomer = Customer.builder()
                .id(2L)
                .name("New Corp")
                .email("new@example.com")
                .build();
        customerService.saveCustomer(newCustomer);
        assertEquals(1, newCustomer.getEnabled());
        verify(customerRepository).save(newCustomer);
    }

    @Test
    void testConstructor_withRepository_createsInstance() {
        CustomerServiceImpl service = new CustomerServiceImpl(customerRepository);
        assertNotNull(service);
    }
}
