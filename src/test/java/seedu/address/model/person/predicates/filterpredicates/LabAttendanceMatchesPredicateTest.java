package seedu.address.model.person.predicates.filterpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.ATTEND_MSG_EXACT;
import static seedu.address.testutil.TestConstants.ATTEND_MSG_GE;
import static seedu.address.testutil.TestConstants.ATTEND_MSG_GT;
import static seedu.address.testutil.TestConstants.ATTEND_MSG_LE;
import static seedu.address.testutil.TestConstants.ATTEND_MSG_LT;
import static seedu.address.testutil.TestConstants.ATTEND_SUCCESS_PREFIX;
import static seedu.address.testutil.TestConstants.ATTEND_SUCCESS_SUFFIX;
import static seedu.address.testutil.TestConstants.LAB_LIST_10_0Y;
import static seedu.address.testutil.TestConstants.LAB_LIST_10_5Y;
import static seedu.address.testutil.TestConstants.LAB_LIST_10_7Y;

import org.junit.jupiter.api.Test;

import seedu.address.logic.helpers.Comparison;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class LabAttendanceMatchesPredicateTest {

    // Helpers to build expected success messages
    private static String msg(String phrase, int value) {
        return ATTEND_SUCCESS_PREFIX + phrase + value + ATTEND_SUCCESS_SUFFIX;
    }

    @Test
    public void equals() {
        LabAttendanceMatchesPredicate p1 =
                new LabAttendanceMatchesPredicate(50, Comparison.EQUAL);
        LabAttendanceMatchesPredicate p1Copy =
                new LabAttendanceMatchesPredicate(50, Comparison.EQUAL);
        LabAttendanceMatchesPredicate diffValue =
                new LabAttendanceMatchesPredicate(60, Comparison.EQUAL);
        LabAttendanceMatchesPredicate diffOp =
                new LabAttendanceMatchesPredicate(50, Comparison.GREATER_THAN);

        // same object -> true
        assertTrue(p1.equals(p1));
        // same values -> true
        assertTrue(p1.equals(p1Copy));
        // null -> false
        assertFalse(p1.equals(null));
        // different value -> false
        assertFalse(p1.equals(diffValue));
        // different operator -> false
        assertFalse(p1.equals(diffOp));
    }

    @Test
    public void test_equal_returnsExpected() {
        // 5/10 = 50%, 7/10 = 70%, 0/10 = 0%
        Person fifty = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_5Y).build();
        Person seventy = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_7Y).build();

        LabAttendanceMatchesPredicate eq50 = new LabAttendanceMatchesPredicate(50, Comparison.EQUAL);

        assertTrue(eq50.test(fifty));
        assertFalse(eq50.test(seventy));
    }

    @Test
    public void test_ge_returnsExpected() {
        Person zero = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_0Y).build();
        Person fifty = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_5Y).build();
        Person seventy = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_7Y).build();

        LabAttendanceMatchesPredicate ge50 = new LabAttendanceMatchesPredicate(50, Comparison.GREATER_THAN_OR_EQUAL);

        assertTrue(ge50.test(fifty));
        assertTrue(ge50.test(seventy));
        assertFalse(ge50.test(zero));
    }

    @Test
    public void test_le_returnsExpected() {
        Person zero = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_0Y).build();
        Person fifty = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_5Y).build();
        Person seventy = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_7Y).build();

        LabAttendanceMatchesPredicate le50 = new LabAttendanceMatchesPredicate(50, Comparison.LESS_THAN_OR_EQUAL);

        assertTrue(le50.test(zero));
        assertTrue(le50.test(fifty));
        assertFalse(le50.test(seventy));
    }

    @Test
    public void test_gt_returnsExpected() {
        Person fifty = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_5Y).build();
        Person seventy = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_7Y).build();

        LabAttendanceMatchesPredicate gt50 = new LabAttendanceMatchesPredicate(50, Comparison.GREATER_THAN);

        assertTrue(gt50.test(seventy));
        assertFalse(gt50.test(fifty));
    }

    @Test
    public void test_lt_returnsExpected() {
        Person zero = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_0Y).build();
        Person fifty = new PersonBuilder().withLabAttendanceList(LAB_LIST_10_5Y).build();

        LabAttendanceMatchesPredicate lt50 = new LabAttendanceMatchesPredicate(50, Comparison.LESS_THAN);

        assertTrue(lt50.test(zero));
        assertFalse(lt50.test(fifty));
    }

    @Test
    public void successMessage_returnsExpected() {
        assertEquals(msg(ATTEND_MSG_EXACT, 60),
                new LabAttendanceMatchesPredicate(60, Comparison.EQUAL).successMessage());
        assertEquals(msg(ATTEND_MSG_GE, 40),
                new LabAttendanceMatchesPredicate(40, Comparison.GREATER_THAN_OR_EQUAL).successMessage());
        assertEquals(msg(ATTEND_MSG_LE, 75),
                new LabAttendanceMatchesPredicate(75, Comparison.LESS_THAN_OR_EQUAL).successMessage());
        assertEquals(msg(ATTEND_MSG_GT, 20),
                new LabAttendanceMatchesPredicate(20, Comparison.GREATER_THAN).successMessage());
        assertEquals(msg(ATTEND_MSG_LT, 90),
                new LabAttendanceMatchesPredicate(90, Comparison.LESS_THAN).successMessage());
    }

    @Test
    public void statusToMessage_mapsOperatorsCorrectly() {
        assertEquals(ATTEND_MSG_EXACT,
                new LabAttendanceMatchesPredicate(0, Comparison.EQUAL).statusToMessage());
        assertEquals(ATTEND_MSG_GE,
                new LabAttendanceMatchesPredicate(0, Comparison.GREATER_THAN_OR_EQUAL).statusToMessage());
        assertEquals(ATTEND_MSG_LE,
                new LabAttendanceMatchesPredicate(0, Comparison.LESS_THAN_OR_EQUAL).statusToMessage());
        assertEquals(ATTEND_MSG_GT,
                new LabAttendanceMatchesPredicate(0, Comparison.GREATER_THAN).statusToMessage());
        assertEquals(ATTEND_MSG_LT,
                new LabAttendanceMatchesPredicate(0, Comparison.LESS_THAN).statusToMessage());
    }

    @Test
    public void toStringMethod() {
        LabAttendanceMatchesPredicate p = new LabAttendanceMatchesPredicate(55, Comparison.GREATER_THAN);
        String s = p.toString();

        assertTrue(s.contains("value"));
        assertTrue(s.contains("55"));
        assertTrue(s.contains("comparison"));
        assertTrue(s.contains("GREATER_THAN"));
    }
}
