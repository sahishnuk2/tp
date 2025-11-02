package seedu.address.model.person.predicates.filterpredicates;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.helpers.Comparison;
import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.Person;


/**
 * Tests that a {@code Person}'s {@code Github Username} matches any of the keywords given.
 */
public class LabAttendanceMatchesPredicate extends FilterPredicate {
    private final double value;
    private final Comparison comparison;

    /**
     * Creates a predicate that compares a person's lab-attendance percentage against a target.
     *
     * @param value      target attendance percentage in the range 0–100
     * @param comparison comparison operator to apply (EQ, GE, LE, GT, LT)
     */
    public LabAttendanceMatchesPredicate(double value, Comparison comparison) {
        this.value = value;
        this.comparison = comparison;
    }

    @Override
    public boolean test(Person person) {
        LabAttendanceList attendaceList = person.getLabAttendanceList();
        double labAttendance = attendaceList.calculateLabAttendance();
        switch (comparison) {
        case EQUAL:
            return labAttendance == value;
        case GREATER_THAN_OR_EQUAL:
            return labAttendance >= value;
        case LESS_THAN_OR_EQUAL:
            return labAttendance <= value;
        case GREATER_THAN:
            return labAttendance > value;
        case LESS_THAN:
            return labAttendance < value;
        default:
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LabAttendanceMatchesPredicate)) {
            return false;
        }

        LabAttendanceMatchesPredicate otherPredicate = (LabAttendanceMatchesPredicate) other;
        return comparison.equals(otherPredicate.comparison)
                && value == otherPredicate.value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("value", value)
                .add("comparison", comparison)
                .toString();
    }

    @Override
    public String successMessage() {
        return "have attended " + statusToMessage() + (int) value + " percent of labs";
    }

    /**
     * Helper method that maps operators to their message phrase.
     *
     * @return The phrase corresponding to the operator
     */
    public String statusToMessage() {
        switch(comparison) {
        case EQUAL:
            return "exactly ";
        case GREATER_THAN_OR_EQUAL:
            return "more than or equal to ";
        case LESS_THAN_OR_EQUAL:
            return "less than or equal to ";
        case GREATER_THAN:
            return "more than ";
        case LESS_THAN:
            return "less than ";
        default:
            return "invalid filter ";
        }
    }

}
