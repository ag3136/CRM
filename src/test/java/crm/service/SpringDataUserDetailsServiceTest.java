package crm.service;

import crm.entity.CurrentUser;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private SpringDataUserDetailsService springDataUserDetailsService;

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
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("encodedPassword")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void testLoadUserByUsername_existingUser_returnsCurrentUser() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userService).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_existingUser_hasCorrectAuthority() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        assertNotNull(result.getAuthorities());
        assertEquals(1, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_nonExistingUser_throwsUsernameNotFoundException() {
        when(userService.findByUsername("nonexistent")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class,
                () -> springDataUserDetailsService.loadUserByUsername("nonexistent"));
        verify(userService).findByUsername("nonexistent");
    }

    @Test
    void testLoadUserByUsername_returnsCurrentUserInstance() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        assertInstanceOf(CurrentUser.class, result);
    }

    @Test
    void testLoadUserByUsername_withAdminRole_hasAdminAuthority() {
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");
        user.setRole(adminRole);

        when(userService.findByUsername("adminuser")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("adminuser");
        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsername_userPassword_isCorrect() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        assertEquals("encodedPassword", result.getPassword());
    }
}
