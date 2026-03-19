package seedu.address.storage;

import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Encounter;

/**
 * Jackson-friendly version of {@link Encounter}.
 */
class JsonAdaptedEncounter {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Encounter's %s field is missing!";

    private final String dateTime;
    private final String location;
    private final String description;
    private final String outcome; // null if absent

    /**
     * Constructs a {@code JsonAdaptedEncounter} with the given encounter details.
     */
    @JsonCreator
    public JsonAdaptedEncounter(
            @JsonProperty("dateTime") String dateTime,
            @JsonProperty("location") String location,
            @JsonProperty("description") String description,
            @JsonProperty("outcome") String outcome) {
        this.dateTime = dateTime;
        this.location = location;
        this.description = description;
        this.outcome = outcome;
    }

    /**
     * Converts a given {@code Encounter} into this class for Jackson use.
     */
    public JsonAdaptedEncounter(Encounter source) {
        this.dateTime = source.getFormattedDateTime();
        this.location = source.location;
        this.description = source.description;
        this.outcome = source.outcome.orElse(null);
    }

    /**
     * Converts this Jackson-friendly adapted encounter object into the model's {@code Encounter} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted encounter.
     */
    public Encounter toModelType() throws IllegalValueException {
        if (dateTime == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "dateTime"));
        }
        final LocalDateTime modelDateTime;
        try {
            modelDateTime = LocalDateTime.parse(dateTime, Encounter.FORMATTER);
        } catch (Exception e) {
            throw new IllegalValueException(
                    "Invalid encounter dateTime format: " + dateTime);
        }

        if (location == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "location"));
        }
        if (!Encounter.isValidLocation(location)) {
            throw new IllegalValueException(Encounter.MESSAGE_LOCATION_CONSTRAINTS);
        }

        if (description == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "description"));
        }
        if (!Encounter.isValidDescription(description)) {
            throw new IllegalValueException(Encounter.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }

        final Optional<String> modelOutcome;
        if (outcome != null) {
            if (!Encounter.isValidOutcome(outcome)) {
                throw new IllegalValueException(Encounter.MESSAGE_OUTCOME_CONSTRAINTS);
            }
            modelOutcome = Optional.of(outcome);
        } else {
            modelOutcome = Optional.empty();
        }

        return new Encounter(modelDateTime, location, description, modelOutcome);
    }
}
