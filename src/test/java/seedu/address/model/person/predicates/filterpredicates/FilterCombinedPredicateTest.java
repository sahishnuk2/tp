package seedu.address.model.person.predicates.filterpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.EX_LIST_10_ALL_N;
import static seedu.address.testutil.TestConstants.EX_LIST_10_D0_OTHERS_N;
import static seedu.address.testutil.TestConstants.LAB_LIST_10_Y1_OTHERS_N;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.helpers.Comparison;
import seedu.address.model.person.Person;
import seedu.address.model.person.Status;
import seedu.address.testutil.PersonBuilder;

public class FilterCombinedPredicateTest {

    @Test
    public void equals() {
        FilterPredicate p1 = new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterPredicate p2 = new LabStatusMatchesPredicate(Index.fromZeroBased(0), "Y");
        FilterPredicate p3 = new LabAttendanceMatchesPredicate(50, Comparison.GREATER_THAN);

        FilterCombinedPredicate combo12 = new FilterCombinedPredicate(List.of(p1, p2));
        FilterCombinedPredicate combo12Copy = new FilterCombinedPredicate(List.of(
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE),
                new LabStatusMatchesPredicate(Index.fromZeroBased(0), "Y")
        ));
        FilterCombinedPredicate combo21 = new FilterCombinedPredicate(List.of(p2, p1));
        FilterCombinedPredicate combo123 = new FilterCombinedPredicate(List.of(p1, p2, p3));

        // same object -> true
        assertTrue(combo12.equals(combo12));
        // same values, same order -> true
        assertTrue(combo12.equals(combo12Copy));
        // different order -> false (order matters per equals implementation)
        assertFalse(combo12.equals(combo21));
        // null -> false
        assertFalse(combo12.equals(null));
        // different type -> false
        assertFalse(combo12.equals("not a predicate"));
        // different size -> false
        assertFalse(combo12.equals(combo123));
    }

    @Test
    public void test_allMatch_returnsTrue() {
        // exercise 0 = D, lab 1 = Y
        Person person = new PersonBuilder()
                .withExerciseList(EX_LIST_10_D0_OTHERS_N)
                .withLabAttendanceList(LAB_LIST_10_Y1_OTHERS_N)
                .build();

        FilterPredicate ex0Done = new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterPredicate lab1Yes = new LabStatusMatchesPredicate(Index.fromZeroBased(0), "Y");

        FilterCombinedPredicate combined = new FilterCombinedPredicate(List.of(ex0Done, lab1Yes));
        assertTrue(combined.test(person));
    }

    @Test
    public void test_oneFails_returnsFalse() {
        // Case A: exercise matches (D at 0), lab fails (L1 is N)
        Person onlyExerciseMatches = new PersonBuilder()
                .withExerciseList(EX_LIST_10_D0_OTHERS_N)
                .withLabAttendanceList("L1: N L2: N L3: N L4: N L5: N L6: N L7: N L8: N L9: N L10: N")
                .build();

        // Case B: lab matches (L1 is Y), exercise fails (ex0 is N)
        Person onlyLabMatches = new PersonBuilder()
                .withExerciseList(EX_LIST_10_ALL_N)
                .withLabAttendanceList(LAB_LIST_10_Y1_OTHERS_N)
                .build();

        FilterPredicate ex0Done = new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterPredicate lab1Yes = new LabStatusMatchesPredicate(Index.fromZeroBased(0), "Y");
        FilterCombinedPredicate combined = new FilterCombinedPredicate(List.of(ex0Done, lab1Yes));

        assertFalse(combined.test(onlyExerciseMatches));
        assertFalse(combined.test(onlyLabMatches));
    }

    @Test
    public void successMessage_concatenatesChildMessages_inOrder() {
        // Order matters, exercise message then lab message
        FilterPredicate ex0Done = new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterPredicate lab1Yes = new LabStatusMatchesPredicate(Index.fromZeroBased(0), "Y");

        FilterCombinedPredicate combined = new FilterCombinedPredicate(List.of(ex0Done, lab1Yes));
        // Exercise message uses zero-based index in output
        // Lab message uses one-based index in output
        String expected = "have completed exercise 0 and have attended lab 1";
        assertEquals(expected, combined.successMessage());
    }
}
