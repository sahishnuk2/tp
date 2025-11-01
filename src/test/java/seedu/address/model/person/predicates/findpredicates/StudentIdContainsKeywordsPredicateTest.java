package seedu.address.model.person.predicates.findpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.EMAIL_ALICE_GMAIL;
import static seedu.address.testutil.TestConstants.NAME_ALICE;
import static seedu.address.testutil.TestConstants.SID_A0012340F;
import static seedu.address.testutil.TestConstants.SID_A1091235L;
import static seedu.address.testutil.TestConstants.SID_A1231234B;
import static seedu.address.testutil.TestConstants.SID_A1231239X;
import static seedu.address.testutil.TestConstants.SID_A1234056B;
import static seedu.address.testutil.TestConstants.SID_A1234567X;
import static seedu.address.testutil.TestConstants.SID_A3041021F;
import static seedu.address.testutil.TestConstants.SID_A5671234X;
import static seedu.address.testutil.TestConstants.SID_A7654321Z;
import static seedu.address.testutil.TestConstants.SID_A9879876G;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_340F;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_3456;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_7123;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_9X_LOWER;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_A12;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_A123;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_A1234;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_A123_LOWER;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_A765_LOWER;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_C000;
import static seedu.address.testutil.TestConstants.SID_KEYWORD_X;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class StudentIdContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList(SID_A1231234B);
        List<String> secondPredicateKeywordList = Arrays.asList(SID_A9879876G, SID_A1091235L);

        StudentIdContainsKeywordsPredicate firstPredicate =
                new StudentIdContainsKeywordsPredicate(firstPredicateKeywordList);
        StudentIdContainsKeywordsPredicate secondPredicate =
                new StudentIdContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        StudentIdContainsKeywordsPredicate firstPredicateCopy =
                new StudentIdContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_studentIdContainsKeywords_returnsTrue() {
        // One keyword
        StudentIdContainsKeywordsPredicate predicate =
                new StudentIdContainsKeywordsPredicate(Collections.singletonList(SID_KEYWORD_A123_LOWER));
        assertTrue(predicate.test(new PersonBuilder().withStudentId(SID_A1234567X).build()));

        // Multiple keywords
        predicate = new StudentIdContainsKeywordsPredicate(Arrays.asList(SID_KEYWORD_A765_LOWER, SID_KEYWORD_9X_LOWER));
        assertTrue(predicate.test(new PersonBuilder().withStudentId(SID_A7654321Z).build()));
        assertTrue(predicate.test(new PersonBuilder().withStudentId(SID_A1231239X).build()));

        // Digits-only substring
        predicate = new StudentIdContainsKeywordsPredicate(Collections.singletonList(SID_KEYWORD_3456));
        assertTrue(predicate.test(new PersonBuilder().withStudentId(SID_A1234567X).build()));
    }

    @Test
    public void test_studentIdDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        StudentIdContainsKeywordsPredicate predicate =
                new StudentIdContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withStudentId(SID_A1234567X).build()));

        // Non-matching keyword
        predicate = new StudentIdContainsKeywordsPredicate(Collections.singletonList(SID_KEYWORD_C000));
        assertFalse(predicate.test(new PersonBuilder().withStudentId(SID_A1234567X).build()));

        // Keywords match other fields, but not student ID
        predicate = new StudentIdContainsKeywordsPredicate(Arrays.asList(NAME_ALICE, "gmail"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName(NAME_ALICE)
                .withEmail(EMAIL_ALICE_GMAIL)
                .withStudentId(SID_A7654321Z)
                .build()));
    }

    @Test
    public void test_substringPositions_returnsExpected() {
        StudentIdContainsKeywordsPredicate predicate =
                new StudentIdContainsKeywordsPredicate(Collections.singletonList(SID_KEYWORD_A1234));

        // Present return true, different positions
        assertTrue(predicate.test(new PersonBuilder().withStudentId(SID_A1234567X).build())); // start

        predicate = new StudentIdContainsKeywordsPredicate(Collections.singletonList(SID_KEYWORD_7123));
        assertTrue(predicate.test(new PersonBuilder().withStudentId(SID_A5671234X).build())); // middle

        predicate = new StudentIdContainsKeywordsPredicate(Collections.singletonList(SID_KEYWORD_340F));
        assertTrue(predicate.test(new PersonBuilder().withStudentId(SID_A0012340F).build())); // end

        // Not present as contiguous substring return false
        assertFalse(predicate.test(new PersonBuilder().withStudentId(SID_A3041021F).build())); // non-contiguous
        assertFalse(predicate.test(new PersonBuilder().withStudentId(SID_A1234056B).build())); // partial of keyword
    }

    @Test
    public void getKeywords_returnsOriginalList() {
        List<String> list = Arrays.asList(SID_KEYWORD_A12, SID_KEYWORD_X);
        StudentIdContainsKeywordsPredicate predicate = new StudentIdContainsKeywordsPredicate(list);

        // Same contents
        assertEquals(list, predicate.getKeywords());
    }

    @Test
    public void successMessage_returnsExpected() {
        StudentIdContainsKeywordsPredicate predicate =
                new StudentIdContainsKeywordsPredicate(Collections.singletonList(SID_KEYWORD_A123));
        assertEquals(" student id", predicate.successMessage());
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of(SID_KEYWORD_A12, "345");
        StudentIdContainsKeywordsPredicate predicate = new StudentIdContainsKeywordsPredicate(keywords);

        String expected = StudentIdContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
