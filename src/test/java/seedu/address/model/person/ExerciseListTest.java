package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;

class ExerciseListTest {

    @BeforeEach
    void setUp() {
        // Set a known week number for consistent behavior
        ExerciseList.setCurrentWeek(10);
    }

    @AfterEach
    void tearDown() {
        // Reset static state to prevent interference with other tests
        ExerciseList.setCurrentWeek(0);
    }

    @Test
    void constructor_defaultInitializesTenExercises() {
        ExerciseList list = new ExerciseList();
        ArrayList<Boolean> doneList = list.getIsDoneList();

        assertEquals(ExerciseList.NUMBER_OF_EXERCISES, doneList.size());
        assertTrue(doneList.stream().noneMatch(Boolean::booleanValue), "All exercises should start as not done");
    }

    @Test
    void constructor_withList_fillsAndPadsCorrectly() {
        ArrayList<Boolean> input = new ArrayList<>(List.of(true, false, true));
        ExerciseList list = new ExerciseList(input);
        ArrayList<Boolean> doneList = list.getIsDoneList();

        assertEquals(ExerciseList.NUMBER_OF_EXERCISES, doneList.size());
        assertTrue(doneList.get(0));
        assertFalse(doneList.get(1));
        assertTrue(doneList.get(2));
        for (int i = 3; i < doneList.size(); i++) {
            assertFalse(doneList.get(i));
        }
    }

    @Test
    void constructor_tooManyStatuses_throwsException() {
        ArrayList<Boolean> input = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            input.add(true);
        }
        assertThrows(IllegalArgumentException.class, () -> new ExerciseList(input));
    }

    @Test
    void markExercise_updatesStatusCorrectly() {
        ExerciseList list = new ExerciseList();
        Index idx = Index.fromZeroBased(3);
        list.markExercise(idx, true);
        assertTrue(list.getIsDoneList().get(3));
    }

    @Test
    void calculateProgress_computesCorrectPercentage() {
        ArrayList<Boolean> input =
                new ArrayList<>(List.of(true, true, false, false, false, false, false, false, false, false));
        ExerciseList list = new ExerciseList(input);
        assertEquals(20.0, list.calculateProgress(), 0.001);
    }

    @Test
    void compareTo_returnsPositiveWhenMoreProgress() {
        ArrayList<Boolean> input1 =
                new ArrayList<>(List.of(true, true, false, false, false, false, false, false, false, false));
        ArrayList<Boolean> input2 =
                new ArrayList<>(List.of(false, false, false, false, false, false, false, false, false, false));
        ExerciseList a = new ExerciseList(input1);
        ExerciseList b = new ExerciseList(input2);
        assertTrue(a.compareTo(b) > 0);
    }

    @Test
    void equals_sameReference_returnsTrue() {
        ExerciseList list = new ExerciseList();
        assertTrue(list.equals(list));
    }

    @Test
    void equals_null_returnsFalse() {
        ExerciseList list = new ExerciseList();
        assertFalse(list.equals(null));
    }

    @Test
    void equals_differentType_returnsFalse() {
        ExerciseList list = new ExerciseList();
        String notList = "exercise";
        assertFalse(list.equals(notList));
    }

    @Test
    void equals_sameContents_returnsTrue() {
        ExerciseList l1 = new ExerciseList();
        ExerciseList l2 = new ExerciseList();
        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
    }

    @Test
    void equals_differentContents_returnsFalse() {
        ExerciseList l1 = new ExerciseList();
        ExerciseList l2 = new ExerciseList();
        l1.markExercise(Index.fromZeroBased(1), true);
        assertNotEquals(l1, l2);
    }

    @Test
    void copy_createsDeepCopy() {
        ExerciseList original = new ExerciseList();
        original.markExercise(Index.fromZeroBased(2), true);

        ExerciseList copy = original.copy();

        assertNotSame(original, copy);
        assertEquals(original, copy);

        // Mutate copy
        copy.markExercise(Index.fromZeroBased(4), true);
        assertNotEquals(original, copy, "Changing copy should not affect original");
    }

    @Test
    void getTrackerColours_returnsMatchingColours() {
        ArrayList<Boolean> input =
                new ArrayList<>(List.of(true, false, false, false, false, false, false, false, false, false));
        ExerciseList list = new ExerciseList(input);

        List<TrackerColour> colours = list.getTrackerColours();

        assertEquals(ExerciseList.NUMBER_OF_EXERCISES, colours.size());
        assertEquals(TrackerColour.GREEN, colours.get(0)); // done
        assertTrue(colours.stream().anyMatch(c -> c == TrackerColour.GREY || c == TrackerColour.RED));
    }

    @Test
    void getLabels_returnsCorrectlyFormattedLabels() {
        ExerciseList list = new ExerciseList();
        List<String> labels = list.getLabels();
        assertEquals(ExerciseList.NUMBER_OF_EXERCISES, labels.size());
        assertEquals("EX0", labels.get(0));
        assertEquals("EX9", labels.get(9));
    }

    // ---------------- isValidExerciseList tests ----------------

    @Test
    void isValidExerciseList_null_returnsFalse() {
        assertFalse(ExerciseList.isValidExerciseList(null));
    }

    @Test
    void isValidExerciseList_invalidLength_returnsFalse() {
        String shortList = "ex 0: N ex 1: N";
        assertFalse(ExerciseList.isValidExerciseList(shortList));
    }

    @Test
    void isValidExerciseList_wrongKeyword_returnsFalse() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ExerciseList.NUMBER_OF_EXERCISES; i++) {
            sb.append("wrong ").append(i).append(": N ");
        }
        assertFalse(ExerciseList.isValidExerciseList(sb.toString().trim()));
    }

    @Test
    void isValidExerciseList_wrongIndexFormat_returnsFalse() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ExerciseList.NUMBER_OF_EXERCISES; i++) {
            sb.append("ex ").append(i).append(" N "); // missing colon
        }
        assertFalse(ExerciseList.isValidExerciseList(sb.toString().trim()));
    }

    @Test
    void isValidExerciseList_invalidStatus_returnsFalse() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ExerciseList.NUMBER_OF_EXERCISES; i++) {
            sb.append("ex ").append(i).append(": X ");
        }
        assertFalse(ExerciseList.isValidExerciseList(sb.toString().trim()));
    }

    @Test
    void isValidExerciseList_validFormat_returnsTrue() {
        String[] statuses = {"N", "D", "O"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ExerciseList.NUMBER_OF_EXERCISES; i++) {
            sb.append("ex ").append(i).append(": ").append(statuses[i % 3]).append(" ");
        }
        assertTrue(ExerciseList.isValidExerciseList(sb.toString().trim()));
    }

    @Test
    void toString_containsAllExercises() {
        ExerciseList list = new ExerciseList();
        String output = list.toString();
        for (int i = 0; i < ExerciseList.NUMBER_OF_EXERCISES; i++) {
            assertTrue(output.contains("ex " + i + ":"));
        }
    }
}
