package seedu.address.model.person.predicates.findpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.EMAIL_ALICE_EDUSG_COM;
import static seedu.address.testutil.TestConstants.EMAIL_ALICE_NUS_EDU_SG;
import static seedu.address.testutil.TestConstants.EMAIL_ALICE_NUS_EDU_SG_UPPER;
import static seedu.address.testutil.TestConstants.EMAIL_ALICE_SCHOOL_ORG;
import static seedu.address.testutil.TestConstants.EMAIL_EDU_SG_AT_DOMAIN_COM;
import static seedu.address.testutil.TestConstants.EMAIL_WITH_MIDDLE_EDU_SG;
import static seedu.address.testutil.TestConstants.EMAIL_XYZ_JKL_ABC;
import static seedu.address.testutil.TestConstants.EMAIL_X_GMAIL_COM;
import static seedu.address.testutil.TestConstants.KEYWORD_ALICE;
import static seedu.address.testutil.TestConstants.KEYWORD_ALICE_CAP;
import static seedu.address.testutil.TestConstants.KEYWORD_EDU;
import static seedu.address.testutil.TestConstants.KEYWORD_EDU_DOT_SG;
import static seedu.address.testutil.TestConstants.KEYWORD_GITHUB;
import static seedu.address.testutil.TestConstants.KEYWORD_GMAIL;
import static seedu.address.testutil.TestConstants.KEYWORD_NUS;
import static seedu.address.testutil.TestConstants.KEYWORD_NUS_DOT_EDU;
import static seedu.address.testutil.TestConstants.KEYWORD_YAHOO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class EmailContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList(KEYWORD_NUS_DOT_EDU);
        List<String> secondPredicateKeywordList = Arrays.asList(KEYWORD_GMAIL, KEYWORD_EDU);

        EmailContainsKeywordsPredicate firstPredicate =
                new EmailContainsKeywordsPredicate(firstPredicateKeywordList);
        EmailContainsKeywordsPredicate secondPredicate =
                new EmailContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        EmailContainsKeywordsPredicate firstPredicateCopy =
                new EmailContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_emailContainsKeywords_returnsTrue() {
        // One keyword
        EmailContainsKeywordsPredicate predicate =
                new EmailContainsKeywordsPredicate(Collections.singletonList(KEYWORD_NUS_DOT_EDU));
        assertTrue(predicate.test(new PersonBuilder().withEmail(EMAIL_ALICE_NUS_EDU_SG_UPPER).build()));

        // Multiple keywords
        predicate = new EmailContainsKeywordsPredicate(Arrays.asList(KEYWORD_GMAIL, KEYWORD_ALICE));
        assertTrue(predicate.test(new PersonBuilder().withEmail(EMAIL_ALICE_SCHOOL_ORG).build())); // "alice" matches
        assertTrue(predicate.test(new PersonBuilder().withEmail(EMAIL_X_GMAIL_COM).build())); // "gmail" matches
    }

    @Test
    public void test_emailDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        EmailContainsKeywordsPredicate predicate =
                new EmailContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withEmail(EMAIL_ALICE_NUS_EDU_SG).build()));

        // Non-matching keyword
        predicate = new EmailContainsKeywordsPredicate(Collections.singletonList(KEYWORD_YAHOO));
        assertFalse(predicate.test(new PersonBuilder().withEmail(EMAIL_ALICE_NUS_EDU_SG).build()));

        // Keywords match other fields, but not email
        predicate = new EmailContainsKeywordsPredicate(Arrays.asList(KEYWORD_ALICE_CAP, KEYWORD_GITHUB));
        assertFalse(predicate.test(new PersonBuilder()
                .withName(KEYWORD_ALICE_CAP)
                .withGithubUsername(KEYWORD_GITHUB)
                .withEmail(EMAIL_XYZ_JKL_ABC)
                .build()));
    }

    @Test
    public void test_substringPositions_returnsExpected() {
        EmailContainsKeywordsPredicate predicate =
                new EmailContainsKeywordsPredicate(Collections.singletonList(KEYWORD_EDU_DOT_SG));

        // Present return true, different positions
        assertTrue(predicate.test(new PersonBuilder().withEmail(EMAIL_ALICE_NUS_EDU_SG).build())); // end
        assertTrue(predicate.test(new PersonBuilder().withEmail(EMAIL_EDU_SG_AT_DOMAIN_COM).build())); // start
        assertTrue(predicate.test(new PersonBuilder().withEmail(EMAIL_WITH_MIDDLE_EDU_SG).build())); // middle

        // Not present as contiguous substring return false
        assertFalse(predicate.test(new PersonBuilder().withEmail(EMAIL_ALICE_EDUSG_COM).build())); // missing dot
    }

    @Test
    public void getKeywords_returnsOriginalList() {
        List<String> list = Arrays.asList(KEYWORD_NUS, KEYWORD_EDU);
        EmailContainsKeywordsPredicate predicate = new EmailContainsKeywordsPredicate(list);

        // Same contents
        assertEquals(list, predicate.getKeywords());
    }

    @Test
    public void successMessage_returnsExpected() {
        EmailContainsKeywordsPredicate predicate =
                new EmailContainsKeywordsPredicate(Collections.singletonList(KEYWORD_GMAIL));
        assertEquals(" email", predicate.successMessage());
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of(KEYWORD_NUS, KEYWORD_EDU);
        EmailContainsKeywordsPredicate predicate = new EmailContainsKeywordsPredicate(keywords);

        String expected = EmailContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
