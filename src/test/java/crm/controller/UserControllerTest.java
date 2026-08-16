package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .enabled(1)
                .build();
    }

    @Test
    void showFormEditUser_shouldReturnEditUserView() throws Exception {
        // Arrange
        when(userService.showUser(1L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/user/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"))
                .andExpect(model().attributeExists("user"));
        
        verify(userService).showUser(1L);
    }

    @Test
    void deleteUser_shouldRedirectToUserList() throws Exception {
        // Arrange
        when(userService.showUser(1L)).thenReturn(user);
        doNothing().when(userService).deleteUser(any(User.class));

        // Act & Assert
        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
        
        verify(userService).showUser(1L);
        verify(userService).deleteUser(any(User.class));
    }
}
