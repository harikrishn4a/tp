package seedu.address.model.person;

/**
 * Represents a Person's risk level.
 */
public enum Risk {
    LOW,
    MEDIUM,
    HIGH;

    public static final String MESSAGE_CONSTRAINTS =
            "Invalid risk. Risk must be one of: low, medium, high.";

    /**
     * Returns a Risk enum from a given string.
     * @param raw The string to convert to a Risk enum.
     * @return The Risk enum.
     * @throws IllegalArgumentException if the string is not a valid Risk enum.
     */
    public static Risk fromString(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Risk is null");
        }
        String normalised = raw.trim().toUpperCase();
        return Risk.valueOf(normalised);
    }

    /**
     * Returns the default risk level.
     */
    public static Risk getDefault() {
        return MEDIUM;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}

