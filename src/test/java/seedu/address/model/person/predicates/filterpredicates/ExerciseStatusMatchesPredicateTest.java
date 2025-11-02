package seedu.address.model.person.predicates.filterpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.EX_LIST_10;
import static seedu.address.testutil.TestConstants.MSG_HAVE_COMPLETED;
import static seedu.address.testutil.TestConstants.MSG_HAVE_NOT_MET_DEADLINE;
import static seedu.address.testutil.TestConstants.MSG_HAVE_NOT_YET_COMPLETED;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.person.Person;
import seedu.address.model.person.Status;
import seedu.address.testutil.PersonBuilder;

public class ExerciseStatusMatchesPredicateTest {


    @Test
    public void equals() {
        ExerciseStatusMatchesPredicate same =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        ExerciseStatusMatchesPredicate sameCopy =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        ExerciseStatusMatchesPredicate diffIndex =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(1), Status.DONE);
        ExerciseStatusMatchesPredicate diffStatus =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.NOT_DONE);

        assertTrue(same.equals(same)); // same object
        assertTrue(same.equals(sameCopy)); // same values
        assertFalse(same.equals(null)); // null
        assertFalse(same.equals(diffIndex)); // different index
        assertFalse(same.equals(diffStatus)); // different status
    }

    @Test
    public void test_matches_returnsTrue() {
        Person person = new PersonBuilder()
                .withExerciseList(EX_LIST_10)
                .build();

        assertTrue(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE).test(person)); // D
        assertTrue(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(1), Status.NOT_DONE).test(person)); // N
    }

    @Test
    public void test_doesNotMatch_returnsFalse() {
        Person person = new PersonBuilder().withExerciseList(EX_LIST_10).build();

        assertFalse(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.NOT_DONE).test(person));
        assertFalse(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(1), Status.DONE).test(person));
        assertFalse(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(2), Status.NOT_DONE).test(person));
        assertFalse(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(3), Status.DONE).test(person));
    }

    @Test
    public void successMessage_returnsExpected() {
        assertEquals(MSG_HAVE_COMPLETED + " exercise 0",
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE).successMessage());
        assertEquals(MSG_HAVE_NOT_YET_COMPLETED + " exercise 1",
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(1), Status.NOT_DONE).successMessage());
        assertEquals(MSG_HAVE_NOT_MET_DEADLINE + " exercise 2",
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(2), Status.OVERDUE).successMessage());
    }

    @Test
    public void statusToMessage_mapsStatusesCorrectly() {
        assertEquals(MSG_HAVE_COMPLETED,
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE).statusToMessage());
        assertEquals(MSG_HAVE_NOT_YET_COMPLETED,
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.NOT_DONE).statusToMessage());
        assertEquals(MSG_HAVE_NOT_MET_DEADLINE,
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.OVERDUE).statusToMessage());
    }

    @Test
    public void toStringMethod() {
        ExerciseStatusMatchesPredicate predicate =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(3), Status.DONE);
        String s = predicate.toString();

        assertTrue(s.contains("Status"));
        assertTrue(s.contains("D"));
        assertTrue(s.contains("index"));
        assertTrue(s.contains("3"));
    }
}
