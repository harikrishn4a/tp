package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_ALIAS = new Prefix("al/");
    public static final Prefix PREFIX_NOTES = new Prefix("note/");
    public static final Prefix PREFIX_RISK = new Prefix("r/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_STAGE = new Prefix("s/");
    public static final Prefix PREFIX_PASSWORD = new Prefix("pw/");

    /* Log command prefix definitions */
    public static final Prefix PREFIX_DATE = new Prefix("d/");
    /**
     * Note: PREFIX_TIME uses the same string value as PREFIX_TAG ("t/").
     * This is intentional — PREFIX_TIME is only used in {@code LogCommandParser},
     * while PREFIX_TAG is only used in add/edit parsers. They are never mixed in the same tokenize call.
     */
    public static final Prefix PREFIX_TIME = new Prefix("t/");
    public static final Prefix PREFIX_LOCATION = new Prefix("l/");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("desc/");
    public static final Prefix PREFIX_OUTCOME = new Prefix("out/");
}
