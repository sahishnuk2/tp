package seedu.address.model.person;

import static seedu.address.logic.parser.GradeCommandParser.MESSAGE_INVALID_EXAM_NAME_FORMAT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.exceptions.InvalidExamNameException;

/**
 * Wraps a HashMap with String keys and Examination values.
 */
public class GradeMap implements GradeTracker {
    public static final String[] VALID_EXAM_NAMES = {"pe0", "pe1", "midterm", "pe2", "final"};
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    private final HashMap<String, Examination> examMap;

    /**
     * Fills the hashmap with the valid exam names.
     */
    public GradeMap() {
        examMap = new HashMap<>();

        for (String examName : VALID_EXAM_NAMES) {
            examMap.put(examName, new Examination(examName));
        }
    }

    @Override
    public String toString() {
        return Arrays.stream(VALID_EXAM_NAMES)
                .map(name -> examMap.get(name).toString())
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof GradeMap)) {
            return false;
        }

        GradeMap other = (GradeMap) obj;
        return this.examMap.equals(other.examMap);
    }

    public HashMap<String, Examination> getExamMap() {
        return examMap;
    }

    public void putExam(String key, Examination exam) {
        examMap.put(key, exam);
    }

    /**
     * Marks an exam as passed.
     * @param name of the exam to mark as passed
     * @throws InvalidExamNameException if the exam name is invalid
     */
    public void markExamPassed(String name) throws InvalidExamNameException {
        logger.info(String.format("Marking %s as Passed", name));

        Examination exam = examMap.get(name);
        if (exam == null) {
            throw new InvalidExamNameException(
                    String.format(
                            MESSAGE_INVALID_EXAM_NAME_FORMAT,
                            Arrays.toString(VALID_EXAM_NAMES)
                    )
            );
        }

        exam.markPassed();
    }

    /**
     * Marks an exam as failed.
     * @param name of the exam to mark as failed
     * @throws InvalidExamNameException if the exam name is invalid
     */
    public void markExamFailed(String name) throws InvalidExamNameException {
        logger.info(String.format("Marking %s as Failed", name));

        Examination exam = examMap.get(name);

        if (exam == null) {
            throw new InvalidExamNameException(
                    String.format(
                            MESSAGE_INVALID_EXAM_NAME_FORMAT,
                            Arrays.toString(VALID_EXAM_NAMES)
                    )
            );
        }

        exam.markFailed();
    }

    /**
     * Returns a deep copy of this GradeMap.
     * @return a new GradeMap with copied data
     */
    public GradeMap copy() {
        GradeMap newGradeMap = new GradeMap();
        for (String examName : VALID_EXAM_NAMES) {
            Examination original = this.examMap.get(examName);
            Examination copied = newGradeMap.examMap.get(examName);
            // Skip exams that have no pass/fail status
            if (original.isPassed().isEmpty()) {
                continue;
            }
            // Copy pass/fail state directly
            boolean isPassed = original.isPassed().get();
            if (isPassed) {
                copied.markPassed();
            } else {
                copied.markFailed();
            }
        }
        return newGradeMap;
    }


    @Override
    public List<TrackerColour> getTrackerColours() {
        return Arrays.stream(VALID_EXAM_NAMES)
                .map(examName -> {
                    Examination exam = examMap.get(examName);
                    if (exam.isPassed().isEmpty()) {
                        return TrackerColour.GREY; // Not graded yet
                    }
                    return exam.isPassed().get() ? TrackerColour.GREEN : TrackerColour.RED;
                })
                .collect(Collectors.toList());
    }
    @Override
    public List<String> getLabels() {
        return Arrays.stream(VALID_EXAM_NAMES)
                .map(String::toUpperCase)
                .toList();
    }
}
