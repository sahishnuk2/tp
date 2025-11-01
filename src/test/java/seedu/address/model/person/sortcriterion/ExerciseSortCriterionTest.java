package seedu.address.model.person.sortcriterion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ExerciseSortCriterionTest {

//    @Test
//    public void getComparator() {
//        ExerciseSortCriterion exerciseSortCriterion = new ExerciseSortCriterion();
//        Comparator<Person> comparator = exerciseSortCriterion.getComparator();
//
//        Person person1 = new PersonBuilder().withStudentId("A1111111L").build();
//        Person person2 = new PersonBuilder().withStudentId("A1111111L").build();
//        Person person3 = new PersonBuilder().withStudentId("A1234567L").build();
//        Person person4 = new PersonBuilder().withStudentId("A1234567M").build();
//        Person person5 = new PersonBuilder().withStudentId("A2000000L").build();
//
//        // Same number
//        assertEquals(0, comparator.compare(person1, person2));
//
//        // Number different
//        assertTrue(comparator.compare(person2, person3) < 0);
//        assertTrue(comparator.compare(person4, person5) < 0);
//        assertTrue(comparator.compare(person5, person1) > 0);
//
//        // Number same, letter different
//        assertTrue(comparator.compare(person3, person4) < 0);
//    }
}
