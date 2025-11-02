package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.Week;
import seedu.address.model.person.Person;
import seedu.address.model.timeslot.Timeslot;
import seedu.address.storage.Storage;
import seedu.address.ui.TimeslotsWindow;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    // make these constants public so test code can reference them
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data to file: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file due to permission issues: %s";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        CommandResult commandResult;
        Command command = addressBookParser.parseCommand(commandText);
        commandResult = command.execute(model);
        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            String msg = String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage());
            throw new CommandException(msg, e);
        } catch (IOException ioe) {
            String msg = String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage());
            throw new CommandException(msg, ioe);
        }

        // Persist timeslots if model supports it
        try {
            if (model instanceof ModelManager) {
                storage.saveTimeslots(((ModelManager) model).getTimeslots());
            }
        } catch (AccessDeniedException e) {
            // wrap as CommandException with permission detail
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        /*
         * If the command produced timeslot ranges payload, show the UI window here (on the JavaFX thread).
         * We catch IllegalStateException in case JavaFX toolkit is not initialized (e.g., during some tests).
         */
        if (commandResult.getTimeslotRanges() != null && !commandResult.getTimeslotRanges().isEmpty()) {
            try {
                if (model instanceof ModelManager) {
                    List<Timeslot> allTimeslots = ((ModelManager) model).getTimeslots().getTimeslotList();
                    Platform.runLater(() -> TimeslotsWindow.showTimetable(commandResult.getTimeslotRanges(),
                                                                         allTimeslots));
                } else {
                    Platform.runLater(() -> TimeslotsWindow.showTimetable(commandResult.getTimeslotRanges(),
                            Collections.emptyList()));
                }
            } catch (IllegalStateException e) {
                // JavaFX not initialized; ignore UI launch, command result still returned.
            }
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    public Week getCurrentWeek() {
        return model.getCurrentWeek();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
