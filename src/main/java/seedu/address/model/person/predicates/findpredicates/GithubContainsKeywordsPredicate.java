package seedu.address.model.person.predicates.findpredicates;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;


/**
 * Tests that a {@code Person}'s {@code Github Username} matches any of the keywords given.
 */
public class GithubContainsKeywordsPredicate extends FindPredicate {
    private final List<String> keywords;

    public GithubContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        String github = person.getGithubUsername().toString().toLowerCase();
        return keywords.stream()
                .map(String::toLowerCase)
                .anyMatch(github::contains);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GithubContainsKeywordsPredicate)) {
            return false;
        }

        GithubContainsKeywordsPredicate otherPredicate = (GithubContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public String successMessage() {
        return " github username";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
