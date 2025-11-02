package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.KEYWORD_123;
import static seedu.address.testutil.TestConstants.KEYWORD_456;
import static seedu.address.testutil.TestConstants.KEYWORD_999;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.predicates.findpredicates.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }


    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }


    @Test
    public void execute_singleKeyword_multiplePersonsFound() {
        NameContainsKeywordsPredicate predicate = preparePredicate(KEYWORD_123);
        FindCommand command = new FindCommand(predicate);

        CommandResult result = command.execute(model);
        expectedModel.updateFilteredPersonList(predicate);

        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());

        String expectedMessage = String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW + "\nSearching for " + predicate.successMessage(),
                expectedModel.getFilteredPersonList().size()
        );
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        NameContainsKeywordsPredicate predicate = preparePredicate(KEYWORD_123 + " " + KEYWORD_456);
        FindCommand command = new FindCommand(predicate);

        CommandResult result = command.execute(model);
        expectedModel.updateFilteredPersonList(predicate);

        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
        String expectedMessage = String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW + "\nSearching for " + predicate.successMessage(),
                expectedModel.getFilteredPersonList().size()
        );
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_noMatches_zeroPersonsFound() {
        NameContainsKeywordsPredicate predicate = preparePredicate(KEYWORD_999);
        FindCommand command = new FindCommand(predicate);

        CommandResult result = command.execute(model);
        expectedModel.updateFilteredPersonList(predicate);

        assertEquals(0, model.getFilteredPersonList().size());
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());

        String expectedMessage = String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW + "\nSearching for " + predicate.successMessage(),
                0
        );
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_returnsNonNullCommandResult() {
        NameContainsKeywordsPredicate predicate = preparePredicate(KEYWORD_456);
        FindCommand command = new FindCommand(predicate);
        CommandResult result = command.execute(model);
        assertNotNull(result);
    }


}
