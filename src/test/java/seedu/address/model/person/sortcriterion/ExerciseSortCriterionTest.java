package seedu.address.model.person.sortcriterion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ExerciseSortCriterionTest {

    @Test
    public void getComparator() {
        String exerciseTenPercent = "ex 0: N ex 1: N ex 2: N ex 3: N ex 4: N ex 5: N ex 6: N ex 7: N ex 8: N ex 9: N";
        String exerciseTenPercentDuplicate = "ex 0: N ex 1: N ex 2: N ex 3: N ex 4: N ex 5: N ex 6: N ex 7: N ex 8: N ex 9: N";
        String exerciseTwentyPercent = "ex 0: N ex 1: N ex 2: N ex 3: N ex 4: N ex 5: N ex 6: N ex 7: N ex 8: N ex 9: N";
        String exerciseThirtyPercent = "ex 0: N ex 1: N ex 2: N ex 3: N ex 4: N ex 5: N ex 6: N ex 7: N ex 8: N ex 9: N";
        String exerciseFortyPercent = "ex 0: N ex 1: N ex 2: N ex 3: N ex 4: N ex 5: N ex 6: N ex 7: N ex 8: N ex 9: N";

        ExerciseSortCriterion exerciseSortCriterion = new ExerciseSortCriterion();
        Comparator<Person> comparator = exerciseSortCriterion.getComparator();

        Person person1 = new PersonBuilder().withExerciseList(exerciseTenPercent).build();
        Person person2 = new PersonBuilder().withExerciseList(exerciseTenPercentDuplicate).build();
        Person person3 = new PersonBuilder().withExerciseList(exerciseTwentyPercent).build();
        Person person4 = new PersonBuilder().withExerciseList(exerciseThirtyPercent).build();
        Person person5 = new PersonBuilder().withExerciseList(exerciseFortyPercent).build();

        // Same number
        assertEquals(0, comparator.compare(person1, person2));

//        // Different exercise attendance percentage
//        assertTrue(comparator.compare(person2, person3) > 0);
//        assertTrue(comparator.compare(person3, person4) > 0);
//        assertTrue(comparator.compare(person4, person5) > 0);
//        assertTrue(comparator.compare(person5, person1) < 0);
    }
}
