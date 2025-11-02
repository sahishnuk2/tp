package seedu.address.model.person;

import java.util.HashMap;
import java.util.List;

/**
 * Represents a contract for managing and tracking exam grades for a student.
 * Provides methods for marking exams, retrieving grades, and producing copies.
 */
public interface GradeTracker extends Trackable {

    /**
     * Returns the internal exam map containing all examinations.
     *
     * @return a HashMap mapping exam names to their Examination objects
     */
    HashMap<String, Examination> getExamMap();

    /**
     * Inserts or replaces an {@link Examination} in the map.
     *
     * @param key the exam name
     * @param exam the Examination object to associate with the name
     */
    void putExam(String key, Examination exam);

    /**
     * Marks the given exam as passed.
     *
     * @param name the name of the exam
     * @throws seedu.address.model.person.exceptions.InvalidExamNameException
     *         if the given name is not one of the valid exam names
     */
    void markExamPassed(String name) throws seedu.address.model.person.exceptions.InvalidExamNameException;

    /**
     * Marks the given exam as failed.
     *
     * @param name the name of the exam
     * @throws seedu.address.model.person.exceptions.InvalidExamNameException
     *         if the given name is not one of the valid exam names
     */
    void markExamFailed(String name) throws seedu.address.model.person.exceptions.InvalidExamNameException;

    /**
     * Creates and returns a deep copy of this GradeTrackable object.
     *
     * @return a new GradeTrackable instance with copied data
     */
    GradeTracker copy();

    /**
     * Returns a list of tracker colours representing the status of each exam.
     *
     * @return a list of {@link TrackerColour} values in exam order
     */
    @Override
    List<TrackerColour> getTrackerColours();

    /**
     * Returns the labels used for display (typically the exam names).
     *
     * @return a list of exam name labels
     */
    @Override
    List<String> getLabels();
}
