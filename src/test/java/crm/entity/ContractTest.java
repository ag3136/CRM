package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractTest {

    private Contract contract;

    @BeforeEach
    void setUp() {
        contract = new Contract();
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        assertNotNull(contract);
    }

    @Test
    void testAllArgsConstructor() {
        Customer customer = new Customer();
        User user = new User();
        LocalDate begin = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 12, 31);

        Contract c = new Contract(1L, "Contract1", "Content", new BigDecimal("1000.00"),
                begin, end, Status.PROPOSED, customer, user);

        assertEquals(1L, c.getId());
        assertEquals("Contract1", c.getName());
        assertEquals("Content", c.getContent());
        assertEquals(new BigDecimal("1000.00"), c.getValue());
        assertEquals(begin, c.getBeginDate());
        assertEquals(end, c.getEndDate());
        assertEquals(Status.PROPOSED, c.getStatus());
        assertEquals(customer, c.getCustomer());
        assertEquals(user, c.getUser());
    }

    @Test
    void testBuilder() {
        Contract c = Contract.builder()
                .id(1L)
                .name("TestContract")
                .content("Some content")
                .value(new BigDecimal("500.00"))
                .beginDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .status(Status.NEGOTIATED)
                .build();

        assertEquals(1L, c.getId());
        assertEquals("TestContract", c.getName());
        assertEquals("Some content", c.getContent());
        assertEquals(new BigDecimal("500.00"), c.getValue());
        assertEquals(Status.NEGOTIATED, c.getStatus());
    }

    @Test
    void testSetAndGetId() {
        contract.setId(10L);
        assertEquals(10L, contract.getId());
    }

    @Test
    void testSetAndGetName() {
        contract.setName("MyContract");
        assertEquals("MyContract", contract.getName());
    }

    @Test
    void testSetAndGetContent() {
        contract.setContent("Contract content here");
        assertEquals("Contract content here", contract.getContent());
    }

    @Test
    void testSetAndGetValue() {
        contract.setValue(new BigDecimal("9999.99"));
        assertEquals(new BigDecimal("9999.99"), contract.getValue());
    }

    @Test
    void testSetAndGetBeginDate() {
        LocalDate date = LocalDate.of(2023, 6, 15);
        contract.setBeginDate(date);
        assertEquals(date, contract.getBeginDate());
    }

    @Test
    void testSetAndGetEndDate() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        contract.setEndDate(date);
        assertEquals(date, contract.getEndDate());
    }

    @Test
    void testSetAndGetStatus_proposed() {
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatus_done() {
        contract.setStatus(Status.DONE);
        assertEquals(Status.DONE, contract.getStatus());
    }

    @Test
    void testSetAndGetStatus_implemented() {
        contract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatus_negotiated() {
        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());
    }

    @Test
    void testSetAndGetCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        contract.setCustomer(customer);
        assertEquals(customer, contract.getCustomer());
    }

    @Test
    void testSetAndGetUser() {
        User user = new User();
        user.setId(1L);
        contract.setUser(user);
        assertEquals(user, contract.getUser());
    }

    @Test
    void testSetName_null() {
        contract.setName(null);
        assertNull(contract.getName());
    }

    @Test
    void testSetValue_zero() {
        contract.setValue(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, contract.getValue());
    }

    @Test
    void testEquals_sameObject() {
        contract.setId(1L);
        contract.setName("C1");
        assertEquals(contract, contract);
    }

    @Test
    void testEquals_equalObjects() {
        Contract c1 = Contract.builder().id(1L).name("C1").build();
        Contract c2 = Contract.builder().id(1L).name("C1").build();
        assertEquals(c1, c2);
    }

    @Test
    void testHashCode_consistency() {
        Contract c1 = Contract.builder().id(1L).name("C1").build();
        Contract c2 = Contract.builder().id(1L).name("C1").build();
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString_notNull() {
        contract.setId(1L);
        contract.setName("TestContract");
        assertNotNull(contract.toString());
    }
}
