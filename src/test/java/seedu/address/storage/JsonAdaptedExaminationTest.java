package seedu.address.storage;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Examination;

public class JsonAdaptedExaminationTest {

    @Test
    public void constructor_fromFields_success() {
        JsonAdaptedExamination exam = new JsonAdaptedExamination("Midterm", "passed");
        assertEquals("Midterm", exam.getName());
        assertEquals("passed", exam.getResult());
    }

    @Test
    public void constructor_fromModelPassedExam_success() {
        Examination modelExam = new Examination("Final");
        modelExam.markPassed();
        JsonAdaptedExamination adapted = new JsonAdaptedExamination(modelExam);
        assertEquals("Final", adapted.getName());
        assertEquals("passed", adapted.getResult());
    }

    @Test
    public void constructor_fromModelFailedExam_success() {
        Examination modelExam = new Examination("Quiz");
        modelExam.markFailed();
        JsonAdaptedExamination adapted = new JsonAdaptedExamination(modelExam);
        assertEquals("Quiz", adapted.getName());
        assertEquals("failed", adapted.getResult());
    }

    @Test
    public void constructor_fromModelUngradedExam_success() {
        Examination modelExam = new Examination("LabTest");
        JsonAdaptedExamination adapted = new JsonAdaptedExamination(modelExam);
        assertEquals("LabTest", adapted.getName());
        assertEquals("na", adapted.getResult());
    }

    @Test
    public void toModelType_passed_success() {
        JsonAdaptedExamination adapted = new JsonAdaptedExamination("Midterm", "passed");
        Examination exam = adapted.toModelType();
        assertEquals("Midterm", exam.getName());
        assertTrue(exam.isPassed().isPresent());
        assertTrue(exam.isPassed().get());
    }

    @Test
    public void toModelType_failed_success() {
        JsonAdaptedExamination adapted = new JsonAdaptedExamination("Midterm", "failed");
        Examination exam = adapted.toModelType();
        assertEquals("Midterm", exam.getName());
        assertTrue(exam.isPassed().isPresent());
        assertFalse(exam.isPassed().get());
    }

    @Test
    public void toModelType_na_success() {
        JsonAdaptedExamination adapted = new JsonAdaptedExamination("Midterm", "na");
        Examination exam = adapted.toModelType();
        assertEquals("Midterm", exam.getName());
        assertTrue(exam.isPassed().isEmpty());
    }

    @Test
    public void toModelType_nullResult_success() {
        JsonAdaptedExamination adapted = new JsonAdaptedExamination("Midterm", null);
        Examination exam = adapted.toModelType();
        assertEquals("Midterm", exam.getName());
        assertTrue(exam.isPassed().isEmpty());
    }

    @Test
    public void toModelType_caseInsensitive_success() {
        JsonAdaptedExamination passed = new JsonAdaptedExamination("Midterm", "PaSsEd");
        Examination exam1 = passed.toModelType();
        assertTrue(exam1.isPassed().orElse(false));

        JsonAdaptedExamination failed = new JsonAdaptedExamination("Midterm", "FaILeD");
        Examination exam2 = failed.toModelType();
        assertFalse(exam2.isPassed().orElse(true));
    }
}
