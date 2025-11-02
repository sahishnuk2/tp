package seedu.address.logic.commands;


import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.Week;
import seedu.address.model.person.ExerciseList;
import seedu.address.model.person.ExerciseTracker;
import seedu.address.model.person.LabList;
import seedu.address.model.person.Person;

/**
 * Sets the current week number for the semester.
 * Updates all students' lab attendance to reflect which labs are now past.
 */
public class SetWeekCommand extends Command {
    public static final String COMMAND_WORD = "set-week";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets the current week for the semester\n"
            + "Parameters: WEEK_NUMBER (must be between 0 and 13)\n"
            + "Example: " + COMMAND_WORD + " 7";

    public static final String MESSAGE_SUCCESS = "Current week set to: Week %1$d\n"
            + "Updated lab statuses for %2$d students.";

    private final Week currentWeek;

    /**
     * @param currentWeek current week of the semester
     */
    public SetWeekCommand(Week currentWeek) {
        requireNonNull(currentWeek);

        this.currentWeek = currentWeek;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        model.saveAddressBook(); // Save current state BEFORE making changes for undo functionality

        model.setCurrentWeek(currentWeek);
        int currentWeekNumber = currentWeek.getWeekNumber();

        LabList.setCurrentWeek(currentWeekNumber);
        ExerciseList.setCurrentWeek(currentWeekNumber);

        List<Person> allPersons = model.getAddressBook().getPersonList();
        int updatedCount = 0;

        for (Person person : allPersons) {
            LabList oldLabList = (LabList) person.getLabAttendanceList();
            LabList updatedLabList = oldLabList.copy();
            ExerciseTracker exerciseTracker = person.getExerciseTracker();
            ExerciseTracker updatedExerciseTracker = exerciseTracker.copy();
            Person updatedPerson = new Person(
                    person.getStudentId(),
                    person.getName(),
                    person.getPhone(),
                    person.getEmail(),
                    person.getTags(),
                    person.getGithubUsername(),
                    updatedExerciseTracker,
                    updatedLabList, // New LabList with updated weeks
                    person.getGradeTracker()
            );
            model.setPerson(person, updatedPerson);
            updatedCount++;
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, currentWeekNumber, updatedCount));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SetWeekCommand)) {
            return false;
        }

        SetWeekCommand otherCommand = (SetWeekCommand) other;
        return currentWeek.equals(otherCommand.currentWeek);
    }
}
