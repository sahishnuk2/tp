package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUB_USERNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.MultiIndex;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.ExerciseTracker;
import seedu.address.model.person.GithubUsername;
import seedu.address.model.person.GradeTracker;
import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of one or more existing students in LambdaLab.
 */
public class EditCommand extends MultiIndexCommand {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of one or more students "
            + "identified by their index numbers in the displayed student list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: (must be a positive integer or range X:Y) "
            + "[" + PREFIX_STUDENTID + "STUDENT_ID] "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_GITHUB_USERNAME + "GITHUB_USERNAME] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 5 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com \n"
            + "Example: " + COMMAND_WORD + " 1:2 " + PREFIX_TAG + "struggling";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Student(s):\n%1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This student already exists in LambdaLab.";

    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param multiIndex of the persons in the filtered person list to edit
     * @param editPersonDescriptor details to edit the persons with
     */
    public EditCommand(MultiIndex multiIndex, EditPersonDescriptor editPersonDescriptor) {
        super(multiIndex);
        requireNonNull(editPersonDescriptor);
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    protected Person applyActionToPerson(Model model, Person personToEdit) throws CommandException {
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        if (personToEdit.equals(editedPerson)) {
            throw new CommandException("No changes were made, all fields are identical.");
        }
        model.setPerson(personToEdit, editedPerson);
        return editedPerson;
    }

    @Override
    protected CommandResult buildResult(List<Person> updatedPersons) {
        StringBuilder sb = new StringBuilder();
        for (Person p : updatedPersons) {
            sb.append(Messages.format(p)).append("\n");
        }
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, sb.toString().trim()));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        StudentId updatedStudentId = editPersonDescriptor.getStudentId().orElse(personToEdit.getStudentId());
        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        GithubUsername updatedGithubUsername = editPersonDescriptor.getGithubUsername()
                .orElse(personToEdit.getGithubUsername());
        ExerciseTracker updatedExerciseTracker = editPersonDescriptor.getExerciseTracker()
                .orElse(personToEdit.getExerciseTracker());
        LabAttendanceList updatedLabAttendanceList = editPersonDescriptor.getLabAttendanceList()
                .orElse(personToEdit.getLabAttendanceList());
        GradeTracker updatedGradeTracker = editPersonDescriptor.getGradeTracker()
                .orElse(personToEdit.getGradeTracker());
        return new Person(updatedStudentId, updatedName, updatedPhone, updatedEmail,
                updatedTags, updatedGithubUsername,
                updatedExerciseTracker, updatedLabAttendanceList, updatedGradeTracker);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand e = (EditCommand) other;
        return multiIndex.equals(e.multiIndex)
                && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("multiIndex", multiIndex)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private StudentId studentId;
        private Name name;
        private Phone phone;
        private Email email;
        private Set<Tag> tags;
        private GithubUsername githubUsername;
        private ExerciseTracker exerciseTracker;
        private LabAttendanceList labAttendanceList;
        private GradeTracker gradeTracker;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setStudentId(toCopy.studentId);
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setTags(toCopy.tags);
            setGithubUsername(toCopy.githubUsername);
            setExerciseTracker(toCopy.exerciseTracker);
            setLabAttendanceList(toCopy.labAttendanceList);
            setGradeTracker(toCopy.gradeTracker);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(studentId, name, phone, email, tags, githubUsername);
        }

        public void setStudentId(StudentId studentId) {
            this.studentId = studentId;
        }

        public Optional<StudentId> getStudentId() {
            return Optional.ofNullable(studentId);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setExerciseTracker(ExerciseTracker exerciseTracker) {
            this.exerciseTracker = exerciseTracker;
        }

        public Optional<ExerciseTracker> getExerciseTracker() {
            return Optional.ofNullable(exerciseTracker);
        }

        public void setLabAttendanceList(LabAttendanceList labAttendanceList) {
            this.labAttendanceList = labAttendanceList;
        }

        public void setGradeTracker(GradeTracker gradeTracker) {
            this.gradeTracker = gradeTracker;
        }

        public Optional<LabAttendanceList> getLabAttendanceList() {
            return Optional.ofNullable(labAttendanceList);
        }

        public Optional<GradeTracker> getGradeTracker() {
            return Optional.ofNullable(gradeTracker);
        }

        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        public void setGithubUsername(GithubUsername githubUsername) {
            this.githubUsername = githubUsername;
        }

        public Optional<GithubUsername> getGithubUsername() {
            return Optional.ofNullable(githubUsername);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(studentId, otherEditPersonDescriptor.studentId)
                    && Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(githubUsername, otherEditPersonDescriptor.githubUsername)
                    && Objects.equals(exerciseTracker, otherEditPersonDescriptor.exerciseTracker)
                    && Objects.equals(labAttendanceList, otherEditPersonDescriptor.labAttendanceList);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("studentId", studentId)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("tags", tags)
                    .add("githubUsername", githubUsername)
                    .add("exerciseTracker", exerciseTracker)
                    .add("labAttendanceList", labAttendanceList)
                    .toString();
        }
    }
}
