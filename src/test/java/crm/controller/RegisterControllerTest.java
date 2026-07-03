package crm.controller;

import crm.entity.Role;
import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RegisterController registerController;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("newuser")
                .email("newuser@example.com")
                .firstName("New")
                .lastName("User")
                .password("password")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void testShowRegistrationPage_returnsRegisterView() {
        String view = registerController.showRegistrationPage(model, user);
        assertEquals("register", view);
        verify(model).addAttribute(eq("user"), eq(user));
    }

    @Test
    void testProcessRegistrationForm_userAlreadyExists_returnsRegisterView() {
        when(userService.findByUsername("newuser")).thenReturn(user);
        String view = registerController.processRegistrationForm(model, user, bindingResult);
        assertEquals("register", view);
        verify(model).addAttribute(eq("alreadyRegisteredMessage"), anyString());
        verify(bindingResult).reject("email");
        verify(userService, never()).saveUser(any());
    }

    @Test
    void testProcessRegistrationForm_withBindingErrors_redirectsToRegister() {
        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = registerController.processRegistrationForm(model, user, bindingResult);
        assertEquals("redirect:/register", view);
        verify(userService, never()).saveUser(any());
    }

    @Test
    void testProcessRegistrationForm_newUserNoErrors_returnsSuccessView() {
        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        String view = registerController.processRegistrationForm(model, user, bindingResult);
        assertEquals("success", view);
        verify(userService).saveUser(user);
    }

    @Test
    void testConstructor_withService_createsInstance() {
        RegisterController controller = new RegisterController(userService);
        assertNotNull(controller);
    }
}
