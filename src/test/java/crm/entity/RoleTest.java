package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        assertNotNull(role);
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        role.setName("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void testSetName_withNull_returnsNull() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    void testSetName_withRoleUser_returnsCorrectName() {
        role.setName("ROLE_USER");
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testSetName_withRoleManager_returnsCorrectName() {
        role.setName("ROLE_MANAGER");
        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    void testEquals_equalObjects_returnsTrue() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_ADMIN");

        assertEquals(role1, role2);
    }

    @Test
    void testEquals_differentObjects_returnsFalse() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_USER");

        assertNotEquals(role1, role2);
    }

    @Test
    void testHashCode_equalObjects_sameHashCode() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_ADMIN");

        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    void testToString_containsFieldValues() {
        role.setId(1);
        role.setName("ROLE_ADMIN");
        String toString = role.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ROLE_ADMIN"));
    }

    @Test
    void testSetId_withZero_returnsZero() {
        role.setId(0);
        assertEquals(0, role.getId());
    }
}
