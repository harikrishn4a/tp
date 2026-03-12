package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents the investigation stage of a contact.
 * Allowed values (case-insensitive):
 * - surveillance
 * - approached
 * - cooperating
 * - arrested
 * - closed
 */
public enum Stage {
    SURVEILLANCE("surveillance"),
    APPROACHED("approached"),
    COOPERATING("cooperating"),
    ARRESTED("arrested"),
    CLOSED("closed");

    public static final String MESSAGE_CONSTRAINTS =
            "Invalid stage. Allowed values: surveillance, approached, cooperating, arrested, closed.";

    private final String value;

    Stage(String value) {
        this.value = value;
    }

    /**
     * Returns a {@code Stage} corresponding to the given string, ignoring case.
     *
     * @throws IllegalArgumentException if the provided value is not one of the allowed values.
     */
    public static Stage fromString(String stage) {
        requireNonNull(stage);
        String lower = stage.trim().toLowerCase();
        switch (lower) {
        case "surveillance":
            return SURVEILLANCE;
        case "approached":
            return APPROACHED;
        case "cooperating":
            return COOPERATING;
        case "arrested":
            return ARRESTED;
        case "closed":
            return CLOSED;
        default:
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}

