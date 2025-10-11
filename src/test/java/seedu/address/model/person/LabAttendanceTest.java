package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LabAttendanceTest {

    @Test
    public void constructor_default_isAttendedFalse() {
        Lab labAttendance = new Lab();
        assertFalse(labAttendance.isAttended());
    }

    @Test
    public void markAsAttended_unmarkedLab_isAttendedTrue() {
        Lab labAttendance = new Lab();

        labAttendance.markAsAttended();
        assertTrue(labAttendance.isAttended());
    }

    @Test
    public void markAsAttended_markedLab_throwIllegalStateException() {
        Lab labAttendance = new Lab();

        labAttendance.markAsAttended();
        assertTrue(labAttendance.isAttended());

        assertThrows(IllegalStateException.class, labAttendance::markAsAttended);
    }

    @Test
    public void equals() {
        Lab labAttendance1 = new Lab();
        Lab labAttendance2 = new Lab();

        assertEquals(labAttendance1, labAttendance2);

        labAttendance1.markAsAttended();
        assertNotEquals(labAttendance1, labAttendance2);

        labAttendance2.markAsAttended();
        assertEquals(labAttendance1, labAttendance2);
    }
}
