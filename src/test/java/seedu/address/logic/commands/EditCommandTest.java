package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(new MultiIndex(INDEX_FIRST_PERSON), descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        Person editedPerson = new PersonBuilder(lastPerson)
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();

        EditCommand editCommand = new EditCommand(new MultiIndex(indexLastPerson), descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIndices_successWithPartialChanges() {
        // Apply same edit to multiple students, but only some fields actually change
        MultiIndex multiIndex = new MultiIndex(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTags(VALID_TAG_HUSBAND)
                .build();

        EditCommand editCommand = new EditCommand(multiIndex, descriptor);

        Person firstEdited = new PersonBuilder(model.getFilteredPersonList().get(0))
                .withTags(VALID_TAG_HUSBAND).build();
        Person secondEdited = new PersonBuilder(model.getFilteredPersonList().get(1))
                .withTags(VALID_TAG_HUSBAND).build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), firstEdited);
        expectedModel.setPerson(model.getFilteredPersonList().get(1), secondEdited);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(firstEdited) + "\n" + Messages.format(secondEdited));

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_failure() {
        EditCommand editCommand = new EditCommand(new MultiIndex(INDEX_FIRST_PERSON), new EditPersonDescriptor());
        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(
                new MultiIndex(INDEX_FIRST_PERSON),
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build()
        );

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(new MultiIndex(INDEX_SECOND_PERSON), descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(
                new MultiIndex(INDEX_FIRST_PERSON),
                new EditPersonDescriptorBuilder(personInList).build()
        );

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(new MultiIndex(outOfBoundIndex), descriptor);

        String expectedMessage = String.format(
                Messages.MESSAGE_INVALID_INDEX_FORMAT,
                outOfBoundIndex.getOneBased(),
                "student",
                1,
                model.getFilteredPersonList().size()
        );

        assertCommandFailure(editCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(
                new MultiIndex(outOfBoundIndex),
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build()
        );

        String expectedMessage = String.format(Messages.MESSAGE_INVALID_INDEX_FORMAT, 2, "student", 1, 1);
        assertCommandFailure(editCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(new MultiIndex(INDEX_FIRST_PERSON), DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(new MultiIndex(INDEX_FIRST_PERSON), copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different type -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(new MultiIndex(INDEX_SECOND_PERSON), DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(new MultiIndex(INDEX_FIRST_PERSON), DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(new MultiIndex(index), descriptor);

        String expected = EditCommand.class.getCanonicalName()
                + "{multiIndex=" + new MultiIndex(index)
                + ", editPersonDescriptor=" + descriptor + "}";
        assertEquals(expected, editCommand.toString());
    }
}
