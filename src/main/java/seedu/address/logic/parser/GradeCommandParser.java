package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXAM_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.ParserUtil.validateFields;

import seedu.address.commons.core.index.MultiIndex;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.InvalidIndexException;
import seedu.address.logic.commands.GradeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new GradeCommand object.
 * Expected format:
 *   grade 1:3 n/pe1 s/y
 */
public class GradeCommandParser implements Parser<GradeCommand> {

    public static final String MESSAGE_INVALID_EXAM_NAME_FORMAT =
            "Exam name is invalid! Exam name must be one of %s";
    private static final String EMPTY_PREFIX_FORMAT = "Prefix %s : has empty value!";

    @Override
    public GradeCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        ParserUtil.verifyNoUnwantedPrefixes(userInput, PREFIX_EXAM_NAME, PREFIX_STATUS);
        ArgumentMultimap argumentMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_EXAM_NAME, PREFIX_STATUS);
        argumentMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EXAM_NAME, PREFIX_STATUS);
        MultiIndex studentIndex;
        String examName;
        boolean isPassed;
        //ensure all the prefixes are present
        validateFields(argumentMultimap, GradeCommand.MESSAGE_USAGE, PREFIX_EXAM_NAME, PREFIX_STATUS);
        // Parse the "status" field (s/)
        isPassed = ParserUtil.parseStatus(argumentMultimap.getValue(PREFIX_STATUS).orElseThrow(()
                -> new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, GradeCommand.MESSAGE_USAGE)
        )));
        // Parse the "exam name" field (n/)
        try {
            examName = argumentMultimap.getValue(PREFIX_EXAM_NAME).orElseThrow(()
                    -> new ParseException(String.format(EMPTY_PREFIX_FORMAT, PREFIX_EXAM_NAME)));
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, GradeCommand.MESSAGE_USAGE),
                    ive
            );
        }
        // Parse the index or range of indices (e.g., "1" or "1:3") found before the prefixes
        try {
            studentIndex = ParserUtil.parseMultiIndex(argumentMultimap.getPreamble());
        } catch (InvalidIndexException iie) {
            throw new ParseException("Student " + iie.getMessage());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, GradeCommand.MESSAGE_USAGE),
                    pe
            );
        }
        // Construct and return the GradeCommand with parsed values
        return new GradeCommand(studentIndex, examName, isPassed);
    }
}
