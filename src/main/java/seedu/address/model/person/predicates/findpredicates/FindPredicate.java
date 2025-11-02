package seedu.address.model.person.predicates.findpredicates;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.model.person.Person;

/**
 * Person predicate with getKeywords and successMessage methods
 */
public abstract class FindPredicate implements Predicate<Person> {

    public abstract List<String> getKeywords();

    public abstract String successMessage();


}
