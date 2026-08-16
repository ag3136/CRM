package crm.service;

import crm.entity.Role;
import crm.entity.User;
import crm.repository.RoleRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SpringDataUserDetailsService springDataUserDetailsService;

    @InjectMocks
    private UserServiceImpl userService;

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
                .password("password")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void findByUsername_withValidUsername_shouldReturnUser() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // Act
        User result = userService.findByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void findByUsername_withInvalidUsername_shouldReturnNull() {
        // Arrange
        when(userRepository.findByUsername("invalid")).thenReturn(null);

        // Act
        User result = userService.findByUsername("invalid");

        // Assert
        assertNull(result);
    }

    @Test
    void listAllUsers_shouldReturnAllEnabledUsers() {
        // Arrange
        when(userRepository.findAllByEnabled(1)).thenReturn(Arrays.asList(user));

        // Act
        Iterable<User> result = userService.listAllUsers();

        // Assert
        assertNotNull(result);
        verify(userRepository).findAllByEnabled(1);
    }

    @Test
    void showUser_withValidId_shouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.showUser(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void showUser_withInvalidId_shouldReturnNull() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        User result = userService.showUser(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void editUser_withValidUser_shouldUpdateUser() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.editUser(user);

        // Assert
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser_withValidUser_shouldDisableUser() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.deleteUser(user);

        // Assert
        verify(userRepository).save(argThat(u -> u.getEnabled() == 0 && u.getPassword() == null));
    }

    @Test
    void editUser_withNullRole_shouldUseDefaultRole() {
        // Arrange
        user.setRole(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.editUser(user);

        // Assert
        verify(roleRepository).findByName("ROLE_USER");
        verify(userRepository).save(any(User.class));
    }
}
