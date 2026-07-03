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
    private Role role;
    private Set<GrantedAuthority> authorities;

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

        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        currentUser = new CurrentUser();
        currentUser.setUser(user);
        currentUser.setAuthorities(authorities);
    }

    @Test
    void testGetUsername_returnsUserUsername() {
        assertEquals("testuser", currentUser.getUsername());
    }

    @Test
    void testGetPassword_returnsUserPassword() {
        assertEquals("encodedPassword", currentUser.getPassword());
    }

    @Test
    void testGetAuthorities_returnsAuthorities() {
        assertNotNull(currentUser.getAuthorities());
        assertEquals(1, currentUser.getAuthorities().size());
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
    void testSetAndGetUser_returnsCorrectUser() {
        User newUser = User.builder()
                .id(2L)
                .username("newuser")
                .password("newpass")
                .build();
        currentUser.setUser(newUser);
        assertEquals(newUser, currentUser.getUser());
    }

    @Test
    void testSetAndGetAuthorities_returnsCorrectAuthorities() {
        Set<GrantedAuthority> newAuthorities = new HashSet<>();
        newAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(newAuthorities);
        assertEquals(newAuthorities, currentUser.getAuthorities());
    }

    @Test
    void testGetAuthorities_containsRoleUser() {
        boolean hasRoleUser = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        assertTrue(hasRoleUser);
    }

    @Test
    void testEquals_equalCurrentUsers_returnsTrue() {
        CurrentUser currentUser2 = new CurrentUser();
        currentUser2.setUser(user);
        currentUser2.setAuthorities(authorities);
        assertEquals(currentUser, currentUser2);
    }

    @Test
    void testHashCode_equalCurrentUsers_sameHashCode() {
        CurrentUser currentUser2 = new CurrentUser();
        currentUser2.setUser(user);
        currentUser2.setAuthorities(authorities);
        assertEquals(currentUser.hashCode(), currentUser2.hashCode());
    }

    @Test
    void testToString_isNotNull() {
        assertNotNull(currentUser.toString());
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        CurrentUser newCurrentUser = new CurrentUser();
        assertNotNull(newCurrentUser);
    }
}
