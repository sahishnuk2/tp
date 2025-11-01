package seedu.address.model.person.sortcriterion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class LabSortCriterionTest {
    @Test
    public void getComparator() {
        String labTenPercent = "L1: Y L2: N L3: N L4: N L5: N L6: N L7: N L8: N L9: N L10: N ";
        String labTenPercentDuplicate = "L1: N L2: Y L3: N L4: N L5: N L6: N L7: N L8: N L9: N L10: N ";
        String labTwentyPercent = "L1: Y L2: Y L3: N L4: N L5: N L6: N L7: N L8: N L9: N L10: N ";
        String labThirtyPercent = "L1: Y L2: Y L3: Y L4: N L5: N L6: N L7: N L8: N L9: N L10: N ";
        String labFortyPercent = "L1: Y L2: Y L3: Y L4: Y L5: N L6: N L7: N L8: N L9: N L10: N ";
        
        LabSortCriterion labSortCriterion = new LabSortCriterion();
        Comparator<Person> comparator = labSortCriterion.getComparator();

        Person person1 = new PersonBuilder().withLabAttendanceList(labTenPercent).build();
        Person person2 = new PersonBuilder().withLabAttendanceList(labTenPercentDuplicate).build();
        Person person3 = new PersonBuilder().withLabAttendanceList(labTwentyPercent).build();
        Person person4 = new PersonBuilder().withLabAttendanceList(labThirtyPercent).build();
        Person person5 = new PersonBuilder().withLabAttendanceList(labFortyPercent).build();

        // Same number
        assertEquals(0, comparator.compare(person1, person2));

        // Different lab attendance percentage
        assertTrue(comparator.compare(person2, person3) > 0);
        assertTrue(comparator.compare(person3, person4) > 0);
        assertTrue(comparator.compare(person4, person5) > 0);
        assertTrue(comparator.compare(person5, person1) < 0);
    }
}
