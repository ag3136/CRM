package crm.service;

import crm.entity.CurrentUser;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringDataUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SpringDataUserDetailsService userDetailsService;

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
                .password("password")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void loadUserByUsername_withValidUsername_shouldReturnUserDetails() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(user);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CurrentUser);
        assertEquals("testuser", result.getUsername());
        assertEquals("password", result.getPassword());
        verify(userService).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_withInvalidUsername_shouldThrowException() {
        // Arrange
        when(userService.findByUsername("invalid")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> 
            userDetailsService.loadUserByUsername("invalid"));
    }

    @Test
    void loadUserByUsername_shouldSetAuthorities() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(user);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result.getAuthorities());
        assertFalse(result.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsername_withAdminRole_shouldSetAdminAuthority() {
        // Arrange
        role.setName("ROLE_ADMIN");
        when(userService.findByUsername("admin")).thenReturn(user);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_shouldReturnCurrentUserInstance() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(user);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertTrue(result instanceof CurrentUser);
        CurrentUser currentUser = (CurrentUser) result;
        assertEquals(user, currentUser.getUser());
    }
}
