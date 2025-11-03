package seedu.address.model.person;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ExerciseTest {

    @Test
    void constructor_setsFieldsCorrectlyAndPastWeekCalculatedProperly() {
        int currentWeek = 10;

        // Exercise 7 is more than 2 weeks behind (10 - 7 > 2) → past week
        Exercise oldExercise = new Exercise(7, false, currentWeek);
        assertEquals(7, oldExercise.getExerciseNumber());
        assertFalse(oldExercise.isDone());
        assertEquals(Status.OVERDUE, oldExercise.getStatus());

        // Exercise 9 is within 2 weeks → not past week
        Exercise recentExercise = new Exercise(9, false, currentWeek);
        assertEquals(Status.NOT_DONE, recentExercise.getStatus());

        // Exercise 10 is current → done should override past week
        Exercise doneExercise = new Exercise(10, true, currentWeek);
        assertEquals(Status.DONE, doneExercise.getStatus());
    }

    @Test
    void markStatus_changesStatusWhenDifferent() {
        Exercise ex = new Exercise(3, false, 5);
        ex.markStatus(true);
        assertTrue(ex.isDone());
        assertEquals(Status.DONE, ex.getStatus());
    }

    @Test
    void markStatus_sameStatus_throwsIllegalStateException() {
        Exercise ex = new Exercise(2, false, 5);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> ex.markStatus(false)
        );
        assertTrue(exception.getMessage().contains("already been marked"));
    }

    @Test
    void getStatus_returnsCorrectEnumValues() {
        // done = true → DONE
        Exercise done = new Exercise(5, true, 8);
        assertEquals(Status.DONE, done.getStatus());

        // not done but past → OVERDUE
        Exercise overdue = new Exercise(1, false, 10);
        assertEquals(Status.OVERDUE, overdue.getStatus());

        // not done and current → NOT_DONE
        Exercise notDone = new Exercise(9, false, 10);
        assertEquals(Status.NOT_DONE, notDone.getStatus());
    }

    @Test
    void toString_displaysCorrectFormat() {
        Exercise ex = new Exercise(5, false, 10);
        assertTrue(ex.toString().startsWith("ex 5: "));
        assertTrue(ex.toString().contains(ex.getStatus().toString()));
    }

    // ---------------------- equals() tests ----------------------

    @Test
    void equals_sameObject_returnsTrue() {
        Exercise ex = new Exercise(3, false, 6);
        assertTrue(ex.equals(ex), "Reflexivity check failed");
    }

    @Test
    void equals_null_returnsFalse() {
        Exercise ex = new Exercise(3, false, 6);
        assertFalse(ex.equals(null));
    }

    @Test
    void equals_differentType_returnsFalse() {
        Exercise ex = new Exercise(3, false, 6);
        String notExercise = "not an exercise";
        assertFalse(ex.equals(notExercise));
    }

    @Test
    void equals_sameFields_returnsTrue() {
        Exercise e1 = new Exercise(4, true, 8);
        Exercise e2 = new Exercise(4, true, 8);
        assertEquals(e1, e2);
    }

    @Test
    void equals_differentExerciseNumber_returnsFalse() {
        Exercise e1 = new Exercise(4, true, 8);
        Exercise e2 = new Exercise(5, true, 8);
        assertNotEquals(e1, e2);
    }

    @Test
    void equals_differentIsDone_returnsFalse() {
        Exercise e1 = new Exercise(4, true, 8);
        Exercise e2 = new Exercise(4, false, 8);
        assertNotEquals(e1, e2);
    }

    @Test
    void hashCode_sameFields_sameHash() {
        Exercise e1 = new Exercise(4, false, 8);
        Exercise e2 = new Exercise(4, false, 8);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void hashCode_differentFields_differentHash() {
        Exercise e1 = new Exercise(4, false, 8);
        Exercise e2 = new Exercise(5, false, 8);
        assertNotEquals(e1.hashCode(), e2.hashCode());
    }
}
