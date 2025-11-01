package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.commons.exceptions.InvalidIndexException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.helpers.Comparison;
import seedu.address.logic.helpers.ExerciseIndexStatus;
import seedu.address.logic.helpers.LabAttendanceComparison;
import seedu.address.logic.helpers.LabIndexStatus;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Examination;
import seedu.address.model.person.ExerciseList;
import seedu.address.model.person.ExerciseTracker;
import seedu.address.model.person.GithubUsername;
import seedu.address.model.person.GradeMap;
import seedu.address.model.person.GradeTracker;
import seedu.address.model.person.Lab;
import seedu.address.model.person.LabAttendance;
import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.LabList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Status;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.sortcriterion.ExerciseSortCriterion;
import seedu.address.model.person.sortcriterion.LabSortCriterion;
import seedu.address.model.person.sortcriterion.NameSortCriterion;
import seedu.address.model.person.sortcriterion.SortCriterion;
import seedu.address.model.person.sortcriterion.StudentIdSortCriterion;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "index must be a number greater than 0";
    public static final String MESSAGE_INVALID_STATUS = "Status input must be Y or N";
    private static final String MESSAGE_INVALID_EXERCISE_INDEX =
            "Exercise index is invalid! It must be between 0 and "
                    + (ExerciseList.NUMBER_OF_EXERCISES - 1) + " (inclusive).";
    private static final String MESSAGE_INVALID_LAB_INDEX =
            "Lab index is invalid! It must be between 1 and " + LabList.NUMBER_OF_LABS + " (inclusive).";
    private static final String MESSAGE_INVALID_FILTER_EXERCISE_STATUS =
            "Exercise status must be Y, N or O";
    private static final String MESSAGE_INVALID_FILTER_LAB_STATUS =
            "Lab status must be Y, N or A";
    private static final String MESSAGE_MISSING_EXERCISE_STATUS =
            "Exercise index must always be followed by exercise status";
    private static final String MESSAGE_MISSING_LAB_STATUS =
            "Lab index must always be followed by lab status";
    private static final String MESSAGE_EMPTY_INPUT = "Input string is empty!";
    private static final String MESSAGE_INVALID_MULTIINDEX_BOUNDS =
            "%s is invalid! Lower bound cannot be greater than upper bound";
    private static final String MESSAGE_MISSING_OPERATOR =
            "Missing appropriate operator for comparison, one of ==, >=, <=, >, < should follow la/";
    private static final String MESSAGE_INVALID_PERCENTAGE =
            "Attendance percentage must be an integer between 0 and 100.";
    private static final String MESSAGE_INVALID_PREFIX = "Invalid prefix(s) found: %s";

    /**
     * @param input a string that is either in the "X:Y" or "X" form
     * @return a MultiIndex instance
     * @throws ParseException if the input is invalid
     */
    public static MultiIndex parseMultiIndex(String input) throws ParseException {
        if (input.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_INPUT);
        }
        if (input.contains(":")) {
            return parseRange(input);
        } else {
            return new MultiIndex(parseIndex(input));
        }
    }
    /**
     * Parses a range input like "2:5" into a MultiIndex.
     * */
    private static MultiIndex parseRange(String input) throws ParseException {
        String[] parts = input.split(":");
        if (parts.length != 2) {
            throw new InvalidIndexException("Invalid range format: " + input);
        }

        Index lower = ParserUtil.parseIndex(parts[0].trim());
        Index upper = ParserUtil.parseIndex(parts[1].trim());

        if (lower.getZeroBased() > upper.getZeroBased()) {
            throw new InvalidIndexException(String.format(MESSAGE_INVALID_MULTIINDEX_BOUNDS, input));
        }

        return new MultiIndex(lower, upper);
    }
    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (trimmedIndex.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_INPUT);
        }
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new InvalidIndexException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed. The index must be between 1 and the maximum number of labs (inclusive).
     * @throws InvalidIndexException if the specified index is invalid
     */
    public static Index parseLabIndex(String oneBasedIndex) throws InvalidIndexException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new InvalidIndexException(MESSAGE_INVALID_LAB_INDEX);
        }

        int oneBased = Integer.parseInt(trimmedIndex);
        if (oneBased > LabList.NUMBER_OF_LABS) {
            throw new InvalidIndexException(MESSAGE_INVALID_LAB_INDEX);
        }
        return Index.fromOneBased(oneBased);
    }

    /**
     * Parses {@code zeroBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed. The index must be between 1 and the maximum number of labs - 1 (inclusive).
     * @throws InvalidIndexException if the specified index is invalid
     */
    public static Index parseExerciseIndex(String zeroBasedIndex) throws InvalidIndexException {
        String trimmedIndex = zeroBasedIndex.trim();
        if (!StringUtil.isNonNegativeUnsignedInteger(trimmedIndex)) {
            throw new InvalidIndexException(MESSAGE_INVALID_EXERCISE_INDEX);
        }
        int zeroBased = Integer.parseInt(trimmedIndex);
        if (zeroBased >= ExerciseList.NUMBER_OF_EXERCISES) {
            throw new InvalidIndexException(MESSAGE_INVALID_EXERCISE_INDEX);
        }
        return Index.fromZeroBased(zeroBased);
    }
    /**
     * Parses a {@code String studentId} into a {@code StudentId}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws InvalidIndexException if the given {@code studentId} is invalid.
     */
    public static StudentId parseStudentId(String studentId) throws ParseException {
        requireNonNull(studentId);
        String trimmedStudentId = studentId.trim();
        if (!StudentId.isValidStudentId(trimmedStudentId)) {
            throw new ParseException(StudentId.MESSAGE_CONSTRAINTS);
        }
        return new StudentId(trimmedStudentId);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String githubUsername} into an {@code GithubUsername}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code githubUsername} is invalid.
     */
    public static GithubUsername parseGithubUsername(String githubUsername) throws ParseException {
        requireNonNull(githubUsername);
        String trimmedGithubUsername = githubUsername.trim();
        if (!GithubUsername.isValidGithubUsername(trimmedGithubUsername)) {
            throw new ParseException(GithubUsername.MESSAGE_CONSTRAINTS);
        }
        return new GithubUsername(trimmedGithubUsername);
    }

    /**
     * Parses a {@code String labStatus} into a boolean value.
     *
     * @param labStatus The status string to parse.
     * @return true if the status is "y", false if the status is "n".
     * @throws ParseException if the given {@code labStatus} is invalid.
     */
    public static boolean parseStatus(String labStatus) throws ParseException {
        requireNonNull(labStatus);
        String trimmed = labStatus.trim();
        switch (trimmed.toLowerCase()) {
        case "y": return true;
        case "n": return false;
        default:
            throw new ParseException(MESSAGE_INVALID_STATUS);
        }
    }

    /**
     * Parses a {@code String labAttendanceListString} into an {@code LabAttendanceList}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code labAttendanceListString} is invalid.
     */
    public static LabAttendanceList parseLabAttendanceList(String labAttendanceList) throws ParseException {
        requireNonNull(labAttendanceList);
        String trimmed = labAttendanceList.trim();
        if (!LabList.isValidLabList(trimmed)) {
            throw new ParseException(LabList.MESSAGE_CONSTRAINTS);
        }

        LabAttendance[] labs = new LabAttendance[LabList.NUMBER_OF_LABS];
        String[] parts = trimmed.split("\\s+");

        for (int i = 0; i < labs.length; i++) {
            String status = parts[i * 2 + 1];

            labs[i] = new Lab(i + 1, LabList.getCurrentWeek());
            if (status.equals("Y")) {
                labs[i].markAsAttended();
            }
        }
        return new LabList(labs);
    }
    /**
     * Parses a {@code String exerciseTrackerString} into an {@code ExerciseTracker}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code exerciseTrackerString} is invalid.
     */
    public static ExerciseTracker parseExerciseTracker(String exerciseTrackerString) throws ParseException {
        requireNonNull(exerciseTrackerString);
        String trimmed = exerciseTrackerString.trim();

        if (!ExerciseList.isValidExerciseList(trimmed)) {
            throw new ParseException(ExerciseList.MESSAGE_CONSTRAINTS);
        }

        Boolean[] statuses = new Boolean[ExerciseList.NUMBER_OF_EXERCISES];
        String[] parts = trimmed.split("\\s+");

        for (int i = 0; i < ExerciseList.NUMBER_OF_EXERCISES; i++) {
            String statusString = parts[i * 3 + 2];
            if (statusString.equals("D")) {
                statuses[i] = true;
            } else if (statusString.equals("N") || statusString.equals("O")) {
                statuses[i] = false;
            }
        }
        return new ExerciseList(new ArrayList<>(Arrays.asList(statuses)));
    }

    /**
     * Parses a {@code GradeTrackerString} into a {@code GradeTracker}.
     * Expected format: "pe1: Passed, midterm: Failed, pe2: NA"
     *
     * @throws ParseException if the given {@code GradeTrackerString} is invalid.
     */
    public static GradeTracker parseGradeTracker(String input) throws ParseException {
        requireNonNull(input);
        GradeTracker gradeTracker = new GradeMap();
        for (String entry : input.split(",")) {
            String[] parts = entry.trim().split(":");

            if (parts.length != 2) {
                continue;
            }

            String name = parts[0].trim().toLowerCase();
            String resultStr = parts[1].trim().toLowerCase();

            Examination exam = getExamination(name, resultStr);

            gradeTracker.putExam(name, exam);
        }

        return gradeTracker;
    }

    private static Examination getExamination(String name, String resultStr) throws ParseException {
        Examination exam = new Examination(name);

        if (!resultStr.equals("na")) {
            if (resultStr.equals("passed")) {
                exam.markPassed();
            } else if (resultStr.equals("failed")) {
                exam.markFailed();
            } else {
                throw new ParseException(
                        String.format("Invalid exam result '%s' for '%s'. Must be 'Passed', 'Failed', or 'NA'.",
                                resultStr, name)
                );
            }
        }
        return exam;
    }


    /**
     * Parses a {@code String criterionString} into a {@code SortCriterion}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code criterionString} is not a valid criterion.
     */
    public static SortCriterion parseSortCriterion(String criterionString) throws ParseException {
        requireNonNull(criterionString);
        String trimmedCriterion = criterionString.trim().toLowerCase();

        switch (trimmedCriterion) {
        case NameSortCriterion.CRITERION_KEYWORD:
            return new NameSortCriterion();

        case StudentIdSortCriterion.CRITERION_KEYWORD:
            return new StudentIdSortCriterion();

        case LabSortCriterion.CRITERION_KEYWORD:
            return new LabSortCriterion();

        case ExerciseSortCriterion.CRITERION_KEYWORD:
            return new ExerciseSortCriterion();

        default:
            throw new ParseException(SortCriterion.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String} exerciseIndexString into a {@code Pair} of exercise index
     * and exercise status.
     *
     * @param exerciseIndexString a string containing both the index and status as a combined string
     * @throws ParseException if the given {@code String} does not include a status.
     */
    public static ExerciseIndexStatus parseExerciseIndexStatus(String exerciseIndexString) throws ParseException {
        ArgumentMultimap exerciseMultimap =
                ArgumentTokenizer.tokenize(exerciseIndexString, PREFIX_STATUS);
        Optional<String> statusString = exerciseMultimap.getValue(PREFIX_STATUS);

        Index exerciseNumber = parseExerciseIndex(exerciseMultimap.getPreamble());
        if (statusString.isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_EXERCISE_STATUS);
        }
        Status status = parseExerciseStatusForFilter(exerciseMultimap.getValue(PREFIX_STATUS).orElse(""));
        return new ExerciseIndexStatus(exerciseNumber, status);
    }

    /**
     * Parses a {@code String} labNumberString into a {@code Pair} of
     * lab index and lab attendance status.
     *
     * @param labNumberString a string containing both the index and status as a combined string
     * @throws ParseException if the given {@code String} does not include a status.
     */
    public static LabIndexStatus parseLabNumberStatus(String labNumberString) throws ParseException {
        ArgumentMultimap exerciseMultimap =
                ArgumentTokenizer.tokenize(labNumberString, PREFIX_STATUS);
        Optional<String> statusString = exerciseMultimap.getValue(PREFIX_STATUS);

        Index labNumber = parseLabIndex(exerciseMultimap.getPreamble());
        if (statusString.isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_LAB_STATUS);
        }
        // If needed, can replace OrElse with just get
        String statusStr = parseLabStatusForFilter(exerciseMultimap.getValue(PREFIX_STATUS).orElse(""));
        return new LabIndexStatus(labNumber, statusStr);
    }

    private static String parseLabStatusForFilter(String labStatus) throws ParseException {
        requireNonNull(labStatus);
        String trimmed = labStatus.trim();
        switch (trimmed.toUpperCase()) {
        case "Y": return "Y";
        case "N": return "N";
        case "A": return "A";
        default:
            throw new ParseException(MESSAGE_INVALID_FILTER_LAB_STATUS);
        }
    }

    private static Status parseExerciseStatusForFilter(String exerciseStatus) throws ParseException {
        requireNonNull(exerciseStatus);
        String trimmed = exerciseStatus.trim();
        switch (trimmed.toUpperCase()) {
        case "Y": return Status.DONE;
        case "N": return Status.NOT_DONE;
        case "O": return Status.OVERDUE;
        default:
            throw new ParseException(MESSAGE_INVALID_FILTER_EXERCISE_STATUS);
        }
    }

    /**
     * Parses an attendance comparison like ">=60", "75%", "<=85".
     *
     * Accepted forms (spaces optional; '%' optional):
     * - ==70, >=60, <=85, >40, <25
     * - 75 or 75%
     *
     * Value must be an integer from 0 to 100.
     * Operator must be one of: ==, >=, <=, >, <.
     *
     * @param attendanceComparison raw input string
     * @return a LabAttendanceComparison with the parsed value and operator
     * @throws ParseException if input is null, malformed, has an unsupported operator,
     *                        or the value is outside 0–100
     */
    public static LabAttendanceComparison parseAttendanceComparison(String attendanceComparison) throws ParseException {
        assert(attendanceComparison != null);
        String s = attendanceComparison.trim().replaceAll("\\s+", "");

        // Accepts: ==70, >=60, <=85, >40, <25, 75, and any of those with a trailing %
        // Group 1 operator: ==, >=, <=, >, < or empty,
        // group 2 value: 1–3 digits checks if negative, optional trailing '%'.
        Matcher m = Pattern
                .compile("^(?:([<>]=?|==?|))?(-?\\d{1,3})(?:%)?$")
                .matcher(s);
        if (!m.matches()) {
            throw new ParseException(FilterCommand.ATTENDED_PERCENTAGE_USAGE);
        }

        String operator = m.group(1);
        int value = Integer.parseInt(m.group(2));
        if (value < 0 || value > 100) {
            throw new ParseException(MESSAGE_INVALID_PERCENTAGE);
        }

        Comparison comparison;
        switch (operator) {
        case "==":
            comparison = Comparison.EQUAL;
            break;
        case ">=":
            comparison = Comparison.GREATER_THAN_OR_EQUAL;
            break;
        case "<=":
            comparison = Comparison.LESS_THAN_OR_EQUAL;
            break;
        case ">":
            comparison = Comparison.GREATER_THAN;
            break;
        case "<":
            comparison = Comparison.LESS_THAN;
            break;
        default:
            throw new ParseException(MESSAGE_MISSING_OPERATOR);
        };

        return new LabAttendanceComparison(value, comparison);
    }


    /**
     * Ensures that prefixes of a command's required fields are used
     * @param argumentMultimap of the command parser
     * @param usageMessage that instructs the user of the proper format
     * @param requiredPrefixes prefixes of fields required for the parsed command
     * @throws ParseException thrown when a field is left empty
     */
    public static void validateFields(ArgumentMultimap argumentMultimap,
                                  String usageMessage,
                                  Prefix... requiredPrefixes) throws ParseException {
        if (argumentMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage));
        }
        for (Prefix prefix : requiredPrefixes) {
            if (argumentMultimap.getValue(prefix).isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage));
            }
        }
    }

    /**
     * Checks if the args string contains any prefix-like patterns (e.g., "x/") that are not in the allowed list.
     *
     * @param args The raw argument string to check
     * @param allowedPrefixes The prefixes that are allowed for this command
     * @throws ParseException if unwanted prefixes are detected
     */
    public static void verifyNoUnwantedPrefixes(String args, Prefix... allowedPrefixes) throws ParseException {
        Set<String> allowedPrefixStrings = new HashSet<>();
        for (Prefix prefix : allowedPrefixes) {
            allowedPrefixStrings.add(prefix.getPrefix());
        }

        // Pattern to match any prefix-like structure: space followed by one or more word chars and a forward slash
        Pattern prefixPattern = Pattern.compile("\\s+(\\w+/)");
        Matcher matcher = prefixPattern.matcher(args);

        Set<String> unwantedPrefixes = new HashSet<>();
        while (matcher.find()) {
            String foundPrefix = matcher.group(1);
            if (!allowedPrefixStrings.contains(foundPrefix)) {
                unwantedPrefixes.add(foundPrefix);
            }
        }

        if (!unwantedPrefixes.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_PREFIX, String.join(", ", unwantedPrefixes)));
        }
    }
}
