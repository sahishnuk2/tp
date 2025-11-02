package seedu.address.storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.timeslot.ConsultationTimeslot;
import seedu.address.model.timeslot.Timeslot;

/**
 * Jackson-friendly version of {@link Timeslot}.
 */
public class JsonAdaptedTimeslot {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Timeslot's %s field is missing!";

    private final String start;
    private final String end;
    private final String studentName;

    /**
     * Constructs a {@code JsonAdaptedTimeslot} with the given fields.
     */
    @JsonCreator
    public JsonAdaptedTimeslot(@JsonProperty("start") String start,
                               @JsonProperty("end") String end,
                               @JsonProperty("studentName") String studentName) {
        this.start = start;
        this.end = end;
        this.studentName = studentName;
    }

    /**
     * Constructs a {@code JsonAdaptedTimeslot} from a model {@code Timeslot}.
     */
    public JsonAdaptedTimeslot(Timeslot source) {
        this.start = source.getStart().format(Timeslot.FORMATTER);
        this.end = source.getEnd().format(Timeslot.FORMATTER);
        this.studentName = source.getStudentName(); // may be null
    }

    // Helper: parse ISO_LOCAL_DATE_TIME but accept "T24:00" by normalizing to next-day 00:00
    private static LocalDateTime parseIsoAllow24(String dateTimeStr) throws DateTimeParseException {
        if (dateTimeStr == null) {
            throw new DateTimeParseException("Null datetime", dateTimeStr, 0);
        }
        String trimmed = dateTimeStr.trim();
        try {
            // use strict resolver to reject invalid calendar dates
            return LocalDateTime.parse(trimmed, Timeslot.FORMATTER.withResolverStyle(ResolverStyle.STRICT));
        } catch (DateTimeParseException ex) {
            // Handle "YYYY-MM-DDT24:MM:SS" by moving to next day and using 00:MM:SS
            int tIndex = trimmed.indexOf('T');
            if (tIndex > 0 && trimmed.length() > tIndex + 2 && trimmed.charAt(tIndex + 1) == '2'
                    && trimmed.charAt(tIndex + 2) == '4') {
                // split date and time
                String datePart = trimmed.substring(0, tIndex);
                String timePart = trimmed.substring(tIndex + 1); // e.g., "24:00:00" or "24:00"
                // replace leading 24: with 00:
                String fixedTimePart = timePart.replaceFirst("^24:", "00:");
                // parse date and add one day using strict resolver
                LocalDate date = LocalDate.parse(datePart, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE.withResolverStyle(ResolverStyle.STRICT));
                LocalTime time = LocalTime.parse(fixedTimePart, java.time.format.DateTimeFormatter.ISO_LOCAL_TIME.withResolverStyle(ResolverStyle.STRICT));
                return LocalDateTime.of(date.plusDays(1), time);
            }
            // rethrow original exception if not a 24:xx case
            throw ex;
        }
    }

    /**
     * Converts this Jackson-friendly adapted timeslot object into the model's {@code Timeslot} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Timeslot toModelType() throws IllegalValueException {
        if (start == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "start"));
        }
        if (end == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "end"));
        }

        LocalDateTime startDt;
        LocalDateTime endDt;
        try {
            startDt = parseIsoAllow24(start);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException("Invalid start datetime format: " + start);
        }
        try {
            endDt = parseIsoAllow24(end);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException("Invalid end datetime format: " + end);
        }

        // If studentName is present (even if empty), create a ConsultationTimeslot so the concrete type is preserved.
        // Previously we required non-empty which caused consultations with empty studentName to be downgraded to Timeslot.
        if (studentName != null) {
            return new ConsultationTimeslot(startDt, endDt, studentName);
        } else {
            return new Timeslot(startDt, endDt);
        }
    }
}
