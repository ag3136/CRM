package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();
    }

    @Test
    void customer_shouldCreateInstance() {
        assertNotNull(customer);
    }

    @Test
    void builder_shouldCreateCustomerWithAllFields() {
        assertEquals(1L, customer.getId());
        assertEquals("Test Customer", customer.getName());
        assertEquals("test@example.com", customer.getEmail());
        assertEquals(123456789, customer.getPhone());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("New York", customer.getCity());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void setName_withValidName_shouldSetName() {
        // Arrange
        String newName = "Updated Customer";
        
        // Act
        customer.setName(newName);
        
        // Assert
        assertEquals(newName, customer.getName());
    }

    @Test
    void setEmail_withValidEmail_shouldSetEmail() {
        // Arrange
        String newEmail = "newemail@example.com";
        
        // Act
        customer.setEmail(newEmail);
        
        // Assert
        assertEquals(newEmail, customer.getEmail());
    }

    @Test
    void setPhone_withValidPhone_shouldSetPhone() {
        // Arrange
        int newPhone = 987654321;
        
        // Act
        customer.setPhone(newPhone);
        
        // Assert
        assertEquals(newPhone, customer.getPhone());
    }

    @Test
    void setCategories_withValidCategories_shouldSetCategories() {
        // Arrange
        Set<Category> categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("VIP");
        categories.add(category);
        
        // Act
        customer.setCategories(categories);
        
        // Assert
        assertNotNull(customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void setEnabled_withZero_shouldDisableCustomer() {
        // Act
        customer.setEnabled(0);
        
        // Assert
        assertEquals(0, customer.getEnabled());
    }

    @Test
    void customer_shouldHaveEntityAnnotation() {
        assertTrue(Customer.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyCustomer() {
        // Act
        Customer emptyCustomer = new Customer();
        
        // Assert
        assertNotNull(emptyCustomer);
    }

    @Test
    void allArgsConstructor_shouldCreateCustomerWithAllFields() {
        // Arrange
        Set<Category> categories = new HashSet<>();
        
        // Act
        Customer newCustomer = new Customer(2L, "Name", "email@test.com", 
            111222333, categories, "Jane", "Smith", "Boston", "456 Oak St", 1);
        
        // Assert
        assertEquals(2L, newCustomer.getId());
        assertEquals("Name", newCustomer.getName());
        assertEquals("email@test.com", newCustomer.getEmail());
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        // Arrange
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@test.com")
                .build();
        
        Customer customer2 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@test.com")
                .build();
        
        // Assert
        assertEquals(customer1, customer2);
    }
}
