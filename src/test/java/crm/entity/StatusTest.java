package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void status_shouldHaveProposedValue() {
        assertNotNull(Status.PROPOSED);
        assertEquals("PROPOSED", Status.PROPOSED.name());
    }

    @Test
    void status_shouldHaveNegotiatedValue() {
        assertNotNull(Status.NEGOTIATED);
        assertEquals("NEGOTIATED", Status.NEGOTIATED.name());
    }

    @Test
    void status_shouldHaveImplementedValue() {
        assertNotNull(Status.IMPLEMENTED);
        assertEquals("IMPLEMENTED", Status.IMPLEMENTED.name());
    }

    @Test
    void status_shouldHaveDoneValue() {
        assertNotNull(Status.DONE);
        assertEquals("DONE", Status.DONE.name());
    }

    @Test
    void all_shouldContainAllStatuses() {
        assertEquals(4, Status.ALL.length);
        assertArrayEquals(new Status[]{Status.PROPOSED, Status.NEGOTIATED, 
            Status.IMPLEMENTED, Status.DONE}, Status.ALL);
    }

    @Test
    void valueOf_withValidName_shouldReturnStatus() {
        assertEquals(Status.PROPOSED, Status.valueOf("PROPOSED"));
        assertEquals(Status.NEGOTIATED, Status.valueOf("NEGOTIATED"));
        assertEquals(Status.IMPLEMENTED, Status.valueOf("IMPLEMENTED"));
        assertEquals(Status.DONE, Status.valueOf("DONE"));
    }

    @Test
    void valueOf_withInvalidName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> 
            Status.valueOf("INVALID"));
    }

    @Test
    void values_shouldReturnAllStatuses() {
        Status[] values = Status.values();
        assertEquals(4, values.length);
        assertTrue(containsStatus(values, Status.PROPOSED));
        assertTrue(containsStatus(values, Status.NEGOTIATED));
        assertTrue(containsStatus(values, Status.IMPLEMENTED));
        assertTrue(containsStatus(values, Status.DONE));
    }

    @Test
    void status_shouldBeEnum() {
        assertTrue(Status.class.isEnum());
    }

    @Test
    void ordinal_shouldReturnCorrectOrder() {
        assertEquals(0, Status.PROPOSED.ordinal());
        assertEquals(1, Status.NEGOTIATED.ordinal());
        assertEquals(2, Status.IMPLEMENTED.ordinal());
        assertEquals(3, Status.DONE.ordinal());
    }

    private boolean containsStatus(Status[] statuses, Status status) {
        for (Status s : statuses) {
            if (s == status) {
                return true;
            }
        }
        return false;
    }
}
