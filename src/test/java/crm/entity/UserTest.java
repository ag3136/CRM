package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

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
    void user_shouldCreateInstance() {
        assertNotNull(user);
    }

    @Test
    void builder_shouldCreateUserWithAllFields() {
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertEquals(1, user.getEnabled());
        assertNotNull(user.getRole());
    }

    @Test
    void getColumnCount_shouldReturnNumberOfFields() {
        // Act
        int columnCount = user.getColumnCount();
        
        // Assert
        assertTrue(columnCount > 0);
    }

    @Test
    void getRole_id_shouldReturnRoleId() {
        // Act
        int roleId = user.getRole_id();
        
        // Assert
        assertEquals(1, roleId);
    }

    @Test
    void getRole_name_shouldReturnRoleName() {
        // Act
        String roleName = user.getRole_name();
        
        // Assert
        assertEquals("ROLE_USER", roleName);
    }

    @Test
    void getName_shouldReturnFullName() {
        // Act
        String fullName = user.getName();
        
        // Assert
        assertEquals("John Doe", fullName);
    }

    @Test
    void setUsername_withValidUsername_shouldSetUsername() {
        // Arrange
        String newUsername = "newuser";
        
        // Act
        user.setUsername(newUsername);
        
        // Assert
        assertEquals(newUsername, user.getUsername());
    }

    @Test
    void setEmail_withValidEmail_shouldSetEmail() {
        // Arrange
        String newEmail = "newemail@example.com";
        
        // Act
        user.setEmail(newEmail);
        
        // Assert
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void setEnabled_withZero_shouldDisableUser() {
        // Act
        user.setEnabled(0);
        
        // Assert
        assertEquals(0, user.getEnabled());
    }

    @Test
    void user_shouldHaveEntityAnnotation() {
        assertTrue(User.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyUser() {
        // Act
        User emptyUser = new User();
        
        // Assert
        assertNotNull(emptyUser);
    }

    @Test
    void allArgsConstructor_shouldCreateUserWithAllFields() {
        // Act
        User newUser = new User(2L, "user2", "user2@test.com", 
            "Jane", "Smith", "pass456", 1, role);
        
        // Assert
        assertEquals(2L, newUser.getId());
        assertEquals("user2", newUser.getUsername());
        assertEquals("user2@test.com", newUser.getEmail());
    }

    @Test
    void getName_withNullFirstName_shouldHandleGracefully() {
        // Arrange
        user.setFirstName(null);
        user.setLastName("Doe");
        
        // Act
        String name = user.getName();
        
        // Assert
        assertNotNull(name);
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        // Arrange
        User user1 = User.builder()
                .id(1L)
                .username("test")
                .email("test@test.com")
                .build();
        
        User user2 = User.builder()
                .id(1L)
                .username("test")
                .email("test@test.com")
                .build();
        
        // Assert
        assertEquals(user1, user2);
    }
}
