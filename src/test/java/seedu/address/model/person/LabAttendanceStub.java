package seedu.address.model.person;

/**
 * A stub implementation of {@code Lab} for testing purposes.
 * Provides a simple in-memory tracking of attendance status.
 */
public class LabAttendanceStub implements LabAttendance {
    private boolean isAttended = false;

    @Override
    public void markAsAttended() {
        isAttended = true;
    }

    @Override
    public boolean isAttended() {
        return isAttended;
    }
}
