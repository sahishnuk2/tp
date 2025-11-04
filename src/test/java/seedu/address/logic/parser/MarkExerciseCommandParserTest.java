package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUB_USERNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_EXERCISE_INDEX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_MULTIINDEX_BOUNDS;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_PREFIX;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_STATUS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.MarkExerciseCommand;

/**
 * Unit tests for MarkExerciseCommandParser.
 */
public class MarkExerciseCommandParserTest {

    private final MarkExerciseCommandParser parser = new MarkExerciseCommandParser();
    private final Index validExerciseIndex = Index.fromZeroBased(0);

    // ========================= VALID CASES ============================= //

    @Test
    public void parse_singleIndexAndExerciseSpecified_success() {
        String userInput = INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                + " " + PREFIX_STATUS + "y";

        MarkExerciseCommand expectedCommand = new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON),
                validExerciseIndex,
                true
        );

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multiIndexAndExerciseSpecified_success() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + ":" + INDEX_SECOND_PERSON.getOneBased()
                + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                + " " + PREFIX_STATUS + "n";

        MarkExerciseCommand expectedCommand = new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON),
                validExerciseIndex,
                false
        );

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // ========================= MISSING FIELDS ============================= //

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedUsage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkExerciseCommand.MESSAGE_USAGE);

        // No parameters at all → usage message
        assertParseFailure(parser, MarkExerciseCommand.COMMAND_WORD, expectedUsage);

        // Missing index → this triggers parseIndex(), so expect index error
        assertParseFailure(parser, MarkExerciseCommand.COMMAND_WORD
                + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                + " " + PREFIX_STATUS + "y", MESSAGE_INVALID_INDEX);

        // Missing exercise index → usage message
        assertParseFailure(parser, MarkExerciseCommand.COMMAND_WORD
                + " " + INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_STATUS + "y", expectedUsage);

        // Missing status → usage message
        assertParseFailure(parser, MarkExerciseCommand.COMMAND_WORD
                        + " " + INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased(),
                expectedUsage);
    }


    // ========================= INVALID INDEXES ============================= //

    @Test
    public void parse_invalidIndex_parseException() {
        String expectedMessage = MESSAGE_INVALID_INDEX;

        // Invalid student indices
        assertParseFailure(parser, "0 "
                + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                + " " + PREFIX_STATUS + "y", expectedMessage);

        assertParseFailure(parser, "abc "
                + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                + " " + PREFIX_STATUS + "y", expectedMessage);

        assertParseFailure(parser, "0:1 "
                + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                + " " + PREFIX_STATUS + "y", expectedMessage);
    }
    @Test
    public void parse_statusNotDone_success() {
        String userInput = INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                + " " + PREFIX_STATUS + "n";
        MarkExerciseCommand expectedCommand = new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON),
                validExerciseIndex,
                false
        );
        assertParseSuccess(parser, userInput, expectedCommand);
    }


    @Test
    public void parse_invalidRange_parseException() {
        String invalidRange = "3:1";
        assertParseFailure(parser, invalidRange
                        + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                        + " " + PREFIX_STATUS + "n",
                String.format(MESSAGE_INVALID_MULTIINDEX_BOUNDS, invalidRange));
    }

    // ========================= INVALID EXERCISE INDEX ============================= //

    @Test
    public void parse_invalidExerciseIndex_parseException() {
        // Too high
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + 999
                        + " " + PREFIX_STATUS + "y",
                "Exercise index is invalid! It must be between 0 and 9 (inclusive).");

        // Negative
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + -1
                        + " " + PREFIX_STATUS + "y",
                "Exercise index is invalid! It must be between 0 and 9 (inclusive).");

        // Non-integer
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + "abc"
                        + " " + PREFIX_STATUS + "y",
                "Exercise index is invalid! It must be between 0 and 9 (inclusive).");
    }

    // ========================= INVALID STATUS ============================= //

    @Test
    public void parse_invalidStatus_parseException() {
        String expectedMessage = MESSAGE_INVALID_STATUS;

        // Empty status
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                + " " + PREFIX_STATUS, expectedMessage);

        // Invalid character
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                + " " + PREFIX_STATUS + "x", expectedMessage);

        // Whitespaces
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                + " " + PREFIX_STATUS + "   ", expectedMessage);
    }

    // ========================= DUPLICATE / EXTRA PREFIXES ============================= //

    @Test
    public void parse_extraPrefix_parseException() {
        String expectedMessage = MESSAGE_INVALID_PREFIX;

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                        + " " + PREFIX_STATUS + "y"
                        + " " + PREFIX_NAME + " Ed",
                String.format(expectedMessage, PREFIX_NAME));

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                        + " " + PREFIX_STATUS + "y"
                        + " " + PREFIX_GITHUB_USERNAME + " johndoe",
                String.format(expectedMessage, PREFIX_GITHUB_USERNAME));

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                        + " " + PREFIX_LAB_NUMBER + " 1"
                        + " " + PREFIX_STATUS + "y",
                String.format(expectedMessage, PREFIX_LAB_NUMBER));
    }

    @Test
    public void parse_duplicatePrefix_parseException() {
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                        + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                        + " " + PREFIX_STATUS + "y",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EXERCISE_INDEX));

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                        + " " + PREFIX_STATUS + "y"
                        + " " + PREFIX_STATUS + "n",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STATUS));

        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                        + " " + PREFIX_EXERCISE_INDEX + validExerciseIndex.getZeroBased()
                        + " " + PREFIX_STATUS + "y"
                        + " " + PREFIX_STATUS + "n",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STATUS, PREFIX_EXERCISE_INDEX));
    }
    @Test
    public void parse_emptyExercisePrefix_failure() {
        String expectedMessage = "Exercise index is invalid! It must be between 0 and 9 (inclusive).";
        assertParseFailure(parser,
                INDEX_FIRST_PERSON.getOneBased() + " "
                        + PREFIX_EXERCISE_INDEX + " "
                        + PREFIX_STATUS + "y",
                expectedMessage);
    }


    @Test
    public void parse_illegalExerciseIndex_failure() {
        String expectedMessage = MESSAGE_INVALID_EXERCISE_INDEX;

        assertParseFailure(parser,
                INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_EXERCISE_INDEX + "@# "
                        + PREFIX_STATUS + "y",
                expectedMessage);
    }

}
