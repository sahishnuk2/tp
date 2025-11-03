package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.GetConsultationsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new GetConsultationsCommand object
 */
public class GetConsultationsCommandParser implements Parser<GetConsultationsCommand> {

    @Override
    public GetConsultationsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (!trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    GetConsultationsCommand.MESSAGE_USAGE));
        }
        return new GetConsultationsCommand();
    }
}
