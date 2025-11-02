package seedu.address.model.person.predicates.findpredicates;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;


/**
 * Tests that a {@code Person}'s {@code Phone Number} matches any of the keywords given.
 */
public class PhoneContainsKeywordsPredicate extends FindPredicate {
    private final List<String> keywords;

    public PhoneContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        String phone = person.getPhone().toString();
        return keywords.stream()
                .map(String::toLowerCase)
                .anyMatch(phone::contains);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PhoneContainsKeywordsPredicate)) {
            return false;
        }

        PhoneContainsKeywordsPredicate otherPredicate = (PhoneContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public String successMessage() {
        return " phone number";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
