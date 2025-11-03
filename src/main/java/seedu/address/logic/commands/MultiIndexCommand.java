package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * An abstract command that operates on one or more persons identified by a {@link MultiIndex}.
 * Concrete subclasses define the specific action to perform on each selected person.
 */
public abstract class MultiIndexCommand extends Command {

    protected final MultiIndex multiIndex;

    /**
     * Constructs a {@code MultiIndexCommand} with the specified {@code MultiIndex}.
     */
    protected MultiIndexCommand(MultiIndex multiIndex) {
        this.multiIndex = multiIndex;
    }

    /**
     * Executes the command for each person represented by {@code multiIndex}.
     *
     * @param model The model containing the person list.
     * @return A {@code CommandResult} representing the outcome.
     * @throws CommandException if an index is invalid or the action fails.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = getPeople(model);

        model.saveAddressBook();

        List<Person> updatedPersons = new ArrayList<>();
        List<Person> personsToUpdate = multiIndex
                .toIndexList()
                .stream()
                .map(index -> lastShownList.get(index.getZeroBased()))
                .toList();
        for (Person person : personsToUpdate) {
            Person editedPerson = applyActionToPerson(model, person);
            if (editedPerson != null) {
                updatedPersons.add(editedPerson);
            }
        }

        return buildResult(updatedPersons);
    }

    private List<Person> getPeople(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        assert multiIndex.getLowerBound().getOneBased() >= 1
                : "lower bound cannot be less than 1";
        if (multiIndex.getUpperBound().getOneBased() > lastShownList.size()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_INVALID_INDEX_FORMAT,
                    multiIndex.getUpperBound().getOneBased(),
                    "student",
                    1,
                    lastShownList.size()
                )
            );
        }
        return lastShownList;
    }

    /**
     * Applies the command's specific action to the given person.
     * @param model The model to update.
     * @param person The person to modify.
     * @return The updated {@code Person} if modified, or {@code null} if unchanged.
     * @throws CommandException If an operation fails.
     */
    protected abstract Person applyActionToPerson(Model model, Person person) throws CommandException;

    /**
     * Builds the final {@code CommandResult} after processing all persons.
     * @param updatedPersons List of persons successfully modified.
     * @return A {@code CommandResult} describing the outcome.
     */
    protected abstract CommandResult buildResult(List<Person> updatedPersons);

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MultiIndexCommand)) {
            return false;
        }
        MultiIndexCommand otherCommand = (MultiIndexCommand) other;
        return multiIndex.equals(otherCommand.multiIndex);
    }
}
