package seedu.address.model.timeslot;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a timeslot with a start and end time.
 * Immutable.
 */
public class Timeslot {

    // Use ISO for storage/serialization so existing JSON files remain compatible.
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Separate, human-friendly formatter for display in the UI and user-facing messages.
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("d MMM uuuu, HH:mm");

    private final LocalDateTime start;
    private final LocalDateTime end;

    // Optional student name; null for ordinary blocked timeslots, non-null for consultations.
    @JsonProperty("studentName")
    private final String studentName;

    /**
     * Jackson-friendly constructor that also accepts an optional studentName.
     *
     * @param start must not be null.
     * @param end must not be null and must be after the start time.
     * @param studentName may be null (represents no associated student).
     */
    @JsonCreator
    public Timeslot(@JsonProperty("start") LocalDateTime start,
                    @JsonProperty("end") LocalDateTime end,
                    @JsonProperty("studentName") String studentName) {
        requireNonNull(start);
        requireNonNull(end);
        if (end.isBefore(start) || end.equals(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // validate year is positive (reject negative/zero years)
        if (start.getYear() <= 0 || end.getYear() <= 0) {
            throw new IllegalArgumentException("Year must be a positive value");
        }

        // Time-of-day constraint:
        // - start time must not be before 08:00
        // - end time is allowed to be midnight (00:00) of the next day; otherwise end time must not be before 08:00
        java.time.LocalTime earliest = java.time.LocalTime.of(8, 0);
        java.time.LocalTime midnight = java.time.LocalTime.MIDNIGHT;
        boolean endIsMidnightNextDay = end.toLocalTime().equals(midnight) && end.isAfter(start);

        if (start.toLocalTime().isBefore(earliest) || 
            (end.toLocalTime().isBefore(earliest) && !endIsMidnightNextDay)) {
            throw new IllegalArgumentException("Timeslot must be within 08:00 to 24:00 (midnight of next day allowed)");
        }

        this.start = start;
        this.end = end;
        this.studentName = studentName; // may be null
    }

    /**
     * Creates a Timeslot with no associated student (studentName == null).
     */
    public Timeslot(LocalDateTime start, LocalDateTime end) {
        this(start, end, null);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Returns the associated student name, or {@code null} if none.
     */
    @JsonProperty("studentName")
    public String getStudentName() {
        return studentName;
    }

    @Override
    public String toString() {
        if (studentName == null) {
            return String.format("Timeslot[Start: %s, End: %s]", start.format(FORMATTER), end.format(FORMATTER));
        } else {
            return String.format("Timeslot[Start: %s, End: %s, Student: %s]",
                    start.format(FORMATTER), end.format(FORMATTER), studentName);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Timeslot)) {
            return false;
        }
        Timeslot otherTs = (Timeslot) other;
        return start.equals(otherTs.start) && end.equals(otherTs.end)
                && Objects.equals(studentName, otherTs.studentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, studentName);
    }
}
