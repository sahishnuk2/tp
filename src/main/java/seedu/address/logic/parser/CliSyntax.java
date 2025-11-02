package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_STUDENTID = new Prefix("i/");
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_EXERCISE_INDEX = new Prefix("ei/");
    public static final Prefix PREFIX_STATUS = new Prefix("s/");
    public static final Prefix PREFIX_GITHUB_USERNAME = new Prefix("g/");
    public static final Prefix PREFIX_LAB_NUMBER = new Prefix("l/");
    public static final Prefix PREFIX_EXAM_NAME = new Prefix("en/");
    public static final Prefix PREFIX_SORT_CRITERION = new Prefix("c/");

    // Timeslot prefixes
    public static final Prefix PREFIX_TIMESLOT_START = new Prefix("ts/"); // e.g. ts/2023-10-01T09:00:00
    public static final Prefix PREFIX_TIMESLOT_END = new Prefix("te/"); // e.g. te/2023-10-01T10:00:00

    // Filter prefixes
    public static final Prefix PREFIX_LAB_ATTENDANCE = new Prefix("la/");

}
