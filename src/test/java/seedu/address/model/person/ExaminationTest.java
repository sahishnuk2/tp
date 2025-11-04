package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ExaminationTest {

    @Test
    void constructor_initializesWithEmptyResult() {
        Examination exam = new Examination("midterm");

        assertEquals("midterm", exam.getName());
        assertTrue(exam.isPassed().isEmpty(), "New exam should start with no result");
    }

    @Test
    void markPassed_setsIsPassedTrue() {
        Examination exam = new Examination("final");
        exam.markPassed();

        assertTrue(exam.isPassed().isPresent());
        assertTrue(exam.isPassed().get(), "Exam should be marked as passed");
    }

    @Test
    void markFailed_setsIsPassedFalse() {
        Examination exam = new Examination("pe1");
        exam.markFailed();

        assertTrue(exam.isPassed().isPresent());
        assertFalse(exam.isPassed().get(), "Exam should be marked as failed");
    }

    @Test
    void toString_returnsExpectedOutput() {
        Examination exam = new Examination("pe0");

        // not graded yet
        assertEquals("pe0: NA", exam.toString());

        exam.markPassed();
        assertEquals("pe0: Passed", exam.toString());

        exam.markFailed();
        assertEquals("pe0: Failed", exam.toString());
    }

    @Test
    void equals_sameValues_returnsTrue() {
        Examination e1 = new Examination("midterm");
        Examination e2 = new Examination("midterm");

        assertEquals(e1, e2);

        e1.markPassed();
        e2.markPassed();
        assertEquals(e1, e2);
    }

    @Test
    void equals_differentNamesOrStatus_returnsFalse() {
        Examination e1 = new Examination("midterm");
        Examination e2 = new Examination("final");
        assertNotEquals(e1, e2, "Different names should not be equal");

        Examination e3 = new Examination("pe1");
        Examination e4 = new Examination("pe1");
        e3.markPassed();
        e4.markFailed();
        assertNotEquals(e3, e4, "Same name but different status should not be equal");
    }

    @Test
    void getName_returnsCorrectName() {
        Examination exam = new Examination("pe2");
        assertEquals("pe2", exam.getName());
    }
    @Test
    void equals_sameObject_returnsTrue() {
        Examination exam = new Examination("pe0");
        assertTrue(exam.equals(exam), "An object must equal itself (reflexivity check)");
    }

    @Test
    void equals_nullObject_returnsFalse() {
        Examination exam = new Examination("midterm");
        assertFalse(exam.equals(null), "Exam should not be equal to null");
    }

    @Test
    void equals_differentType_returnsFalse() {
        Examination exam = new Examination("final");
        String notAnExam = "final";
        assertFalse(exam.equals(notAnExam), "Exam should not be equal to a non-Examination object");
    }

}
