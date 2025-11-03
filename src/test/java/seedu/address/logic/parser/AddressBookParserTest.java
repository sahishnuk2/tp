package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_CRITERION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LAB;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddConsultationCommand;
import seedu.address.logic.commands.BlockTimeslotCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearTimeslotsCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.GetConsultationsCommand;
import seedu.address.logic.commands.GetTimeslotCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.UnblockTimeslotCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.sortcriterion.NameSortCriterion;
import seedu.address.model.person.sortcriterion.SortCriterion;
import seedu.address.model.timeslot.ConsultationTimeslot;
import seedu.address.model.timeslot.Timeslot;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_addConsultation() throws Exception {
        // ensure add-consultation command is recognized by the parser
        String input = "add-consultation ts/2025-10-04T10:00:00 te/2025-10-04T11:00:00 n/John";
        AddConsultationCommand cmd = (AddConsultationCommand) parser.parseCommand(input);
        ConsultationTimeslot expectedTs = new ConsultationTimeslot(
                LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 11, 0),
                "John");
        assertEquals(new AddConsultationCommand(expectedTs), cmd);
    }

    @Test
    public void parseCommand_blockTimeslot() throws Exception {
        String input = "block-timeslot ts/2025-10-04T10:00:00 te/2025-10-04T11:00:00";
        BlockTimeslotCommand cmd = (BlockTimeslotCommand) parser.parseCommand(input);
        Timeslot expected = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 11, 0));
        assertEquals(new BlockTimeslotCommand(expected), cmd);
    }

    @Test
    public void parseCommand_unblockTimeslot() throws Exception {
        String input = "unblock-timeslot ts/2025-10-04T10:00:00 te/2025-10-04T11:00:00";
        UnblockTimeslotCommand cmd = (UnblockTimeslotCommand) parser.parseCommand(input);
        Timeslot expected = new Timeslot(LocalDateTime.of(2025, 10, 4, 10, 0),
                LocalDateTime.of(2025, 10, 4, 11, 0));
        assertEquals(new UnblockTimeslotCommand(expected), cmd);
    }

    @Test
    public void parseCommand_getTimeslotsAndConsultations() throws Exception {
        // get-timeslots (no args)
        GetTimeslotCommand gtCmd = (GetTimeslotCommand) parser.parseCommand(GetTimeslotCommand.COMMAND_WORD);
        assertEquals(new GetTimeslotCommand(), gtCmd);
        // get-consultations (no args)
        GetConsultationsCommand gcCmd = (GetConsultationsCommand) parser.parseCommand(GetConsultationsCommand.COMMAND_WORD);
        assertEquals(new GetConsultationsCommand(), gcCmd);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_clearTimeslots() throws Exception {
        ClearTimeslotsCommand cmd = (ClearTimeslotsCommand) parser.parseCommand(ClearTimeslotsCommand.COMMAND_WORD);
        assertEquals(new ClearTimeslotsCommand(), cmd);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(new MultiIndex(INDEX_FIRST_PERSON)), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(new MultiIndex(INDEX_FIRST_PERSON), descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }


    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_markAttendance() throws Exception {
        MarkAttendanceCommand command = (MarkAttendanceCommand) parser.parseCommand(
                MarkAttendanceCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " "
                        + PREFIX_LAB_NUMBER + INDEX_FIRST_LAB.getOneBased() + " " + PREFIX_STATUS + "y");
        assertEquals(new MarkAttendanceCommand(new MultiIndex(INDEX_FIRST_PERSON), INDEX_FIRST_LAB, true), command);
    }

    @Test
    public void parseCommand_sort() throws Exception {
        final SortCriterion sortCriterion = new NameSortCriterion();

        SortCommand command = (SortCommand) parser.parseCommand(SortCommand.COMMAND_WORD
                + " " + PREFIX_SORT_CRITERION + sortCriterion);
        assertEquals(new SortCommand(sortCriterion), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
