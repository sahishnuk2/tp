package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_INDEX_FORMAT =
            "%d is invalid as a %s index. Use a number from %d to %d";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple prefix(s) specified: ";
    public static final String MESSAGE_DUPLICATE_FIND_FIELDS =
            "Field used more than once, each field is a flag and must be unique. Repeated: ";


    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Returns an error message indicating the duplicate selectors for find command
     */
    public static String getErrorMessageForDuplicateFindSelectors(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIND_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append("StudentId: ")
                .append(person.getStudentId())
                .append("; Name: ")
                .append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; GitHub Username: ")
                .append(person.getGithubUsername())
                .append("; Lab Attendance List: ")
                .append(person.getLabAttendanceList())
                .append("; Tags:");
        if (person.getTags().isEmpty()) {
            builder.append(" ,");
        } else {
            builder.append(" ");
            builder.append(person.getTags().stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", ")));
        }
        return builder.toString();
    }

}
