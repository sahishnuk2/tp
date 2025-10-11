package seedu.address.model.person;

/**
 * Represents a student's attendance status for a single lab session.
 */
public class Lab implements LabAttendance {
    private final int labNumber;
    private boolean isAttended;

    /**
     * Constructs a {@code Lab} with the specified lab number and attendance initially set to false.
     *
     * @param labNumber The lab number (must be positive)
     */
    public Lab(int labNumber) {
        assert labNumber > 0;
        this.labNumber = labNumber;
        this.isAttended = false;
    }

    @Override
    public void markAsAttended() throws IllegalStateException {
        if (isAttended) {
            throw new IllegalStateException("Lab Attendance has already been marked");
        }
        isAttended = true;
    }

    @Override
    public boolean isAttended() {
        return isAttended;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Lab)) {
            return false;
        }

        Lab otherLab = (Lab) other;
        return this.isAttended == otherLab.isAttended
                && this.labNumber == otherLab.labNumber;
    }

    @Override
    public String toString() {
        String status = isAttended ? "Y" : "N";
        return String.format("L%d: %s", labNumber, status);
    }
}
