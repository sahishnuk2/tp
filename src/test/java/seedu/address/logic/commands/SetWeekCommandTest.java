package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.Week;
import seedu.address.model.person.LabList;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class SetWeekCommandTest {
    private static final int WEEK_NUMBER_SEVEN = 7;


    @Test
    public void constructor_nullWeek_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SetWeekCommand(null));
    }

    @Test
    public void constructor_validWeek_throwsNullPointerException() {
        assertDoesNotThrow(() -> new SetWeekCommand(new Week(Week.MAX_WEEK)));
    }

    @Test
    public void execute_validWeek_setSuccessful() throws Exception {
        ModelStubSettingWeek modelStub = new ModelStubSettingWeek();
        Week targetWeek = new Week(WEEK_NUMBER_SEVEN);

        CommandResult commandResult = new SetWeekCommand(targetWeek).execute(modelStub);

        // Check the feedback message is correct
        assertEquals(String.format(SetWeekCommand.MESSAGE_SUCCESS, WEEK_NUMBER_SEVEN, 0),
                commandResult.getFeedbackToUser());

        // Check the week was actually set in the model
        assertEquals(targetWeek, modelStub.getCurrentWeek());

        // Check if week number was set in LabList and ExerciseList
        // Unable to test out ExerciseList as there is no getter method
        assertEquals(WEEK_NUMBER_SEVEN, LabList.getCurrentWeek());
    }

    // More of integration testing it checks with the actual addressbook
    @Test
    public void execute_withPersons_updatesAllPersons() throws Exception {
        ModelStubSettingWeek modelStub = new ModelStubSettingWeek();
        Person person1 = new PersonBuilder().withName("Alice").withStudentId("A0108045X").build();
        Person person2 = new PersonBuilder().withName("Bob").withStudentId("A0308245X").build();
        Person person3 = new PersonBuilder().withName("Charlie").withStudentId("A0438045X").build();

        modelStub.addPerson(person1);
        modelStub.addPerson(person2);
        modelStub.addPerson(person3);

        Week targetWeek = new Week(WEEK_NUMBER_SEVEN);
        CommandResult result = new SetWeekCommand(targetWeek).execute(modelStub);

        // Verify setPerson was called 3 times (once for each person)
        // This indirectly verifies copy() was called for each person's LabList and ExerciseTracker
        assertEquals(3, modelStub.getPersonUpdateCount());

        // Verify the success message shows 3 updated students
        assertEquals(String.format(SetWeekCommand.MESSAGE_SUCCESS, WEEK_NUMBER_SEVEN, 3),
                result.getFeedbackToUser());
    }

    @Test
    public void equals() {
        Week weekMin = new Week(Week.MIN_WEEK);
        Week week0 = new Week(Week.MIN_WEEK);
        Week weekMax = new Week(Week.MAX_WEEK);
        SetWeekCommand setWeekCommand1 = new SetWeekCommand(weekMin);
        SetWeekCommand setWeekCommand2 = new SetWeekCommand(week0);
        SetWeekCommand setWeekCommand3 = new SetWeekCommand(weekMax);

        // Null check
        assertNotEquals(setWeekCommand1, null);

        // Same object
        assertEquals(setWeekCommand1, setWeekCommand1);

        // Same week number
        assertEquals(setWeekCommand1, setWeekCommand2);

        // Different week
        assertNotEquals(setWeekCommand1, setWeekCommand3);
    }

    /**
     * A Model stub that accepts week setting and updates to persons.
     */
    private class ModelStubSettingWeek extends ModelStub {
        private Week currentWeek = new Week(Week.MIN_WEEK);
        private final AddressBook addressBook = new AddressBook();
        private int personUpdateCount = 0;

        @Override
        public void setCurrentWeek(Week currentWeek) {
            requireNonNull(currentWeek);
            this.currentWeek = currentWeek;
        }

        @Override
        public Week getCurrentWeek() {
            return currentWeek;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            requireNonNull(target);
            requireNonNull(editedPerson);
            personUpdateCount++;
        }

        @Override
        public void saveAddressBook() {
            // Do nothing - this is a stub for testing
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            addressBook.addPerson(person);
        }

        public int getPersonUpdateCount() {
            return personUpdateCount;
        }
    }
}
