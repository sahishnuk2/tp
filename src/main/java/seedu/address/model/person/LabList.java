package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of lab attendance records for a student across all lab sessions.
 */
public class LabList implements LabAttendanceList {
    public static final int NUMBER_OF_LABS = 10;
    public static final String MESSAGE_CONSTRAINTS =
            "Lab attendance list should be in the format 'L1: Y/N ... L10: Y/N'";
    private static int currentWeek = 0;
    private final LabAttendance[] labs;

    /**
     * Constructs a {@code LabList} with all labs initialized to not attended.
     */
    public LabList() {
        this(createDefaultLabs(currentWeek));
    }

    /**
     * Constructs a {@code LabAttendanceList} with the specified lab attendance records.
     *
     * @param labs an array of {@code Lab} objects representing the lab attendance records
     */
    public LabList(LabAttendance[] labs) {
        assert labs.length == NUMBER_OF_LABS : "Wrong number of labs";
        this.labs = labs;
    }

    private static LabAttendance[] createDefaultLabs(int currentWeek) {
        LabAttendance[] labAttendanceList = new LabAttendance[NUMBER_OF_LABS];
        for (int i = 0; i < NUMBER_OF_LABS; i++) {
            labAttendanceList[i] = new Lab(i + 1, currentWeek);
        }
        return labAttendanceList;
    }

    public static int getCurrentWeek() {
        return currentWeek;
    }

    public static void setCurrentWeek(int week) {
        currentWeek = week;
    }

    @Override
    public void markLabAsAttended(int index) {
        assert index >= 0 : "Index must be greater than zero (one based)";
        assert index < NUMBER_OF_LABS : "Index must be smaller than " + NUMBER_OF_LABS + " (one based)";
        labs[index].markAsAttended();
    }

    @Override
    public void markLabAsAbsent(int index) {
        assert index >= 0 : "Index must be greater than zero (one based)";
        assert index < NUMBER_OF_LABS : "Index must be smaller than " + NUMBER_OF_LABS + " (one based)";
        labs[index].markAsAbsent();
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

    /**
     * Creates and returns a copy of this LabList with all the same attendance states.
     *
     * @return a new LabList with copied attendance records
     */
    public LabList copy() {
        LabAttendance[] copiedLabs = new LabAttendance[NUMBER_OF_LABS];
        for (int i = 0; i < NUMBER_OF_LABS; i++) {
            Lab originalLab = (Lab) this.labs[i];
            Lab newLab = new Lab(i + 1, currentWeek);
            if (originalLab.isAttended()) {
                newLab.markAsAttended();
            }
            copiedLabs[i] = newLab;
        }
        return new LabList(copiedLabs);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < NUMBER_OF_LABS; i++) {
            result.append(labs[i].toString()).append(" ");
        }
        return result.toString();
    }

    /**
     * Returns true if a given string is a valid lab attendance list format.
     */
    public static boolean isValidLabList(String labListString) {
        if (labListString == null) {
            return false;
        }

        String trimmed = labListString.trim();
        String[] parts = trimmed.split("\\s+");

        // Each lab would have two parts (eg L1, Y)
        if (parts.length != NUMBER_OF_LABS * 2) {
            return false;
        }

        for (int i = 0; i < NUMBER_OF_LABS; i++) {
            String labLabel = parts[i * 2];
            String status = parts[i * 2 + 1];

            if (!labLabel.equals("L" + (i + 1) + ":")) {
                return false;
            }

            if (!status.equals("Y") && !status.equals("N") && !status.equals("A")) {
                return false;
            }
        }
        return true;
    }

    public LabAttendance[] getLabs() {
        return labs;
    }

    @Override
    public double calculateLabAttendance() {
        double count = 0;
        for (LabAttendance lab: labs) {
            if (lab.isAttended()) {
                count++;
            }
        }
        return count / NUMBER_OF_LABS * 100;
    }

    @Override
    public int compareTo(LabAttendanceList other) {
        return Double.compare(this.calculateLabAttendance(), other.calculateLabAttendance());
    }
    @Override
    public List<TrackerColour> getTrackerColours() {
        List<TrackerColour> colours = new ArrayList<>();
        for (LabAttendance lab : labs) {
            String status = lab.getStatus();
            TrackerColour colour = switch (status) {
            case "Y" -> TrackerColour.GREEN;
            case "A" -> TrackerColour.RED;
            default -> TrackerColour.GREY; // "N"
            };
            colours.add(colour);
        }
        return colours;
    }
    @Override
    public List<String> getLabels() {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_LABS; i++) {
            labels.add("L" + (i + 1));
        }
        return labels;
    }
}
