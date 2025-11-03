package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUB_USERNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.SetWeekCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Week;

/**
 * Parses input arguments and creates a new {@code SetWeekCommand} object.
 */
public class SetWeekCommandParser implements Parser<SetWeekCommand> {

    @Override
    public SetWeekCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ParserUtil.verifyNoUnwantedPrefixes(args);

        Week currentWeek;

        try {
            int currentWeekNumber = Integer.parseInt(args.trim());
            currentWeek = new Week(currentWeekNumber);
        } catch (NumberFormatException nfe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetWeekCommand.MESSAGE_USAGE), nfe);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage(), e);
        }

        return new SetWeekCommand(currentWeek);
    }
}
