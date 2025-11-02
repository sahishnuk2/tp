package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.person.Email;
import seedu.address.model.person.Examination;
import seedu.address.model.person.ExerciseList;
import seedu.address.model.person.GithubUsername;
import seedu.address.model.person.GradeMap;
import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.LabList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String studentId;
    private final String name;
    private final String phone;
    private final String email;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String githubUsername;
    private final List<String> exerciseStatuses = new ArrayList<>();
    private final String labAttendanceList;
    private final Map<String, JsonAdaptedExamination> gradeMap = new HashMap<>();
    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("studentId") String studentId,
                             @JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("githubUsername") String githubUsername,
                             @JsonProperty("exerciseStatuses") List<String> exerciseStatuses,
                             @JsonProperty("labAttendanceList") String labAttendanceList,
                             @JsonProperty("gradeMap") Map<String, JsonAdaptedExamination> gradeMap) {
        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        if (exerciseStatuses != null) {
            this.exerciseStatuses.addAll(exerciseStatuses);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.githubUsername = githubUsername;
        this.labAttendanceList = labAttendanceList;
        if (gradeMap != null) {
            this.gradeMap.putAll(gradeMap);
        }
    }

    /**
     * Simplified constructor used in tests.
     */
    public JsonAdaptedPerson(String studentId, String name, String phone,
                             String email, List<JsonAdaptedTag> tags,
                             String githubUsername,
                             String labAttendanceList, Map<String, JsonAdaptedExamination> gradeMap) {
        this(studentId, name, phone, email, tags, githubUsername, new ArrayList<>(), labAttendanceList, gradeMap);
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        studentId = source.getStudentId().value;
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .toList());
        exerciseStatuses.addAll(source
                .getExerciseTracker()
                .getIsDoneList()
                .stream()
                .map(x -> x ? "y" : "n")
                .toList());
        githubUsername = source.getGithubUsername().value;
        labAttendanceList = source.getLabAttendanceList().toString();
        gradeMap.putAll(
                source.getGradeTracker()
                        .getExamMap()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> {
                                    Examination exam = entry.getValue();
                                    return new JsonAdaptedExamination(exam);
                                }
                        ))
        );

    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final ArrayList<Boolean> isDoneList = new ArrayList<>();
        for (String stat : exerciseStatuses) {
            if (!stat.isEmpty()) {
                isDoneList.add(ParserUtil.parseStatus(stat));
            }
        }
        final GradeMap gradeMapModel = new GradeMap();
        for (Map.Entry<String, JsonAdaptedExamination> entry : gradeMap.entrySet()) {
            String key = entry.getKey();
            JsonAdaptedExamination adaptedExam = entry.getValue();

            Examination exam = adaptedExam.toModelType();
            gradeMapModel.putExam(key, exam);
        }

        if (studentId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    StudentId.class.getSimpleName()));
        }
        if (!StudentId.isValidStudentId(studentId)) {
            throw new IllegalValueException(StudentId.MESSAGE_CONSTRAINTS);
        }
        final StudentId modelStudentId = new StudentId(studentId);

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        if (githubUsername == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    GithubUsername.class.getSimpleName()));
        }
        if (!GithubUsername.isValidGithubUsername(githubUsername)) {
            throw new IllegalValueException(GithubUsername.MESSAGE_CONSTRAINTS);
        }
        final GithubUsername modelGithubUsername = new GithubUsername(githubUsername);

        if (labAttendanceList == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LabAttendanceList.class.getSimpleName()));
        }

        if (!LabList.isValidLabList(labAttendanceList)) {
            throw new IllegalValueException(LabList.MESSAGE_CONSTRAINTS);
        }
        final LabAttendanceList modelLabAttendanceList = ParserUtil.parseLabAttendanceList(labAttendanceList);
        if (gradeMap == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LabAttendanceList.class.getSimpleName()));
        }

        return new Person(modelStudentId, modelName, modelPhone, modelEmail,
                modelTags, modelGithubUsername,
                new ExerciseList(isDoneList), modelLabAttendanceList, gradeMapModel);
    }
}
