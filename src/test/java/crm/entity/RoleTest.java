package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void role_shouldCreateInstance() {
        assertNotNull(role);
    }

    @Test
    void setId_withValidId_shouldSetId() {
        // Arrange
        int expectedId = 1;
        
        // Act
        role.setId(expectedId);
        
        // Assert
        assertEquals(expectedId, role.getId());
    }

    @Test
    void setName_withValidName_shouldSetName() {
        // Arrange
        String expectedName = "ROLE_ADMIN";
        
        // Act
        role.setName(expectedName);
        
        // Assert
        assertEquals(expectedName, role.getName());
    }

    @Test
    void setName_withNullName_shouldSetNull() {
        // Act
        role.setName(null);
        
        // Assert
        assertNull(role.getName());
    }

    @Test
    void role_shouldHaveEntityAnnotation() {
        assertTrue(Role.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void role_shouldHaveTableAnnotation() {
        assertTrue(Role.class.isAnnotationPresent(jakarta.persistence.Table.class));
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");
        
        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_USER");
        
        // Assert
        assertEquals(role1, role2);
    }

    @Test
    void hashCode_withSameValues_shouldReturnSameHashCode() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");
        
        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_USER");
        
        // Assert
        assertEquals(role1.hashCode(), role2.hashCode());
    }
}
