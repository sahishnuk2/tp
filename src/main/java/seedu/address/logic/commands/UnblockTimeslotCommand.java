package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.timeslot.Timeslot;

/**
 * Removes a timeslot from the stored timeslots.
 */
public class UnblockTimeslotCommand extends Command {

    public static final String COMMAND_WORD = "unblock-timeslot";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes a portion of stored timeslots that overlap the given range by trimming or splitting them.\n"
            + "Parameters: ts/START_DATETIME te/END_DATETIME\n"
            + "Accepted datetime formats:\n"
            + " - ISO_LOCAL_DATE_TIME: 2023-10-01T09:00:00\n"
            + " - Human-friendly: 4 Oct 2025, 10:00\n"
            + "Example: " + COMMAND_WORD + " ts/2025-10-04T10:00:00 te/2025-10-04T13:00:00";

    public static final String MESSAGE_SUCCESS = "Updated stored timeslots";
    public static final String MESSAGE_TIMESLOT_NOT_FOUND = "No stored timeslot overlaps the given range: %1$s";

    private final Timeslot timeslot;

    public UnblockTimeslotCommand(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Objects.requireNonNull(timeslot);

        // find stored timeslots that overlap the given range
        List<Timeslot> stored = model.getTimeslots().getTimeslotList();
        List<Timeslot> overlapping = stored.stream()
                .filter(s -> overlaps(s, timeslot))
                .collect(Collectors.toList());

        if (overlapping.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_TIMESLOT_NOT_FOUND, timeslot));
        }

        List<Timeslot> toRemove = new ArrayList<>();
        List<Timeslot> toAdd = new ArrayList<>();

        for (Timeslot s : overlapping) {
            // s = [s0, s1], u = [u0, u1]
            boolean coversStart = !timeslot.getStart().isAfter(s.getStart()); // u0 <= s0
            boolean coversEnd = !timeslot.getEnd().isBefore(s.getEnd()); // u1 >= s1

            if (coversStart && coversEnd) {
                // unblock range fully covers stored timeslot -> remove it
                toRemove.add(s);
            } else if (coversStart) {
                // overlap at left side: new stored becomes [u1, s1]
                toRemove.add(s);
                if (timeslot.getEnd().isBefore(s.getEnd())) {
                    toAdd.add(new Timeslot(timeslot.getEnd(), s.getEnd()));
                }
            } else if (coversEnd) {
                // overlap at right side: new stored becomes [s0, u0]
                toRemove.add(s);
                if (timeslot.getStart().isAfter(s.getStart())) {
                    toAdd.add(new Timeslot(s.getStart(), timeslot.getStart()));
                }
            } else {
                // unblock range is strictly inside stored timeslot -> split into two
                toRemove.add(s);
                // left part [s0, u0] and right part [u1, s1] (each valid by construction)
                if (timeslot.getStart().isAfter(s.getStart())) {
                    toAdd.add(new Timeslot(s.getStart(), timeslot.getStart()));
                }
                if (timeslot.getEnd().isBefore(s.getEnd())) {
                    toAdd.add(new Timeslot(timeslot.getEnd(), s.getEnd()));
                }
            }
        }

        // persist previous state for undo
        model.saveAddressBook();

        // apply removals and additions
        toRemove.forEach(model::removeTimeslot);
        for (Timeslot t : toAdd) {
            try {
                model.addTimeslot(t);
            } catch (IllegalArgumentException e) {
                // Convert runtime validation failure into CommandException with a helpful message.
                String userMsg = e.getMessage() != null && e.getMessage().toLowerCase().contains("overlap")
                        ? "Failed to apply unblock: resulting timeslot overlaps an existing timeslot."
                        : "Failed to apply unblock: " + e.getMessage();
                throw new CommandException(userMsg);
            }
        }

        return new CommandResult(MESSAGE_SUCCESS);
    }

    // Two timeslots overlap if their intervals intersect (end > start and start < end).
    private static boolean overlaps(Timeslot a, Timeslot b) {
        return a.getEnd().isAfter(b.getStart()) && a.getStart().isBefore(b.getEnd());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UnblockTimeslotCommand)) {
            return false;
        }
        UnblockTimeslotCommand otherCmd = (UnblockTimeslotCommand) other;
        return timeslot.equals(otherCmd.timeslot);
    }
}
