package seedu.address.model.person.predicates.filterpredicates;

import java.util.function.Predicate;

import seedu.address.model.person.Person;

/**
 * Implements Person Predicate while providing a human-readable success message.
 */
public abstract class FilterPredicate implements Predicate<Person> {
    public abstract String successMessage();

    /**
     * Returns a new FilterPredicate that is the logical AND of this predicate and the other.
     * It is based on Predicates implementation of and
     */
    public FilterPredicate and(FilterPredicate other) {
        assert(other != null);
        FilterPredicate self = this;

        // Returns an anonymous class that has test implemented to perform a logical AND operation
        return new FilterPredicate() {
            @Override public boolean test(Person t) {
                return self.test(t) && other.test(t);
            }
            @Override public String successMessage() {
                return self.successMessage() + " and " + other.successMessage();
            }
        };
    }

}
