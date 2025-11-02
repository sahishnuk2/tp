package seedu.address.model.person.predicates.findpredicates;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.parser.FindCommandParser;
import seedu.address.model.person.Person;

/**
 *  A {@code Predicate} over {@code Person} that OR-combines multiple field-level predicates.
 */
public class PersonContainsKeywordsPredicate extends FindPredicate {

    private static final int MAX_PREDICATES = FindCommandParser
            .PrefixPredicateContainer
            .getAllPrefixPredicate()
            .size();
    private Predicate<Person> combinedPredicate;
    private final List<FindPredicate> predicates;


    /**
     * Constructs a predicate that matches a {@code Person} if any of the provided predicates match.
     *
     * @param predicates a non-empty list of individual field {@code Predicate}
     *                  targeting different {@code Person} fields.
     */
    public PersonContainsKeywordsPredicate(List<FindPredicate> predicates) {
        this.predicates = predicates;
        combinedPredicate = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            combinedPredicate = combinedPredicate.or(predicates.get(i));
        }
    }

    public List<Predicate<Person>> getPredicates() {
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
        if (!(other instanceof PersonContainsKeywordsPredicate)) {
            return false;
        }

        PersonContainsKeywordsPredicate otherPredicate = (PersonContainsKeywordsPredicate) other;

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

        List<String> keywords = getKeywords();
        StringBuilder success = new StringBuilder();
        StringBuilder keywordsString = new StringBuilder();
        for (int i = 0; i < keywords.size() - 1; i++) {
            keywordsString.append(keywords.get(i))
                    .append(", ");
        }
        keywordsString.append(keywords.get(keywords.size() - 1));
        StringBuilder fieldsString = new StringBuilder();
        if (predicates.size() == MAX_PREDICATES) {
            fieldsString.append(" all");
        } else {
            for (int i = 0; i < predicates.size() - 1; i++) {
                fieldsString.append(predicates.get(i).successMessage())
                        .append(", ");
            }
            fieldsString.append(predicates.get(predicates.size() - 1).successMessage());
        }
        success.append(keywordsString).append(" within").append(fieldsString).append(" fields.");



        return success.toString();
    }

    @Override
    public List<String> getKeywords() {
        return predicates.get(0).getKeywords();
    }

}
