package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

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
    void testBuilder_createsCustomerWithAllFields() {
        assertNotNull(customer);
        assertEquals(1L, customer.getId());
        assertEquals("Acme Corp", customer.getName());
        assertEquals("acme@example.com", customer.getEmail());
        assertEquals(1234567890, customer.getPhone());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("New York", customer.getCity());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void testNoArgsConstructor_createsEmptyCustomer() {
        Customer emptyCustomer = new Customer();
        assertNotNull(emptyCustomer);
        assertNull(emptyCustomer.getId());
        assertNull(emptyCustomer.getName());
    }

    @Test
    void testAllArgsConstructor_createsCustomerWithAllFields() {
        Customer allArgsCustomer = new Customer(2L, "Beta Corp", "beta@example.com", 987654321,
                categories, "Jane", "Smith", "Los Angeles", "456 Oak Ave", 1);
        assertNotNull(allArgsCustomer);
        assertEquals(2L, allArgsCustomer.getId());
        assertEquals("Beta Corp", allArgsCustomer.getName());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        customer.setId(5L);
        assertEquals(5L, customer.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        customer.setName("New Corp");
        assertEquals("New Corp", customer.getName());
    }

    @Test
    void testSetAndGetEmail_returnsCorrectEmail() {
        customer.setEmail("new@example.com");
        assertEquals("new@example.com", customer.getEmail());
    }

    @Test
    void testSetAndGetPhone_returnsCorrectPhone() {
        customer.setPhone(999999999);
        assertEquals(999999999, customer.getPhone());
    }

    @Test
    void testSetAndGetFirstName_returnsCorrectFirstName() {
        customer.setFirstName("Alice");
        assertEquals("Alice", customer.getFirstName());
    }

    @Test
    void testSetAndGetLastName_returnsCorrectLastName() {
        customer.setLastName("Johnson");
        assertEquals("Johnson", customer.getLastName());
    }

    @Test
    void testSetAndGetCity_returnsCorrectCity() {
        customer.setCity("Chicago");
        assertEquals("Chicago", customer.getCity());
    }

    @Test
    void testSetAndGetAddress_returnsCorrectAddress() {
        customer.setAddress("789 Pine Rd");
        assertEquals("789 Pine Rd", customer.getAddress());
    }

    @Test
    void testSetEnabled_withZero_disablesCustomer() {
        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }

    @Test
    void testSetEnabled_withOne_enablesCustomer() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void testSetAndGetCategories_returnsCorrectCategories() {
        Set<Category> newCategories = new HashSet<>();
        Category newCat = new Category();
        newCat.setId(2L);
        newCat.setName("Finance");
        newCategories.add(newCat);
        customer.setCategories(newCategories);
        assertEquals(newCategories, customer.getCategories());
    }

    @Test
    void testEquals_equalCustomers_returnsTrue() {
        Customer customer2 = Customer.builder()
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
        assertEquals(customer, customer2);
    }

    @Test
    void testEquals_differentCustomers_returnsFalse() {
        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Other Corp")
                .email("other@example.com")
                .build();
        assertNotEquals(customer, customer2);
    }

    @Test
    void testHashCode_equalCustomers_sameHashCode() {
        Customer customer2 = Customer.builder()
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
        assertEquals(customer.hashCode(), customer2.hashCode());
    }

    @Test
    void testToString_containsName() {
        String toString = customer.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Acme Corp"));
    }
}
