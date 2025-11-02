package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXAM_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.model.person.GradeMap.VALID_EXAM_NAMES;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.GradeTracker;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.InvalidExamNameException;

/**
 * Marks the specified exam as passed or failed
 * for one or more students in the address book.
 */
public class GradeCommand extends MultiIndexCommand {

    public static final String COMMAND_WORD = "grade";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the specified exam as passed or failed "
            + "for one or more students identified by their index numbers in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer or range X:Y) "
            + PREFIX_EXAM_NAME + "EXAM_NAME "
            + PREFIX_STATUS + "STATUS (y/n)\n"
            + "Example: " + COMMAND_WORD + " 1:3 " + PREFIX_EXAM_NAME + "midterm " + PREFIX_STATUS + "y\n"
            + "Example: " + COMMAND_WORD + " 2 " + PREFIX_EXAM_NAME + "pe1 " + PREFIX_STATUS + "n\n";

    public static final String MESSAGE_GRADE_SUCCESS = "%s marked as %s for: %s";
    public static final String MESSAGE_FAILURE_INVALID_NAME =
            "Exam name is invalid! Here are the valid exam names: %s";

    private final String examName;
    private final boolean isPassed;

    /**
     * Constructs a GradeCommand.
     *
     * @param multiIndex List of students to update.
     * @param examName   Name of the exam to mark.
     * @param isPassed   True if passed, false if failed.
     */
    public GradeCommand(MultiIndex multiIndex, String examName, boolean isPassed) {
        super(multiIndex);
        requireAllNonNull(multiIndex, examName);
        this.examName = examName.toLowerCase();
        this.isPassed = isPassed;
    }

    @Override
    protected Person applyActionToPerson(Model model, Person personToGrade) throws CommandException {
        GradeTracker updatedGradeTracker = personToGrade.getGradeTracker().copy();

        try {
            if (isPassed) {
                updatedGradeTracker.markExamPassed(examName);
            } else {
                updatedGradeTracker.markExamFailed(examName);
            }
            if (updatedGradeTracker.getExamMap().get(examName) == null) {
                throw new AssertionError("Updated GradeTracker should contain the graded exam");
            }
        } catch (InvalidExamNameException e) {
            throw new CommandException(String.format(
                    MESSAGE_FAILURE_INVALID_NAME,
                    Arrays.toString(VALID_EXAM_NAMES)
            ));
        }

        Person gradedPerson = new Person(
                personToGrade.getStudentId(),
                personToGrade.getName(),
                personToGrade.getPhone(),
                personToGrade.getEmail(),
                personToGrade.getTags(),
                personToGrade.getGithubUsername(),
                personToGrade.getExerciseTracker(),
                personToGrade.getLabAttendanceList(),
                updatedGradeTracker
        );

        model.setPerson(personToGrade, gradedPerson);
        return gradedPerson;
    }

    @Override
    protected CommandResult buildResult(List<Person> updatedPersons) {
        String affectedStudents = updatedPersons.stream()
                .map(Person::getNameAndID)
                .collect(Collectors.joining(", "));

        String resultString;

        if (isPassed) {
            resultString = "passed";
        } else {
            resultString = "failed";
        }

        return new CommandResult(String.format(
                MESSAGE_GRADE_SUCCESS,
                examName,
                resultString,
                affectedStudents
        ));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof GradeCommand)) {
            return false;
        }

        GradeCommand otherCommand = (GradeCommand) other;

        if (!multiIndex.equals(otherCommand.multiIndex)) {
            return false;
        }

        if (isPassed != otherCommand.isPassed) {
            return false;
        }

        return examName.equals(otherCommand.examName);
    }
}
