package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import seedu.address.logic.commands.AddConsultationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.timeslot.ConsultationTimeslot;
import seedu.address.model.timeslot.Timeslot;

/**
 * Parses input arguments and creates a new AddConsultationCommand object.
 *
 * Expected format: ts/START_DATETIME te/END_DATETIME n/STUDENT_NAME
 */
public class AddConsultationCommandParser implements Parser<AddConsultationCommand> {

    // Standardized error message for datetime parsing failures
    private static final String INVALID_DATETIME_MESSAGE =
        "Invalid datetime: either wrong format or an impossible calendar date \n(for example, '30 Feb' does not exist). ";

    @Override
    public AddConsultationCommand parse(String args) throws ParseException {
        // include the name prefix so "n/..." is parsed separately (otherwise it becomes part of end datetime)
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                CliSyntax.PREFIX_TIMESLOT_START, CliSyntax.PREFIX_TIMESLOT_END, CliSyntax.PREFIX_NAME);

        // disallow duplicated ts/ or te/ prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(CliSyntax.PREFIX_TIMESLOT_START, CliSyntax.PREFIX_TIMESLOT_END);

        // no free-form preamble allowed
        if (!argMultimap.getPreamble().isEmpty()) {
            String msg = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddConsultationCommand.MESSAGE_USAGE);
            throw new ParseException(msg);
        }

        // use shared CliSyntax constants when reading values
        String startStr = argMultimap.getValue(CliSyntax.PREFIX_TIMESLOT_START).orElse("");
        String endStr = argMultimap.getValue(CliSyntax.PREFIX_TIMESLOT_END).orElse("");
        String studentName = argMultimap.getValue(CliSyntax.PREFIX_NAME).orElse("").trim();

        if (startStr.isEmpty() || endStr.isEmpty() || studentName.isEmpty()) {
            String msg = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddConsultationCommand.MESSAGE_USAGE);
            throw new ParseException(msg);
        }

        LocalDateTime start = parseDateTime(startStr);
        LocalDateTime end = parseDateTime(endStr);

        if (!end.isAfter(start)) {
            throw new ParseException("End datetime must be after start datetime.");
        }

        ConsultationTimeslot consultation = new ConsultationTimeslot(start, end, studentName);
        return new AddConsultationCommand(consultation);
    }

    /**
     * Try a few accepted datetime formats. Throws ParseException if none match.
     */
    private LocalDateTime parseDateTime(String input) throws ParseException {
        String trimmed = input.trim();
        DateTimeFormatter[] fmts = new DateTimeFormatter[] {
            // use strict resolver style to reject impossible dates (e.g., 30 Feb)
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.withResolverStyle(ResolverStyle.STRICT),
            Timeslot.FORMATTER.withResolverStyle(ResolverStyle.STRICT),
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d MMM uuuu, HH:mm")
                    .toFormatter(Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT),
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d MMM uuuu HH:mm")
                    .toFormatter(Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT)
        };

        for (DateTimeFormatter fmt : fmts) {
            try {
                return LocalDateTime.parse(trimmed, fmt);
            } catch (DateTimeParseException e) {
                // try next
            }
        }
        // unified, user-friendly message
        throw new ParseException(INVALID_DATETIME_MESSAGE);
    }
}
