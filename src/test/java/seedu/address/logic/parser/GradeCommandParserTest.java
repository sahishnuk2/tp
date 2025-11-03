package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXAM_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.commands.GradeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Unit tests for {@link GradeCommandParser}.
 */
public class GradeCommandParserTest {

    private GradeCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new GradeCommandParser();
    }

    @Test
    public void parse_validInputSingleIndexPassed_success() throws Exception {
        String userInput = "1 " + PREFIX_EXAM_NAME + "pe1 " + PREFIX_STATUS + "y";
        GradeCommand command = parser.parse(userInput);

        MultiIndex expectedIndex = new MultiIndex(Index.fromOneBased(1));
        GradeCommand expectedCommand = new GradeCommand(expectedIndex, "pe1", true);

        assertEquals(expectedCommand, command);
    }


    @Test
    public void parse_validInputRangeFailed_success() throws Exception {
        String userInput = "2:4 " + PREFIX_EXAM_NAME + "midterm " + PREFIX_STATUS + "n";
        GradeCommand command = parser.parse(userInput);

        MultiIndex expectedIndex = new MultiIndex(Index.fromOneBased(2), Index.fromOneBased(4));
        GradeCommand expectedCommand = new GradeCommand(expectedIndex, "midterm", false);

        assertEquals(expectedCommand, command);
    }

    @Test
    public void parse_missingExamPrefix_throwsParseException() {
        String userInput = "1 " + PREFIX_STATUS + "y";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingStatusPrefix_throwsParseException() {
        String userInput = "1 " + PREFIX_EXAM_NAME + "pe1";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidStatusCharacter_throwsParseException() {
        String userInput = "1 " + PREFIX_EXAM_NAME + "pe1 " + PREFIX_STATUS + "maybe";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        String userInput = "zero " + PREFIX_EXAM_NAME + "pe1 " + PREFIX_STATUS + "y";

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_extraUnwantedPrefix_throwsParseException() {
        String userInput = "1 " + PREFIX_EXAM_NAME + "pe1 " + PREFIX_STATUS + "y t/tag";
        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(userInput));
        // MESSAGE_INVALID_PREFIX = "Invalid prefix(s) found: %s"
        assertEquals("Invalid prefix(s) found: t/", thrown.getMessage());
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        String userInput = "1 " + PREFIX_EXAM_NAME + "pe1 " + PREFIX_EXAM_NAME + "pe2 " + PREFIX_STATUS + "y";
        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(userInput));
        // From ArgumentMultimap.verifyNoDuplicatePrefixesFor
        String expectedMsg = "Multiple prefix(s) specified: en/";
        // Adjust expectedMsg if your helper uses a different duplicate error message
        assertEquals(expectedMsg, thrown.getMessage());
    }

    @Test
    public void parse_indexRangeInvalidBounds_throwsParseException() {
        String userInput = "5:2 " + PREFIX_EXAM_NAME + "pe1 " + PREFIX_STATUS + "y";
        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals("5:2 is invalid! Lower bound cannot be greater than upper bound", thrown.getMessage());
    }

    @Test
    public void parse_statusUppercase_success() throws Exception {
        String userInput = "3 " + PREFIX_EXAM_NAME + "final " + PREFIX_STATUS + "Y";
        GradeCommand command = parser.parse(userInput);

        MultiIndex expectedIndex = new MultiIndex(Index.fromOneBased(3));
        GradeCommand expectedCommand = new GradeCommand(expectedIndex, "final", true);

        assertEquals(expectedCommand, command);
    }

}
