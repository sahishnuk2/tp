package seedu.address.model.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.Timeslots;
import seedu.address.model.person.Email;
import seedu.address.model.person.ExerciseList;
import seedu.address.model.person.GithubUsername;
import seedu.address.model.person.GradeMap;
import seedu.address.model.person.LabList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;
import seedu.address.model.timeslot.ConsultationTimeslot;
import seedu.address.model.timeslot.Timeslot;

/**
 * Contains utility methods for populating {@code AddressBook} and {@code Timeslots} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new StudentId("A1231230B"), new Name("Alex Yeoh"), new Phone("87438807"),
                    new Email("alexyeoh@example.com"),
                    getTagSet("Smart"),
                    new GithubUsername("AlexYeoh"),
                    new ExerciseList(), new LabList(), new GradeMap()),
            new Person(new StudentId("A1231231B"), new Name("Bernice Yu"), new Phone("99272758"),
                    new Email("berniceyu@example.com"),
                    getTagSet("Consulting", "Struggling"),
                    new GithubUsername("BerniceYu"),
                    new ExerciseList(), new LabList(), new GradeMap()),
            new Person(new StudentId("A1231232B"), new Name("Charlotte Oliveiro"), new Phone("93210283"),
                    new Email("charlotte@example.com"),
                    getTagSet(),
                    new GithubUsername("CharlotteOliveiro"),
                    new ExerciseList(), new LabList(), new GradeMap()),
            new Person(new StudentId("A1231233B"), new Name("David Li"), new Phone("91031282"),
                    new Email("lidavid@example.com"),
                    getTagSet(),
                    new GithubUsername("DavidLi"),
                    new ExerciseList(), new LabList(), new GradeMap()),
            new Person(new StudentId("A1231234B"), new Name("Irfan Ibrahim"), new Phone("92492021"),
                    new Email("irfan@example.com"),
                    getTagSet("Consulting"),
                    new GithubUsername("IrfanIbrahim"),
                    new ExerciseList(), new LabList(), new GradeMap()),
            new Person(new StudentId("A1231235B"), new Name("Roy Balakrishnan"), new Phone("92624417"),
                    new Email("royb@example.com"),
                    getTagSet("Struggling"),
                    new GithubUsername("RoyBalakrishnan"),
                    new ExerciseList(), new LabList(), new GradeMap())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a sample Timeslots populated with a few sample Timeslot entries.
     * Generates 4 full weeks of sample data (current week + 3 subsequent weeks) with different slot patterns.
     */
    public static Timeslots getSampleTimeslots() {
        Timeslots sample = new Timeslots();

        // Determine this week's Monday as the base date
        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);

        // Names to rotate for consultations
        String[] consultNames = new String[] { "Alice", "Bob", "Charlie", "Daisy",
            "Eve", "Frank", "Grace", "Heidi" };

        int nameIndex = 0;

        // Generate 4 consecutive weeks with varied patterns so they are not identical
        for (int w = 0; w < 4; w++) {
            LocalDate base = weekStart.plusWeeks(w);

            // Week-specific patterns
            switch (w) {
            case 0:
                // Baseline week (similar to original)
                sample.addTimeslot(new Timeslot(base.atTime(9, 0), base.atTime(10, 0)));
                sample.addTimeslot(new Timeslot(base.atTime(10, 30), base.atTime(11, 0)));
                sample.addTimeslot(new Timeslot(base.atTime(14, 0), base.atTime(15, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(1).atTime(9, 0), base.plusDays(1).atTime(10, 0)));
                sample.addTimeslot(new Timeslot(base.plusDays(1).atTime(13, 0), base.plusDays(1).atTime(14, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(2).atTime(12, 0), base.plusDays(2).atTime(13, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(3).atTime(8, 30), base.plusDays(3).atTime(10, 0)));
                sample.addTimeslot(new Timeslot(base.plusDays(3).atTime(18, 0), base.plusDays(3).atTime(19, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(4).atTime(15, 0), base.plusDays(4).atTime(17, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(5).atTime(10, 0), base.plusDays(5).atTime(12, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(6).atTime(9, 0), base.plusDays(6).atTime(13, 0)));

                // Consultations for week 0
                sample.addTimeslot(new ConsultationTimeslot(base.plusDays(1).atTime(10, 0),
                        base.plusDays(1).atTime(10, 30), consultNames[nameIndex++ % consultNames.length]));
                sample.addTimeslot(new ConsultationTimeslot(base.plusDays(3).atTime(14, 0),
                        base.plusDays(3).atTime(14, 30), consultNames[nameIndex++ % consultNames.length]));
                break;

            case 1:
                // Week 1: morning slots shifted later, slightly different durations
                sample.addTimeslot(new Timeslot(base.atTime(9, 30), base.atTime(10, 30)));
                sample.addTimeslot(new Timeslot(base.atTime(11, 0), base.atTime(11, 30)));
                sample.addTimeslot(new Timeslot(base.atTime(15, 0), base.atTime(16, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(1).atTime(9, 30), base.plusDays(1).atTime(10, 30)));
                sample.addTimeslot(new Timeslot(base.plusDays(1).atTime(13, 30), base.plusDays(1).atTime(15, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(2).atTime(12, 30), base.plusDays(2).atTime(13, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(3).atTime(9, 0), base.plusDays(3).atTime(10, 30)));
                sample.addTimeslot(new Timeslot(base.plusDays(3).atTime(18, 30), base.plusDays(3).atTime(19, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(4).atTime(14, 0), base.plusDays(4).atTime(16, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(5).atTime(10, 30), base.plusDays(5).atTime(12, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(6).atTime(8, 0), base.plusDays(6).atTime(11, 0)));

                // Consultations for week 1 (different days/times)
                sample.addTimeslot(new ConsultationTimeslot(base.plusDays(2).atTime(10, 0),
                        base.plusDays(2).atTime(10, 30), consultNames[nameIndex++ % consultNames.length]));
                sample.addTimeslot(new ConsultationTimeslot(base.plusDays(4).atTime(13, 0),
                        base.plusDays(4).atTime(13, 30), consultNames[nameIndex++ % consultNames.length]));
                break;

            case 2:
                // Week 2: earlier starts and longer morning blocks
                sample.addTimeslot(new Timeslot(base.atTime(8, 0), base.atTime(9, 30)));
                sample.addTimeslot(new Timeslot(base.atTime(11, 0), base.atTime(12, 0)));
                sample.addTimeslot(new Timeslot(base.atTime(13, 30), base.atTime(15, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(1).atTime(8, 30), base.plusDays(1).atTime(9, 30)));
                sample.addTimeslot(new Timeslot(base.plusDays(1).atTime(12, 30), base.plusDays(1).atTime(14, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(2).atTime(11, 30), base.plusDays(2).atTime(12, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(3).atTime(8, 30), base.plusDays(3).atTime(9, 0)));
                sample.addTimeslot(new Timeslot(base.plusDays(3).atTime(17, 30), base.plusDays(3).atTime(18, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(4).atTime(14, 30), base.plusDays(4).atTime(16, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(5).atTime(9, 0), base.plusDays(5).atTime(11, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(6).atTime(10, 0), base.plusDays(6).atTime(12, 0)));

                // Consultations for week 2
                sample.addTimeslot(new ConsultationTimeslot(base.atTime(12, 0),
                        base.atTime(12, 30), consultNames[nameIndex++ % consultNames.length]));
                sample.addTimeslot(new ConsultationTimeslot(base.plusDays(5).atTime(14, 0),
                        base.plusDays(5).atTime(14, 30), consultNames[nameIndex++ % consultNames.length]));
                break;

            default:
                // Week 3: later-evening heavy week, different spread
                sample.addTimeslot(new Timeslot(base.atTime(18, 0), base.atTime(20, 0)));
                sample.addTimeslot(new Timeslot(base.atTime(10, 0), base.atTime(11, 0)));
                sample.addTimeslot(new Timeslot(base.atTime(13, 0), base.atTime(14, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(1).atTime(19, 0), base.plusDays(1).atTime(21, 0)));
                sample.addTimeslot(new Timeslot(base.plusDays(1).atTime(9, 0), base.plusDays(1).atTime(10, 0)));

                sample.addTimeslot(new Timeslot(base.plusDays(2).atTime(16, 0), base.plusDays(2).atTime(17, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(3).atTime(18, 30), base.plusDays(3).atTime(19, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(4).atTime(14, 0), base.plusDays(4).atTime(15, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(5).atTime(11, 0), base.plusDays(5).atTime(12, 30)));

                sample.addTimeslot(new Timeslot(base.plusDays(6).atTime(8, 30), base.plusDays(6).atTime(10, 30)));

                // Consultations for week 3 (morning/evening)
                sample.addTimeslot(new ConsultationTimeslot(base.plusDays(1).atTime(8, 0),
                        base.plusDays(1).atTime(8, 30), consultNames[nameIndex++ % consultNames.length]));
                sample.addTimeslot(new ConsultationTimeslot(base.plusDays(3).atTime(19, 30),
                        base.plusDays(3).atTime(20, 0), consultNames[nameIndex++ % consultNames.length]));
                break;
            }
        }

        return sample;
    }
}
