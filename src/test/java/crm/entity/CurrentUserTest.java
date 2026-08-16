package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserTest {

    private CurrentUser currentUser;
    private User user;
    private Set<GrantedAuthority> authorities;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .enabled(1)
                .build();
        
        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        currentUser = new CurrentUser();
        currentUser.setUser(user);
        currentUser.setAuthorities(authorities);
    }

    @Test
    void currentUser_shouldCreateInstance() {
        assertNotNull(currentUser);
    }

    @Test
    void getAuthorities_shouldReturnAuthorities() {
        assertEquals(authorities, currentUser.getAuthorities());
        assertEquals(1, currentUser.getAuthorities().size());
    }

    @Test
    void getPassword_shouldReturnUserPassword() {
        assertEquals("password123", currentUser.getPassword());
    }

    @Test
    void getUsername_shouldReturnUsername() {
        assertEquals("testuser", currentUser.getUsername());
    }

    @Test
    void isAccountNonExpired_shouldReturnTrue() {
        assertTrue(currentUser.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_shouldReturnTrue() {
        assertTrue(currentUser.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_shouldReturnTrue() {
        assertTrue(currentUser.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_shouldReturnTrue() {
        assertTrue(currentUser.isEnabled());
    }

    @Test
    void setUser_withValidUser_shouldSetUser() {
        // Arrange
        User newUser = User.builder()
                .id(2L)
                .username("newuser")
                .build();
        
        // Act
        currentUser.setUser(newUser);
        
        // Assert
        assertEquals(newUser, currentUser.getUser());
    }

    @Test
    void setAuthorities_withValidAuthorities_shouldSetAuthorities() {
        // Arrange
        Set<GrantedAuthority> newAuthorities = new HashSet<>();
        newAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        
        // Act
        currentUser.setAuthorities(newAuthorities);
        
        // Assert
        assertEquals(newAuthorities, currentUser.getAuthorities());
    }

    @Test
    void getUser_shouldReturnUser() {
        assertEquals(user, currentUser.getUser());
    }

    @Test
    void currentUser_shouldImplementUserDetails() {
        assertTrue(org.springframework.security.core.userdetails.UserDetails.class
                .isAssignableFrom(CurrentUser.class));
    }

    @Test
    void getPassword_withNullUser_shouldReturnNull() {
        // Arrange
        currentUser.setUser(null);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> currentUser.getPassword());
    }

    @Test
    void getUsername_withNullUser_shouldReturnNull() {
        // Arrange
        currentUser.setUser(null);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> currentUser.getUsername());
    }
}
