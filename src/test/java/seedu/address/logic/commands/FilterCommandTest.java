package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.helpers.Comparison;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Status;
import seedu.address.model.person.predicates.filterpredicates.ExerciseStatusMatchesPredicate;
import seedu.address.model.person.predicates.filterpredicates.FilterCombinedPredicate;
import seedu.address.model.person.predicates.filterpredicates.FilterPredicate;
import seedu.address.model.person.predicates.filterpredicates.LabAttendanceMatchesPredicate;
import seedu.address.model.person.predicates.filterpredicates.LabStatusMatchesPredicate;

public class FilterCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }


    @Test
    public void equals_sameObject_returnsTrue() {
        FilterPredicate doneAtExerciseIndex0Predicate =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterCommand command = new FilterCommand(doneAtExerciseIndex0Predicate);
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_samePredicate_returnsTrue() {
        FilterPredicate doneAtExerciseIndex0Predicate =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterCommand left = new FilterCommand(doneAtExerciseIndex0Predicate);
        FilterCommand right = new FilterCommand(doneAtExerciseIndex0Predicate);
        assertTrue(left.equals(right));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        FilterPredicate doneAtExerciseIndex0Predicate =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterCommand command = new FilterCommand(doneAtExerciseIndex0Predicate);
        assertFalse(command.equals(1));
    }

    @Test
    public void equals_null_returnsFalse() {
        FilterPredicate doneAtExerciseIndex0Predicate =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterCommand command = new FilterCommand(doneAtExerciseIndex0Predicate);
        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentPredicate_returnsFalse() {
        FilterPredicate doneAtExerciseIndex0Predicate =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterPredicate absentAtLabIndex1Predicate =
                new LabStatusMatchesPredicate(Index.fromOneBased(1), "A");
        FilterCommand left = new FilterCommand(doneAtExerciseIndex0Predicate);
        FilterCommand right = new FilterCommand(absentAtLabIndex1Predicate);
        assertFalse(left.equals(right));
    }


    @Test
    public void toString_includesPredicateSummary() {
        FilterPredicate doneAtExerciseIndex0Predicate =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterCommand command = new FilterCommand(doneAtExerciseIndex0Predicate);
        String expectedString = FilterCommand.class.getCanonicalName()
                + "{predicate=" + doneAtExerciseIndex0Predicate + "}";
        assertEquals(expectedString, command.toString());
    }


    @Test
    public void execute_exerciseDone_filtersByExerciseIndexAndFormatsMessage() {
        FilterPredicate doneAtExerciseIndex0Predicate =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE);
        FilterCommand command = new FilterCommand(doneAtExerciseIndex0Predicate);

        CommandResult actualResult = command.execute(model);
        expectedModel.updateFilteredPersonList(doneAtExerciseIndex0Predicate);

        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());

        String expectedMessage = String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW
                        + "\nFiltering for students who "
                        + doneAtExerciseIndex0Predicate.successMessage()
                        + ".",
                expectedModel.getFilteredPersonList().size());
        assertEquals(expectedMessage, actualResult.getFeedbackToUser());
    }

    @Test
    public void execute_labAbsent_filtersByLabIndexAndFormatsMessage() {
        FilterPredicate absentAtLabIndex1Predicate =
                new LabStatusMatchesPredicate(Index.fromOneBased(1), "A");
        FilterCommand command = new FilterCommand(absentAtLabIndex1Predicate);

        CommandResult actualResult = command.execute(model);
        expectedModel.updateFilteredPersonList(absentAtLabIndex1Predicate);

        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());

        String expectedMessage = String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW
                        + "\nFiltering for students who "
                        + absentAtLabIndex1Predicate.successMessage()
                        + ".",
                expectedModel.getFilteredPersonList().size());
        assertEquals(expectedMessage, actualResult.getFeedbackToUser());
    }

    @Test
    public void execute_attendanceAtLeastSixtyPercent_filtersAndFormatsMessage() {
        FilterPredicate attendanceAtLeast60PercentPredicate =
                new LabAttendanceMatchesPredicate(60.0, Comparison.GREATER_THAN_OR_EQUAL);
        FilterCommand command = new FilterCommand(attendanceAtLeast60PercentPredicate);

        CommandResult actualResult = command.execute(model);
        expectedModel.updateFilteredPersonList(attendanceAtLeast60PercentPredicate);

        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());

        String expectedMessage = String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW
                        + "\nFiltering for students who "
                        + attendanceAtLeast60PercentPredicate.successMessage()
                        + ".",
                expectedModel.getFilteredPersonList().size());
        assertEquals(expectedMessage, actualResult.getFeedbackToUser());
    }

    @Test
    public void execute_threeFiltersAllConditionsMustMatch_messageListsCountAndCriteria() {
        List<FilterPredicate> combinedFilterPredicates = new ArrayList<>();
        FilterPredicate notDoneAtExerciseIndex0Predicate =
                new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.NOT_DONE);
        FilterPredicate absentAtLabIndex2Predicate =
                new LabStatusMatchesPredicate(Index.fromOneBased(2), "A");
        FilterPredicate attendanceAtLeast60PercentPredicate =
                new LabAttendanceMatchesPredicate(60.0, Comparison.GREATER_THAN_OR_EQUAL);

        combinedFilterPredicates.add(notDoneAtExerciseIndex0Predicate);
        combinedFilterPredicates.add(absentAtLabIndex2Predicate);
        combinedFilterPredicates.add(attendanceAtLeast60PercentPredicate);

        FilterPredicate combinedAllPredicate = new FilterCombinedPredicate(combinedFilterPredicates);
        FilterCommand command = new FilterCommand(combinedAllPredicate);

        CommandResult actualResult = command.execute(model);
        expectedModel.updateFilteredPersonList(combinedAllPredicate);

        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());

        String expectedMessage = String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW
                        + "\nFiltering for students who "
                        + combinedAllPredicate.successMessage()
                        + ".",
                expectedModel.getFilteredPersonList().size());
        assertEquals(expectedMessage, actualResult.getFeedbackToUser());
    }

    @Test
    public void execute_returnsNonNullCommandResult() {
        FilterPredicate attendanceAtMostEightyPercentPredicate =
                new LabAttendanceMatchesPredicate(80.0, Comparison.LESS_THAN_OR_EQUAL);
        FilterCommand command = new FilterCommand(attendanceAtMostEightyPercentPredicate);
        CommandResult result = command.execute(model);
        assertNotNull(result);
    }
}
