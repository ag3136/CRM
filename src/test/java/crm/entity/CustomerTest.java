package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        assertNotNull(customer);
    }

    @Test
    void testAllArgsConstructor() {
        Set<Category> categories = new HashSet<>();
        Customer c = new Customer(1L, "AcmeCorp", "acme@example.com", 1234567890,
                categories, "John", "Doe", "New York", "123 Main St", 1);

        assertEquals(1L, c.getId());
        assertEquals("AcmeCorp", c.getName());
        assertEquals("acme@example.com", c.getEmail());
        assertEquals(1234567890, c.getPhone());
        assertEquals("John", c.getFirstName());
        assertEquals("Doe", c.getLastName());
        assertEquals("New York", c.getCity());
        assertEquals("123 Main St", c.getAddress());
        assertEquals(1, c.getEnabled());
    }

    @Test
    void testBuilder() {
        Customer c = Customer.builder()
                .id(1L)
                .name("TestCorp")
                .email("test@corp.com")
                .phone(9876543)
                .firstName("Jane")
                .lastName("Smith")
                .city("Chicago")
                .address("456 Oak Ave")
                .enabled(1)
                .build();

        assertEquals(1L, c.getId());
        assertEquals("TestCorp", c.getName());
        assertEquals("test@corp.com", c.getEmail());
        assertEquals(9876543, c.getPhone());
        assertEquals("Jane", c.getFirstName());
        assertEquals("Smith", c.getLastName());
        assertEquals("Chicago", c.getCity());
        assertEquals("456 Oak Ave", c.getAddress());
        assertEquals(1, c.getEnabled());
    }

    @Test
    void testSetAndGetId() {
        customer.setId(5L);
        assertEquals(5L, customer.getId());
    }

    @Test
    void testSetAndGetName() {
        customer.setName("TestCustomer");
        assertEquals("TestCustomer", customer.getName());
    }

    @Test
    void testSetAndGetEmail() {
        customer.setEmail("customer@test.com");
        assertEquals("customer@test.com", customer.getEmail());
    }

    @Test
    void testSetAndGetPhone() {
        customer.setPhone(5551234);
        assertEquals(5551234, customer.getPhone());
    }

    @Test
    void testSetAndGetFirstName() {
        customer.setFirstName("Alice");
        assertEquals("Alice", customer.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        customer.setLastName("Johnson");
        assertEquals("Johnson", customer.getLastName());
    }

    @Test
    void testSetAndGetCity() {
        customer.setCity("Boston");
        assertEquals("Boston", customer.getCity());
    }

    @Test
    void testSetAndGetAddress() {
        customer.setAddress("789 Pine St");
        assertEquals("789 Pine St", customer.getAddress());
    }

    @Test
    void testSetAndGetEnabled_one() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void testSetAndGetEnabled_zero() {
        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }

    @Test
    void testSetAndGetCategories() {
        Set<Category> categories = new HashSet<>();
        Category cat = new Category();
        cat.setId(1L);
        cat.setName("VIP");
        categories.add(cat);
        customer.setCategories(categories);
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void testSetCategories_empty() {
        customer.setCategories(new HashSet<>());
        assertNotNull(customer.getCategories());
        assertTrue(customer.getCategories().isEmpty());
    }

    @Test
    void testEquals_sameObject() {
        customer.setId(1L);
        assertEquals(customer, customer);
    }

    @Test
    void testEquals_equalObjects() {
        Customer c1 = Customer.builder().id(1L).name("Corp").email("a@b.com").build();
        Customer c2 = Customer.builder().id(1L).name("Corp").email("a@b.com").build();
        assertEquals(c1, c2);
    }

    @Test
    void testHashCode_equalObjects() {
        Customer c1 = Customer.builder().id(1L).name("Corp").build();
        Customer c2 = Customer.builder().id(1L).name("Corp").build();
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString_notNull() {
        customer.setId(1L);
        customer.setName("TestCorp");
        assertNotNull(customer.toString());
    }

    @Test
    void testSetId_null() {
        customer.setId(null);
        assertNull(customer.getId());
    }

    @Test
    void testSetName_null() {
        customer.setName(null);
        assertNull(customer.getName());
    }
}
