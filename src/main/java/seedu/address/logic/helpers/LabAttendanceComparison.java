package seedu.address.logic.helpers;

/**
 * Simple data transfer object pairing a lab attendance percentage value with its comparison operator.
 * Immutable after construction.
 */
public class LabAttendanceComparison {

    private final double attendance;
    private final Comparison comparison;

    /**
     * Creates an instance with the given attendance and comparison
     *
     * @param attendance the percentage of attendance being compared
     * @param comparison the comparison being done
     */
    public LabAttendanceComparison(double attendance, Comparison comparison) {
        this.attendance = attendance;
        this.comparison = comparison;
    }

    public double getAttendance() {
        return attendance;
    }

    public Comparison getComparison() {
        return comparison;
    }


}
