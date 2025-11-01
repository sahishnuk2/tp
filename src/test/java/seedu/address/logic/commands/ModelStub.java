package seedu.address.logic.commands;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyTimeslots;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.Week;
import seedu.address.model.person.Person;

/**
 * A default model stub that have all of the methods failing.
 */
public class ModelStub implements Model {
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public GuiSettings getGuiSettings() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Path getAddressBookFilePath() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setAddressBook(ReadOnlyAddressBook newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasPerson(Person person) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void deletePerson(Person target) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void sortPersonList(Comparator<Person> comparator) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void saveAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean canUndoAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void undoAddressBook() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public ReadOnlyTimeslots getTimeslots() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setTimeslots(seedu.address.model.ReadOnlyTimeslots newData) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public boolean hasTimeslot(seedu.address.model.timeslot.Timeslot timeslot) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void addTimeslot(seedu.address.model.timeslot.Timeslot timeslot) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void removeTimeslot(seedu.address.model.timeslot.Timeslot timeslot) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void clearTimeslots() {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public void setCurrentWeek(Week currentWeek) {
        throw new AssertionError("This method should not be called.");
    }

    @Override
    public Week getCurrentWeek() {
        throw new AssertionError("This method should not be called.");
    }
}
