package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.UnblockTimeslotCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.timeslot.Timeslot;

/**
 * Parses input arguments and creates a new UnblockTimeslotCommand object.
 */
public class UnblockTimeslotCommandParser implements Parser<UnblockTimeslotCommand> {

    // Alternate human-friendly formats (case-insensitive):
    // with comma: "1 Jan 2025, 10:00"
    private static final DateTimeFormatter ALTERNATE_WITH_COMMA =
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d MMM uuuu, HH:mm")
                    .toFormatter(Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);
    // without comma: "1 Jan 2025 10:00"
    private static final DateTimeFormatter ALTERNATE_NO_COMMA =
            new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .appendPattern("d MMM uuuu HH:mm")
                    .toFormatter(Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    // Standardized error message for datetime parsing failures
    private static final String INVALID_DATETIME_MESSAGE =
            "Invalid datetime: either wrong format or an impossible calendar date \n"
            + "(for example, '30 Feb' does not exist). ";

    private static LocalDateTime parseFlexibleDateTime(String input) throws DateTimeParseException {
        Objects.requireNonNull(input);
        String trimmed = input.trim();

        // try ISO first (strict)
        try {
            java.time.format.DateTimeFormatter isoStrict =
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME.withResolverStyle(ResolverStyle.STRICT);
            return LocalDateTime.parse(trimmed, isoStrict);
        } catch (DateTimeParseException e) {
            // fall through
        }

        // try Timeslot's configured formatter (strict)
        try {
            return LocalDateTime.parse(trimmed, Timeslot.FORMATTER.withResolverStyle(ResolverStyle.STRICT));
        } catch (DateTimeParseException e) {
            // try human-friendly with comma
            try {
                return LocalDateTime.parse(trimmed, ALTERNATE_WITH_COMMA);
            } catch (DateTimeParseException ex2) {
                // try human-friendly without comma
                return LocalDateTime.parse(trimmed, ALTERNATE_NO_COMMA);
            }
        }
    }

    @Override
    public UnblockTimeslotCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                CliSyntax.PREFIX_TIMESLOT_START, CliSyntax.PREFIX_TIMESLOT_END);

        // disallow duplicated ts/ or te/ prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(CliSyntax.PREFIX_TIMESLOT_START, CliSyntax.PREFIX_TIMESLOT_END);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        String startStr = null;
        String endStr = null;

        // Prefer values from the tokenizer if present
        if (argMultimap.getValue(CliSyntax.PREFIX_TIMESLOT_START).isPresent()
                && argMultimap.getValue(CliSyntax.PREFIX_TIMESLOT_END).isPresent()) {
            startStr = argMultimap.getValue(CliSyntax.PREFIX_TIMESLOT_START).get();
            endStr = argMultimap.getValue(CliSyntax.PREFIX_TIMESLOT_END).get();
        } else {
            // Fallback: try to extract "ts/<start> te/<end>" from raw args (allows spaces/commas inside)
            Pattern p = Pattern.compile("(?i)\\bts/(.+?)\\s+te/(.+)$");
            Matcher m = p.matcher(args.trim());
            if (m.find()) {
                startStr = m.group(1).trim();
                endStr = m.group(2).trim();
            }
        }

        if (startStr == null || endStr == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnblockTimeslotCommand.MESSAGE_USAGE));
        }

        try {
            LocalDateTime start = parseFlexibleDateTime(startStr);
            LocalDateTime end = parseFlexibleDateTime(endStr);

            // Construct Timeslot and let Timeslot constructor enforce business rules.
            Timeslot ts = new Timeslot(start, end);
            return new UnblockTimeslotCommand(ts);
        } catch (DateTimeException e) {
            // unified, user-friendly message
            throw new ParseException(INVALID_DATETIME_MESSAGE);
        } catch (IllegalArgumentException e) {
            // Timeslot constructor validations (year, time window, end before start, etc.)
            throw new ParseException("Invalid timeslot: " + e.getMessage());
        }
    }
}
