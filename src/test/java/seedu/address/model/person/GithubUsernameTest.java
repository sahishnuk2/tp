package seedu.address.model.person;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GithubUsernameTest {

    // ---------- Validation Tests ----------

    @Test
    void isValidGithubUsername_validUsernames_returnTrue() {
        // Minimum and maximum length
        assertTrue(GithubUsername.isValidGithubUsername("a"));
        assertTrue(GithubUsername.isValidGithubUsername("a".repeat(39)));

        // Letters, numbers, and hyphens (but no invalid placements)
        assertTrue(GithubUsername.isValidGithubUsername("JohnDoe"));
        assertTrue(GithubUsername.isValidGithubUsername("john-doe"));
        assertTrue(GithubUsername.isValidGithubUsername("user123"));
        assertTrue(GithubUsername.isValidGithubUsername("A1b2C3"));
        assertTrue(GithubUsername.isValidGithubUsername("abc-def-123"));
    }

    @Test
    void isValidGithubUsername_invalidUsernames_returnFalse() {
        // Empty or too long
        assertFalse(GithubUsername.isValidGithubUsername(""));
        assertFalse(GithubUsername.isValidGithubUsername("a".repeat(40)));

        // Starts or ends with hyphen
        assertFalse(GithubUsername.isValidGithubUsername("-abc"));
        assertFalse(GithubUsername.isValidGithubUsername("abc-"));

        // Contains consecutive hyphens
        assertFalse(GithubUsername.isValidGithubUsername("ab--cd"));

        // Contains invalid characters
        assertFalse(GithubUsername.isValidGithubUsername("user!"));
        assertFalse(GithubUsername.isValidGithubUsername("user name"));
        assertFalse(GithubUsername.isValidGithubUsername("user@123"));
    }

    // ---------- Constructor Tests ----------

    @Test
    void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new GithubUsername(null));
    }

    @Test
    void constructor_invalidUsername_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new GithubUsername("-invalid"));
        assertThrows(IllegalArgumentException.class, () -> new GithubUsername("ab--cd"));
    }

    @Test
    void constructor_validUsername_success() {
        GithubUsername username = new GithubUsername("validUser");
        assertEquals("validUser", username.value);
    }

    // ---------- equals() Tests ----------

    @Test
    void equals_sameObject_returnsTrue() {
        GithubUsername username = new GithubUsername("same");
        assertTrue(username.equals(username));
    }

    @Test
    void equals_null_returnsFalse() {
        GithubUsername username = new GithubUsername("same");
        assertFalse(username.equals(null));
    }

    @Test
    void equals_differentType_returnsFalse() {
        GithubUsername username = new GithubUsername("same");
        assertFalse(username.equals("same")); // comparing to String
    }

    @Test
    void equals_differentValue_returnsFalse() {
        GithubUsername u1 = new GithubUsername("user1");
        GithubUsername u2 = new GithubUsername("user2");
        assertNotEquals(u1, u2);
    }

    @Test
    void equals_sameValue_returnsTrue() {
        GithubUsername u1 = new GithubUsername("sameUser");
        GithubUsername u2 = new GithubUsername("sameUser");
        assertEquals(u1, u2);
    }

    // ---------- toString() and hashCode() ----------

    @Test
    void toString_returnsValue() {
        GithubUsername username = new GithubUsername("CoolUser");
        assertEquals("CoolUser", username.toString());
    }

    @Test
    void hashCode_sameValue_sameHash() {
        GithubUsername u1 = new GithubUsername("userX");
        GithubUsername u2 = new GithubUsername("userX");
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void hashCode_differentValue_differentHash() {
        GithubUsername u1 = new GithubUsername("userX");
        GithubUsername u2 = new GithubUsername("userY");
        assertNotEquals(u1.hashCode(), u2.hashCode());
    }
}
