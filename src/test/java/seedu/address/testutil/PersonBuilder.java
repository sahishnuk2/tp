package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.ExerciseList;
import seedu.address.model.person.ExerciseTracker;
import seedu.address.model.person.GithubUsername;
import seedu.address.model.person.GradeMap;
import seedu.address.model.person.GradeTracker;
import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.LabList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_STUDENTID = "A1231230X";
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_GITHUB_USERNAME = "TestUsername";
    public static final String DEFAULT_LAB_ATTENDANCE_LIST = new LabList().toString();
    public static final String DEFAULT_GRADEMAP = new GradeMap().toString();


    private StudentId studentId;
    private Name name;
    private Phone phone;
    private Email email;
    private Set<Tag> tags;
    private ExerciseTracker exerciseList;
    private GithubUsername githubUsername;
    private LabAttendanceList labAttendanceList;
    private GradeTracker gradeMap;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        studentId = new StudentId(DEFAULT_STUDENTID);
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        tags = new HashSet<>();
        exerciseList = new ExerciseList();
        githubUsername = new GithubUsername(DEFAULT_GITHUB_USERNAME);
        labAttendanceList = new LabList();
        gradeMap = new GradeMap();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        studentId = personToCopy.getStudentId();
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        tags = new HashSet<>(personToCopy.getTags());
        exerciseList = personToCopy.getExerciseTracker();
        githubUsername = personToCopy.getGithubUsername();
        labAttendanceList = personToCopy.getLabAttendanceList();
        gradeMap = personToCopy.getGradeTracker();
    }

    /**
     * Sets the {@code StudentId} of the {@code Person} that we are building.
     */
    public PersonBuilder withStudentId(String studentId) {
        this.studentId = new StudentId(studentId);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code GithubUsername} of the {@code Person} that we are building.
     */
    public PersonBuilder withGithubUsername(String githubUsername) {
        this.githubUsername = new GithubUsername(githubUsername);
        return this;
    }

    /**
     * Sets the {@code LabAttendanceList} of the {@code Person} that we are building.
     */
    public PersonBuilder withLabAttendanceList(String labListString) {
        try {
            labAttendanceList = ParserUtil.parseLabAttendanceList(labListString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid Lab Attendance List format"); // For developers
        }
        return this;
    }

    /**
     * Sets the {@code ExerciseList} of the {@code Person} that we are building.
     */
    public PersonBuilder withExerciseList(String exerciseListString) {
        try {
            exerciseList = ParserUtil.parseExerciseTracker(exerciseListString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid Exercise Tracker format"); // For developers
        }
        return this;
    }

    /**
     * Sets the {@code gradeMap} of the {@code Person} that we are building.
     */
    public PersonBuilder withGradeMap(String gradeMapString) {
        try {
            gradeMap = ParserUtil.parseGradeTracker(gradeMapString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid Grade Map format"); // For developers
        }
        return this;
    }

    /**
     * builds a person using the fields
     * @return the person that is built
     */
    public Person build() {
        return new Person(studentId,
                name,
                phone,
                email,
                tags,
                githubUsername,
                exerciseList,
                labAttendanceList,
                gradeMap
                );
    }
}
