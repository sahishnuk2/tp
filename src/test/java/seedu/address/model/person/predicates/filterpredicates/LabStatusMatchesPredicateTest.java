package seedu.address.model.person.predicates.filterpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.LAB_A;
import static seedu.address.testutil.TestConstants.LAB_LIST_10;
import static seedu.address.testutil.TestConstants.LAB_MSG_ABSENT;
import static seedu.address.testutil.TestConstants.LAB_MSG_ATTENDED;
import static seedu.address.testutil.TestConstants.LAB_MSG_NOT_YET_ATTENDED;
import static seedu.address.testutil.TestConstants.LAB_N;
import static seedu.address.testutil.TestConstants.LAB_Y;
import static seedu.address.testutil.TestConstants.MSG_INVALID_FILTER;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class LabStatusMatchesPredicateTest {

    @Test
    public void equals() {
        LabStatusMatchesPredicate same =
                new LabStatusMatchesPredicate(Index.fromZeroBased(0), LAB_Y);
        LabStatusMatchesPredicate sameCopy =
                new LabStatusMatchesPredicate(Index.fromZeroBased(0), LAB_Y);
        LabStatusMatchesPredicate diffIndex =
                new LabStatusMatchesPredicate(Index.fromZeroBased(1), LAB_Y);
        LabStatusMatchesPredicate diffStatus =
                new LabStatusMatchesPredicate(Index.fromZeroBased(0), LAB_N);

        assertTrue(same.equals(same)); // same object
        assertTrue(same.equals(sameCopy)); // same values
        assertFalse(same.equals(null)); // null
        assertFalse(same.equals(diffIndex)); // different index
        assertFalse(same.equals(diffStatus)); // different status
    }

    @Test
    public void test_matches_returnsTrue() {
        Person person = new PersonBuilder()
                .withLabAttendanceList(LAB_LIST_10)
                .build();

        assertTrue(new LabStatusMatchesPredicate(Index.fromZeroBased(0), LAB_Y).test(person)); // L1: Y
        assertTrue(new LabStatusMatchesPredicate(Index.fromZeroBased(2), LAB_N).test(person)); // L3: N
        assertTrue(new LabStatusMatchesPredicate(Index.fromZeroBased(1), LAB_N).test(person)); // L2: N
    }

    @Test
    public void test_doesNotMatch_returnsFalse() {
        Person person = new PersonBuilder().withLabAttendanceList(LAB_LIST_10).build();

        assertFalse(new LabStatusMatchesPredicate(Index.fromZeroBased(0), LAB_N).test(person)); // L1 is Y
        assertFalse(new LabStatusMatchesPredicate(Index.fromZeroBased(1), LAB_Y).test(person)); // L2 is N
        assertFalse(new LabStatusMatchesPredicate(Index.fromZeroBased(2), LAB_Y).test(person)); // L3 is N
        assertFalse(new LabStatusMatchesPredicate(Index.fromZeroBased(3), LAB_Y).test(person)); // L4 is N
    }

    @Test
    public void successMessage_returnsExpected() {
        // successMessage uses one-based index in the printed text
        assertEquals(LAB_MSG_ATTENDED + " lab 1",
                new LabStatusMatchesPredicate(Index.fromZeroBased(0), LAB_Y).successMessage());
        assertEquals(LAB_MSG_NOT_YET_ATTENDED + " lab 2",
                new LabStatusMatchesPredicate(Index.fromZeroBased(1), LAB_N).successMessage());
        assertEquals(LAB_MSG_ABSENT + " lab 3",
                new LabStatusMatchesPredicate(Index.fromZeroBased(2), LAB_A).successMessage());
    }

    @Test
    public void statusToMessage_mapsStatusesCorrectly() {
        assertEquals(LAB_MSG_ATTENDED,
                new LabStatusMatchesPredicate(Index.fromZeroBased(0), LAB_Y).statusToMessage());
        assertEquals(LAB_MSG_NOT_YET_ATTENDED,
                new LabStatusMatchesPredicate(Index.fromZeroBased(0), LAB_N).statusToMessage());
        assertEquals(LAB_MSG_ABSENT,
                new LabStatusMatchesPredicate(Index.fromZeroBased(0), LAB_A).statusToMessage());
    }

    @Test
    public void statusToMessage_invalidStatus_returnsInvalidFilter() {
        assertEquals(MSG_INVALID_FILTER,
                new LabStatusMatchesPredicate(Index.fromZeroBased(0), "Z").statusToMessage());
    }

    @Test
    public void toStringMethod() {
        LabStatusMatchesPredicate predicate =
                new LabStatusMatchesPredicate(Index.fromZeroBased(4), LAB_Y);
        String s = predicate.toString();

        assertTrue(s.contains("Status"));
        assertTrue(s.contains(LAB_Y));
        assertTrue(s.contains("index"));
        assertTrue(s.contains("4"));
    }
}
