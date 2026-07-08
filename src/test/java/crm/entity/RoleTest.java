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
    void testSetAndGetId() {
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    void testSetAndGetName() {
        role.setName("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void testSetName_roleUser() {
        role.setName("ROLE_USER");
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testSetName_roleManager() {
        role.setName("ROLE_MANAGER");
        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    void testSetName_null() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    void testSetId_zero() {
        role.setId(0);
        assertEquals(0, role.getId());
    }

    @Test
    void testSetId_negative() {
        role.setId(-1);
        assertEquals(-1, role.getId());
    }

    @Test
    void testEquals_sameObject() {
        role.setId(1);
        role.setName("ROLE_USER");
        assertEquals(role, role);
    }

    @Test
    void testEquals_equalObjects() {
        Role r1 = new Role();
        r1.setId(1);
        r1.setName("ROLE_USER");

        Role r2 = new Role();
        r2.setId(1);
        r2.setName("ROLE_USER");

        assertEquals(r1, r2);
    }

    @Test
    void testEquals_differentObjects() {
        Role r1 = new Role();
        r1.setId(1);
        r1.setName("ROLE_USER");

        Role r2 = new Role();
        r2.setId(2);
        r2.setName("ROLE_ADMIN");

        assertNotEquals(r1, r2);
    }

    @Test
    void testHashCode_equalObjects() {
        Role r1 = new Role();
        r1.setId(1);
        r1.setName("ROLE_USER");

        Role r2 = new Role();
        r2.setId(1);
        r2.setName("ROLE_USER");

        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testToString_notNull() {
        role.setId(1);
        role.setName("ROLE_ADMIN");
        assertNotNull(role.toString());
        assertTrue(role.toString().contains("ROLE_ADMIN"));
    }
}
