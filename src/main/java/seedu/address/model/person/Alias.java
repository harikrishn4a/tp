package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's alias in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Alias {

    public static final String MESSAGE_CONSTRAINTS =
            "Aliases should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String aliasString;

    /**
     * Constructs a {@code Alias}.
     *
     * @param alias A valid alias.
     */
    public Alias(String alias) {
        requireNonNull(alias);
        checkArgument(isValidAlias(alias), MESSAGE_CONSTRAINTS);
        aliasString = alias;
    }

    /**
     * Returns true if a given string is a valid alias.
     */
    public static boolean isValidAlias(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return aliasString;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Alias)) {
            return false;
        }

        Alias otherAlias = (Alias) other;
        return aliasString.equals(otherAlias.aliasString);
    }

    @Override
    public int hashCode() {
        return aliasString.hashCode();
    }

}
