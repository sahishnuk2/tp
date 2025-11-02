package seedu.address.model.timeslot;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Timeslot that represents a consultation with an associated student's name.
 * Delegates the studentName storage to the base Timeslot so JSON contains a studentName field
 * which is null for ordinary timeslots and non-null for consultations.
 */
public class ConsultationTimeslot extends Timeslot {

    /**
     * Jackson-friendly constructor and main constructor.
     *
     * @param start       start datetime (non-null)
     * @param end         end datetime (non-null)
     * @param studentName student name (must not be null or empty for consultations)
     */
    @JsonCreator
    public ConsultationTimeslot(@JsonProperty("start") LocalDateTime start,
                                @JsonProperty("end") LocalDateTime end,
                                @JsonProperty("studentName") String studentName) {
        super(start, end, studentName);
    }

    @Override
    public String toString() {
        String sname = getStudentName();
        if (sname == null || sname.isEmpty()) {
            return super.toString();
        }
        return String.format("%s (Consultation with %s)", super.toString(), sname);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ConsultationTimeslot)) {
            return false;
        }
        ConsultationTimeslot o = (ConsultationTimeslot) other;
        return super.equals(o); // super.equals compares start, end and studentName
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Preserve concrete type when creating a new timeslot for a new range.
     * This ensures that trimming/splitting a ConsultationTimeslot produces ConsultationTimeslot(s)
     * (i.e., the associated studentName is preserved).
     */
    @Override
    public Timeslot withRange(java.time.LocalDateTime newStart, java.time.LocalDateTime newEnd) {
        return new ConsultationTimeslot(newStart, newEnd, getStudentName());
    }
}
