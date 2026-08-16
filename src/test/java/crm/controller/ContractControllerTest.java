package crm.controller;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import crm.service.ContractService;
import crm.service.CustomerService;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {

    @Mock
    private ContractService contractService;

    @Mock
    private CustomerService customerService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ContractController contractController;

    private MockMvc mockMvc;
    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(contractController).build();
        
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
    void showAllContracts_shouldReturnContractListView() throws Exception {
        // Arrange
        when(contractService.listAllContracts()).thenReturn(Arrays.asList(contract));

        // Act & Assert
        mockMvc.perform(get("/contract/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract/list"))
                .andExpect(model().attributeExists("contracts"));
        
        verify(contractService).listAllContracts();
    }

    @Test
    void showFormAddContract_shouldReturnAddContractView() throws Exception {
        // Arrange
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList(customer));
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));

        // Act & Assert
        mockMvc.perform(get("/contract/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract/add"))
                .andExpect(model().attributeExists("contract"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void showFormEditContract_shouldReturnEditContractView() throws Exception {
        // Arrange
        when(contractService.showContract(1L)).thenReturn(contract);

        // Act & Assert
        mockMvc.perform(get("/contract/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract/edit"))
                .andExpect(model().attributeExists("contract"));
        
        verify(contractService).showContract(1L);
    }

    @Test
    void showNameSearchForm_shouldReturnNameSearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/contract/name-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract/name-search"))
                .andExpect(model().attributeExists("contract"));
    }

    @Test
    void showValueLessThanEqualSearchForm_shouldReturnValueLeSearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/contract/value-le-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract/value-le-search"))
                .andExpect(model().attributeExists("contract"));
    }

    @Test
    void showValueGreaterThanEqualSearchForm_shouldReturnValueGeSearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/contract/value-ge-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract/value-ge-search"))
                .andExpect(model().attributeExists("contract"));
    }

    @Test
    void showBeginDateSearchForm_shouldReturnBeginDateSearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/contract/begin-date-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract/begin-date-search"))
                .andExpect(model().attributeExists("contract"));
    }

    @Test
    void showStatusSearchForm_shouldReturnStatusSearchView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/contract/status-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract/status-search"))
                .andExpect(model().attributeExists("contract"));
    }

    @Test
    void showCustomerSearchForm_shouldReturnCustomerSearchView() throws Exception {
        // Arrange
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList(customer));

        // Act & Assert
        mockMvc.perform(get("/contract/customer-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract/customer-search"))
                .andExpect(model().attributeExists("contract"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    void showUserSearchForm_shouldReturnUserSearchView() throws Exception {
        // Arrange
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));

        // Act & Assert
        mockMvc.perform(get("/contract/user-search"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract/user-search"))
                .andExpect(model().attributeExists("contract"))
                .andExpect(model().attributeExists("users"));
    }
}
