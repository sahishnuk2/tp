package seedu.address.logic.helpers;

import seedu.address.commons.core.index.Index;
import seedu.address.model.person.Status;

/**
 * Simple data transfer object pairing an exercise index with its Status.
 * Immutable after construction.
 */
public class ExerciseIndexStatus {
    private final Index exerciseIndex;
    private final Status status;

    /**
     * Creates an instance with the given index and status.
     *
     * @param exerciseIndex the exercise index
     * @param status the status to associate with the exercise
     */
    public ExerciseIndexStatus(Index exerciseIndex, Status status) {
        this.exerciseIndex = exerciseIndex;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public Index getExerciseIndex() {
        return exerciseIndex;
    }
}
