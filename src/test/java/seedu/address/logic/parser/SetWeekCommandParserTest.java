package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ARGUMENT_WEEK_NUMBER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NUMBER_FORMAT_WEEK_NUMBER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WEEK_NUMBER;
import static seedu.address.logic.commands.CommandTestUtil.WEEK_NUMBER_DESC;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SetWeekCommand;
import seedu.address.model.Week;

public class SetWeekCommandParserTest {
    private final SetWeekCommandParser parser = new SetWeekCommandParser();

    @Test
    public void parse_validInput_success() {
        assertParseSuccess(parser, WEEK_NUMBER_DESC, new SetWeekCommand(new Week(VALID_WEEK_NUMBER)));
    }

    @Test
    public void parse_invalidInput_success() {
        String invalidCommandFormat = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetWeekCommand.MESSAGE_USAGE);
        String invalidWeekNumber = Week.MESSAGE_CONSTRAINTS;

        // Week Number is not a number
        assertParseFailure(parser, INVALID_NUMBER_FORMAT_WEEK_NUMBER_DESC, invalidCommandFormat);

        // Week number is not in the range
        assertParseFailure(parser, INVALID_ARGUMENT_WEEK_NUMBER_DESC, invalidWeekNumber);
    }
}
