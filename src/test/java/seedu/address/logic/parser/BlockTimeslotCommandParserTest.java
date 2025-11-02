package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.BlockTimeslotCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.timeslot.Timeslot;

public class BlockTimeslotCommandParserTest {

    private final BlockTimeslotCommandParser parser = new BlockTimeslotCommandParser();

    @Test
    public void parse_isoFormat_success() throws Exception {
        String args = "ts/2025-10-04T10:00:00 te/2025-10-04T13:00:00";
        BlockTimeslotCommand cmd = parser.parse(args);

        Timeslot expected = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 13, 0));
        assertEquals(new BlockTimeslotCommand(expected), cmd);
    }

    @Test
    public void parse_humanFormatWithComma_success() throws Exception {
        String args = "ts/4 Oct 2025, 10:00 te/4 Oct 2025, 13:00";
        BlockTimeslotCommand cmd = parser.parse(args);

        Timeslot expected = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 13, 0));
        assertEquals(new BlockTimeslotCommand(expected), cmd);
    }

    @Test
    public void parse_humanFormatNoComma_success() throws Exception {
        String args = "ts/4 Oct 2025 10:00 te/4 Oct 2025 13:00";
        BlockTimeslotCommand cmd = parser.parse(args);

        Timeslot expected = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 13, 0));
        assertEquals(new BlockTimeslotCommand(expected), cmd);
    }

    @Test
    public void parse_invalidFormat_throwsParseException() {
        String args = "ts/invalid te/alsoinvalid";
        assertThrows(ParseException.class, () -> parser.parse(args));
    }

    @Test
    public void parse_timeBefore08_throwsParseException() {
        String args = "ts/2025-10-04T07:00:00 te/2025-10-04T09:00:00";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(args));
        assertTrue(ex.getMessage().contains("Timeslot must be within 08:00 to 24:00 (midnight of next day allowed)"));
    }

    @Test
    public void parse_invalidCalendarDate_throwsParseException() {
        // 30 Feb 2025 does not exist
        String args = "ts/2025-02-30T10:00:00 te/2025-02-30T11:00:00";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(args));
        assertTrue(ex.getMessage().contains("Invalid datetime: either wrong format or an impossible calendar date"));
    }

    @Test
    public void parse_nonPositiveYear_throwsParseException() {
        // Year 0000 -> should trigger year validation (<= 0)
        String args = "ts/0000-01-01T10:00:00 te/2025-10-04T11:00:00";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(args));
        assertTrue(ex.getMessage().contains("Invalid timeslot"));
        assertTrue(ex.getMessage().toLowerCase().contains("year must be a positive"));
    }
}
