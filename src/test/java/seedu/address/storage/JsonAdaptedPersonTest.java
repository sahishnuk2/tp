package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Email;
import seedu.address.model.person.GithubUsername;
import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.LabList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;

public class JsonAdaptedPersonTest {
    private static final String INVALID_STUDENTID = "A010T";
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_GITHUB_USERNAME = " ";
    private static final String INVALID_LAB_ATTENDANCE_LIST = "L2: Y";

    private static final String VALID_STUDENTID = BENSON.getStudentId().toString();
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final String VALID_GITHUB_USERNAME = BENSON.getGithubUsername().toString();
    private static final String VALID_LAB_ATTENDANCE_LIST = BENSON.getLabAttendanceList().toString();
    private static final Map<String, JsonAdaptedExamination> VALID_GRADE_ASSESSMENTS =
            BENSON.getGradeTracker().getExamMap().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> new JsonAdaptedExamination(entry.getValue())
                    ));

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidStudentId_throwsIllegalValueException() throws IllegalValueException {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_STUDENTID, VALID_NAME, VALID_PHONE,
                        VALID_EMAIL, VALID_TAGS, VALID_GITHUB_USERNAME,
                        VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = StudentId.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullStudentId_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_NAME,
                VALID_PHONE, VALID_EMAIL, VALID_TAGS, VALID_GITHUB_USERNAME,
                VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, StudentId.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_STUDENTID, INVALID_NAME, VALID_PHONE,
                        VALID_EMAIL, VALID_TAGS, VALID_GITHUB_USERNAME,
                        VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_STUDENTID, null,
                VALID_PHONE, VALID_EMAIL, VALID_TAGS, VALID_GITHUB_USERNAME,
                VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_STUDENTID, VALID_NAME, INVALID_PHONE,
                        VALID_EMAIL, VALID_TAGS, VALID_GITHUB_USERNAME,
                        VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_STUDENTID, VALID_NAME, null,
                VALID_EMAIL, VALID_TAGS, VALID_GITHUB_USERNAME,
                VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_STUDENTID, VALID_NAME, VALID_PHONE,
                        INVALID_EMAIL, VALID_TAGS, VALID_GITHUB_USERNAME,
                        VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_STUDENTID, VALID_NAME, VALID_PHONE, null,
                 VALID_TAGS, VALID_GITHUB_USERNAME,
                VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }



    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_STUDENTID, VALID_NAME,
                VALID_PHONE, VALID_EMAIL, invalidTags, VALID_GITHUB_USERNAME,
                VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidGithubUsername_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_STUDENTID, VALID_NAME, VALID_PHONE,
                        VALID_EMAIL, VALID_TAGS, INVALID_GITHUB_USERNAME,
                        VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = GithubUsername.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullGithubUsername_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_STUDENTID, VALID_NAME,
                VALID_PHONE, VALID_EMAIL, VALID_TAGS, null,
                VALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, GithubUsername.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidLabAttendanceList_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_STUDENTID, VALID_NAME, VALID_PHONE,
                        VALID_EMAIL, VALID_TAGS,
                        VALID_GITHUB_USERNAME, INVALID_LAB_ATTENDANCE_LIST, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = LabList.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullLabAttendanceList_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_STUDENTID, VALID_NAME,
                VALID_PHONE, VALID_EMAIL, VALID_TAGS,
                VALID_GITHUB_USERNAME, null, VALID_GRADE_ASSESSMENTS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, LabAttendanceList.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

}
