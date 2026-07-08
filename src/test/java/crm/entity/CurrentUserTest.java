package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserTest {

    private CurrentUser currentUser;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEnabled(1);

        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        user.setRole(role);

        currentUser = new CurrentUser();
        currentUser.setUser(user);

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(authorities);
    }

    @Test
    void testGetPassword_returnsUserPassword() {
        assertEquals("encodedPassword", currentUser.getPassword());
    }

    @Test
    void testGetUsername_returnsUserUsername() {
        assertEquals("testuser", currentUser.getUsername());
    }

    @Test
    void testIsAccountNonExpired_returnsTrue() {
        assertTrue(currentUser.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked_returnsTrue() {
        assertTrue(currentUser.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired_returnsTrue() {
        assertTrue(currentUser.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled_returnsTrue() {
        assertTrue(currentUser.isEnabled());
    }

    @Test
    void testGetAuthorities_returnsCorrectAuthorities() {
        assertNotNull(currentUser.getAuthorities());
        assertEquals(1, currentUser.getAuthorities().size());
        assertTrue(currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testGetUser_returnsCorrectUser() {
        assertEquals(user, currentUser.getUser());
    }

    @Test
    void testSetUser_updatesUser() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpass");
        currentUser.setUser(newUser);
        assertEquals(newUser, currentUser.getUser());
    }

    @Test
    void testSetAuthorities_updatesAuthorities() {
        Set<GrantedAuthority> newAuthorities = new HashSet<>();
        newAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(newAuthorities);
        assertEquals(1, currentUser.getAuthorities().size());
        assertTrue(currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testGetAuthorities_multipleRoles() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities);
        assertEquals(2, currentUser.getAuthorities().size());
    }

    @Test
    void testEquals_sameObject() {
        assertEquals(currentUser, currentUser);
    }

    @Test
    void testToString_notNull() {
        assertNotNull(currentUser.toString());
    }
}
