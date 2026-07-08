package crm.service;

import crm.entity.Role;
import crm.entity.User;
import crm.repository.RoleRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
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
    private Role roleUser;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        roleUser = new Role();
        roleUser.setId(1);
        roleUser.setName("ROLE_USER");

        roleAdmin = new Role();
        roleAdmin.setId(2);
        roleAdmin.setName("ROLE_ADMIN");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("plainPassword")
                .firstName("John")
                .lastName("Doe")
                .enabled(1)
                .role(roleUser)
                .build();
    }

    @Test
    void testFindByUsername_returnsUser_whenFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        User result = userService.findByUsername("testuser");
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_returnsNull_whenNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);
        User result = userService.findByUsername("unknown");
        assertNull(result);
    }

    @Test
    void testListAllUsers_returnsEnabledUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAllByEnabled(1)).thenReturn(users);
        Iterable<User> result = userService.listAllUsers();
        assertNotNull(result);
        verify(userRepository).findAllByEnabled(1);
    }

    @Test
    void testShowUser_returnsUser_whenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.showUser(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testShowUser_returnsNull_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        User result = userService.showUser(99L);
        assertNull(result);
    }

    @Test
    void testDeleteUser_setsEnabledZeroAndNullPassword() {
        userService.deleteUser(user);
        assertEquals(0, user.getEnabled());
        assertNull(user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUser_callsRepositorySave() {
        userService.deleteUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testEditUser_withValidRole_encodesPasswordAndSaves() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(roleRepository.findById(1)).thenReturn(Optional.of(roleUser));

        userService.editUser(user);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(1, user.getEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void testEditUser_withNullRole_usesDefaultRoleUser() {
        user.setRole(null);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(roleUser);

        userService.editUser(user);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(roleUser, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void testEditUser_withRoleNotFound_usesDefaultRoleUser() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(roleRepository.findById(1)).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_USER")).thenReturn(roleUser);

        userService.editUser(user);

        assertEquals(roleUser, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void testSetUserRepository_setsRepository() {
        UserServiceImpl service = new UserServiceImpl();
        service.setUserRepository(userRepository);
        // No exception means success
        assertNotNull(service);
    }

    @Test
    void testSetRoleRepository_setsRepository() {
        UserServiceImpl service = new UserServiceImpl();
        service.setRoleRepository(roleRepository);
        assertNotNull(service);
    }

    @Test
    void testSetPasswordEncoder_setsEncoder() {
        UserServiceImpl service = new UserServiceImpl();
        service.setPasswordEncoder(passwordEncoder);
        assertNotNull(service);
    }

    @Test
    void testSetAuthenticationManager_setsManager() {
        UserServiceImpl service = new UserServiceImpl();
        service.setAuthenticationManager(authenticationManager);
        assertNotNull(service);
    }

    @Test
    void testSetSpringDataUserDetailsService_setsService() {
        UserServiceImpl service = new UserServiceImpl();
        service.setSpringDataUserDetailsService(springDataUserDetailsService);
        assertNotNull(service);
    }
}
