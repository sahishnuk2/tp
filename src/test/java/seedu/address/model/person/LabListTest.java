package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LabListTest {
    private LabAttendance[] labs;

    @BeforeEach
    public void setUpLabs() {
        this.labs = createLabArray();
    }

    private LabAttendance[] createLabArray() {
        LabAttendance[] newLabs = new LabAttendance[LabList.NUMBER_OF_LABS];
        for (int i = 0; i < newLabs.length; i++) {
            newLabs[i] = new LabAttendanceStub();
        }
        return newLabs;
    }

    // Not Fully Unit Testing
    // Tests with Lab class, not the stub
    @Test
    public void constructorNoParam_default_success() {
        LabList labAttendanceList = new LabList();
        for (int i = 0; i < labs.length; i++) {
            assertFalse(labs[i].isAttended());
        }
    }

    @Test
    public void constructorWithParam_default_success() {
        LabList labAttendanceList = new LabList(labs);
        for (int i = 0; i < labs.length; i++) {
            assertFalse(labs[i].isAttended());
        }
    }

    @Test
    public void markLab_validIndex_success() {
        LabList labAttendanceList = new LabList(labs);

        assertFalse(labs[0].isAttended());
        labAttendanceList.markLabAsAttended(0);
        assertTrue(labs[0].isAttended());

        assertFalse(labs[5].isAttended());
        labAttendanceList.markLabAsAttended(5);
        assertTrue(labs[5].isAttended());
    }

    @Test
    public void markLab_invalidIndex_throwsIndexOutOfBoundsException() {
        LabList labAttendanceList = new LabList(labs);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            labAttendanceList.markLabAsAttended(-1);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            labAttendanceList.markLabAsAttended(10);
        });
    }

    @Test
    public void equals() {
        LabAttendance[] labs1 = createLabArray();
        LabAttendance[] labs2 = createLabArray();
        LabList labAttendanceList1 = new LabList(labs1);
        LabList labAttendanceList2 = new LabList(labs2);
        LabList labAttendanceList3 = labAttendanceList1;

        assertEquals(labAttendanceList1, labAttendanceList3);

        assertEquals(labAttendanceList1, labAttendanceList2);

        labAttendanceList1.markLabAsAttended(2);
        assertNotEquals(labAttendanceList1, labAttendanceList2);

        assertNotEquals(labAttendanceList1, null);
    }

    @Test
    public void toString_default_success() {
        LabList labAttendanceList = new LabList(labs);
        // Can transfer these expected Strings to another test util class in the future
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LabList.NUMBER_OF_LABS; i++) {
            sb.append("LN").append(" ");
        }
        assertEquals(sb.toString(), labAttendanceList.toString());

        labAttendanceList.markLabAsAttended(2);
        sb = new StringBuilder();
        for (int i = 0; i < LabList.NUMBER_OF_LABS; i++) {
            if (i == 2) {
                sb.append("LY").append(" ");
            } else {
                sb.append("LN").append(" ");
            }

        }
        assertEquals(sb.toString(), labAttendanceList.toString());
    }
}
