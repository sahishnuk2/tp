package seedu.address.model.person.predicates.findpredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TestConstants.KEYWORD_ALI;
import static seedu.address.testutil.TestConstants.KEYWORD_ALICE;
import static seedu.address.testutil.TestConstants.KEYWORD_BOB;
import static seedu.address.testutil.TestConstants.KEYWORD_GHUSER;
import static seedu.address.testutil.TestConstants.KEYWORD_NUS;
import static seedu.address.testutil.TestConstants.NAME_ALICE;
import static seedu.address.testutil.TestConstants.PHONE_98765432;
import static seedu.address.testutil.TestConstants.TAG_KEYWORD_LAB;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        // Inner predicates
        NameContainsKeywordsPredicate nameA = new NameContainsKeywordsPredicate(List.of(KEYWORD_ALICE));
        EmailContainsKeywordsPredicate emailNus = new EmailContainsKeywordsPredicate(List.of("nus.edu"));
        GithubContainsKeywordsPredicate ghName = new GithubContainsKeywordsPredicate(List.of(KEYWORD_GHUSER));

        PersonContainsKeywordsPredicate a =
                new PersonContainsKeywordsPredicate(List.of(nameA, emailNus));
        PersonContainsKeywordsPredicate aCopy =
                new PersonContainsKeywordsPredicate(List.of(new NameContainsKeywordsPredicate(List.of(KEYWORD_ALICE)),
                        new EmailContainsKeywordsPredicate(List.of("nus.edu"))));
        PersonContainsKeywordsPredicate b =
                new PersonContainsKeywordsPredicate(List.of(ghName));

        // same object -> true
        assertEquals(a, a);
        // same values -> true
        assertEquals(a, aCopy);
        // different -> false
        assertNotEquals(a, b);
        // null -> false
        assertNotEquals(a, null);
        // different type -> false
        assertNotEquals(a, "not a predicate");
    }

    @Test
    public void equals_sameElementsDifferentOrder_returnsFalse() {
        PersonContainsKeywordsPredicate firstThenSecond =
                new PersonContainsKeywordsPredicate(List.of(
                        new NameContainsKeywordsPredicate(List.of(KEYWORD_ALICE)),
                        new EmailContainsKeywordsPredicate(List.of("nus.edu"))
                ));
        PersonContainsKeywordsPredicate secondThenFirst =
                new PersonContainsKeywordsPredicate(List.of(
                        new EmailContainsKeywordsPredicate(List.of("nus.edu")),
                        new NameContainsKeywordsPredicate(List.of(KEYWORD_ALICE))
                ));
        // Order matters
        assertNotEquals(firstThenSecond, secondThenFirst);
    }


    @Test
    public void test_orAcrossFieldsWithOneMatch_returnsTrue() {
        // Person with name match other fields default, all individual predicates already tested
        Person person = new PersonBuilder()
                .withName(NAME_ALICE)
                .build();

        NameContainsKeywordsPredicate nameMatch =
                new NameContainsKeywordsPredicate(List.of(KEYWORD_ALI)); // matches
        EmailContainsKeywordsPredicate emailNo =
                new EmailContainsKeywordsPredicate(List.of(KEYWORD_NUS)); // default email "amy@gmail.com" no match
        GithubContainsKeywordsPredicate ghNo =
                new GithubContainsKeywordsPredicate(List.of(KEYWORD_GHUSER)); // default gh "TestUsername" no match

        PersonContainsKeywordsPredicate combined =
                new PersonContainsKeywordsPredicate(List.of(nameMatch, emailNo, ghNo));

        assertTrue(combined.test(person));
    }

    @Test
    public void test_orAcrossFieldsWithNoMatch_returnsFalse() {
        // Person with name match; other fields default
        Person person = new PersonBuilder()
                .withName(NAME_ALICE)
                .build();

        NameContainsKeywordsPredicate nameNo =
                new NameContainsKeywordsPredicate(List.of(KEYWORD_BOB)); // no match with Alice Tan
        EmailContainsKeywordsPredicate emailNo =
                new EmailContainsKeywordsPredicate(List.of(KEYWORD_NUS)); // default email "amy@gmail.com" no match
        GithubContainsKeywordsPredicate ghNo =
                new GithubContainsKeywordsPredicate(List.of(KEYWORD_GHUSER)); // default gh "TestUsername" no match
        PhoneContainsKeywordsPredicate phNo =
                new PhoneContainsKeywordsPredicate(List.of(PHONE_98765432)); // default ph "85355255" no match

        PersonContainsKeywordsPredicate combined =
                new PersonContainsKeywordsPredicate(List.of(nameNo, emailNo, ghNo, phNo));

        assertFalse(combined.test(person));
    }

    @Test
    public void successMessage_withThreeFields_includesRelevantFieldLabels() {
        // Build with name + email + tag predicates
        String[] search = {KEYWORD_ALICE, KEYWORD_NUS, TAG_KEYWORD_LAB};
        NameContainsKeywordsPredicate namePred = new NameContainsKeywordsPredicate(List.of(search));
        EmailContainsKeywordsPredicate emailPred = new EmailContainsKeywordsPredicate(List.of(search));
        TagContainsKeywordsPredicate tagPred = new TagContainsKeywordsPredicate(List.of(search));

        PersonContainsKeywordsPredicate combined =
                new PersonContainsKeywordsPredicate(List.of(namePred, emailPred, tagPred));

        String msg = combined.successMessage();
        // Contains the fields
        assertTrue(msg.toLowerCase().contains("name"));
        assertTrue(msg.toLowerCase().contains("email"));
        assertTrue(msg.toLowerCase().contains("tag"));

        // Contains the keywords
        assertTrue(msg.toLowerCase().contains(KEYWORD_ALICE));
        assertTrue(msg.toLowerCase().contains(KEYWORD_NUS));
        assertTrue(msg.toLowerCase().contains(TAG_KEYWORD_LAB));
    }

    @Test
    public void successMessage_allFields_success() {
        // Build all predicates
        NameContainsKeywordsPredicate namePred =
                new NameContainsKeywordsPredicate(List.of(KEYWORD_ALICE));
        StudentIdContainsKeywordsPredicate sidPred =
                new StudentIdContainsKeywordsPredicate(List.of(KEYWORD_ALICE));
        PhoneContainsKeywordsPredicate phonePred =
                new PhoneContainsKeywordsPredicate(List.of(KEYWORD_ALICE));
        EmailContainsKeywordsPredicate emailPred =
                new EmailContainsKeywordsPredicate(List.of(KEYWORD_ALICE));
        GithubContainsKeywordsPredicate ghPred =
                new GithubContainsKeywordsPredicate(List.of(KEYWORD_ALICE));
        TagContainsKeywordsPredicate tagPred =
                new TagContainsKeywordsPredicate(List.of(KEYWORD_ALICE));

        PersonContainsKeywordsPredicate combined =
                new PersonContainsKeywordsPredicate(
                        List.of(namePred, sidPred, phonePred, emailPred, ghPred, tagPred));

        String msg = combined.successMessage();

        assertTrue(msg.contains(" within all fields."));
        // Also contains the keywords:
        assertTrue(msg.toLowerCase().contains(KEYWORD_ALICE));
    }

    @Test
    public void successMessage_selectedFields_listsFieldNames() {
        NameContainsKeywordsPredicate namePred =
                new NameContainsKeywordsPredicate(List.of(KEYWORD_BOB));
        EmailContainsKeywordsPredicate emailPred =
                new EmailContainsKeywordsPredicate(List.of(KEYWORD_BOB));

        PersonContainsKeywordsPredicate combined =
                new PersonContainsKeywordsPredicate(List.of(namePred, emailPred));

        String msg = combined.successMessage();
        assertFalse(msg.contains(" within all fields.")); // not all
        assertTrue(msg.toLowerCase().contains(" within")); // has “within … fields.”
        assertTrue(msg.toLowerCase().contains(" name")); // field labels come from child successMessage()
        assertTrue(msg.toLowerCase().contains(" email"));
        assertTrue(msg.toLowerCase().contains(KEYWORD_BOB)); // shows keywords
    }
}
