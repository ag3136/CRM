package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role roleAdmin;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        roleAdmin = new Role();
        roleAdmin.setId(1);
        roleAdmin.setName("ROLE_ADMIN");

        roleUser = new Role();
        roleUser.setId(2);
        roleUser.setName("ROLE_USER");
    }

    @Test
    void testListAllRoles_returnsAllRoles() {
        List<Role> roles = Arrays.asList(roleAdmin, roleUser);
        when(roleRepository.findAll()).thenReturn(roles);
        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        verify(roleRepository).findAll();
    }

    @Test
    void testListAllRoles_returnsEmptyList_whenNoRoles() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList());
        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(roleRepository).findAll();
    }

    @Test
    void testListAllRoles_returnsSingleRole() {
        List<Role> roles = Arrays.asList(roleAdmin);
        when(roleRepository.findAll()).thenReturn(roles);
        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
        verify(roleRepository).findAll();
    }

    @Test
    void testConstructor_withRepository_createsInstance() {
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);
        assertNotNull(service);
    }
}
