package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's password in CrimeWatch.
 * Guarantees: immutable; is valid as declared in {@link #isValidPassword(String)}
 */
public class Password {

    public static final String MESSAGE_CONSTRAINTS =
            "Passwords should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String password;

    /**
     * Constructs a {@code Password}.
     *
     * @param password A valid password.
     */
    public Password(String password) {
        requireNonNull(password);
        checkArgument(isValidPassword(password), MESSAGE_CONSTRAINTS);
        this.password = password;
    }

    /**
     * Constructs a default {@code Password}.
     * Default password is "password123".
     */
    public Password() {
        this("password123");
    }

    /**
     * Returns true if a given string is a valid password.
     */
    public static boolean isValidPassword(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return password;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Password)) {
            return false;
        }

        Password otherPassword = (Password) other;
        return password.equals(otherPassword.password);
    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }

}
