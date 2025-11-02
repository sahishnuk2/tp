package seedu.address.model.person;

/**
 * Represents a collection of lab attendance statuses.
 */
public interface LabAttendanceList extends Comparable<LabAttendanceList> , Trackable {
    /**
     * Marks the specified lab session as attended.
     * @param index the zero-based index of the lab session.
     */
    public void markLabAsAttended(int index);

    /**
     * Marks the specified lab session as not attended.
     * @param index the zero-based index of the lab session.
     */
    public void markLabAsAbsent(int index);

    /**
     * Calculates the lab attendance rate as a percentage.
     * @return the attendance rate between 0.0 and 100.0.
     */
    public double calculateLabAttendance();

    LabAttendance[] getLabs();
}
