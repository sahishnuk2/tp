package seedu.address.model.person.predicates.findpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.EMAIL_ALICE_NUS_EDU_SG;
import static seedu.address.testutil.TestConstants.NAME_ALICE;
import static seedu.address.testutil.TestConstants.TAG_CS2030S;
import static seedu.address.testutil.TestConstants.TAG_CS2030S_UPPER;
import static seedu.address.testutil.TestConstants.TAG_GROUP_A;
import static seedu.address.testutil.TestConstants.TAG_GROUP_B;
import static seedu.address.testutil.TestConstants.TAG_KEYWORD_CS2030;
import static seedu.address.testutil.TestConstants.TAG_KEYWORD_CS2030S;
import static seedu.address.testutil.TestConstants.TAG_KEYWORD_GROUP;
import static seedu.address.testutil.TestConstants.TAG_KEYWORD_GROUP_A;
import static seedu.address.testutil.TestConstants.TAG_KEYWORD_LAB;
import static seedu.address.testutil.TestConstants.TAG_KEYWORD_LAB1;
import static seedu.address.testutil.TestConstants.TAG_KEYWORD_PROJ;
import static seedu.address.testutil.TestConstants.TAG_KEYWORD_PROJECT;
import static seedu.address.testutil.TestConstants.TAG_KEYWORD_RANDOM;
import static seedu.address.testutil.TestConstants.TAG_LAB1;
import static seedu.address.testutil.TestConstants.TAG_LAB1_PROPER;
import static seedu.address.testutil.TestConstants.TAG_LAB_GROUP_1;
import static seedu.address.testutil.TestConstants.TAG_PROJECT_X;
import static seedu.address.testutil.TestConstants.TAG_WEEKLY;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class TagContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList(TAG_KEYWORD_LAB1);
        List<String> secondPredicateKeywordList = Arrays.asList(TAG_KEYWORD_CS2030S, TAG_KEYWORD_GROUP_A);

        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TagContainsKeywordsPredicate firstPredicateCopy =
                new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_tagsContainKeywords_returnsTrue() {
        // One keyword
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Collections.singletonList(TAG_KEYWORD_LAB1));
        assertTrue(predicate.test(new PersonBuilder().withTags(TAG_LAB1_PROPER).build()));

        // Multiple keywords
        predicate = new TagContainsKeywordsPredicate(Arrays.asList(TAG_KEYWORD_PROJECT, TAG_KEYWORD_GROUP_A));
        assertTrue(predicate.test(new PersonBuilder().withTags(TAG_GROUP_A).build())); // matches "groupA"
        assertTrue(predicate.test(new PersonBuilder().withTags(TAG_PROJECT_X).build())); // matches "project"

        // Substring within a tag
        predicate = new TagContainsKeywordsPredicate(Collections.singletonList(TAG_KEYWORD_GROUP));
        assertTrue(predicate.test(new PersonBuilder().withTags(TAG_LAB_GROUP_1).build()));

        // Mixed case across tags
        predicate = new TagContainsKeywordsPredicate(Collections.singletonList(TAG_KEYWORD_CS2030));
        assertTrue(predicate.test(new PersonBuilder().withTags(TAG_CS2030S_UPPER, TAG_WEEKLY).build()));
    }

    @Test
    public void test_tagsDoNotContainKeywords_returnsFalse() {
        // Zero keywords
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withTags(TAG_LAB1).build()));

        // Non-matching keyword
        predicate = new TagContainsKeywordsPredicate(Collections.singletonList(TAG_KEYWORD_RANDOM));
        assertFalse(predicate.test(new PersonBuilder().withTags(TAG_LAB1, TAG_CS2030S).build()));

        // Keywords match other fields, but not tags
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("alice", "nus"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName(NAME_ALICE)
                .withEmail(EMAIL_ALICE_NUS_EDU_SG)
                .withTags(TAG_GROUP_B)
                .build()));
    }

    @Test
    public void getKeywords_returnsOriginalList() {
        List<String> list = Arrays.asList(TAG_KEYWORD_LAB, TAG_KEYWORD_PROJ);
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(list);

        // Same contents
        assertEquals(list, predicate.getKeywords());
    }

    @Test
    public void successMessage_returnsExpected() {
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Collections.singletonList(TAG_KEYWORD_LAB));
        assertEquals(" tags", predicate.successMessage());
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of(TAG_KEYWORD_CS2030S, TAG_KEYWORD_LAB1);
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(keywords);

        String expected = TagContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
