package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class DeleteCommandParserTest {

    private final DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validSingleIndex_success() throws Exception {
        DeleteCommand command = parser.parse("1");
        MultiIndex expectedIndex = new MultiIndex(Index.fromOneBased(1));
        assertEquals(new DeleteCommand(expectedIndex), command);
    }

    @Test
    public void parse_validRangeIndex_success() throws Exception {
        DeleteCommand command = parser.parse("2:5");
        MultiIndex expected = new MultiIndex(Index.fromOneBased(2), Index.fromOneBased(5));
        assertEquals(new DeleteCommand(expected), command);
    }

    @Test
    public void parse_invalidIndexFormat_throwsParseException() {
        // invalid because non-numeric
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse("a"));
        assertTrue(exception.getMessage().contains(MESSAGE_INVALID_INDEX));
    }

    @Test
    public void parse_invalidRange_throwsParseException() {
        // lower > upper triggers InvalidIndexException
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse("5:2"));
        assertTrue(exception.getMessage().contains("cannot be greater than upper bound"));
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(""));
        assertTrue(exception.getMessage().contains(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCommand.MESSAGE_USAGE)));
    }

    @Test
    public void parse_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }
}
