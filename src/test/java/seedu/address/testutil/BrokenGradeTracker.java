package seedu.address.testutil;

import java.util.HashMap;
import java.util.List;

import seedu.address.model.person.Examination;
import seedu.address.model.person.GradeTracker;
import seedu.address.model.person.TrackerColour;

/**
 * A stubbed GradeTracker that intentionally does not add the exam to the map
 * when marked as passed or failed, for testing assertion behavior.
 */
public class BrokenGradeTracker implements GradeTracker {

    @Override
    public GradeTracker copy() {
        return this;
    }

    @Override
    public List<TrackerColour> getTrackerColours() {
        return List.of();
    }

    @Override
    public List<String> getLabels() {
        return List.of();
    }

    @Override
    public HashMap<String, Examination> getExamMap() {
        return new HashMap<>();
    }

    @Override
    public void putExam(String key, Examination exam) {

    }

    @Override
    public void markExamPassed(String examName) {
        // Do nothing intentionally
    }

    @Override
    public void markExamFailed(String examName) {
        // Do nothing intentionally
    }
}
