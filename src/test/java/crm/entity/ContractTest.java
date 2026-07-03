package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractTest {

    private Contract contract;
    private Customer customer;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .enabled(1)
                .role(role)
                .build();

        customer = Customer.builder()
                .id(1L)
                .name("Acme Corp")
                .email("acme@example.com")
                .phone(1234567890)
                .firstName("Jane")
                .lastName("Smith")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        contract = Contract.builder()
                .id(1L)
                .name("Contract-001")
                .content("Contract content here")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    void testBuilder_createsContractWithAllFields() {
        assertNotNull(contract);
        assertEquals(1L, contract.getId());
        assertEquals("Contract-001", contract.getName());
        assertEquals("Contract content here", contract.getContent());
        assertEquals(new BigDecimal("10000.00"), contract.getValue());
        assertEquals(LocalDate.of(2024, 1, 1), contract.getBeginDate());
        assertEquals(LocalDate.of(2024, 12, 31), contract.getEndDate());
        assertEquals(Status.PROPOSED, contract.getStatus());
        assertEquals(customer, contract.getCustomer());
        assertEquals(user, contract.getUser());
    }

    @Test
    void testNoArgsConstructor_createsEmptyContract() {
        Contract emptyContract = new Contract();
        assertNotNull(emptyContract);
        assertNull(emptyContract.getId());
        assertNull(emptyContract.getName());
    }

    @Test
    void testAllArgsConstructor_createsContractWithAllFields() {
        Contract allArgsContract = new Contract(2L, "Contract-002", "Content", new BigDecimal("5000.00"),
                LocalDate.of(2024, 2, 1), LocalDate.of(2024, 11, 30), Status.NEGOTIATED, customer, user);
        assertNotNull(allArgsContract);
        assertEquals(2L, allArgsContract.getId());
        assertEquals("Contract-002", allArgsContract.getName());
        assertEquals(Status.NEGOTIATED, allArgsContract.getStatus());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        contract.setId(5L);
        assertEquals(5L, contract.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        contract.setName("New Contract");
        assertEquals("New Contract", contract.getName());
    }

    @Test
    void testSetAndGetContent_returnsCorrectContent() {
        contract.setContent("New content");
        assertEquals("New content", contract.getContent());
    }

    @Test
    void testSetAndGetValue_returnsCorrectValue() {
        contract.setValue(new BigDecimal("20000.00"));
        assertEquals(new BigDecimal("20000.00"), contract.getValue());
    }

    @Test
    void testSetAndGetBeginDate_returnsCorrectDate() {
        LocalDate newDate = LocalDate.of(2025, 3, 15);
        contract.setBeginDate(newDate);
        assertEquals(newDate, contract.getBeginDate());
    }

    @Test
    void testSetAndGetEndDate_returnsCorrectDate() {
        LocalDate newDate = LocalDate.of(2025, 12, 31);
        contract.setEndDate(newDate);
        assertEquals(newDate, contract.getEndDate());
    }

    @Test
    void testSetAndGetStatus_proposed() {
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatus_negotiated() {
        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatus_implemented() {
        contract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatus_done() {
        contract.setStatus(Status.DONE);
        assertEquals(Status.DONE, contract.getStatus());
    }

    @Test
    void testSetAndGetCustomer_returnsCorrectCustomer() {
        Customer newCustomer = Customer.builder().id(2L).name("Beta Corp").build();
        contract.setCustomer(newCustomer);
        assertEquals(newCustomer, contract.getCustomer());
    }

    @Test
    void testSetAndGetUser_returnsCorrectUser() {
        User newUser = User.builder().id(2L).username("newuser").build();
        contract.setUser(newUser);
        assertEquals(newUser, contract.getUser());
    }

    @Test
    void testEquals_equalContracts_returnsTrue() {
        Contract contract2 = Contract.builder()
                .id(1L)
                .name("Contract-001")
                .content("Contract content here")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        assertEquals(contract, contract2);
    }

    @Test
    void testEquals_differentContracts_returnsFalse() {
        Contract contract2 = Contract.builder()
                .id(2L)
                .name("Contract-002")
                .build();
        assertNotEquals(contract, contract2);
    }

    @Test
    void testHashCode_equalContracts_sameHashCode() {
        Contract contract2 = Contract.builder()
                .id(1L)
                .name("Contract-001")
                .content("Contract content here")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
        assertEquals(contract.hashCode(), contract2.hashCode());
    }

    @Test
    void testToString_containsName() {
        String toString = contract.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Contract-001"));
    }

    @Test
    void testSetValue_withNull_returnsNull() {
        contract.setValue(null);
        assertNull(contract.getValue());
    }

    @Test
    void testSetValue_withZero_returnsZero() {
        contract.setValue(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, contract.getValue());
    }
}
