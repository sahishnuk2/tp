package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.exceptions.InvalidExamNameException;

class GradeMapTest {

    @Test
    void constructor_initializesAllValidExamNames() {
        GradeMap gradeMap = new GradeMap();
        List<String> validNames = Arrays.asList(GradeMap.VALID_EXAM_NAMES);

        for (String name : validNames) {
            assertTrue(gradeMap.getExamMap().containsKey(name));
            assertEquals(name, gradeMap.getExamMap().get(name).getName());
            assertTrue(gradeMap.getExamMap().get(name).isPassed().isEmpty());
        }
    }

    @Test
    void markExamPassed_validExam_marksAsPassed() throws InvalidExamNameException {
        GradeMap gradeMap = new GradeMap();
        gradeMap.markExamPassed("pe1");
        assertTrue(gradeMap.getExamMap().get("pe1").isPassed().get());
    }

    @Test
    void markExamFailed_validExam_marksAsFailed() throws InvalidExamNameException {
        GradeMap gradeMap = new GradeMap();
        gradeMap.markExamFailed("midterm");
        assertFalse(gradeMap.getExamMap().get("midterm").isPassed().get());
    }

    @Test
    void markExamPassed_invalidExam_throwsException() {
        GradeMap gradeMap = new GradeMap();
        InvalidExamNameException ex = assertThrows(
                InvalidExamNameException.class, () -> gradeMap.markExamPassed("quiz1")
        );
        assertTrue(ex.getMessage().contains("Exam name is invalid!"));
    }

    @Test
    void copy_createsDeepCopy() throws InvalidExamNameException {
        GradeMap original = new GradeMap();
        original.markExamPassed("pe0");
        original.markExamFailed("final");

        GradeMap copy = original.copy();

        // The two maps should not be the same object
        assertNotSame(original.getExamMap(), copy.getExamMap());
        // Their contents should be equal
        assertEquals(original, copy);

        // Mutate copy to ensure deep copy
        copy.markExamPassed("midterm");
        assertTrue(copy.getExamMap().get("midterm").isPassed().isPresent());
        assertTrue(original.getExamMap().get("midterm").isPassed().isEmpty());
    }

    @Test
    void getTrackerColours_returnsCorrectColours() throws InvalidExamNameException {
        GradeMap gradeMap = new GradeMap();
        gradeMap.markExamPassed("pe0");
        gradeMap.markExamFailed("midterm");

        List<TrackerColour> colours = gradeMap.getTrackerColours();

        assertEquals(GradeMap.VALID_EXAM_NAMES.length, colours.size());
        assertEquals(TrackerColour.GREEN, colours.get(0)); // pe0
        assertEquals(TrackerColour.RED, colours.get(2)); // midterm
        assertEquals(TrackerColour.GREY, colours.get(4)); // final (ungraded)
    }

    @Test
    void toString_reflectsExamStatuses() throws InvalidExamNameException {
        GradeMap gradeMap = new GradeMap();
        gradeMap.markExamPassed("pe1");
        gradeMap.markExamFailed("final");

        String output = gradeMap.toString();

        assertTrue(output.contains("pe1: Passed"));
        assertTrue(output.contains("final: Failed"));
        assertTrue(output.contains("midterm: NA"));
    }

    @Test
    void equals_comparesCorrectly() throws InvalidExamNameException {
        GradeMap g1 = new GradeMap();
        GradeMap g2 = new GradeMap();

        g1.markExamPassed("pe2");
        g2.markExamPassed("pe2");

        assertEquals(g1, g2);

        g2.markExamFailed("midterm");
        assertNotEquals(g1, g2);
    }
    @Test
    void markExamFailed_nullName_throwsInvalidExamNameException() {
        GradeMap gradeMap = new GradeMap();

        InvalidExamNameException ex = assertThrows(
                InvalidExamNameException.class, () -> gradeMap.markExamFailed(null)
        );

        assertTrue(ex.getMessage().contains("Exam name is invalid!"));
        assertTrue(ex.getMessage().contains("[pe0, pe1, midterm, pe2, final]"));
    }

    @Test
    void markExamPassed_nullName_throwsInvalidExamNameException() {
        GradeMap gradeMap = new GradeMap();

        InvalidExamNameException ex = assertThrows(
                InvalidExamNameException.class, () -> gradeMap.markExamPassed(null)
        );

        assertTrue(ex.getMessage().contains("Exam name is invalid!"));
        assertTrue(ex.getMessage().contains("[pe0, pe1, midterm, pe2, final]"));
    }
    @Test
    void getLabels_returnsUppercaseExamNamesInOrder() {
        GradeMap gradeMap = new GradeMap();

        List<String> labels = gradeMap.getLabels();

        // Expected: uppercase version of VALID_EXAM_NAMES
        List<String> expected = Arrays.stream(GradeMap.VALID_EXAM_NAMES)
                .map(String::toUpperCase)
                .toList();

        assertEquals(expected, labels, "getLabels() should return uppercase exam names in the same order");
        assertEquals(5, labels.size(), "Label list size should match VALID_EXAM_NAMES length");

        // Double-check content
        assertEquals("PE0", labels.get(0));
        assertEquals("FINAL", labels.get(4));
    }

}
