package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.ParserUtil.validateFields;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.commons.exceptions.InvalidIndexException;
import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code MarkAttendanceCommand} object.
 */
public class MarkAttendanceCommandParser implements Parser<MarkAttendanceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code MarkAttendanceCommand}
     * and returns a {@code MarkAttendanceCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public MarkAttendanceCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserUtil.verifyNoUnwantedPrefixes(args, PREFIX_LAB_NUMBER, PREFIX_STATUS);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_LAB_NUMBER, PREFIX_STATUS);

        // Parse exception directly goes to AddressBook Parser
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_LAB_NUMBER, PREFIX_STATUS);

        // Check if required fields are present
        validateFields(argMultimap, MarkAttendanceCommand.MESSAGE_USAGE, PREFIX_LAB_NUMBER, PREFIX_STATUS);
        MultiIndex multiIndex;
        Index labNumber;
        boolean isAttended;

        try {
            labNumber = ParserUtil.parseLabIndex(argMultimap.getValue(PREFIX_LAB_NUMBER).orElse(""));
        } catch (InvalidIndexException e) {
            throw new ParseException(e.getMessage());
        }

        // The ParseException from this would go to AddressBook Parser
        isAttended = ParserUtil.parseStatus(argMultimap.getValue(PREFIX_STATUS).orElse(""));

        // Parse multi-index (it will throw uncaught parse exception if index is wrong);
        try {
            multiIndex = ParserUtil.parseMultiIndex(argMultimap.getPreamble());
        } catch (InvalidIndexException e) {
            throw new ParseException("Student " + e.getMessage());
        }

        return new MarkAttendanceCommand(multiIndex, labNumber, isAttended);
    }
}
