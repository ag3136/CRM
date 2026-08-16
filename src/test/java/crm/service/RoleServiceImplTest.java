package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
    }

    @Test
    void listAllRoles_shouldReturnAllRoles() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        verify(roleRepository).findAll();
    }

    @Test
    void listAllRoles_withEmptyRepository_shouldReturnEmptyList() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        verify(roleRepository).findAll();
    }
}
