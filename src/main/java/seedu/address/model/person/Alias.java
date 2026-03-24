package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's alias in the CrimeWatch.
 * Guarantees: immutable; is valid as declared in {@link #isValidAlias(String)}
 */
public class Alias {

    public static final int MAX_LENGTH = 50;
    public static final String MESSAGE_CONSTRAINTS =
            "Invalid alias. Alias must be non-empty, 1 to 50 characters long, and contain only alphanumeric "
                    + "characters and spaces.";

    private static final String VALIDATION_REGEX = "[A-Za-z0-9 ]+";

    public final String value;

    /**
     * Constructs an {@code Alias}.
     *
     * @param alias A valid alias.
     */
    public Alias(String alias) {
        requireNonNull(alias);
        String trimmed = alias.trim();
        checkArgument(isValidAlias(trimmed), MESSAGE_CONSTRAINTS);
        value = trimmed;
    }

    /**
     * Returns if a given string is a valid alias.
     */
    public static boolean isValidAlias(String test) {
        requireNonNull(test);
        String trimmed = test.trim();
        if (trimmed.isEmpty() || trimmed.length() > MAX_LENGTH) {
            return false;
        }
        return trimmed.matches(VALIDATION_REGEX);
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

        if (!(other instanceof Alias)) {
            return false;
        }

        Alias otherAlias = (Alias) other;
        return value.equals(otherAlias.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

