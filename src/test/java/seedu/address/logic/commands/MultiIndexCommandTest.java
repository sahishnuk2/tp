package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_HUNDRED_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Tests for {@link MultiIndexCommand}.
 */
public class MultiIndexCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    // -------------------------------------------------------------------------
    // Successful execution
    // -------------------------------------------------------------------------
    @Test
    public void execute_validRange_success() throws CommandException {
        MultiIndex multiIndex = new MultiIndex(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        StubMultiIndexCommand command = new StubMultiIndexCommand(multiIndex);

        List<Person> originalList = List.copyOf(model.getFilteredPersonList());

        String expectedMessage = String.format("Updated %d students successfully.",
                multiIndex.toIndexList().size());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // confirm the same subset was processed
        assertEquals(List.of(originalList.get(0), originalList.get(1)), command.getProcessedPersons());
    }

    // -------------------------------------------------------------------------
    // Invalid index
    // -------------------------------------------------------------------------
    @Test
    public void execute_outOfBoundsIndex_throwsCommandException() {
        MultiIndex multiIndex = new MultiIndex(INDEX_HUNDRED_PERSON);
        StubMultiIndexCommand command = new StubMultiIndexCommand(multiIndex);

        CommandException thrown = assertThrows(
                CommandException.class, () -> command.execute(model)
        );

        String expectedMessage = String.format(
                Messages.MESSAGE_INVALID_INDEX_FORMAT,
                INDEX_HUNDRED_PERSON.getOneBased(),
                "student",
                1,
                model.getFilteredPersonList().size()
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    // -------------------------------------------------------------------------
    // equals()
    // -------------------------------------------------------------------------
    @Test
    public void equals() {
        StubMultiIndexCommand standardCommand =
                new StubMultiIndexCommand(new MultiIndex(INDEX_FIRST_PERSON));

        // same values -> true
        StubMultiIndexCommand commandWithSameValues =
                new StubMultiIndexCommand(new MultiIndex(INDEX_FIRST_PERSON));
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> true
        assertEquals(standardCommand, standardCommand);

        // null -> false
        assertNotEquals(null, standardCommand);

        // different type -> false
        assertNotEquals(new ClearCommand(), standardCommand);

        // different range -> false
        assertNotEquals(
                new StubMultiIndexCommand(new MultiIndex(INDEX_SECOND_PERSON)),
                standardCommand);
    }

    // -------------------------------------------------------------------------
    // Stub class
    // -------------------------------------------------------------------------
    /**
     * A minimal concrete subclass of MultiIndexCommand for testing.
     * Simply records which persons were processed.
     */
    private static class StubMultiIndexCommand extends MultiIndexCommand {
        private final List<Person> processedPersons = new java.util.ArrayList<>();

        StubMultiIndexCommand(MultiIndex multiIndex) {
            super(multiIndex);
        }

        @Override
        protected Person applyActionToPerson(Model model, Person person) {
            processedPersons.add(person);
            // Return the same person to simulate a successful edit
            return person;
        }

        @Override
        protected CommandResult buildResult(List<Person> updatedPersons) {
            return new CommandResult(String.format(
                    "Updated %d students successfully.", updatedPersons.size()));
        }

        public List<Person> getProcessedPersons() {
            return processedPersons;
        }
    }
}
