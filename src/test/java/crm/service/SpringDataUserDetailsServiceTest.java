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
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void testLoadUserByUsername_returnsCurrentUser_whenUserExists() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void testLoadUserByUsername_returnsCurrentUserWithAuthorities() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        assertNotNull(result.getAuthorities());
        assertFalse(result.getAuthorities().isEmpty());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_throwsUsernameNotFoundException_whenUserNotFound() {
        when(userService.findByUsername("unknown")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class,
                () -> springDataUserDetailsService.loadUserByUsername("unknown"));
    }

    @Test
    void testLoadUserByUsername_returnsCurrentUserInstance() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        assertInstanceOf(CurrentUser.class, result);
    }

    @Test
    void testLoadUserByUsername_setsUserOnCurrentUser() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        CurrentUser currentUser = (CurrentUser) result;
        assertEquals(user, currentUser.getUser());
    }

    @Test
    void testLoadUserByUsername_withAdminRole() {
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");
        user.setRole(adminRole);

        when(userService.findByUsername("admin")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("admin");
        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsername_callsUserServiceOnce() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        springDataUserDetailsService.loadUserByUsername("testuser");
        verify(userService, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_exceptionMessageContainsUsername() {
        when(userService.findByUsername("missinguser")).thenReturn(null);
        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> springDataUserDetailsService.loadUserByUsername("missinguser"));
        assertEquals("missinguser", ex.getMessage());
    }
}
