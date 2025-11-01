package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.Week;
import seedu.address.model.person.Person;
import seedu.address.model.person.sortcriterion.LabSortCriterion;
import seedu.address.model.person.sortcriterion.NameSortCriterion;
import seedu.address.model.person.sortcriterion.SortCriterion;
import seedu.address.model.person.sortcriterion.StudentIdSortCriterion;

public class SortCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private final SortCriterion nameSortCriterion = new NameSortCriterion();
    private final SortCriterion studentIdSortCriterion = new StudentIdSortCriterion();


    @Test
    public void execute_validCriterion_sortSuccessful() throws Exception {
        ModelStubSort modelStub = new ModelStubSort();
        SortCriterion nameCriterion = new NameSortCriterion();
        SortCriterion labCriterion = new LabSortCriterion();

        CommandResult commandResult = new SortCommand(nameCriterion).execute(modelStub);

        assertEquals(String.format(SortCommand.MESSAGE_SUCCESS, nameCriterion.getDisplayString()),
                commandResult.getFeedbackToUser());

        assertEquals(1, modelStub.getSortCallCount());

        commandResult = new SortCommand(labCriterion).execute(modelStub);

        assertEquals(String.format(SortCommand.MESSAGE_SUCCESS, labCriterion.getDisplayString()),
                commandResult.getFeedbackToUser());

        assertEquals(2, modelStub.getSortCallCount());

    }

    @Test
    public void equals() {
        final SortCommand standardCommand = new SortCommand(nameSortCriterion);

        // Same values
        SortCommand commandWithSameValues = new SortCommand(nameSortCriterion);
        assertEquals(standardCommand, commandWithSameValues);

        // Same object
        assertEquals(standardCommand, standardCommand);

        // Null
        assertNotEquals(standardCommand, null);

        // Different Command
        assertNotEquals(new ListCommand(), standardCommand);

        // Different criterion
        assertNotEquals(new SortCommand(studentIdSortCriterion), standardCommand);
    }

    /**
     * A Model stub that tracks calls to sortPersonList.
     */
    private class ModelStubSort extends ModelStub {
        private int sortCallCount = 0;

        @Override
        public void sortPersonList(Comparator<Person> comparator) {
            requireNonNull(comparator);
            sortCallCount++;
        }

        public int getSortCallCount() {
            return sortCallCount;
        }
    }
}
