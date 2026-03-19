package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents an Encounter with a Person in the address book.
 * Guarantees: immutable; all required fields are present and not null; all fields satisfy their constraints.
 */
public class Encounter {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    public static final int DESCRIPTION_MAX_LENGTH = 500;
    public static final int OUTCOME_MAX_LENGTH = 300;

    public static final String MESSAGE_LOCATION_CONSTRAINTS =
            "Encounter location can take any value, and should not be blank";
    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Encounter descriptions must be between 1 and " + DESCRIPTION_MAX_LENGTH
            + " characters, and should not be blank";
    public static final String MESSAGE_OUTCOME_CONSTRAINTS =
            "Encounter outcome must not exceed " + OUTCOME_MAX_LENGTH + " characters";

    public static final String LOCATION_VALIDATION_REGEX = "[^\\s].*";
    public static final String DESCRIPTION_VALIDATION_REGEX = "[^\\s].*";

    public final LocalDateTime dateTime;
    public final String location;
    public final String description;
    public final Optional<String> outcome;

    /**
     * Constructs an {@code Encounter} with an optional outcome.
     *
     * @param dateTime    The date and time of the encounter; must not be null.
     * @param location    The location of the encounter; must not be blank.
     * @param description A valid description (1-500 characters).
     * @param outcome     An optional outcome (up to 300 characters);
     *                    use {@code Optional.empty()} if absent.
     */
    public Encounter(LocalDateTime dateTime, String location, String description, Optional<String> outcome) {
        requireNonNull(dateTime);
        requireNonNull(location);
        requireNonNull(description);
        requireNonNull(outcome);
        checkArgument(isValidLocation(location), MESSAGE_LOCATION_CONSTRAINTS);
        checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);
        outcome.ifPresent(o -> checkArgument(isValidOutcome(o), MESSAGE_OUTCOME_CONSTRAINTS));
        this.dateTime = dateTime;
        this.location = location;
        this.description = description;
        this.outcome = outcome;
    }

    /**
     * Returns true if a given string is a valid location (non-blank).
     */
    public static boolean isValidLocation(String test) {
        return test.matches(LOCATION_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid encounter description
     * (non-blank and at most {@value DESCRIPTION_MAX_LENGTH} characters).
     */
    public static boolean isValidDescription(String test) {
        return test.matches(DESCRIPTION_VALIDATION_REGEX)
                && test.length() <= DESCRIPTION_MAX_LENGTH;
    }

    /**
     * Returns true if a given string is a valid outcome
     * (at most {@value OUTCOME_MAX_LENGTH} characters).
     */
    public static boolean isValidOutcome(String test) {
        return test.length() <= OUTCOME_MAX_LENGTH;
    }

    /**
     * Returns the date and time of the encounter formatted as {@value DATE_TIME_FORMAT}.
     */
    public String getFormattedDateTime() {
        return dateTime.format(FORMATTER);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("dateTime", getFormattedDateTime())
                .add("location", location)
                .add("description", description)
                .add("outcome", outcome.orElse(""))
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Encounter)) {
            return false;
        }

        Encounter otherEncounter = (Encounter) other;
        return dateTime.equals(otherEncounter.dateTime)
                && location.equals(otherEncounter.location)
                && description.equals(otherEncounter.description)
                && outcome.equals(otherEncounter.outcome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, location, description, outcome);
    }

}
