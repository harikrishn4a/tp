package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /** Shown when the user tries any command except {@code exit} while the data file failed to load. */
    public static final String MESSAGE_ADDRESS_BOOK_DATA_CORRUPTED_COMMAND_BLOCKED =
            "Data file could not be loaded. Only \"exit\" is allowed. Fix or restore your data file, then restart.";

    /** Dialog title when the address book file failed to load on startup. */
    public static final String MESSAGE_ADDRESS_BOOK_DATA_CORRUPTED_ALERT_TITLE = "Data file could not be loaded";

    /** Dialog body explaining that only exit is allowed and saves are blocked. */
    public static final String MESSAGE_ADDRESS_BOOK_DATA_CORRUPTED_ALERT_CONTENT =
            "Your address book file exists but could not be read (it may be corrupted).\n"
                    + "Only the \"exit\" command is available. CrimeWatch will not save changes, to avoid overwriting "
                    + "your file.\n"
                    + "Repair or restore the file, then restart the application.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Stage: ")
                .append(person.getStage())
                .append("; Aliases: ")
                .append(String.join(", ", person.getAliases().stream().map(a -> a.value).toList()))
                .append("; Notes: ")
                .append(person.getNotes())
                .append("; Risk: ")
                .append(person.getRisk())
                .append("; Protected: ")
                .append(person.hasPassword() ? "yes" : "no")
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

}
