package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents optional notes for a Person.
 * Guarantees: immutable; is valid as declared in {@link #isValidNotes(String)}
 */
public class Notes {

    public static final int MAX_LENGTH = 500;
    public static final String MESSAGE_CONSTRAINTS =
            "Invalid notes. Notes must be at most 500 characters long and must not contain newline characters.";

    public final String value;

    /**
     * Constructs {@code Notes}.
     *
     * @param notes Notes text (may be empty).
     */
    public Notes(String notes) {
        requireNonNull(notes);
        checkArgument(isValidNotes(notes), MESSAGE_CONSTRAINTS);
        value = notes;
    }

    /**
     * Returns true if a given string is valid notes.
     */
    public static boolean isValidNotes(String test) {
        requireNonNull(test);
        return test.length() <= MAX_LENGTH && !test.contains("\n") && !test.contains("\r");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Notes)) {
            return false;
        }

        Notes otherNotes = (Notes) other;
        return value.equals(otherNotes.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

