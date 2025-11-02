package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class WeekTest {

    @Test
    public void constructor_validWeek_success() {
        assertDoesNotThrow(() -> new Week(Week.MIN_WEEK));
        assertDoesNotThrow(() -> new Week(Week.MAX_WEEK));
    }

    @Test
    public void constructor_invalidWeek_illegalStateException() {
        assertThrows(IllegalArgumentException.class, () -> new Week(Week.MAX_WEEK + 1));
        assertThrows(IllegalArgumentException.class, () -> new Week(Week.MIN_WEEK - 1));
    }

    @Test
    public void toString_default_success() {
        assertEquals("Week 0", new Week(Week.MIN_WEEK).toString());
        assertEquals("Week 13", new Week(Week.MAX_WEEK).toString());
    }

    @Test
    public void equals() {
        Week week0 = new Week(Week.MIN_WEEK);
        Week weekZero = new Week(Week.MIN_WEEK);
        Week week13 = new Week(Week.MAX_WEEK);

        // Same object
        assertEquals(week0, week0);

        // Null check
        assertNotEquals(week0, null);

        // Same week number
        assertEquals(week0, weekZero);

        // Different week number
        assertNotEquals(week0, week13);
        assertNotEquals(weekZero, week13);
    }
}
