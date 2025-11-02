package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.person.exceptions.InvalidExamNameException;

class GradeMapTest {

    private GradeMap gradeMap;

    @BeforeEach
    void setUp() {
        gradeMap = new GradeMap();
    }

    @Test
    void markExamPassed_validExam_marksAsPassed() throws InvalidExamNameException {
        String validExamName = "pe1";

        gradeMap.markExamPassed(validExamName);

        Examination exam = gradeMap.getExamMap().get(validExamName);
        // check that status is set
        assertTrue(exam.isPassed().isPresent(), "Exam should have a pass/fail status");
        // check that exam is marked passed
        assertTrue(exam.isPassed().get(), "Exam should be marked as passed");
    }

    @Test
    void markExamPassed_invalidExam_throwsInvalidExamNameException() {
        String invalidExamName = "quiz1";
        // verify that invalid exam name throws exception
        InvalidExamNameException exception = assertThrows(
                InvalidExamNameException.class, ()
                        -> gradeMap.markExamPassed(invalidExamName)
        );
        // check that exception message is correct
        assertTrue(
                exception.getMessage().contains("Exam name is invalid!"),
                "Exception message should indicate invalid exam name"
        );
    }
}
