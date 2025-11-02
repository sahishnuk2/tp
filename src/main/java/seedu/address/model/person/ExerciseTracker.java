package seedu.address.model.person;

import java.util.List;

import seedu.address.commons.core.index.Index;

/**
 * Represents a generic tracker for exercises.
 * Provides core operations for marking exercises, checking progress,
 * and retrieving tracker information without exposing internal details.
 */
public interface ExerciseTracker extends Comparable<ExerciseTracker>, Trackable {

    /**
     * Marks the exercise at the given index with the provided status.
     *
     * @param index the index of the exercise to update
     * @param isDone true if the exercise is completed, false otherwise
     */
    void markExercise(Index index, boolean isDone);

    /**
     * Calculates the overall progress of the tracked exercises as a percentage.
     *
     * @return the completion percentage (0.0 to 100.0)
     */
    double calculateProgress();

    /**
     * Returns a list representing whether each exercise is done.
     *
     * @return list of boolean values corresponding to exercise completion states
     */
    List<Boolean> getIsDoneList();

    /**
     * Returns a list of exercise statuses.
     *
     * @return list of {@link Status} values for all exercises
     */
    List<Status> getStatuses();

    /**
     * Returns a deep copy of the tracker.
     *
     * @return a new {@link ExerciseTracker} instance with copied data
     */
    ExerciseTracker copy();

    /**
     * Returns the tracker colours corresponding to each exercise status.
     *
     * @return list of {@link TrackerColour} values
     */
    List<TrackerColour> getTrackerColours();

    /**
     * Returns the labels used for identifying each exercise.
     *
     * @return list of exercise labels (e.g. EX0, EX1, ...)
     */
    List<String> getLabels();

    /**
     * Returns a string representation of the exercise tracker.
     *
     * @return a formatted string describing all exercises and their statuses
     */
    @Override
    String toString();
}
