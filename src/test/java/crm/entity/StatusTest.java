package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testProposedValue() {
        assertEquals(Status.PROPOSED, Status.valueOf("PROPOSED"));
    }

    @Test
    void testNegotiatedValue() {
        assertEquals(Status.NEGOTIATED, Status.valueOf("NEGOTIATED"));
    }

    @Test
    void testImplementedValue() {
        assertEquals(Status.IMPLEMENTED, Status.valueOf("IMPLEMENTED"));
    }

    @Test
    void testDoneValue() {
        assertEquals(Status.DONE, Status.valueOf("DONE"));
    }

    @Test
    void testAllArray_length() {
        assertEquals(4, Status.ALL.length);
    }

    @Test
    void testAllArray_containsProposed() {
        boolean found = false;
        for (Status s : Status.ALL) {
            if (s == Status.PROPOSED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testAllArray_containsNegotiated() {
        boolean found = false;
        for (Status s : Status.ALL) {
            if (s == Status.NEGOTIATED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testAllArray_containsImplemented() {
        boolean found = false;
        for (Status s : Status.ALL) {
            if (s == Status.IMPLEMENTED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testAllArray_containsDone() {
        boolean found = false;
        for (Status s : Status.ALL) {
            if (s == Status.DONE) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testValues_returnsAllStatuses() {
        Status[] values = Status.values();
        assertEquals(4, values.length);
    }

    @Test
    void testOrdinal_proposed() {
        assertEquals(0, Status.PROPOSED.ordinal());
    }

    @Test
    void testOrdinal_negotiated() {
        assertEquals(1, Status.NEGOTIATED.ordinal());
    }

    @Test
    void testOrdinal_implemented() {
        assertEquals(2, Status.IMPLEMENTED.ordinal());
    }

    @Test
    void testOrdinal_done() {
        assertEquals(3, Status.DONE.ordinal());
    }

    @Test
    void testName_proposed() {
        assertEquals("PROPOSED", Status.PROPOSED.name());
    }

    @Test
    void testName_done() {
        assertEquals("DONE", Status.DONE.name());
    }

    @Test
    void testValueOf_invalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf("INVALID"));
    }

    @Test
    void testToString_proposed() {
        assertEquals("PROPOSED", Status.PROPOSED.toString());
    }
}
