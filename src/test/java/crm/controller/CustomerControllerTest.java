package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("TestCorp")
                .email("test@corp.com")
                .phone(1234567)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        CustomerController controller = new CustomerController(customerService);
        assertNotNull(controller);
    }

    @Test
    void testShowAllCustomers_returnsListView() {
        when(customerService.listAllCustomers()).thenReturn(Arrays.asList(customer));
        String view = customerController.showAllCustomers(model);
        assertEquals("customer/list", view);
        verify(model).addAttribute(eq("customers"), any());
    }

    @Test
    void testShowFormAddCustomer_returnsAddView() {
        String view = customerController.showFormAddCustomer(model);
        assertEquals("customer/add", view);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestAddCustomer_withErrors_redirectsToAdd() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = customerController.processRequestAddCustomer(customer, bindingResult);
        assertEquals("redirect:/customer/add", view);
        verify(customerService, never()).saveCustomer(any());
    }

    @Test
    void testProcessRequestAddCustomer_withoutErrors_savesAndReturnsSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String view = customerController.processRequestAddCustomer(customer, bindingResult);
        assertEquals("customer/success", view);
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testShowFormEditCustomer_returnsEditView() {
        when(customerService.showCustomer(1L)).thenReturn(customer);
        String view = customerController.showFormEditCustomer(model, 1L);
        assertEquals("customer/edit", view);
        verify(model).addAttribute(eq("customer"), eq(customer));
    }

    @Test
    void testProcessRequestEditCustomer_withErrors_redirectsToEdit() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = customerController.processRequestEditCustomer(1L, customer, bindingResult);
        assertEquals("redirect:/customer/edit/1", view);
        verify(customerService, never()).saveCustomer(any());
    }

    @Test
    void testProcessRequestEditCustomer_withoutErrors_savesAndRedirects() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String view = customerController.processRequestEditCustomer(1L, customer, bindingResult);
        assertEquals("redirect:/customer/list", view);
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testShowFormCreateCustomerBasedOnAnotherOne_returnsView() {
        when(customerService.showCustomer(1L)).thenReturn(customer);
        String view = customerController.showFormCreateCustomerBasedOnAnotherOne(model, 1L);
        assertEquals("customer/add-customer-based-on-another-one", view);
        verify(model).addAttribute(eq("customer"), eq(customer));
    }

    @Test
    void testCreateCustomerBasedOnAnotherOne_withErrors_redirects() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = customerController.createCustomerBasedOnAnotherOne(1L, customer, bindingResult);
        assertEquals("redirect:/customer/addCustomerBasedOnAnotherOne/1", view);
    }

    @Test
    void testCreateCustomerBasedOnAnotherOne_withoutErrors_savesAndRedirects() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(customerService.getMaxId()).thenReturn(5L);
        String view = customerController.createCustomerBasedOnAnotherOne(1L, customer, bindingResult);
        assertEquals("redirect:/customer/list", view);
        verify(customerService).saveCustomer(any(Customer.class));
    }

    @Test
    void testShowNameSearchForm_returnsView() {
        String view = customerController.showNameSearchForm(model);
        assertEquals("customer/name-search", view);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestNameSearch_returnsShowOneView() {
        when(customerService.findOneByEnabledTrueAndName("TestCorp")).thenReturn(customer);
        String view = customerController.processRequestNameSearch(customer, model);
        assertEquals("customer/show-one", view);
        verify(model).addAttribute(eq("customer"), eq(customer));
    }

    @Test
    void testShowEmailSearchForm_returnsView() {
        String view = customerController.showEmailSearchForm(model);
        assertEquals("customer/email-search", view);
    }

    @Test
    void testProcessRequestEmailSearch_returnsShowListView() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndEmail("test@corp.com")).thenReturn(customers);
        String view = customerController.processRequestEmailSearch(customer, model);
        assertEquals("customer/show-list", view);
        verify(model).addAttribute(eq("customers"), any());
    }

    @Test
    void testShowPhoneSearchForm_returnsView() {
        String view = customerController.showPhoneSearchForm(model);
        assertEquals("customer/phone-search", view);
    }

    @Test
    void testProcessRequestPhoneSearch_returnsShowListView() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndPhone(1234567)).thenReturn(customers);
        String view = customerController.processRequestPhoneSearch(customer, model);
        assertEquals("customer/show-list", view);
    }

    @Test
    void testShowFirstNameSearchForm_returnsView() {
        String view = customerController.showFirstNameSearchForm(model);
        assertEquals("customer/first-name-search", view);
    }

    @Test
    void testProcessRequestFirstNameSearch_returnsShowListView() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndFirstName("John")).thenReturn(customers);
        String view = customerController.processRequestFirstNameSearch(customer, model);
        assertEquals("customer/show-list", view);
    }

    @Test
    void testShowLastNameSearchForm_returnsView() {
        String view = customerController.showLastNameSearchForm(model);
        assertEquals("customer/last-name-search", view);
    }

    @Test
    void testProcessRequestLastNameSearch_returnsShowListView() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndLastName("Doe")).thenReturn(customers);
        String view = customerController.processRequestLastNameSearch(customer, model);
        assertEquals("customer/show-list", view);
    }

    @Test
    void testShowFirstNameLastNameSearchForm_returnsView() {
        String view = customerController.showFirstNameLastNameSearchForm(model);
        assertEquals("customer/first-name-last-name-search", view);
    }

    @Test
    void testProcessRequestFirstNameLastNameSearch_returnsShowListView() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe")).thenReturn(customers);
        String view = customerController.processRequestFirstNameLastNameSearch(customer, model);
        assertEquals("customer/show-list", view);
    }

    @Test
    void testShowCitySearchForm_returnsView() {
        String view = customerController.showCitySearchForm(model);
        assertEquals("customer/city-search", view);
    }

    @Test
    void testProcessRequestCitySearch_returnsShowListView() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndCity("New York")).thenReturn(customers);
        String view = customerController.processRequestCitySearch(customer, model);
        assertEquals("customer/show-list", view);
    }

    @Test
    void testShowCityAddressSearchForm_returnsView() {
        String view = customerController.showCityAddressSearchForm(model);
        assertEquals("customer/city-address-search", view);
    }

    @Test
    void testProcessRequestCityAddressSearch_returnsShowListView() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndCityAndAddress("New York", "123 Main St")).thenReturn(customers);
        String view = customerController.processRequestCityAddressSearch(customer, model);
        assertEquals("customer/show-list", view);
    }
}
