package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TestConstants.KEYWORD_123;
import static seedu.address.testutil.TestConstants.KEYWORD_456;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.predicates.findpredicates.EmailContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.GithubContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.StudentIdContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.TagContainsKeywordsPredicate;

public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    // No selectors, search all fields
    @Test
    public void parse_keywordsOnly_allFieldsSelected() {
        String userInput = KEYWORD_123 + "   " + KEYWORD_456 + "   ";
        List<String> keywords = Arrays.asList(KEYWORD_123, KEYWORD_456);

        FindCommand expected = new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList(
                new StudentIdContainsKeywordsPredicate(keywords),
                new NameContainsKeywordsPredicate(keywords),
                new EmailContainsKeywordsPredicate(keywords),
                new GithubContainsKeywordsPredicate(keywords),
                new PhoneContainsKeywordsPredicate(keywords),
                new TagContainsKeywordsPredicate(keywords)
        )));

        assertParseSuccess(parser, userInput, expected);
    }

    // Single selector n/
    @Test
    public void parse_withSingleSelector_onlyThatFieldSelected() {
        String userInput = KEYWORD_123 + " n/";
        List<String> keywords = List.of(KEYWORD_123);

        FindCommand expected = new FindCommand(new PersonContainsKeywordsPredicate(List.of(
                new NameContainsKeywordsPredicate(keywords)
        )));

        assertParseSuccess(parser, userInput, expected);
    }

    // Multiple selectors n/ e/
    @Test
    public void parse_withMultipleSelectors_exactFieldsSelected() {
        String userInput = KEYWORD_123 + "   n/   e/";
        List<String> keywords = List.of(KEYWORD_123);

        FindCommand expected = new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList(
                new NameContainsKeywordsPredicate(keywords),
                new EmailContainsKeywordsPredicate(keywords)
        )));

        assertParseSuccess(parser, userInput, expected);
    }


    @Test
    public void parse_withIrregularSpacing_stillParses() {
        String userInput = "  " + KEYWORD_123 + "    t/    ";
        List<String> keywords = List.of(KEYWORD_123);

        FindCommand expected = new FindCommand(new PersonContainsKeywordsPredicate(List.of(
                new TagContainsKeywordsPredicate(keywords)
        )));

        assertParseSuccess(parser, userInput, expected);
    }


    @Test
    public void parse_noKeywords_throwsParseException() {
        String userInput = "  n/ e/   ";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }


    @Test
    public void parse_selectorWithValue_throwsParseException() {
        String userInput = KEYWORD_123 + " n/Alice";
        assertParseFailure(parser, userInput,
                "Field selectors must be empty (e.g., 'n/' not 'n/alice').");
    }


    @Test
    public void parse_duplicateSelectors_throwsParseException() {
        String userInput = KEYWORD_123 + " p/ p/";
        String expectedMessage = Messages.getErrorMessageForDuplicateFindSelectors(PREFIX_PHONE);
        assertParseFailure(parser, userInput, expectedMessage);
    }

    // Multiple different selectors duplicated
    @Test
    public void parse_duplicateMultipleSelectors_throwsParseException() {
        String userInput = KEYWORD_123 + " n/ n/ e/ e/";
        String expectedMessage = Messages.getErrorMessageForDuplicateFindSelectors(PREFIX_NAME, PREFIX_EMAIL);
        assertParseFailure(parser, userInput, expectedMessage);
    }


    @Test
    public void parse_allSelectorsPresent_selectsAll() {
        String userInput = KEYWORD_456 + " i/ n/ e/ g/ p/ t/";
        List<String> keywords = List.of(KEYWORD_456);

        FindCommand expected = new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList(
                new StudentIdContainsKeywordsPredicate(keywords),
                new NameContainsKeywordsPredicate(keywords),
                new EmailContainsKeywordsPredicate(keywords),
                new GithubContainsKeywordsPredicate(keywords),
                new PhoneContainsKeywordsPredicate(keywords),
                new TagContainsKeywordsPredicate(keywords)
        )));

        assertParseSuccess(parser, userInput, expected);
    }

    // Mixed-case keyword preserved
    @Test
    public void parse_mixedCaseKeywords_preserved() throws ParseException {
        String userInput = "AlIcE n/";
        List<String> keywords = List.of("AlIcE");

        FindCommand expected = new FindCommand(new PersonContainsKeywordsPredicate(List.of(
                new NameContainsKeywordsPredicate(keywords)
        )));

        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_onlyWhitespace_throwsParseException() {
        String userInput = "    ";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }
}
