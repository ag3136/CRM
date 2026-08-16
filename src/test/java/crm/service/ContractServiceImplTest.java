package crm.service;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import crm.repository.ContractRepository;
import crm.repository.CustomerRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContractServiceImpl contractService;

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        customer = Customer.builder().id(1L).name("Test Customer").build();
        user = User.builder().id(1L).username("testuser").build();
        
        contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .value(new BigDecimal("10000"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    void findByName_withValidName_shouldReturnContract() {
        // Arrange
        when(contractRepository.findByName("Test Contract")).thenReturn(contract);

        // Act
        Contract result = contractService.findByName("Test Contract");

        // Assert
        assertNotNull(result);
        assertEquals("Test Contract", result.getName());
    }

    @Test
    void listAllContracts_shouldReturnAllContracts() {
        // Arrange
        when(contractRepository.findAll()).thenReturn(Arrays.asList(contract));

        // Act
        Iterable<Contract> result = contractService.listAllContracts();

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAll();
    }

    @Test
    void showContract_withValidId_shouldReturnContract() {
        // Arrange
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));

        // Act
        Contract result = contractService.showContract(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void showContract_withInvalidId_shouldReturnNull() {
        // Arrange
        when(contractRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Contract result = contractService.showContract(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void findAllByValueLessThanEqual_shouldReturnContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("15000");
        when(contractRepository.findAllByValueLessThanEqual(value))
                .thenReturn(Arrays.asList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByValueLessThanEqual(value);
    }

    @Test
    void findAllByValueGreaterThanEqual_shouldReturnContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("5000");
        when(contractRepository.findAllByValueGreaterThanEqual(value))
                .thenReturn(Arrays.asList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(value);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByValueGreaterThanEqual(value);
    }

    @Test
    void findAllByBeginDate_shouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 1, 1);
        when(contractRepository.findAllByBeginDate(date)).thenReturn(Arrays.asList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDate(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDate(date);
    }

    @Test
    void findAllByBeginDateBefore_shouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 6, 1);
        when(contractRepository.findAllByBeginDateBefore(date)).thenReturn(Arrays.asList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateBefore(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateBefore(date);
    }

    @Test
    void findAllByBeginDateAfter_shouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2023, 12, 1);
        when(contractRepository.findAllByBeginDateAfter(date)).thenReturn(Arrays.asList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateAfter(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateAfter(date);
    }

    @Test
    void findAllByStatus_shouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(Arrays.asList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void findAllByCustomer_shouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByCustomer(customer)).thenReturn(Arrays.asList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByCustomer(customer);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByCustomer(customer);
    }

    @Test
    void findAllByUser_shouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByUser(user)).thenReturn(Arrays.asList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByUser(user);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByUser(user);
    }

    @Test
    void saveContract_shouldSaveContract() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);

        // Act
        contractService.saveContract(contract);

        // Assert
        verify(contractRepository).save(contract);
    }
}
