package seedu.address.model.person.predicates.filterpredicates;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.ExerciseTracker;
import seedu.address.model.person.Person;
import seedu.address.model.person.Status;


/**
 * Tests that a {@code Person}'s {@code Exercise status} matches the status of the exercise stated.
 */
public class ExerciseStatusMatchesPredicate extends FilterPredicate {
    private final Status status;
    private final Index index;

    /**
     * Constructs a predicate that matches a {@code Person} if their {@code Exericse} status
     * matches the status of the exercise stated.
     *
     * @param index {@code Index} of the exercise you are trying to filter for.
     * @param status {@code Status} of the exercise chosen.
     */
    public ExerciseStatusMatchesPredicate(Index index, Status status) {
        this.index = index;
        this.status = status;
    }

    @Override
    public boolean test(Person person) {
        ExerciseTracker exerciseList = person.getExerciseTracker();
        List<Status> exercises = exerciseList.getStatuses();
        int number = index.getZeroBased();
        return exercises.get(number).equals(status);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExerciseStatusMatchesPredicate)) {
            return false;
        }

        ExerciseStatusMatchesPredicate otherPredicate = (ExerciseStatusMatchesPredicate) other;
        return status.equals(otherPredicate.status)
                && index.equals(otherPredicate.index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Status", status)
                .add("index", index)
                .toString();
    }

    @Override
    public String successMessage() {
        return statusToMessage() + " exercise " + index.getZeroBased();
    }

    /**
     * Helper method that maps statuses to their message phrase.
     *
     * @return The phrase corresponding to the status
     */
    public String statusToMessage() {
        switch(status) {
        case DONE:
            return "have completed";
        case NOT_DONE:
            return "have not yet completed";
        case OVERDUE:
            return "have not met the deadline for";
        default:
            return "invalid filter";
        }
    }
}
