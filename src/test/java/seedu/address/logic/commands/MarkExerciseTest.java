package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EXERCISE;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_HUNDRED_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EXERCISE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ExerciseTracker;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class MarkExerciseTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_markExercise_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Create updated exercise tracker with first exercise marked done
        ExerciseTracker updatedTracker = firstPerson.getExerciseTracker().copy();
        updatedTracker.markExercise(INDEX_FIRST_EXERCISE, true);

        Person editedPerson = new PersonBuilder(firstPerson)
                .withExerciseList(updatedTracker.toString()).build();

        MarkExerciseCommand markExerciseCommand = new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON), INDEX_FIRST_EXERCISE, true);

        String expectedMessage = String.format(
                MarkExerciseCommand.MESSAGE_MARK_EXERCISE_SUCCESS,
                INDEX_FIRST_EXERCISE.getZeroBased(),
                "done",
                firstPerson.getNameAndID());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(markExerciseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        MarkExerciseCommand markExerciseCommand = new MarkExerciseCommand(
                new MultiIndex(INDEX_HUNDRED_PERSON), INDEX_FIRST_EXERCISE, true);
        assertThrows(CommandException.class, () -> markExerciseCommand.execute(model));
    }

    @Test
    public void execute_invalidExerciseIndex_throwsCommandException() {
        // Out of bounds (assuming NUMBER_OF_EXERCISES = 10)
        Index invalidExerciseIndex = Index.fromZeroBased(999);
        MarkExerciseCommand markExerciseCommand = new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON), invalidExerciseIndex, true);
        assertThrows(CommandException.class, () -> markExerciseCommand.execute(model));
    }

    @Test
    public void execute_exerciseAlreadyMarked_compilesMessageSuccessfully() throws CommandException {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ExerciseTracker tracker = firstPerson.getExerciseTracker().copy();
        tracker.markExercise(INDEX_FIRST_EXERCISE, true);

        Person editedPerson = new PersonBuilder(firstPerson)
                .withExerciseList(tracker.toString()).build();
        model.setPerson(firstPerson, editedPerson);

        MarkExerciseCommand markExerciseCommand = new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON), INDEX_FIRST_EXERCISE, true);

        CommandResult result = markExerciseCommand.execute(model);
        String expectedMessage = String.format(
                MarkExerciseCommand.MESSAGE_FAILURE_ALREADY_MARKED,
                INDEX_FIRST_EXERCISE.getZeroBased(),
                "done",
                firstPerson.getNameAndID());

        assertEquals(expectedMessage.trim(), result.getFeedbackToUser().trim());
    }

    @Test
    public void equals() {
        final MarkExerciseCommand standardCommand = new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON), INDEX_FIRST_EXERCISE, true);

        // same values -> returns true
        MarkExerciseCommand commandWithSameValues = new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON), INDEX_FIRST_EXERCISE, true);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different type -> returns false
        assertNotEquals(new ClearCommand(), standardCommand);

        // different person index -> returns false
        assertNotEquals(new MarkExerciseCommand(
                new MultiIndex(INDEX_SECOND_PERSON), INDEX_FIRST_EXERCISE, true), standardCommand);

        // different exercise index -> returns false
        assertNotEquals(new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON), INDEX_SECOND_EXERCISE, true), standardCommand);

        // different completion status -> returns false
        assertNotEquals(new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON), INDEX_FIRST_EXERCISE, false), standardCommand);
    }
}
