package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LAB;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LAB;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.commons.exceptions.InvalidIndexException;
import seedu.address.logic.helpers.Comparison;
import seedu.address.logic.helpers.ExerciseIndexStatus;
import seedu.address.logic.helpers.LabAttendanceComparison;
import seedu.address.logic.helpers.LabIndexStatus;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.ExerciseList;
import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.LabList;
import seedu.address.model.person.LabListTest;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Status;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_STUDENTID = "B010X";
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_GITHUB_USERNAME = "--ab";

    private static final String VALID_STUDENTID = "A1231230T";
    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_GITHUB_USERNAME = "TestUsername";
    private static final String VALID_LAB_LIST = "L1: Y L2: A L3: A L4: N L5: N L6: N L7: Y L8: N L9: N L10: N ";



    private static final String WHITESPACE = " \t\r\n";
    @Test
    public void parseMultiIndex_validInputs_success() throws Exception {
        // Single index
        MultiIndex single = ParserUtil.parseMultiIndex("3");
        assertEquals(new MultiIndex(Index.fromOneBased(3)), single);

        // Range index
        MultiIndex range = ParserUtil.parseMultiIndex("2:5");
        assertEquals(new MultiIndex(Index.fromOneBased(2), Index.fromOneBased(5)), range);
    }

    @Test
    public void parseMultiIndex_invalidInputs_throwsException() {
        // Too many colons
        assertThrows(InvalidIndexException.class, () -> ParserUtil.parseMultiIndex("1:2:3"));

        // Lower bound > upper bound
        assertThrows(InvalidIndexException.class, () -> ParserUtil.parseMultiIndex("5:2"));

        // Non-numeric input
        assertThrows(InvalidIndexException.class, () -> ParserUtil.parseMultiIndex("a:b"));

        // Empty input
        assertThrows(ParseException.class, () -> ParserUtil.parseMultiIndex(""));
    }
    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("   "));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }


    @Test
    public void parseLabIndex_validInput_success() throws InvalidIndexException {
        assertEquals(INDEX_FIRST_LAB, ParserUtil.parseLabIndex("1"));

        assertEquals(INDEX_FIRST_LAB, ParserUtil.parseLabIndex("  1  "));

        assertEquals(INDEX_SECOND_LAB, ParserUtil.parseLabIndex("  2 "));
    }

    @Test
    public void parseLabIndex_invalidInput_parseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseLabIndex(""));

        assertThrows(ParseException.class, () -> ParserUtil.parseLabIndex("-1"));
        assertThrows(ParseException.class, () -> ParserUtil.parseLabIndex("0"));
        assertThrows(ParseException.class, () -> ParserUtil.parseLabIndex("11"));

        assertThrows(ParseException.class, () -> ParserUtil.parseLabIndex("abc"));
    }

    @Test
    public void parseLabIndex_null_parseException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseLabIndex(null));
    }

    @Test
    public void parseStudentId_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseStudentId((String) null));
    }

    @Test
    public void parseStudentId_invalidValue_throwsParseException() {
        // invalid format (e.g. wrong prefix or pattern)
        assertThrows(ParseException.class, () -> ParserUtil.parseStudentId(INVALID_STUDENTID));
    }

    @Test
    public void parseStudentId_validValueWithoutWhitespace_returnsStudentId() throws Exception {
        StudentId expectedStudentId =
                new StudentId(VALID_STUDENTID);
        assertEquals(expectedStudentId, ParserUtil.parseStudentId(VALID_STUDENTID));
    }

    @Test
    public void parseStudentId_validValueWithWhitespace_returnsTrimmedStudentId() throws Exception {
        String studentIdWithWhitespace = WHITESPACE + VALID_STUDENTID + WHITESPACE;
        StudentId expectedStudentId =
                new StudentId(VALID_STUDENTID);
        assertEquals(expectedStudentId, ParserUtil.parseStudentId(studentIdWithWhitespace));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }


    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseGithubUsername_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseGithubUsername((String) null));
    }

    @Test
    public void parseGithubUsername_invalidValue_throwsParseException() {
        // invalid format (e.g. starts with a dash or double dash)
        assertThrows(ParseException.class, () -> ParserUtil.parseGithubUsername(INVALID_GITHUB_USERNAME));
    }

    @Test
    public void parseGithubUsername_validValueWithoutWhitespace_returnsGithubUsername() throws Exception {
        seedu.address.model.person.GithubUsername expectedGithubUsername =
                new seedu.address.model.person.GithubUsername(VALID_GITHUB_USERNAME);
        assertEquals(expectedGithubUsername, ParserUtil.parseGithubUsername(VALID_GITHUB_USERNAME));
    }

    @Test
    public void parseGithubUsername_validValueWithWhitespace_returnsTrimmedGithubUsername() throws Exception {
        String githubUsernameWithWhitespace = WHITESPACE + VALID_GITHUB_USERNAME + WHITESPACE;
        seedu.address.model.person.GithubUsername expectedGithubUsername =
                new seedu.address.model.person.GithubUsername(VALID_GITHUB_USERNAME);
        assertEquals(expectedGithubUsername, ParserUtil.parseGithubUsername(githubUsernameWithWhitespace));
    }

    @Test
    public void parseLabAttendanceList_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseGithubUsername(null));
    }

    @Test
    public void parseLabAttendanceList_invalid_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseLabAttendanceList(
                LabListTest.INVALID_LAB_LIST_LENGTH));
        assertThrows(ParseException.class, () -> ParserUtil.parseLabAttendanceList(
                LabListTest.INVALID_LAB_LIST_MISSING_COLON));
        assertThrows(ParseException.class, () -> ParserUtil.parseLabAttendanceList(
                LabListTest.INVALID_LAB_LIST_STATUS));
    }

    @Test
    public void parseLabAttendanceList_valid_success() throws ParseException {
        LabAttendanceList labAttendanceList = new LabList();
        labAttendanceList.markLabAsAttended(0);
        labAttendanceList.markLabAsAttended(6);

        LabAttendanceList labAttendanceListFromParser = ParserUtil.parseLabAttendanceList(VALID_LAB_LIST);
        assertEquals(labAttendanceList, labAttendanceListFromParser);
    }

    // parseExerciseIndexStatus

    @Test
    public void parseExerciseIndexStatus_valid_success() throws Exception {
        ExerciseIndexStatus result = ParserUtil.parseExerciseIndexStatus("0 s/Y");
        assertEquals(Index.fromZeroBased(0), result.getExerciseIndex());
        assertEquals(Status.DONE, result.getStatus());

        result = ParserUtil.parseExerciseIndexStatus("  2   s/  O ");
        assertEquals(Index.fromZeroBased(2), result.getExerciseIndex());
        assertEquals(Status.OVERDUE, result.getStatus());
    }

    @Test
    public void parseExerciseIndexStatus_missingStatus_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseExerciseIndexStatus("0"));
    }

    @Test
    public void parseExerciseIndexStatus_invalidStatus_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseExerciseIndexStatus("0 s/X"));
    }

    @Test
    public void parseExerciseIndexStatus_negativeIndex_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, () -> ParserUtil.parseExerciseIndexStatus("-1 s/Y"));
    }

    @Test
    public void parseExerciseIndexStatus_outOfRange_throwsInvalidIndexException() {
        int tooLarge = ExerciseList.NUMBER_OF_EXERCISES;
        assertThrows(InvalidIndexException.class, () -> ParserUtil.parseExerciseIndexStatus(tooLarge + " s/N"));
    }

    // parseLabNumberStatus

    @Test
    public void parseLabNumberStatus_valid_success() throws Exception {
        LabIndexStatus result = ParserUtil.parseLabNumberStatus("1 s/N");
        assertEquals(Index.fromOneBased(1), result.getLabIndex());
        assertEquals("N", result.getStatus());

        result = ParserUtil.parseLabNumberStatus("  10  s/  A ");
        assertEquals(Index.fromOneBased(10), result.getLabIndex());
        assertEquals("A", result.getStatus());
    }

    @Test
    public void parseLabNumberStatus_missingStatus_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseLabNumberStatus("1"));
    }

    @Test
    public void parseLabNumberStatus_invalidStatus_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseLabNumberStatus("1 s/Z"));
    }

    @Test
    public void parseLabNumberStatus_zeroIndex_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, () -> ParserUtil.parseLabNumberStatus("0 s/Y"));
    }

    @Test
    public void parseLabNumberStatus_outOfRange_throwsInvalidIndexException() {
        int tooLarge = LabList.NUMBER_OF_LABS + 1;
        assertThrows(InvalidIndexException.class, () -> ParserUtil.parseLabNumberStatus(tooLarge + " s/Y"));
    }

    // parseAttendanceComparison

    @Test
    public void parseAttendanceComparison_valid_success() throws Exception {
        LabAttendanceComparison c1 = ParserUtil.parseAttendanceComparison("==100%");
        assertEquals(100, c1.getAttendance());
        assertEquals(Comparison.EQUAL, c1.getComparison());

        LabAttendanceComparison c2 = ParserUtil.parseAttendanceComparison(">=60");
        assertEquals(60, c2.getAttendance());
        assertEquals(Comparison.GREATER_THAN_OR_EQUAL, c2.getComparison());

        LabAttendanceComparison c3 = ParserUtil.parseAttendanceComparison("<25%");
        assertEquals(25, c3.getAttendance());
        assertEquals(Comparison.LESS_THAN, c3.getComparison());

        LabAttendanceComparison c4 = ParserUtil.parseAttendanceComparison("<= 80%");
        assertEquals(80, c4.getAttendance());
        assertEquals(Comparison.LESS_THAN_OR_EQUAL, c4.getComparison());

        LabAttendanceComparison c5 = ParserUtil.parseAttendanceComparison(">40");
        assertEquals(40, c5.getAttendance());
        assertEquals(Comparison.GREATER_THAN, c5.getComparison());
    }

    @Test
    public void parseAttendanceComparison_missingOperator_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAttendanceComparison("75%"));
    }

    @Test
    public void parseAttendanceComparison_invalidOperator_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAttendanceComparison("!=70"));
    }

    @Test
    public void parseAttendanceComparison_nonInteger_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAttendanceComparison("70.5"));
    }

    @Test
    public void parseAttendanceComparison_negativeOrTooLarge_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAttendanceComparison("-1%"));
        assertThrows(ParseException.class, () -> ParserUtil.parseAttendanceComparison("101%"));
    }

    @Test
    public void verifyNoUnwantedPrefixes_validPrefixesOnly_success() {
        assertDoesNotThrow(() -> ParserUtil.verifyNoUnwantedPrefixes("1 2 3", PREFIX_NAME, PREFIX_PHONE));

        assertDoesNotThrow(() ->ParserUtil.verifyNoUnwantedPrefixes(
                " n/John p/12345678", PREFIX_NAME, PREFIX_PHONE));

        assertDoesNotThrow(() -> ParserUtil.verifyNoUnwantedPrefixes(
                " n/John n/Jane p/12345678", PREFIX_NAME, PREFIX_PHONE));

        assertDoesNotThrow(() -> ParserUtil.verifyNoUnwantedPrefixes(
                "   n/John    p/12345678   ", PREFIX_NAME, PREFIX_PHONE));
    }

    @Test
    public void verifyNoUnwantedPrefixes_extraPrefixes_parseException() {
        assertThrows(ParseException.class, () -> ParserUtil.verifyNoUnwantedPrefixes(
                "1 2 3 l/1 ", PREFIX_NAME, PREFIX_PHONE));

        assertThrows(ParseException.class, () ->ParserUtil.verifyNoUnwantedPrefixes(
                " n/John p/12345678 e/sk@gmail.com", PREFIX_NAME, PREFIX_PHONE));

        assertThrows(ParseException.class, () -> ParserUtil.verifyNoUnwantedPrefixes(
                " n/John n/Jane ei/0 p/12345678", PREFIX_NAME, PREFIX_PHONE));

        assertThrows(ParseException.class, () -> ParserUtil.verifyNoUnwantedPrefixes(
                "   n/John    p/12345678 ei/0 l/0  ", PREFIX_NAME, PREFIX_PHONE));
    }

}
