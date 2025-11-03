package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUB_USERNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_LAB_INDEX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_MULTIINDEX_BOUNDS;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_PREFIX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_STATUS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LAB;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.MarkAttendanceCommand;

public class MarkAttendanceCommandParserTest {
    private MarkAttendanceCommandParser parser = new MarkAttendanceCommandParser();
    private final Index validLabNumber = INDEX_FIRST_LAB;

    @Test
    public void parse_singleIndexAndLabSpecified_success() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased()
                + " " + PREFIX_STATUS + "y";
        MarkAttendanceCommand expectedCommand = new MarkAttendanceCommand(
                new MultiIndex(INDEX_FIRST_PERSON), validLabNumber, true);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multiIndexAndLabSpecified_success() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + ":" + INDEX_SECOND_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased() + " " + PREFIX_STATUS + "y";
        MarkAttendanceCommand expectedCommand = new MarkAttendanceCommand(
                new MultiIndex(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON), validLabNumber, true);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAttendanceCommand.MESSAGE_USAGE);

        // No Parameters
        assertParseFailure(parser, MarkAttendanceCommand.COMMAND_WORD, expectedMessage);

        // No index
        assertParseFailure(parser, MarkAttendanceCommand.COMMAND_WORD
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased(), expectedMessage);

        //No Lab Number
        assertParseFailure(parser, MarkAttendanceCommand.COMMAND_WORD
                + " " + INDEX_FIRST_PERSON.getOneBased(), expectedMessage);
    }

    @Test
    public void parse_invalidLabNumber_parseException() {
        String expectedMessage = MESSAGE_INVALID_LAB_INDEX;

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + 11 + " " + PREFIX_STATUS + "y", expectedMessage);

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + 0 + " " + PREFIX_STATUS + "y", expectedMessage);

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + -1 + " " + PREFIX_STATUS + "y", expectedMessage);

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + " " + PREFIX_STATUS + "y", expectedMessage);

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + "abc" + " " + PREFIX_STATUS + "y", expectedMessage);
    }


    @Test
    public void parse_invalidIndex_parseException() {
        String expectedMessage = MESSAGE_INVALID_INDEX;

        assertParseFailure(parser, 0
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased() + " " + PREFIX_STATUS + "y", expectedMessage);

        assertParseFailure(parser, "abc"
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased() + " " + PREFIX_STATUS + "y", expectedMessage);

        assertParseFailure(parser, "0:1"
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased() + " " + PREFIX_STATUS + "y", expectedMessage);

    }

    @Test
    public void parse_invalidIndexRange_parseException() {
        String invalidRange = "2:1";

        assertParseFailure(parser, invalidRange
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased()
                + " " + PREFIX_STATUS + "y", String.format(MESSAGE_INVALID_MULTIINDEX_BOUNDS, invalidRange));

    }

    @Test
    public void parse_invalidStatus_parseException() {
        String expectedMessage = MESSAGE_INVALID_STATUS;

        // Empty status
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased()
                + " " + PREFIX_STATUS, expectedMessage);

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased()
                + " " + PREFIX_STATUS + "x", expectedMessage);

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased()
                + " " + PREFIX_STATUS + "   ", expectedMessage);
    }

    @Test
    public void parse_extraPrefix_parseException() {
        String expectedMessage = MESSAGE_INVALID_PREFIX;

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_LAB_NUMBER
                + validLabNumber.getOneBased() + " " + PREFIX_STATUS + "y"
                + " " + PREFIX_NAME + " Ed", String.format(expectedMessage, PREFIX_NAME));

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_LAB_NUMBER
                + validLabNumber.getOneBased() + " " + PREFIX_GITHUB_USERNAME + " Ed" + " " + PREFIX_STATUS + "y",
                String.format(expectedMessage, PREFIX_GITHUB_USERNAME));
    }

    @Test
    public void parse_duplicatePrefix_parseException() {
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased()
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased() + " " + PREFIX_STATUS + "y",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LAB_NUMBER));

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased()
                + " " + PREFIX_STATUS + "y" + " " + PREFIX_STATUS + "y",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STATUS));

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased()
                        + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased()
                        + " " + PREFIX_STATUS + "y" + " " + PREFIX_STATUS + "y",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STATUS, PREFIX_LAB_NUMBER));
    }


}
