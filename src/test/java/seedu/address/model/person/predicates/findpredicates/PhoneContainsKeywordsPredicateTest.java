package seedu.address.model.person.predicates.findpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.EMAIL_ALICE_GENERIC;
import static seedu.address.testutil.TestConstants.KEYWORD_12;
import static seedu.address.testutil.TestConstants.KEYWORD_123;
import static seedu.address.testutil.TestConstants.KEYWORD_1234;
import static seedu.address.testutil.TestConstants.KEYWORD_234;
import static seedu.address.testutil.TestConstants.KEYWORD_34;
import static seedu.address.testutil.TestConstants.KEYWORD_456;
import static seedu.address.testutil.TestConstants.KEYWORD_555;
import static seedu.address.testutil.TestConstants.KEYWORD_777;
import static seedu.address.testutil.TestConstants.KEYWORD_888;
import static seedu.address.testutil.TestConstants.KEYWORD_999;
import static seedu.address.testutil.TestConstants.KEYWORD_ALICE_CAP;
import static seedu.address.testutil.TestConstants.KEYWORD_GMAIL;
import static seedu.address.testutil.TestConstants.PHONE_00055511;
import static seedu.address.testutil.TestConstants.PHONE_01234;
import static seedu.address.testutil.TestConstants.PHONE_1020304;
import static seedu.address.testutil.TestConstants.PHONE_123;
import static seedu.address.testutil.TestConstants.PHONE_1234;
import static seedu.address.testutil.TestConstants.PHONE_123456;
import static seedu.address.testutil.TestConstants.PHONE_12345678;
import static seedu.address.testutil.TestConstants.PHONE_234;
import static seedu.address.testutil.TestConstants.PHONE_7778888;
import static seedu.address.testutil.TestConstants.PHONE_90123490;
import static seedu.address.testutil.TestConstants.PHONE_991234;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PhoneContainsKeywordsPredicateTest {
    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList(KEYWORD_123);
        List<String> secondPredicateKeywordList = Arrays.asList(KEYWORD_123, KEYWORD_456);

        PhoneContainsKeywordsPredicate firstPredicate =
                new PhoneContainsKeywordsPredicate(firstPredicateKeywordList);
        PhoneContainsKeywordsPredicate secondPredicate =
                new PhoneContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PhoneContainsKeywordsPredicate firstPredicateCopy =
                new PhoneContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_phoneContainsKeywords_returnsTrue() {
        // One keyword
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Collections.singletonList(KEYWORD_123));
        assertTrue(predicate.test(new PersonBuilder().withPhone(PHONE_12345678).build()));

        // Multiple keywords
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList(KEYWORD_555, KEYWORD_999));
        assertTrue(predicate.test(new PersonBuilder().withPhone(PHONE_00055511).build()));

        // Only one matching keyword among many
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList(KEYWORD_777, KEYWORD_888, KEYWORD_234));
        assertTrue(predicate.test(new PersonBuilder().withPhone(PHONE_01234).build()));
    }

    @Test
    public void test_phoneDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withPhone(PHONE_12345678).build()));

        // Non-matching keyword
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList(KEYWORD_999));
        assertFalse(predicate.test(new PersonBuilder().withPhone(PHONE_12345678).build()));

        // Keywords match other fields, but not phone
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList(KEYWORD_ALICE_CAP, KEYWORD_GMAIL));
        assertFalse(predicate.test(new PersonBuilder()
                .withName(KEYWORD_ALICE_CAP)
                .withEmail(EMAIL_ALICE_GENERIC)
                .withPhone(PHONE_7778888)
                .build()));
    }

    @Test
    public void test_substringPositions_returnsExpected() {
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Arrays.asList(KEYWORD_1234));

        // Present return true, different positions
        assertTrue(predicate.test(new PersonBuilder().withPhone(PHONE_1234).build())); // exact match
        assertTrue(predicate.test(new PersonBuilder().withPhone(PHONE_123456).build())); // start
        assertTrue(predicate.test(new PersonBuilder().withPhone(PHONE_90123490).build())); // middle
        assertTrue(predicate.test(new PersonBuilder().withPhone(PHONE_991234).build())); // end

        // Not present as contiguous substring return false
        assertFalse(predicate.test(new PersonBuilder().withPhone(PHONE_1020304).build())); // non-contiguous
        assertFalse(predicate.test(new PersonBuilder().withPhone(PHONE_123).build())); // partial of keyword
        assertFalse(predicate.test(new PersonBuilder().withPhone(PHONE_234).build())); // partial of keyword
    }

    @Test
    public void getKeywords_returnsOriginalList() {
        List<String> list = Arrays.asList(KEYWORD_12, KEYWORD_34);
        PhoneContainsKeywordsPredicate predicate = new PhoneContainsKeywordsPredicate(list);

        // Same contents
        assertEquals(list, predicate.getKeywords());
    }

    @Test
    public void successMessage_returnsExpected() {
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Collections.singletonList(KEYWORD_123));
        assertEquals(" phone number", predicate.successMessage());
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of(KEYWORD_12, KEYWORD_34);
        PhoneContainsKeywordsPredicate predicate = new PhoneContainsKeywordsPredicate(keywords);

        String expected = PhoneContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
