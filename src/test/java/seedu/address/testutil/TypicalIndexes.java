package seedu.address.testutil;

import seedu.address.commons.core.index.Index;

/**
 * A utility class containing a list of {@code Index} objects to be used in tests.
 */
public class TypicalIndexes {
    public static final Index INDEX_FIRST_PERSON = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_PERSON = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_PERSON = Index.fromOneBased(3);

    public static final Index INDEX_FIRST_LAB = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_LAB = Index.fromOneBased(2);
    public static final Index INDEX_FIRST_EXERCISE = Index.fromZeroBased(0);
    public static final Index INDEX_SECOND_EXERCISE = Index.fromZeroBased(1);


    // INVALID INDEX
    public static final Index INDEX_HUNDRED_PERSON = Index.fromOneBased(100);

    public static final Index INDEX_HUNDRED_LAB = Index.fromOneBased(100);

}
