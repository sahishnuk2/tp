package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final StudentId studentId;
    private final Name name;
    private final Phone phone;
    private final Email email;

    private final Set<Tag> tags = new HashSet<>();
    private final GithubUsername githubUsername;
    private final LabAttendanceList labAttendanceList;
    private final ExerciseTracker exerciseTracker;
    private final GradeTracker gradeTracker;

    /**
     * Every field must be present and not null.
     */
    public Person(StudentId studentId, Name name, Phone phone, Email email,
                  Set<Tag> tags, GithubUsername githubUsername) {
        requireAllNonNull(studentId, name, phone, email, tags, githubUsername);
        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
        this.exerciseTracker = new ExerciseList();
        this.githubUsername = githubUsername;
        this.labAttendanceList = new LabList();
        this.gradeTracker = new GradeMap();
    }

    /**
     * Initialises a new person object, but with a specific list of exercise statuses
     */
    public Person(StudentId studentId, Name name, Phone phone, Email email, Set<Tag> tags,
                  GithubUsername githubUsername, ExerciseTracker exerciseTracker,
                  LabAttendanceList labAttendanceList, GradeTracker gradeTracker) {
        requireAllNonNull(name, phone, email, tags, githubUsername, exerciseTracker, labAttendanceList);
        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
        this.exerciseTracker = exerciseTracker;
        this.githubUsername = githubUsername;
        this.labAttendanceList = labAttendanceList;
        this.gradeTracker = gradeTracker;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public GithubUsername getGithubUsername() {
        return githubUsername;
    }

    public LabAttendanceList getLabAttendanceList() {
        return labAttendanceList;
    }

    public ExerciseTracker getExerciseTracker() {
        return exerciseTracker;
    }

    public GradeTracker getGradeTracker() {
        return gradeTracker;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getStudentId().equals(getStudentId());
    }
    public String getNameAndID() {
        return String.format("%s (%s)", getName().toString(), getStudentId().toString());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return studentId.equals(otherPerson.studentId)
                && name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && exerciseTracker.equals(otherPerson.exerciseTracker)
                && tags.equals(otherPerson.tags)
                && githubUsername.equals(otherPerson.githubUsername)
                && labAttendanceList.equals(otherPerson.labAttendanceList)
                && gradeTracker.equals(otherPerson.gradeTracker);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(studentId, name, phone, email, tags,
                githubUsername, exerciseTracker, labAttendanceList, gradeTracker);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentId", studentId)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("tags", tags)
                .add("github username", githubUsername)
                .add("exerciseStatuses", exerciseTracker)
                .add("lab attendance list", labAttendanceList)
                .add("gradeTracker", gradeTracker)
                .toString();
    }
}
