package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.GetTimeslotCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new GetTimeslotCommand object
 */
public class GetTimeslotCommandParser implements Parser<GetTimeslotCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the GetTimeslotCommand
     * and returns a GetTimeslotCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format (should be empty).
     */
    @Override
    public GetTimeslotCommand parse(String args) throws ParseException {
        ParserUtil.verifyNoUnwantedPrefixes(args);
        String trimmedArgs = args.trim();
        if (!trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, GetTimeslotCommand.MESSAGE_USAGE));
        }
        return new GetTimeslotCommand();
    }
}
