package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractTest {

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .build();
        
        user = User.builder()
                .id(1L)
                .username("testuser")
                .build();
        
        contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    void contract_shouldCreateInstance() {
        assertNotNull(contract);
    }

    @Test
    void builder_shouldCreateContractWithAllFields() {
        assertEquals(1L, contract.getId());
        assertEquals("Test Contract", contract.getName());
        assertEquals("Contract content", contract.getContent());
        assertEquals(new BigDecimal("10000.00"), contract.getValue());
        assertEquals(LocalDate.of(2024, 1, 1), contract.getBeginDate());
        assertEquals(LocalDate.of(2024, 12, 31), contract.getEndDate());
        assertEquals(Status.PROPOSED, contract.getStatus());
        assertNotNull(contract.getCustomer());
        assertNotNull(contract.getUser());
    }

    @Test
    void setName_withValidName_shouldSetName() {
        // Arrange
        String newName = "Updated Contract";
        
        // Act
        contract.setName(newName);
        
        // Assert
        assertEquals(newName, contract.getName());
    }

    @Test
    void setValue_withValidValue_shouldSetValue() {
        // Arrange
        BigDecimal newValue = new BigDecimal("20000.00");
        
        // Act
        contract.setValue(newValue);
        
        // Assert
        assertEquals(newValue, contract.getValue());
    }

    @Test
    void setStatus_withDifferentStatus_shouldUpdateStatus() {
        // Act
        contract.setStatus(Status.IMPLEMENTED);
        
        // Assert
        assertEquals(Status.IMPLEMENTED, contract.getStatus());
    }

    @Test
    void setBeginDate_withValidDate_shouldSetBeginDate() {
        // Arrange
        LocalDate newDate = LocalDate.of(2025, 1, 1);
        
        // Act
        contract.setBeginDate(newDate);
        
        // Assert
        assertEquals(newDate, contract.getBeginDate());
    }

    @Test
    void setEndDate_withValidDate_shouldSetEndDate() {
        // Arrange
        LocalDate newDate = LocalDate.of(2025, 12, 31);
        
        // Act
        contract.setEndDate(newDate);
        
        // Assert
        assertEquals(newDate, contract.getEndDate());
    }

    @Test
    void setCustomer_withValidCustomer_shouldSetCustomer() {
        // Arrange
        Customer newCustomer = Customer.builder().id(2L).name("New Customer").build();
        
        // Act
        contract.setCustomer(newCustomer);
        
        // Assert
        assertEquals(newCustomer, contract.getCustomer());
    }

    @Test
    void setUser_withValidUser_shouldSetUser() {
        // Arrange
        User newUser = User.builder().id(2L).username("newuser").build();
        
        // Act
        contract.setUser(newUser);
        
        // Assert
        assertEquals(newUser, contract.getUser());
    }

    @Test
    void contract_shouldHaveEntityAnnotation() {
        assertTrue(Contract.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyContract() {
        // Act
        Contract emptyContract = new Contract();
        
        // Assert
        assertNotNull(emptyContract);
    }

    @Test
    void allArgsConstructor_shouldCreateContractWithAllFields() {
        // Act
        Contract newContract = new Contract(2L, "Contract2", "Content2", 
            new BigDecimal("5000"), LocalDate.now(), LocalDate.now().plusDays(30), 
            Status.NEGOTIATED, customer, user);
        
        // Assert
        assertEquals(2L, newContract.getId());
        assertEquals("Contract2", newContract.getName());
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        // Arrange
        Contract contract1 = Contract.builder()
                .id(1L)
                .name("Test")
                .build();
        
        Contract contract2 = Contract.builder()
                .id(1L)
                .name("Test")
                .build();
        
        // Assert
        assertEquals(contract1, contract2);
    }
}
