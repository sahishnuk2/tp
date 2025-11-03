package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddConsultationCommand;
import seedu.address.model.timeslot.ConsultationTimeslot;

/**
 * Tests for AddConsultationCommandParser.
 */
public class AddConsultationCommandParserTest {

    private final AddConsultationCommandParser parser = new AddConsultationCommandParser();

    @Test
    public void parse_validIsoInput_success() {
        String input = " ts/2025-10-04T10:00:00 te/2025-10-04T11:00:00 n/John Doe";
        LocalDateTime start = LocalDateTime.parse("2025-10-04T10:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse("2025-10-04T11:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        ConsultationTimeslot expected = new ConsultationTimeslot(start, end, "John Doe");
        AddConsultationCommand expectedCmd = new AddConsultationCommand(expected);

        assertParseSuccess(parser, input, expectedCmd);
    }

    @Test
    public void parse_validHumanReadableWithComma_success() {
        // format "d MMM uuuu, HH:mm"
        String input = " ts/4 Oct 2025, 10:00 te/4 Oct 2025, 11:30 n/Alice";
        LocalDateTime start = LocalDateTime.parse("4 Oct 2025, 10:00",
                DateTimeFormatter.ofPattern("d MMM uuuu, HH:mm"));
        LocalDateTime end = LocalDateTime.parse("4 Oct 2025, 11:30",
                DateTimeFormatter.ofPattern("d MMM uuuu, HH:mm"));

        ConsultationTimeslot expected = new ConsultationTimeslot(start, end, "Alice");
        assertParseSuccess(parser, input, new AddConsultationCommand(expected));
    }

    @Test
    public void parse_validHumanReadableWithoutComma_success() {
        // alternate accepted pattern "d MMM uuuu HH:mm"
        String input = " ts/4 Oct 2025 12:00 te/4 Oct 2025 13:15 n/Bob";
        LocalDateTime start = LocalDateTime.parse("4 Oct 2025 12:00",
                DateTimeFormatter.ofPattern("d MMM uuuu HH:mm"));
        LocalDateTime end = LocalDateTime.parse("4 Oct 2025 13:15",
                DateTimeFormatter.ofPattern("d MMM uuuu HH:mm"));

        ConsultationTimeslot expected = new ConsultationTimeslot(start, end, "Bob");
        assertParseSuccess(parser, input, new AddConsultationCommand(expected));
    }

    @Test
    public void parse_missingFields_failure() {
        String missingTs = " te/2025-10-04T11:00:00 n/John Doe";
        String missingTe = " ts/2025-10-04T10:00:00 n/John Doe";
        String missingName = " ts/2025-10-04T10:00:00 te/2025-10-04T11:00:00";

        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddConsultationCommand.MESSAGE_USAGE);

        assertParseFailure(parser, missingTs, expectedMessage);
        assertParseFailure(parser, missingTe, expectedMessage);
        assertParseFailure(parser, missingName, expectedMessage);
    }

    @Test
    public void parse_invalidDatetimeFormat_failure() {
        String badStart = " ts/not-a-date te/2025-10-04T11:00:00 n/John";
        String badEnd = " ts/2025-10-04T10:00:00 te/also-bad n/John";

        String expectedMessage = "Invalid datetime: either wrong format or an impossible calendar date"
                + " (for example, '30 Feb' does not exist). ";

        assertParseFailure(parser, badStart, expectedMessage);
        assertParseFailure(parser, badEnd, expectedMessage);
    }

    @Test
    public void parse_endNotAfterStart_failure() {
        // end equals start -> not after
        String inputEq = " ts/2025-10-04T10:00:00 te/2025-10-04T10:00:00 n/John";
        assertParseFailure(parser, inputEq, "End datetime must be after start datetime.");

        // end before start
        String inputBefore = " ts/2025-10-04T11:00:00 te/2025-10-04T10:00:00 n/John";
        assertParseFailure(parser, inputBefore, "End datetime must be after start datetime.");
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        String input = "preamble ts/2025-10-04T10:00:00 te/2025-10-04T11:00:00 n/John";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddConsultationCommand.MESSAGE_USAGE);
        assertParseFailure(parser, input, expectedMessage);
    }
}
