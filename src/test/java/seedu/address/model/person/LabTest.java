package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LabTest {
    public static final int LAB_NUMBER_ONE = 1;
    public static final int LAB_NUMBER_TWO = 2;
    public static final int LAB_NUMBER_THREE = 3;
    public static final int CURRENT_WEEK_FIVE = 5;

    @Test
    public void constructor_default_isAttendedFalse() {
        Lab labAttendance1 = new Lab(LAB_NUMBER_ONE, CURRENT_WEEK_FIVE);
        assertFalse(labAttendance1.isAttended());
        assertEquals("A", labAttendance1.getStatus());

        Lab labAttendance2 = new Lab(LAB_NUMBER_THREE, CURRENT_WEEK_FIVE);
        assertFalse(labAttendance2.isAttended());
        assertEquals("N", labAttendance2.getStatus());

    }

    @Test
    public void getLabNumber_default_success() {
        Lab labAttendance1 = new Lab(LAB_NUMBER_ONE, CURRENT_WEEK_FIVE);
        assertEquals(1, labAttendance1.getLabNumber());

        Lab labAttendance2 = new Lab(LAB_NUMBER_THREE, CURRENT_WEEK_FIVE);
        assertEquals(3, labAttendance2.getLabNumber());
    }

    @Test
    public void markAsAttended_notAttendedLab_isAttendedTrue() {
        Lab labAttendance = new Lab(LAB_NUMBER_ONE, CURRENT_WEEK_FIVE);

        labAttendance.markAsAttended();
        assertTrue(labAttendance.isAttended());
        assertEquals("Y", labAttendance.getStatus());
    }

    @Test
    public void markAsAttended_attendedLab_throwIllegalStateException() {
        Lab labAttendance = new Lab(LAB_NUMBER_ONE, CURRENT_WEEK_FIVE);

        labAttendance.markAsAttended();
        assertTrue(labAttendance.isAttended());
        assertEquals("Y", labAttendance.getStatus());

        assertThrows(IllegalStateException.class, labAttendance::markAsAttended);
    }

    @Test
    public void markAsAbsent_attendedLab_isAttendedFalse() {
        Lab labAttendance1 = new Lab(LAB_NUMBER_ONE, CURRENT_WEEK_FIVE);
        labAttendance1.markAsAttended();
        assertTrue(labAttendance1.isAttended());

        labAttendance1.markAsAbsent();
        assertFalse(labAttendance1.isAttended());
        assertEquals("A", labAttendance1.getStatus());

        Lab labAttendance2 = new Lab(LAB_NUMBER_THREE, CURRENT_WEEK_FIVE);
        labAttendance2.markAsAttended();
        assertTrue(labAttendance2.isAttended());

        labAttendance2.markAsAbsent();
        assertFalse(labAttendance2.isAttended());
        assertEquals("N", labAttendance2.getStatus());
    }

    @Test
    public void markAsAbsent_notAttendedLab_throwIllegalStateException() {
        Lab labAttendance = new Lab(LAB_NUMBER_ONE, CURRENT_WEEK_FIVE);
        assertFalse(labAttendance.isAttended());
        assertEquals("A", labAttendance.getStatus());

        assertThrows(IllegalStateException.class, labAttendance::markAsAbsent);
    }

    @Test
    public void equals() {
        Lab labAttendance1 = new Lab(LAB_NUMBER_ONE, CURRENT_WEEK_FIVE);
        Lab labAttendance2 = new Lab(LAB_NUMBER_ONE, CURRENT_WEEK_FIVE);
        Lab labAttendance3 = new Lab(LAB_NUMBER_TWO, CURRENT_WEEK_FIVE);

        // Same object
        assertEquals(labAttendance1, labAttendance1);

        // Object with same lab data fields
        assertEquals(labAttendance1, labAttendance2);

        // Object with different lab number field
        assertNotEquals(labAttendance1, labAttendance3);

        // Object with same lab isAttended field
        labAttendance1.markAsAttended();
        assertNotEquals(labAttendance1, labAttendance2);

        // Object with same lab data fields
        labAttendance2.markAsAttended();
        assertEquals(labAttendance1, labAttendance2);

        assertNotEquals(labAttendance1, null);
    }

    @Test
    public void toString_default_success() {
        Lab lab = new Lab(1, 1);
        assertEquals("L1: N", lab.toString());

        lab.markAsAttended();
        assertEquals("L1: Y", lab.toString());
    }
}
