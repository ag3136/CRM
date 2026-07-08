package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        assertNotNull(category);
    }

    @Test
    void testSetAndGetId() {
        category.setId(1L);
        assertEquals(1L, category.getId());
    }

    @Test
    void testSetAndGetName() {
        category.setName("VIP");
        assertEquals("VIP", category.getName());
    }

    @Test
    void testSetId_null() {
        category.setId(null);
        assertNull(category.getId());
    }

    @Test
    void testSetName_null() {
        category.setName(null);
        assertNull(category.getName());
    }

    @Test
    void testSetName_emptyString() {
        category.setName("");
        assertEquals("", category.getName());
    }

    @Test
    void testEquals_sameObject() {
        category.setId(1L);
        category.setName("VIP");
        assertEquals(category, category);
    }

    @Test
    void testEquals_equalObjects() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("VIP");

        Category cat2 = new Category();
        cat2.setId(1L);
        cat2.setName("VIP");

        assertEquals(cat1, cat2);
    }

    @Test
    void testEquals_differentObjects() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("VIP");

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("Regular");

        assertNotEquals(cat1, cat2);
    }

    @Test
    void testHashCode_equalObjects() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("VIP");

        Category cat2 = new Category();
        cat2.setId(1L);
        cat2.setName("VIP");

        assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testToString_notNull() {
        category.setId(1L);
        category.setName("VIP");
        assertNotNull(category.toString());
        assertTrue(category.toString().contains("VIP"));
    }

    @Test
    void testSetId_largeValue() {
        category.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, category.getId());
    }
}
