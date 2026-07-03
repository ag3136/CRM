package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

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
                .password("password123")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void testBuilder_createsUserWithAllFields() {
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertEquals(1, user.getEnabled());
        assertEquals(role, user.getRole());
    }

    @Test
    void testNoArgsConstructor_createsEmptyUser() {
        User emptyUser = new User();
        assertNotNull(emptyUser);
        assertNull(emptyUser.getId());
        assertNull(emptyUser.getUsername());
    }

    @Test
    void testAllArgsConstructor_createsUserWithAllFields() {
        User allArgsUser = new User(2L, "user2", "user2@example.com", "Jane", "Smith", "pass456", 1, role);
        assertNotNull(allArgsUser);
        assertEquals(2L, allArgsUser.getId());
        assertEquals("user2", allArgsUser.getUsername());
        assertEquals("user2@example.com", allArgsUser.getEmail());
        assertEquals("Jane", allArgsUser.getFirstName());
        assertEquals("Smith", allArgsUser.getLastName());
    }

    @Test
    void testGetName_returnsFirstNameAndLastName() {
        String name = user.getName();
        assertEquals("John Doe", name);
    }

    @Test
    void testGetName_withNullFirstName_returnsNullPlusLastName() {
        user.setFirstName(null);
        String name = user.getName();
        assertTrue(name.contains("Doe"));
    }

    @Test
    void testGetRole_id_returnsRoleId() {
        int roleId = user.getRole_id();
        assertEquals(1, roleId);
    }

    @Test
    void testGetRole_name_returnsRoleName() {
        String roleName = user.getRole_name();
        assertEquals("ROLE_USER", roleName);
    }

    @Test
    void testGetColumnCount_returnsFieldCount() {
        int count = user.getColumnCount();
        assertTrue(count > 0);
    }

    @Test
    void testSetUsername_updatesUsername() {
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());
    }

    @Test
    void testSetEmail_updatesEmail() {
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test
    void testSetPassword_updatesPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testSetEnabled_withZero_disablesUser() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }

    @Test
    void testSetEnabled_withOne_enablesUser() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    void testSetRole_updatesRole() {
        Role newRole = new Role();
        newRole.setId(2);
        newRole.setName("ROLE_ADMIN");
        user.setRole(newRole);
        assertEquals(newRole, user.getRole());
    }

    @Test
    void testEquals_equalUsers_returnsTrue() {
        User user2 = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();
        assertEquals(user, user2);
    }

    @Test
    void testEquals_differentUsers_returnsFalse() {
        User user2 = User.builder()
                .id(2L)
                .username("otheruser")
                .email("other@example.com")
                .build();
        assertNotEquals(user, user2);
    }

    @Test
    void testHashCode_equalUsers_sameHashCode() {
        User user2 = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testToString_containsUsername() {
        String toString = user.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("testuser"));
    }
}
