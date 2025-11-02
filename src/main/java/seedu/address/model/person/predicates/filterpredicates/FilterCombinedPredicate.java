package seedu.address.model.person.predicates.filterpredicates;

import java.util.List;

import seedu.address.model.person.Person;

/**
 *  A {@code Predicate} over {@code Person} that AND-combines exercise and lab attendance predicates.
 */
public class FilterCombinedPredicate extends FilterPredicate {

    private FilterPredicate combinedPredicate;
    private final List<FilterPredicate> predicates;

    /**
     * Constructs a predicate that matches a {@code Person} if all of the provided predicates match.
     *
     * @param predicates a non-empty list of individual field {@code Predicate}
     *                  targeting different {@code Person} fields.
     */
    public FilterCombinedPredicate(List<FilterPredicate> predicates) {
        this.predicates = predicates;
        combinedPredicate = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            combinedPredicate = combinedPredicate.and(predicates.get(i));
        }
    }

    public List<FilterPredicate> getPredicates() {
        return List.copyOf(predicates);
    }

    @Override
    public boolean test(Person person) {
        return combinedPredicate.test(person);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCombinedPredicate)) {
            return false;
        }

        FilterCombinedPredicate otherPredicate = (FilterCombinedPredicate) other;

        if (predicates.size() != otherPredicate.predicates.size()) {
            return false;
        }

        for (int i = 0; i < predicates.size(); i++) {
            if (!predicates.get(i).equals(
                    otherPredicate.getPredicates().get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String successMessage() {
        StringBuilder success = new StringBuilder();
        for (int i = 0; i < predicates.size() - 1; i++) {
            success.append(predicates.get(i).successMessage())
                    .append(" and ");
        }
        success.append(predicates.get(predicates.size() - 1).successMessage());
        return success.toString();
    }


}
