package seedu.address.model.person.predicates.findpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.EMAIL_ALICE_GENERIC;
import static seedu.address.testutil.TestConstants.KEYWORD_ALICE;
import static seedu.address.testutil.TestConstants.KEYWORD_ALICE_MIXED;
import static seedu.address.testutil.TestConstants.KEYWORD_BOB;
import static seedu.address.testutil.TestConstants.KEYWORD_BOB_MIXED;
import static seedu.address.testutil.TestConstants.NAME_ALICE;
import static seedu.address.testutil.TestConstants.NAME_ALICE_BOB;
import static seedu.address.testutil.TestConstants.NAME_ALICE_CAROL;
import static seedu.address.testutil.TestConstants.NAME_BOB;
import static seedu.address.testutil.TestConstants.NAME_CAROL;
import static seedu.address.testutil.TestConstants.PHONE_12345;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;


public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsKeywordsPredicate firstPredicateCopy = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList(NAME_ALICE));
        assertTrue(predicate.test(new PersonBuilder().withName(NAME_ALICE_BOB).build()));

        // Multiple keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList(NAME_ALICE, NAME_BOB));
        assertTrue(predicate.test(new PersonBuilder().withName(NAME_ALICE_BOB).build()));

        // Only one matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList(NAME_BOB, NAME_CAROL));
        assertTrue(predicate.test(new PersonBuilder().withName(NAME_ALICE_CAROL).build()));

        // Mixed-case keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList(KEYWORD_ALICE_MIXED, KEYWORD_BOB_MIXED));
        assertTrue(predicate.test(new PersonBuilder().withName(NAME_ALICE_BOB).build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName(NAME_ALICE).build()));

        // Non-matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList(NAME_CAROL));
        assertFalse(predicate.test(new PersonBuilder().withName(NAME_ALICE_BOB).build()));

        // Keywords match phone, email and address, but does not match name
        predicate = new NameContainsKeywordsPredicate(Arrays.asList(PHONE_12345, EMAIL_ALICE_GENERIC));
        assertFalse(predicate.test(new PersonBuilder().withName(NAME_ALICE).withPhone(PHONE_12345)
                .withEmail(EMAIL_ALICE_GENERIC).build()));
    }

    @Test
    public void test_returnTrueIfSubstring() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("ab"));

        // Exact Match
        assertTrue(predicate.test(new PersonBuilder().withName("ab").build()));

        // Keyword is at the start of name
        assertTrue(predicate.test(new PersonBuilder().withName("abcdefg").build()));

        // Keyword is in the middle of name
        assertTrue(predicate.test(new PersonBuilder().withName("cdeabfg").build()));

        // Keyword is split up in the name returns false
        assertFalse(predicate.test(new PersonBuilder().withName("acdebfg").build()));

        // Substring of keyword is in name returns false
        assertFalse(predicate.test(new PersonBuilder().withName("a").build()));

        // Substring of keyword is in name returns false
        assertFalse(predicate.test(new PersonBuilder().withName("b").build()));
    }

    @Test
    public void getKeywords_returnsOriginalList() {
        List<String> list = Arrays.asList(KEYWORD_ALICE, KEYWORD_BOB);
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(list);

        // Same contents
        assertEquals(list, predicate.getKeywords());
    }

    @Test
    public void successMessage_returnsExpected() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList(KEYWORD_ALICE));
        assertEquals(" name", predicate.successMessage());
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);

        String expected = NameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
