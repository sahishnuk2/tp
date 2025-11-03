package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's GitHub Username in the githubUsername book.
 * Guarantees: immutable
 */
public class GithubUsername {

    public static final String MESSAGE_CONSTRAINTS = "GitHub usernames must be 1–39 characters long "
            + "and may include letters, numbers, and hyphens. "
            + "They cannot start or end with a hyphen or contain consecutive hyphens.";

    /*
     * The first character of the username must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     *
     * Must be 1 to 39 characters long
     * Can only contain letters (A–Z, a–z), digits (0–9), and hyphens (-)
     * Cannot start or end with a hyphen
     * Cannot contain consecutive hyphens (“--”)
     */
    public static final String VALIDATION_REGEX = "^(?!.*--)(?!-)[a-zA-Z0-9-]{1,39}(?<!-)$";

    public final String value;

    /**
     * Constructs an {@code GithubUsername}.
     *
     * @param username A valid GitHub username.
     */
    public GithubUsername(String username) {
        requireNonNull(username);
        checkArgument(isValidGithubUsername(username), MESSAGE_CONSTRAINTS);
        value = username;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidGithubUsername(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GithubUsername)) {
            return false;
        }

        GithubUsername otherGithubUsername = (GithubUsername) other;
        return value.equals(otherGithubUsername.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
