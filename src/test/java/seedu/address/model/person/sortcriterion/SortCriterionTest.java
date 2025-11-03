package seedu.address.model.person.sortcriterion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SortCriterionTest {
    private SortCriterion name = new NameSortCriterion();
    private SortCriterion studentId = new StudentIdSortCriterion();
    private SortCriterion exercise = new ExerciseSortCriterion();
    private SortCriterion lab = new LabSortCriterion();

    @Test
    public void equals() {
        SortCriterion name1 = new NameSortCriterion();
        SortCriterion studentId = new StudentIdSortCriterion();
        SortCriterion name2 = new NameSortCriterion();
        SortCriterion lab = new LabSortCriterion();

        assertTrue(name.equals(name));
        assertTrue(name.equals(name1));

        assertTrue(name1.equals(name2));

        assertFalse(name1.equals(studentId));
        assertFalse(studentId.equals(lab));

        assertFalse(name1.equals(null));
    }

    @Test
    public void toString_default_success() {
        assertEquals("name", name.toString());
        assertEquals("id", studentId.toString());
        assertEquals("ex", exercise.toString());
        assertEquals("lab", lab.toString());
    }

    @Test
    public void getDisplayString() {
        assertEquals("name", name.getDisplayString());
        assertEquals("student id", studentId.getDisplayString());
        assertEquals("exercise progress", exercise.getDisplayString());
        assertEquals("lab attendance", lab.getDisplayString());

    }
}
