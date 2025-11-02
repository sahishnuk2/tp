package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.timeslot.Timeslot;

public class UnblockTimeslotCommandTest {

    @Test
    public void execute_removeExactTimeslot_success() throws Exception {
        ModelManager model = new ModelManager();
        Timeslot stored = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 13, 0));
        model.addTimeslot(stored);

        UnblockTimeslotCommand cmd = new UnblockTimeslotCommand(
                new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                              LocalDateTime.of(2025, 10, 4, 13, 0)));

        CommandResult result = cmd.execute(model);

        assertFalse(model.hasTimeslot(stored));
        assertEquals(UnblockTimeslotCommand.MESSAGE_SUCCESS,
                result.getFeedbackToUser());
    }

    @Test
    public void execute_splitStoredTimeslot_success() throws Exception {
        ModelManager model = new ModelManager();
        Timeslot stored = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 13, 0));
        model.addTimeslot(stored);

        // Unblock a middle portion -> expect stored split into two
        UnblockTimeslotCommand cmd = new UnblockTimeslotCommand(
                new Timeslot(LocalDateTime.of(2025, 10, 4, 11, 0),
                              LocalDateTime.of(2025, 10, 4, 12, 0)));

        CommandResult result = cmd.execute(model);

        Timeslot left = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 11, 0));
        Timeslot right = new Timeslot(LocalDateTime.of(2025, 10, 4, 12, 0),
                LocalDateTime.of(2025, 10, 4, 13, 0));

        assertFalse(model.hasTimeslot(stored));
        assertTrue(model.hasTimeslot(left));
        assertTrue(model.hasTimeslot(right));
        assertEquals(UnblockTimeslotCommand.MESSAGE_SUCCESS,
                result.getFeedbackToUser());
    }

    @Test
    public void execute_trimRightSide_success() throws Exception {
        ModelManager model = new ModelManager();
        Timeslot stored = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 13, 0));
        model.addTimeslot(stored);

        // Unblock overlapping the right edge -> expect stored trimmed to left portion
        UnblockTimeslotCommand cmd = new UnblockTimeslotCommand(
                new Timeslot(LocalDateTime.of(2025, 10, 4, 12, 0),
                              LocalDateTime.of(2025, 10, 4, 14, 0)));

        CommandResult result = cmd.execute(model);

        Timeslot remaining = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 12, 0));

        assertFalse(model.hasTimeslot(stored));
        assertTrue(model.hasTimeslot(remaining));
        assertEquals(UnblockTimeslotCommand.MESSAGE_SUCCESS,
                result.getFeedbackToUser());
    }

    @Test
    public void execute_noOverlap_throwsCommandException() {
        ModelManager model = new ModelManager();
        // Stored timeslot is on a different day
        Timeslot stored = new Timeslot(LocalDateTime.of(2025, 10, 5, 10, 0),
                LocalDateTime.of(2025, 10, 5, 11, 0));
        model.addTimeslot(stored);

        Timeslot toUnblock = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                              LocalDateTime.of(2025, 10, 4, 11, 0));
        UnblockTimeslotCommand cmd = new UnblockTimeslotCommand(toUnblock);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));

        assertEquals(String.format(UnblockTimeslotCommand.MESSAGE_TIMESLOT_NOT_FOUND, toUnblock),
                ex.getMessage());
    }
}
