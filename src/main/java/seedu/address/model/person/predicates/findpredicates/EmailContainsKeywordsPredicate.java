package seedu.address.model.person.predicates.findpredicates;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;



/**
 * Tests that a {@code Person}'s {@code Email} matches any of the keywords given.
 */
public class EmailContainsKeywordsPredicate extends FindPredicate {
    private final List<String> keywords;

    public EmailContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        String email = person.getEmail().toString().toLowerCase();
        return keywords.stream()
                .map(String::toLowerCase)
                .anyMatch(email::contains);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EmailContainsKeywordsPredicate)) {
            return false;
        }

        EmailContainsKeywordsPredicate otherPredicate = (EmailContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public String successMessage() {
        return " email";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}

