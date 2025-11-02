package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_ATTENDANCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_NUMBER;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.helpers.Comparison;
import seedu.address.logic.helpers.ExerciseIndexStatus;
import seedu.address.logic.helpers.LabAttendanceComparison;
import seedu.address.logic.helpers.LabIndexStatus;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Status;
import seedu.address.model.person.predicates.filterpredicates.ExerciseStatusMatchesPredicate;
import seedu.address.model.person.predicates.filterpredicates.FilterCombinedPredicate;
import seedu.address.model.person.predicates.filterpredicates.FilterPredicate;
import seedu.address.model.person.predicates.filterpredicates.LabAttendanceMatchesPredicate;
import seedu.address.model.person.predicates.filterpredicates.LabStatusMatchesPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    private static final Prefix[] FILTER_PREFIXES = { PREFIX_EXERCISE_INDEX, PREFIX_LAB_NUMBER, PREFIX_LAB_ATTENDANCE };
    private static final String MESSAGE_MISSING_ATTENDANCE_COMPARISON = "Missing attendance comparison.\n";


    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, FILTER_PREFIXES);

        checkNoKeywords(argMultimap);
        List<FilterPredicate> predicates = getSelectedPredicates(argMultimap);

        return new FilterCommand(new FilterCombinedPredicate(predicates));
    }

    //Collects the predicates to be AND combined into a list
    private List<FilterPredicate> getSelectedPredicates(ArgumentMultimap argMultimap) throws ParseException {
        List<String> exerciseIndexes = argMultimap.getAllValues(PREFIX_EXERCISE_INDEX);
        List<String> labNumbers = argMultimap.getAllValues(PREFIX_LAB_NUMBER);
        List<String> labAttendances = argMultimap.getAllValues(PREFIX_LAB_ATTENDANCE);

        List<FilterPredicate> predicates = new ArrayList<>();

        for (String exerciseIndexStatus : exerciseIndexes) {
            predicates.add(getExercisePredicate(exerciseIndexStatus));
        }

        for (String labNumberStatus : labNumbers) {
            predicates.add(getLabPredicate(labNumberStatus));
        }

        for (String labAttendance : labAttendances) {
            if (labAttendance.isBlank()) {
                throw new ParseException(MESSAGE_MISSING_ATTENDANCE_COMPARISON
                        + FilterCommand.ATTENDED_PERCENTAGE_USAGE);
            }
            predicates.add(getLabAttendancePredicate(labAttendance));
        }

        if (predicates.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        return predicates;
    }

    private ExerciseStatusMatchesPredicate getExercisePredicate(
            String exerciseIndexStatus) throws ParseException {
        ExerciseIndexStatus indexStatusPair = ParserUtil.parseExerciseIndexStatus(exerciseIndexStatus);

        Status exerciseStatus = indexStatusPair.getStatus();
        Index exerciseIndex = indexStatusPair.getExerciseIndex();

        return new ExerciseStatusMatchesPredicate(exerciseIndex, exerciseStatus);
    }

    private LabStatusMatchesPredicate getLabPredicate(
            String labNumberStatus) throws ParseException {
        LabIndexStatus labNumberStatusPair = ParserUtil.parseLabNumberStatus(labNumberStatus);

        String labStatus = labNumberStatusPair.getStatus();
        Index labIndex = labNumberStatusPair.getLabIndex();

        return new LabStatusMatchesPredicate(labIndex, labStatus);
    }

    private LabAttendanceMatchesPredicate getLabAttendancePredicate(
            String labAttendenceStatus) throws ParseException {
        LabAttendanceComparison labAttendanceComparison = ParserUtil
                .parseAttendanceComparison(labAttendenceStatus);

        double value = labAttendanceComparison.getAttendance();
        Comparison comparison = labAttendanceComparison.getComparison();
        return new LabAttendanceMatchesPredicate(value, comparison);
    }

    private void checkNoKeywords(ArgumentMultimap argMultimap) throws ParseException {
        if (!argMultimap.getPreamble().isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
    }

}
