package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testStatusValues_allFourExist() {
        Status[] values = Status.values();
        assertEquals(4, values.length);
    }

    @Test
    void testStatusProposed_exists() {
        assertNotNull(Status.PROPOSED);
        assertEquals("PROPOSED", Status.PROPOSED.name());
    }

    @Test
    void testStatusNegotiated_exists() {
        assertNotNull(Status.NEGOTIATED);
        assertEquals("NEGOTIATED", Status.NEGOTIATED.name());
    }

    @Test
    void testStatusImplemented_exists() {
        assertNotNull(Status.IMPLEMENTED);
        assertEquals("IMPLEMENTED", Status.IMPLEMENTED.name());
    }

    @Test
    void testStatusDone_exists() {
        assertNotNull(Status.DONE);
        assertEquals("DONE", Status.DONE.name());
    }

    @Test
    void testStatusAll_containsAllValues() {
        Status[] all = Status.ALL;
        assertEquals(4, all.length);
        assertEquals(Status.PROPOSED, all[0]);
        assertEquals(Status.NEGOTIATED, all[1]);
        assertEquals(Status.IMPLEMENTED, all[2]);
        assertEquals(Status.DONE, all[3]);
    }

    @Test
    void testValueOf_proposed_returnsCorrectEnum() {
        Status status = Status.valueOf("PROPOSED");
        assertEquals(Status.PROPOSED, status);
    }

    @Test
    void testValueOf_negotiated_returnsCorrectEnum() {
        Status status = Status.valueOf("NEGOTIATED");
        assertEquals(Status.NEGOTIATED, status);
    }

    @Test
    void testValueOf_implemented_returnsCorrectEnum() {
        Status status = Status.valueOf("IMPLEMENTED");
        assertEquals(Status.IMPLEMENTED, status);
    }

    @Test
    void testValueOf_done_returnsCorrectEnum() {
        Status status = Status.valueOf("DONE");
        assertEquals(Status.DONE, status);
    }

    @Test
    void testValueOf_invalidValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf("INVALID"));
    }

    @Test
    void testOrdinal_proposed_isZero() {
        assertEquals(0, Status.PROPOSED.ordinal());
    }

    @Test
    void testOrdinal_done_isThree() {
        assertEquals(3, Status.DONE.ordinal());
    }
}
