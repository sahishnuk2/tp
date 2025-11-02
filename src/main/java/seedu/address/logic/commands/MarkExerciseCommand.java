package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.person.ExerciseList.NUMBER_OF_EXERCISES;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.ExerciseTracker;
import seedu.address.model.person.Person;

/**
 * Marks a specific exercise as a given status for one or more students in the address book.
 */
public class MarkExerciseCommand extends MultiIndexCommand {

    public static final String COMMAND_WORD = "marke";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the exercise status of one or more persons "
            + "identified by their index numbers in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer or range X:Y) "
            + "ei/EXERCISE_NUMBER s/STATUS\n"
            + "Example: " + COMMAND_WORD + " 1:3 ei/1 s/y \n"
            + "Example: " + COMMAND_WORD + " 2 ei/3 s/y";

    public static final String MESSAGE_MARK_EXERCISE_SUCCESS =
            "Exercise %1$d marked as %2$s for: %3$s";
    public static final String MESSAGE_FAILURE_ALREADY_MARKED =
            "Exercise %1$d already marked as %2$s for %3$s";
    public static final String MESSAGE_INDEX_OUT_OF_BOUNDS =
            "The exercise index provided is invalid, index must be between 0 to 9 (inclusive)";

    private final MultiIndex studentIndex;
    private final Index exerciseIndex;
    private final boolean isDone;
    private final List<Person> alreadyMarkedPersons = new ArrayList<>();
    private final String action;

    /**
     * Constructs a command to mark an exercise as done or not done.
     */
    public MarkExerciseCommand(MultiIndex studentIndex, Index exerciseIndex, boolean isDone) {
        super(studentIndex);
        requireAllNonNull(studentIndex, exerciseIndex, isDone);
        this.studentIndex = studentIndex;
        this.exerciseIndex = exerciseIndex;
        this.isDone = isDone;
        this.action = isDone ? "done" : "not done"; // define action string
    }

    /**
     * Executes the command after validating the exercise index.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        // ensure exercise index is within bounds
        if (exerciseIndex.getZeroBased() < 0 || exerciseIndex.getZeroBased() >= NUMBER_OF_EXERCISES) {
            throw new CommandException(MESSAGE_INDEX_OUT_OF_BOUNDS);
        }
        // delegate to parent to handle iteration over student indices
        return super.execute(model);
    }

    /**
     * Applies marking action to a single student.
     */
    @Override
    protected Person applyActionToPerson(Model model, Person personToEdit) throws CommandException {
        ExerciseTracker updatedExerciseTracker = personToEdit.getExerciseTracker().copy();

        try {
            updatedExerciseTracker.markExercise(exerciseIndex, isDone); // mark exercise as done/undone
        } catch (IllegalStateException e) {
            alreadyMarkedPersons.add(personToEdit); // record if already marked
            return null;
        }

        // create updated person with modified tracker
        Person updatedPerson = new Person(
                personToEdit.getStudentId(),
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTags(),
                personToEdit.getGithubUsername(),
                updatedExerciseTracker,
                personToEdit.getLabAttendanceList(),
                personToEdit.getGradeTracker()
        );

        model.setPerson(personToEdit, updatedPerson); // update model
        return updatedPerson;
    }

    /**
     * Builds the final feedback message for the user.
     */
    @Override
    protected CommandResult buildResult(List<Person> updatedPersons) {
        return new CommandResult(generateResponseMessage(alreadyMarkedPersons, updatedPersons));
    }

    /**
     * Generates response message summarizing the command result.
     */
    private String generateResponseMessage(List<Person> alreadyMarkedPersons, List<Person> personsEdited) {
        String editedNames = personsEdited.stream()
                .map(Person::getNameAndID)
                .collect(Collectors.joining(", ")); // collect names of edited persons

        String alreadyMarkedMessage = compileAlreadyMarkedMessage(alreadyMarkedPersons); // message for duplicates
        StringBuilder message = new StringBuilder();

        if (!alreadyMarkedMessage.isEmpty()) {
            message.append(alreadyMarkedMessage).append("\n"); // add duplicate info first
        }

        if (!personsEdited.isEmpty()) {
            String successMessage = String.format(MESSAGE_MARK_EXERCISE_SUCCESS,
                    exerciseIndex.getZeroBased(), action, editedNames); // add success message
            message.append(successMessage);
        }

        return message.toString().trim(); // remove trailing newline
    }

    /**
     * Builds message for students whose exercises were already marked.
     */
    private String compileAlreadyMarkedMessage(List<Person> alreadyMarkedPersons) {
        if (alreadyMarkedPersons.isEmpty()) {
            return ""; // no message if none already marked
        }

        String names = alreadyMarkedPersons.stream()
                .map(Person::getNameAndID)
                .collect(Collectors.joining(", ")); // collect names

        return String.format(MESSAGE_FAILURE_ALREADY_MARKED,
                exerciseIndex.getZeroBased(), action, names); // return formatted warning
    }

    /**
     * Checks equality based on student indices, exercise index, and status.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MarkExerciseCommand)) {
            return false;
        }

        MarkExerciseCommand otherCommand = (MarkExerciseCommand) other;
        return studentIndex.equals(otherCommand.studentIndex)
                && exerciseIndex.equals(otherCommand.exerciseIndex)
                && isDone == otherCommand.isDone; // compare key fields
    }
}
