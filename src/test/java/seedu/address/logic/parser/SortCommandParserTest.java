package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_FIELDS;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_CRITERION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.model.person.sortcriterion.NameSortCriterion;
import seedu.address.model.person.sortcriterion.SortCriterion;
import seedu.address.model.person.sortcriterion.StudentIdSortCriterion;

public class SortCommandParserTest {
    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validSortCriterion_success() {
        // Test with "name" criterion
        String userInput = " " + PREFIX_SORT_CRITERION + "name";
        SortCommand expectedCommand = new SortCommand(new NameSortCriterion());
        assertParseSuccess(parser, userInput, expectedCommand);

        // Test with "id" criterion
        userInput = " " + PREFIX_SORT_CRITERION + "id";
        expectedCommand = new SortCommand(new StudentIdSortCriterion());
        assertParseSuccess(parser, userInput, expectedCommand);

        // Test with uppercase
        userInput = " " + PREFIX_SORT_CRITERION + "NAME";
        expectedCommand = new SortCommand(new NameSortCriterion());
        assertParseSuccess(parser, userInput, expectedCommand);

        // Test with extra whitespace
        userInput = " " + PREFIX_SORT_CRITERION + "  name  ";
        expectedCommand = new SortCommand(new NameSortCriterion());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidSortCriterion_failure() {
        String expectedMessage = SortCriterion.MESSAGE_CONSTRAINTS;

        // Invalid criterion keyword
        assertParseFailure(parser, " " + PREFIX_SORT_CRITERION + "email", expectedMessage);
        assertParseFailure(parser, " " + PREFIX_SORT_CRITERION + "phone", expectedMessage);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE);

        // No parameters at all
        assertParseFailure(parser, "", expectedMessage);

        // Only whitespace
        assertParseFailure(parser, "     ", expectedMessage);

        // Missing criterion value (empty after prefix)
        assertParseFailure(parser, " " + PREFIX_SORT_CRITERION, SortCriterion.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicateFields_failure() {
        String expectedMessage = MESSAGE_DUPLICATE_FIELDS + PREFIX_SORT_CRITERION;

        // Double criterion parameter
        assertParseFailure(parser, " " + PREFIX_SORT_CRITERION + "id" + " " + PREFIX_SORT_CRITERION + "name" ,
                expectedMessage);
        assertParseFailure(parser, " " + PREFIX_SORT_CRITERION + "name" + " " + PREFIX_SORT_CRITERION + "lab",
                expectedMessage);
        assertParseFailure(parser, " " + PREFIX_SORT_CRITERION + "name" + " " + PREFIX_STATUS + "lab",
                "Invalid prefix(s) found: s/");
    }
}
