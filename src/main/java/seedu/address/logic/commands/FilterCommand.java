package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.predicates.filterpredicates.FilterPredicate;


/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String ATTENDED_PERCENTAGE_USAGE = "Lab attended comparison must contain an operator ==, >=,"
            + " <=, >, < and an integer to compare with from 0-100 in percents.\n"
            + "The % symbol after the value is optional.\n";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters all persons whose exercise or lab attendance match the "
            + "specified statuses (case-insensitive) and displays them as a list with index numbers.\n"
            + "Exercise completed status Y - yes, N - not yet, O - overdue.\n"
            + "Lab attended status Y - yes, N - not yet, A - absent.\n"
            + "Exercise index and lab index must always be followed by status.\n"
            + ATTENDED_PERCENTAGE_USAGE
            + "Parameters: [ei/EXERCISE INDEX s/exercise status]... [l/LAB INDEX s/lab status]... "
            + "[la/COMPARISON]...\n"
            + "Example: " + COMMAND_WORD + " ei/1 s/Y \n";


    private final FilterPredicate predicate;

    public FilterCommand(FilterPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        // Include a human-readable summary of the filter conditions.
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW
                        + "\nFiltering for students who "
                        + predicate.successMessage()
                        + ".", model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFindCommand = (FilterCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
