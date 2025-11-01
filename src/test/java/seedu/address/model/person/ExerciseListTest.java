package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LAB;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LAB;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;

public class ExerciseListTest {

    private ExerciseList tracker;

    @BeforeEach
    public void setUp() {
        tracker = new ExerciseList();
        ExerciseList.setCurrentWeek(0);
    }

    // -------------------------------------------------------------------------
    // Validation Tests
    // -------------------------------------------------------------------------

    @Test
    public void isValidExerciseList_validFormat_returnsTrue() {
        String valid = "ex 0: N ex 1: D ex 2: O ex 3: I ex 4: N ex 5: N ex 6: D ex 7: O ex 8: N ex 9: I";
        assertTrue(ExerciseList.isValidExerciseList(valid));
    }

    @Test
    public void isValidExerciseList_invalidFormat_returnsFalse() {
        String invalid = "ex 0: X ex 1: D ex 2: O"; // X is invalid status
        assertFalse(ExerciseList.isValidExerciseList(invalid));
    }

    // -------------------------------------------------------------------------
    // Constructor Tests
    // -------------------------------------------------------------------------

    @Test
    public void constructor_default_initializesAllToNotDone() {
        var statuses = tracker.getStatuses();
        assertEquals(10, statuses.size());
        assertTrue(statuses.stream().allMatch(s -> s == Status.NOT_DONE));
    }

    @Test
    public void constructor_withList_trimsOrPadsCorrectly() {
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(true);
        list.add(false);
        ExerciseList tracker2 = new ExerciseList(list);

        assertEquals(10, tracker2.getStatuses().size());
        assertEquals(Status.DONE, tracker2.getStatuses().get(0));
        assertEquals(Status.NOT_DONE, tracker2.getStatuses().get(1));
    }

    // -------------------------------------------------------------------------
    // markExercise Tests
    // -------------------------------------------------------------------------

    @Test
    public void markExercise_validIndex_updatesStatus() {
        Index index = Index.fromZeroBased(3);
        tracker.markExercise(index, true);
        assertEquals(Status.DONE, tracker.getStatuses().get(3));
    }

    // -------------------------------------------------------------------------
    // equals & hashCode Tests
    // -------------------------------------------------------------------------

    @Test
    public void equals_sameObject_returnsTrue() {
        assertTrue(tracker.equals(tracker));
    }

    @Test
    public void equals_sameStatuses_returnsTrue() {
        ExerciseList other = new ExerciseList(new ArrayList<>(tracker.getIsDoneList()));
        assertTrue(tracker.equals(other));
        assertEquals(tracker.hashCode(), other.hashCode());
    }

    @Test
    public void equals_differentStatuses_returnsFalse() {
        ExerciseList other = tracker.copy();
        other.markExercise(Index.fromZeroBased(1), true);
        assertFalse(tracker.equals(other));
    }

    // -------------------------------------------------------------------------
    // toString Tests
    // -------------------------------------------------------------------------

    @Test
    public void toString_containsExpectedFormat() {
        String result = tracker.toString();
        assertTrue(result.contains("ex 0:"));
        assertTrue(result.contains("ex 9:"));
    }

    // -------------------------------------------------------------------------
    // calculateProgress Tests
    // -------------------------------------------------------------------------

    @Test
    public void calculateProgress_worksCorrectly() {
        ExerciseList exerciseList = new ExerciseList();

        // Initially 0% done
        assertEquals(0.0, exerciseList.calculateProgress());

        // Mark first exercise done
        exerciseList.markExercise(INDEX_FIRST_LAB, true);
        assertEquals(1.0 / ExerciseList.NUMBER_OF_EXERCISES * 100, exerciseList.calculateProgress());

        // Mark second exercise done
        exerciseList.markExercise(INDEX_SECOND_LAB, true);
        assertEquals(2.0 / ExerciseList.NUMBER_OF_EXERCISES * 100, exerciseList.calculateProgress());
    }

    // -------------------------------------------------------------------------
    // copy Tests
    // -------------------------------------------------------------------------

    @Test
    public void copy_createsIndependentClone() {
        ExerciseList copy = tracker.copy();
        assertTrue(tracker.equals(copy));

        // Modify copy, original should remain unchanged
        copy.markExercise(Index.fromZeroBased(0), true);
        assertFalse(tracker.equals(copy));
    }

    // -------------------------------------------------------------------------
    // compareTo Tests
    // -------------------------------------------------------------------------

    @Test
    public void compareTo_returnsCorrectOrder() {
        ExerciseList t1 = new ExerciseList();
        ExerciseList t2 = new ExerciseList();

        t2.markExercise(Index.fromZeroBased(0), true);
        assertTrue(t2.compareTo(t1) > 0);
        assertTrue(t1.compareTo(t2) < 0);
    }
}
