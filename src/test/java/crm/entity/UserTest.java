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

        user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("secret");
        user.setEnabled(1);
        user.setRole(role);
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        User u = new User();
        assertNotNull(u);
    }

    @Test
    void testAllArgsConstructor() {
        User u = new User(2L, "janedoe", "jane@example.com", "Jane", "Doe", "pass", 1, role);
        assertEquals(2L, u.getId());
        assertEquals("janedoe", u.getUsername());
        assertEquals("jane@example.com", u.getEmail());
        assertEquals("Jane", u.getFirstName());
        assertEquals("Doe", u.getLastName());
        assertEquals("pass", u.getPassword());
        assertEquals(1, u.getEnabled());
        assertEquals(role, u.getRole());
    }

    @Test
    void testBuilder() {
        User u = User.builder()
                .id(3L)
                .username("builder_user")
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("Test")
                .password("builderpass")
                .enabled(1)
                .role(role)
                .build();

        assertEquals(3L, u.getId());
        assertEquals("builder_user", u.getUsername());
        assertEquals("builder@test.com", u.getEmail());
    }

    @Test
    void testGetName_concatenatesFirstAndLastName() {
        assertEquals("John Doe", user.getName());
    }

    @Test
    void testGetName_withDifferentNames() {
        user.setFirstName("Alice");
        user.setLastName("Smith");
        assertEquals("Alice Smith", user.getName());
    }

    @Test
    void testGetRole_id_returnsRoleId() {
        assertEquals(1, user.getRole_id());
    }

    @Test
    void testGetRole_name_returnsRoleName() {
        assertEquals("ROLE_USER", user.getRole_name());
    }

    @Test
    void testGetColumnCount_returnsFieldCount() {
        int count = user.getColumnCount();
        assertTrue(count > 0);
    }

    @Test
    void testSetAndGetId() {
        user.setId(99L);
        assertEquals(99L, user.getId());
    }

    @Test
    void testSetAndGetUsername() {
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());
    }

    @Test
    void testSetAndGetEmail() {
        user.setEmail("new@email.com");
        assertEquals("new@email.com", user.getEmail());
    }

    @Test
    void testSetAndGetFirstName() {
        user.setFirstName("Bob");
        assertEquals("Bob", user.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        user.setLastName("Brown");
        assertEquals("Brown", user.getLastName());
    }

    @Test
    void testSetAndGetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testSetAndGetEnabled_one() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    void testSetAndGetEnabled_zero() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }

    @Test
    void testSetAndGetRole() {
        Role newRole = new Role();
        newRole.setId(2);
        newRole.setName("ROLE_ADMIN");
        user.setRole(newRole);
        assertEquals(newRole, user.getRole());
    }

    @Test
    void testEquals_sameObject() {
        assertEquals(user, user);
    }

    @Test
    void testEquals_equalObjects() {
        User u1 = User.builder().id(1L).username("test").email("t@t.com").build();
        User u2 = User.builder().id(1L).username("test").email("t@t.com").build();
        assertEquals(u1, u2);
    }

    @Test
    void testHashCode_equalObjects() {
        User u1 = User.builder().id(1L).username("test").build();
        User u2 = User.builder().id(1L).username("test").build();
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testToString_notNull() {
        assertNotNull(user.toString());
    }

    @Test
    void testSetId_null() {
        user.setId(null);
        assertNull(user.getId());
    }
}
