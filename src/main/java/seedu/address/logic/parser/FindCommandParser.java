package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUB_USERNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENTID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.predicates.findpredicates.EmailContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.FindPredicate;
import seedu.address.model.person.predicates.findpredicates.GithubContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.PersonContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.StudentIdContainsKeywordsPredicate;
import seedu.address.model.person.predicates.findpredicates.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    private static final String MESSAGE_FIELD_NOT_EMPTY = "Field selectors must be empty (e.g., 'n/' not 'n/alice').";
    private static final Prefix[] FIND_PREFIXES = { PREFIX_STUDENTID, PREFIX_NAME,
        PREFIX_EMAIL, PREFIX_GITHUB_USERNAME, PREFIX_PHONE, PREFIX_TAG };

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, FIND_PREFIXES);


        List<String> keywords = getKeywords(argMultimap);
        List<FindPredicate> predicates = getSelectedPredicates(argMultimap, keywords);

        return new FindCommand(new PersonContainsKeywordsPredicate(predicates));
    }


    private List<String> getKeywords(ArgumentMultimap argMultimap) throws ParseException {
        String[] preamble = argMultimap.getPreamble().trim().split("\\s+");
        List<String> keywords = Arrays.asList(preamble);

        // Require at least one keyword in the preamble
        if (keywords.isEmpty() || keywords.get(0).isBlank()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        return keywords;
    }

    private List<FindPredicate> getSelectedPredicates(ArgumentMultimap argMultimap, List<String> keywords)
            throws ParseException {

        checkNoDuplicatePrefix(argMultimap);
        checkSelectorEmpty(argMultimap);

        return selectPredicates(argMultimap, keywords);
    }

    private static void checkSelectorEmpty(ArgumentMultimap argMultimap) throws ParseException {
        for (Prefix p : FIND_PREFIXES) {

            //Already checked by checkNoDuplicatePrefix
            assert argMultimap.getAllValues(p).size() <= 1
                    : "Parser precondition violated: repeated selector " + p;

            if (argMultimap.getValue(p).isPresent()
                    && !argMultimap.getValue(p).get().isBlank()) {
                throw new ParseException(MESSAGE_FIELD_NOT_EMPTY);
            }

        }
    }

    private static void checkNoDuplicatePrefix(ArgumentMultimap argMultimap)
            throws ParseException {
        List<Prefix> duplicates = new ArrayList<>();
        for (Prefix p : FIND_PREFIXES) {
            if (argMultimap.getAllValues(p).size() > 1) {
                duplicates.add(p);
            }
        }

        if (!duplicates.isEmpty()) {
            throw new ParseException(Messages.getErrorMessageForDuplicateFindSelectors(
                    duplicates.toArray(new Prefix[0])
            ));
        }
    }

    private static List<FindPredicate> selectPredicates(ArgumentMultimap argMultimap, List<String> keywords) {
        List<PrefixPredicateContainer> prefixPredicateContainers = PrefixPredicateContainer.getAllPrefixPredicate();

        List<FindPredicate> predicates = new ArrayList<>();

        boolean isAnySelected = Arrays.stream(FIND_PREFIXES).anyMatch(p -> argMultimap.getValue(p).isPresent());

        for (PrefixPredicateContainer prefixPredicateContainer : prefixPredicateContainers) {
            boolean isSelected = argMultimap.getValue(prefixPredicateContainer.getPrefix()).isPresent();
            if (!isAnySelected || isSelected) {
                predicates.add(prefixPredicateContainer
                        .getPredicateWrapper()
                        .buildPredicate(keywords));
            }
        }
        return predicates;
    }


    /**
     * Binds a {@link Prefix} (e.g., {@code n/}, {@code e/}) to a predicate “builder” for a {@link Person}
     */
    public static class PrefixPredicateContainer {
        private final Prefix prefix;
        private final PredicateWrapper predicateWrapper;

        /**
         * Creates a {@code PrefixPredicate}.
         *
         * @param prefix the CLI prefix that identifies the target field.
         * @param predicateWrapper a factory that, given keywords, builds a field-level predicate.
         */
        public PrefixPredicateContainer(Prefix prefix, PredicateWrapper predicateWrapper) {
            this.prefix = prefix;
            this.predicateWrapper = predicateWrapper;
        }

        public static List<PrefixPredicateContainer> getAllPrefixPredicate() {
            return Arrays.asList(
                    new PrefixPredicateContainer(
                            PREFIX_STUDENTID,
                            keywords -> new StudentIdContainsKeywordsPredicate(keywords)
                    ),
                    new PrefixPredicateContainer(
                            PREFIX_NAME,
                            keywords -> new NameContainsKeywordsPredicate(keywords)
                    ),
                    new PrefixPredicateContainer(
                            PREFIX_EMAIL,
                            keywords -> new EmailContainsKeywordsPredicate(keywords)
                    ),
                    new PrefixPredicateContainer(
                            PREFIX_GITHUB_USERNAME,
                            keywords -> new GithubContainsKeywordsPredicate(keywords)
                    ),
                    new PrefixPredicateContainer(
                            PREFIX_PHONE,
                            keywords -> new PhoneContainsKeywordsPredicate(keywords)
                    ),
                    new PrefixPredicateContainer(
                            PREFIX_TAG,
                            keywords -> new TagContainsKeywordsPredicate(keywords)
                    )
            );
        }

        public Prefix getPrefix() {
            return prefix;
        }

        public PredicateWrapper getPredicateWrapper() {
            return predicateWrapper;
        }


        /**
         * Functional interface for building field-level predicates from a list of keywords.
         * Enables lazy building of only predicates that are selected.
         */
        @FunctionalInterface
        public interface PredicateWrapper {

            public FindPredicate buildPredicate(List<String> keywords);
        }
    }
}
