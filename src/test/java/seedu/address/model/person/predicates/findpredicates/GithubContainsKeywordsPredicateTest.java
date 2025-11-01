package seedu.address.model.person.predicates.findpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.EMAIL_ALICE_GMAIL;
import static seedu.address.testutil.TestConstants.GH_ALICE_GH;
import static seedu.address.testutil.TestConstants.GH_CS2103T;
import static seedu.address.testutil.TestConstants.GH_GI;
import static seedu.address.testutil.TestConstants.GH_GIKT;
import static seedu.address.testutil.TestConstants.GH_GITNAME;
import static seedu.address.testutil.TestConstants.GH_GIT_HUB;
import static seedu.address.testutil.TestConstants.GH_GIT_TEST_HUB_NAME;
import static seedu.address.testutil.TestConstants.GH_IAMUSINGGIT;
import static seedu.address.testutil.TestConstants.GH_IT;
import static seedu.address.testutil.TestConstants.GH_MYGITNAME;
import static seedu.address.testutil.TestConstants.GH_TEAMDEV123;
import static seedu.address.testutil.TestConstants.GH_TESTNAME;
import static seedu.address.testutil.TestConstants.GH_USER_DASH_ER;
import static seedu.address.testutil.TestConstants.KEYWORD_ALICE;
import static seedu.address.testutil.TestConstants.KEYWORD_ALICE_CAP;
import static seedu.address.testutil.TestConstants.KEYWORD_DEV;
import static seedu.address.testutil.TestConstants.KEYWORD_GIT;
import static seedu.address.testutil.TestConstants.KEYWORD_GMAIL;
import static seedu.address.testutil.TestConstants.KEYWORD_HUB;
import static seedu.address.testutil.TestConstants.KEYWORD_OPS;
import static seedu.address.testutil.TestConstants.KEYWORD_RANDOM;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;


public class GithubContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList(KEYWORD_DEV);
        List<String> secondPredicateKeywordList = Arrays.asList(KEYWORD_GIT, KEYWORD_HUB);

        GithubContainsKeywordsPredicate firstPredicate =
                new GithubContainsKeywordsPredicate(firstPredicateKeywordList);
        GithubContainsKeywordsPredicate secondPredicate =
                new GithubContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        GithubContainsKeywordsPredicate firstPredicateCopy =
                new GithubContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_githubContainsKeywords_returnsTrue() {
        // One keyword
        GithubContainsKeywordsPredicate predicate =
                new GithubContainsKeywordsPredicate(Collections.singletonList(KEYWORD_DEV));
        assertTrue(predicate.test(new PersonBuilder().withGithubUsername(GH_TEAMDEV123).build()));

        // Multiple keywords
        predicate = new GithubContainsKeywordsPredicate(Arrays.asList(KEYWORD_ALICE, KEYWORD_HUB));
        assertTrue(predicate.test(new PersonBuilder().withGithubUsername(GH_ALICE_GH).build())); // "alice" matches
        assertTrue(predicate.test(new PersonBuilder().withGithubUsername(GH_GIT_HUB).build())); // "hub" matches

        // Mixed characters and hyphens
        predicate = new GithubContainsKeywordsPredicate(Collections.singletonList(KEYWORD_HUB));
        assertTrue(predicate.test(new PersonBuilder().withGithubUsername(GH_GIT_TEST_HUB_NAME).build()));
    }

    @Test
    public void test_githubDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        GithubContainsKeywordsPredicate predicate =
                new GithubContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withGithubUsername(GH_TESTNAME).build()));

        // Non-matching keyword
        predicate = new GithubContainsKeywordsPredicate(Collections.singletonList(KEYWORD_RANDOM));
        assertFalse(predicate.test(new PersonBuilder().withGithubUsername(GH_CS2103T).build()));

        // Keywords match other fields, but not github username
        predicate = new GithubContainsKeywordsPredicate(Arrays.asList(KEYWORD_ALICE_CAP, KEYWORD_GMAIL));
        assertFalse(predicate.test(new PersonBuilder()
                .withName(KEYWORD_ALICE_CAP)
                .withEmail(EMAIL_ALICE_GMAIL)
                .withGithubUsername(GH_USER_DASH_ER)
                .build()));
    }

    @Test
    public void test_substringPositions_returnsExpected() {
        GithubContainsKeywordsPredicate predicate =
                new GithubContainsKeywordsPredicate(Collections.singletonList(KEYWORD_GIT));

        // Present return true, different positions
        assertTrue(predicate.test(new PersonBuilder().withGithubUsername(GH_GITNAME).build())); // start
        assertTrue(predicate.test(new PersonBuilder().withGithubUsername(GH_MYGITNAME).build())); // middle
        assertTrue(predicate.test(new PersonBuilder().withGithubUsername(GH_IAMUSINGGIT).build())); // end

        // Not present as contiguous substring return false
        assertFalse(predicate.test(new PersonBuilder().withGithubUsername(GH_GIKT).build())); // non-contiguous
        assertFalse(predicate.test(new PersonBuilder().withGithubUsername(GH_GI).build())); // partial
        assertFalse(predicate.test(new PersonBuilder().withGithubUsername(GH_IT).build())); // partial
    }

    @Test
    public void getKeywords_returnsOriginalList() {
        List<String> list = Arrays.asList(KEYWORD_DEV, KEYWORD_OPS);
        GithubContainsKeywordsPredicate predicate = new GithubContainsKeywordsPredicate(list);

        // Same contents
        assertEquals(list, predicate.getKeywords());
    }

    @Test
    public void successMessage_returnsExpected() {
        GithubContainsKeywordsPredicate predicate =
                new GithubContainsKeywordsPredicate(Collections.singletonList(KEYWORD_DEV));
        assertEquals(" github username", predicate.successMessage());
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("ghuser", KEYWORD_HUB);
        GithubContainsKeywordsPredicate predicate = new GithubContainsKeywordsPredicate(keywords);

        String expected = GithubContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
