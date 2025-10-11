package seedu.address.model.person;

/**
 * Represents a collection of lab attendance records for a student across all lab sessions.
 */
public class LabList implements LabAttendanceList {
    public static final int NUMBER_OF_LABS = 10;
    private final LabAttendance[] labs;

    /**
     * Constructs a {@code LabList} with all labs initialized to not attended.
     */
    public LabList() {
        this(createDefaultLabs());
    }

    /**
     * Constructs a {@code LabAttendanceList} with the specified lab attendance records.
     *
     * @param labs an array of {@code Lab} objects representing the lab attendance records
     */
    public LabList(LabAttendance[] labs) {
        assert(labs.length == NUMBER_OF_LABS);
        this.labs = labs;
    }

    private static LabAttendance[] createDefaultLabs() {
        LabAttendance[] labAttendanceList = new LabAttendance[NUMBER_OF_LABS];
        for (int i = 0; i < NUMBER_OF_LABS; i++) {
            labAttendanceList[i] = new Lab(i + 1);
        }
        return labAttendanceList;
    }

    /**
     * Marks the specified lab session as attended.
     * @param index the zero-based index of the lab session.
     */
    @Override
    public void markLabAsAttended(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= NUMBER_OF_LABS) {
            throw new IndexOutOfBoundsException("Index should be between 0 and " + (NUMBER_OF_LABS - 1));
        }
        labs[index].markAsAttended();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof LabList)) {
            return false;
        }

        LabList otherLabList = (LabList) other;
        for (int i = 0; i < NUMBER_OF_LABS; i++) {
            if (this.labs[i].isAttended() != otherLabList.labs[i].isAttended()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < NUMBER_OF_LABS; i++) {
            result.append(labs[i].toString()).append(" ");
        }
        return result.toString();
    }
}
