package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.helpers.Comparison;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Status;
import seedu.address.model.person.predicates.filterpredicates.ExerciseStatusMatchesPredicate;
import seedu.address.model.person.predicates.filterpredicates.FilterCombinedPredicate;
import seedu.address.model.person.predicates.filterpredicates.FilterPredicate;
import seedu.address.model.person.predicates.filterpredicates.LabAttendanceMatchesPredicate;
import seedu.address.model.person.predicates.filterpredicates.LabStatusMatchesPredicate;

public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_withPreamble_throwsParseException() {
        assertParseFailure(parser, "preamble ei/0 s/Y",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }
    @Test
    public void parse_singleExercise_success() {
        String userInput = " ei/0 s/Y";
        List<FilterPredicate> predicates = new ArrayList<>();
        predicates.add(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE));
        FilterCommand expected = new FilterCommand(new FilterCombinedPredicate(predicates));
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_multipleExercises_success() {
        String userInput = " ei/0 s/Y ei/2 s/O";
        List<FilterPredicate> predicates = new ArrayList<>();
        predicates.add(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE));
        predicates.add(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(2), Status.OVERDUE));
        FilterCommand expected = new FilterCommand(new FilterCombinedPredicate(predicates));
        assertParseSuccess(parser, userInput, expected);
    }
    @Test
    public void parse_singleLab_success() {
        String userInput = " l/1 s/Y";
        List<FilterPredicate> predicates = new ArrayList<>();
        predicates.add(new LabStatusMatchesPredicate(Index.fromOneBased(1), "Y"));
        FilterCommand expected = new FilterCommand(new FilterCombinedPredicate(predicates));
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_multipleLabs_success() {
        String userInput = " l/1 s/Y l/5 s/N";
        List<FilterPredicate> predicates = new ArrayList<>();
        predicates.add(new LabStatusMatchesPredicate(Index.fromOneBased(1), "Y"));
        predicates.add(new LabStatusMatchesPredicate(Index.fromOneBased(5), "N"));
        FilterCommand expected = new FilterCommand(new FilterCombinedPredicate(predicates));
        assertParseSuccess(parser, userInput, expected);
    }
    @Test
    public void parse_labAttendanceEqual_success() {
        String userInput = " la/==100%";
        List<FilterPredicate> predicates = new ArrayList<>();
        predicates.add(new LabAttendanceMatchesPredicate(100.0, Comparison.EQUAL));
        FilterCommand expected = new FilterCommand(new FilterCombinedPredicate(predicates));
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_labAttendanceGe_success() {
        String userInput = " la/>=70%";
        List<FilterPredicate> predicates = new ArrayList<>();
        predicates.add(new LabAttendanceMatchesPredicate(70.0, Comparison.GREATER_THAN_OR_EQUAL));
        FilterCommand expected = new FilterCommand(new FilterCombinedPredicate(predicates));
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_labAttendanceLt_success() {
        String userInput = " la/<50";
        List<FilterPredicate> predicates = new ArrayList<>();
        predicates.add(new LabAttendanceMatchesPredicate(50.0, Comparison.LESS_THAN));
        FilterCommand expected = new FilterCommand(new FilterCombinedPredicate(predicates));
        assertParseSuccess(parser, userInput, expected);
    }
    @Test
    public void parse_mixedPredicates_success() throws ParseException {
        String userInput = " ei/0 s/Y l/1 s/N la/>=60%";
        List<FilterPredicate> predicates = new ArrayList<>();
        predicates.add(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.DONE));
        predicates.add(new LabStatusMatchesPredicate(Index.fromOneBased(1), "N"));
        predicates.add(new LabAttendanceMatchesPredicate(60.0, Comparison.GREATER_THAN_OR_EQUAL));
        FilterCommand expected = new FilterCommand(new FilterCombinedPredicate(predicates));
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_labAttendanceEmptyValue_throwsParseException() {
        String userInput = " la/";
        String expected = "Missing attendance comparison.\n" + FilterCommand.ATTENDED_PERCENTAGE_USAGE + FilterCommand.LA_EXAMPLE;
        assertParseFailure(parser, userInput, expected);
    }
    @Test
    public void parse_irregularSpacing_success() {
        String userInput = "   ei/0   s/N    l/10   s/A    la/<=  80%   ";
        List<FilterPredicate> predicates = new ArrayList<>();
        predicates.add(new ExerciseStatusMatchesPredicate(Index.fromZeroBased(0), Status.NOT_DONE));
        predicates.add(new LabStatusMatchesPredicate(Index.fromOneBased(10), "A"));
        predicates.add(new LabAttendanceMatchesPredicate(80.0, Comparison.LESS_THAN_OR_EQUAL));
        FilterCommand expected = new FilterCommand(new FilterCombinedPredicate(predicates));
        assertParseSuccess(parser, userInput, expected);
    }
}
