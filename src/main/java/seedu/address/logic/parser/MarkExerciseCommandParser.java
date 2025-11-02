package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.ParserUtil.validateFields;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.InvalidIndexException;
import seedu.address.logic.commands.MarkExerciseCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parser of the mark exercise command
 */
public class MarkExerciseCommandParser implements Parser<MarkExerciseCommand> {
    private static final String EMPTY_PREFIX_FORMAT = "Prefix %s : has empty value!";
    /**
     * Parse the user input into a mark exercise command
     * @param args user input
     * @return MarkExerciseCommand
     * @throws ParseException if user input is invalid
     */
    public MarkExerciseCommand parse(String args) throws ParseException {
        ParserUtil.verifyNoUnwantedPrefixes(args, PREFIX_EXERCISE_INDEX, PREFIX_STATUS);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EXERCISE_INDEX, PREFIX_STATUS);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EXERCISE_INDEX, PREFIX_STATUS);
        MultiIndex personIndex;
        Index exerciseIndex;
        boolean status;
        String statusString;
        validateFields(argMultimap, MarkExerciseCommand.MESSAGE_USAGE, PREFIX_EXERCISE_INDEX, PREFIX_STATUS);
        try {
            exerciseIndex = ParserUtil.parseExerciseIndex(
                    argMultimap.getValue(PREFIX_EXERCISE_INDEX).orElseThrow(() -> new ParseException(
                            String.format(EMPTY_PREFIX_FORMAT, PREFIX_STATUS)
                    ))
            );
            statusString = argMultimap.getValue(PREFIX_STATUS).orElseThrow(() -> new ParseException(
                    String.format(EMPTY_PREFIX_FORMAT, PREFIX_STATUS)
            ));
        } catch (InvalidIndexException iie) {
            throw new ParseException(iie.getMessage());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MarkExerciseCommand.MESSAGE_USAGE), ive);
        }
        try {
            personIndex = ParserUtil.parseMultiIndex(argMultimap.getPreamble());
        } catch (InvalidIndexException iie) {
            throw new ParseException("Student " + iie.getMessage());
        }
        status = ParserUtil.parseStatus(statusString.trim().toUpperCase());
        return new MarkExerciseCommand(personIndex, exerciseIndex, status);
    }
}

