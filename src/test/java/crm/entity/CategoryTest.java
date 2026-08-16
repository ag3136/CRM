package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void category_shouldCreateInstance() {
        assertNotNull(category);
    }

    @Test
    void setId_withValidId_shouldSetId() {
        // Arrange
        Long expectedId = 1L;
        
        // Act
        category.setId(expectedId);
        
        // Assert
        assertEquals(expectedId, category.getId());
    }

    @Test
    void setName_withValidName_shouldSetName() {
        // Arrange
        String expectedName = "VIP";
        
        // Act
        category.setName(expectedName);
        
        // Assert
        assertEquals(expectedName, category.getName());
    }

    @Test
    void setName_withNullName_shouldSetNull() {
        // Act
        category.setName(null);
        
        // Assert
        assertNull(category.getName());
    }

    @Test
    void category_shouldHaveEntityAnnotation() {
        assertTrue(Category.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void category_shouldHaveTableAnnotation() {
        assertTrue(Category.class.isAnnotationPresent(jakarta.persistence.Table.class));
    }

    @Test
    void equals_withSameValues_shouldReturnTrue() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Premium");
        
        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Premium");
        
        // Assert
        assertEquals(category1, category2);
    }

    @Test
    void hashCode_withSameValues_shouldReturnSameHashCode() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Premium");
        
        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Premium");
        
        // Assert
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    void toString_shouldContainFieldValues() {
        // Arrange
        category.setId(1L);
        category.setName("Standard");
        
        // Act
        String result = category.toString();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.contains("1") || result.contains("Standard"));
    }
}
