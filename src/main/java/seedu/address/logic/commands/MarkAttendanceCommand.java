package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.LabList;
import seedu.address.model.person.Person;

/**
 * Marks the specified lab as attended or not attended for one or more persons in the address book.
 */
public class MarkAttendanceCommand extends MultiIndexCommand {

    public static final String COMMAND_WORD = "marka";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the specific lab of the person(s) identified "
            + "by the index number(s) used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer or range X:Y) "
            + PREFIX_LAB_NUMBER + "LAB_NUMBER "
            + PREFIX_STATUS + "STATUS\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_LAB_NUMBER + "1 " + PREFIX_STATUS + "y\n"
            + "Example: " + COMMAND_WORD + " 1:5 "
            + PREFIX_LAB_NUMBER + "1 " + PREFIX_STATUS + "y";

    public static final String MESSAGE_MARK_ATTENDANCE_SUCCESS =
            "Lab %1$d marked as attended for: %2$s";
    public static final String MESSAGE_MARK_ABSENCE_SUCCESS =
            "Lab %1$d marked as not attended for: %2$s";
    public static final String MESSAGE_FAILURE_ALREADY_ATTENDED =
            "Lab %1$d already marked as attended for %2$s";
    public static final String MESSAGE_FAILURE_ALREADY_NOT_ATTENDED =
            "Lab %1$d already marked as not attended for %2$s";

    private final Index labNumber;
    private final boolean isAttended;

    private final List<Person> alreadyMarkedPersons = new ArrayList<>();

    /**
     * @param multiIndex range or single index of persons in the filtered list
     * @param labNumber index of the lab number to be marked
     * @param isAttended attendance status to set
     */
    public MarkAttendanceCommand(MultiIndex multiIndex, Index labNumber, boolean isAttended) {
        super(multiIndex);
        requireAllNonNull(multiIndex, labNumber);
        this.labNumber = labNumber;
        this.isAttended = isAttended;
    }

    @Override
    protected Person applyActionToPerson(Model model, Person personToEdit) throws CommandException {
        LabAttendanceList labAttendanceList = ((LabList) personToEdit.getLabAttendanceList())
                .copy();

        try {
            if (isAttended) {
                labAttendanceList.markLabAsAttended(labNumber.getZeroBased());
            } else {
                labAttendanceList.markLabAsAbsent(labNumber.getZeroBased());
            }
        } catch (IllegalStateException e) {
            alreadyMarkedPersons.add(personToEdit);
            return null;
        }

        Person editedPerson = new Person(
                personToEdit.getStudentId(), personToEdit.getName(), personToEdit.getPhone(),
                personToEdit.getEmail(), personToEdit.getTags(),
                personToEdit.getGithubUsername(), personToEdit.getExerciseTracker(),
                labAttendanceList, personToEdit.getGradeTracker());

        model.setPerson(personToEdit, editedPerson);
        return editedPerson;
    }

    @Override
    protected CommandResult buildResult(List<Person> updatedPersons) {
        return new CommandResult(generateResponseMessage(alreadyMarkedPersons, updatedPersons));
    }

    private String generateResponseMessage(List<Person> alreadyMarkedPersons, List<Person> personsEdited) {
        String studentNamesEdited = personsEdited.stream()
                .map(Person::getNameAndID)
                .collect(Collectors.joining(", "));

        String exceptionMessage = compileExceptionMessage(alreadyMarkedPersons);
        StringBuilder result = new StringBuilder();

        if (!exceptionMessage.isEmpty()) {
            result.append(exceptionMessage).append("\n");
        }

        if (!personsEdited.isEmpty()) {
            String successMessage = isAttended
                    ? String.format(MESSAGE_MARK_ATTENDANCE_SUCCESS,
                    labNumber.getOneBased(), studentNamesEdited)
                    : String.format(MESSAGE_MARK_ABSENCE_SUCCESS,
                    labNumber.getOneBased(), studentNamesEdited);
            result.append(successMessage);
        }

        return result.toString().trim();
    }

    private String compileExceptionMessage(List<Person> alreadyMarkedPersons) {
        if (alreadyMarkedPersons.isEmpty()) {
            return "";
        }

        String names = alreadyMarkedPersons.stream()
                .map(Person::getNameAndID)
                .collect(Collectors.joining(", "));

        return isAttended
                ? String.format(MESSAGE_FAILURE_ALREADY_ATTENDED, labNumber.getOneBased(), names)
                : String.format(MESSAGE_FAILURE_ALREADY_NOT_ATTENDED, labNumber.getOneBased(), names);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MarkAttendanceCommand)) {
            return false;
        }
        MarkAttendanceCommand otherCommand = (MarkAttendanceCommand) other;
        return multiIndex.equals(otherCommand.multiIndex)
                && labNumber.equals(otherCommand.labNumber)
                && isAttended == otherCommand.isAttended;
    }
}
