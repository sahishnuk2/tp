package seedu.address.model.person;

/**
 * Represents a lab session.
 */
public interface LabAttendance {

    /**
     * Marks this lab as attended.
     */
    public void markAsAttended();

    /**
     * Returns whether this lab has been marked as attended.
     *
     * @return {@code true} if the lab has been marked as attended;
     *         {@code false} otherwise
     */
    public boolean isAttended();
}
