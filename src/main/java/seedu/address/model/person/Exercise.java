package seedu.address.model.person;

import java.util.Objects;

/**
 * Represents an exercise.
 */
public class Exercise {
    public static final int EXERCISE_WEEK_DIFFERENCE = 2;
    private boolean isDone;
    private final int exerciseNumber;
    private final boolean isPastWeek;
    /**
     * Creates an exercise object with the following parameters.
     * @param exerciseNumber the exercise number
     * @param isDone the completion status of the exercise
     */
    public Exercise(int exerciseNumber, boolean isDone, int currentWeek) {
        assert exerciseNumber >= 0 : "Invalid exercise number";
        this.exerciseNumber = exerciseNumber;
        this.isDone = isDone;
        this.isPastWeek = exerciseNumber < (currentWeek - EXERCISE_WEEK_DIFFERENCE);
    }

    @Override
    public String toString() {
        return String.format("ex %d: %s", exerciseNumber, getStatus());
    }

    /**
     * sets isDone to a given boolean value
     * @param status to set isDone to
     */
    public void markStatus(boolean status) {
        if (this.isDone == status) {
            throw new IllegalStateException("Lab Attendance has already been marked as not attended");
        }
        this.isDone = status;
    }

    public boolean isDone() {
        return isDone;
    }

    public Status getStatus() {
        if (isDone) {
            return Status.DONE;
        } else if (isPastWeek) {
            return Status.OVERDUE;
        } else {
            return Status.NOT_DONE;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Exercise otherExercise)) {
            return false;
        }
        return exerciseNumber == otherExercise.exerciseNumber && isDone == otherExercise.isDone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseNumber, isDone);
    }
}
