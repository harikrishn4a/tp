package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Email;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Name;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Password;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Risk;
import seedu.address.model.person.Stage;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String stage;
    private final List<String> aliases = new ArrayList<>();
    private final String notes;
    private final String risk;
    private final String password;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedEncounter> encounters = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
            @JsonProperty("phone") String phone,
            @JsonProperty("email") String email,
            @JsonProperty("address") String address,
            @JsonProperty("stage") String stage,
            @JsonProperty("aliases") List<String> aliases,
            @JsonProperty("notes") String notes,
            @JsonProperty("risk") String risk,
            @JsonProperty("password") String password,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("encounters") List<JsonAdaptedEncounter> encounters) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.stage = stage;
        if (aliases != null) {
            this.aliases.addAll(aliases);
        }
        this.notes = notes;
        this.risk = risk;
        this.password = password;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (encounters != null) {
            this.encounters.addAll(encounters);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        stage = source.getStage().toString();
        aliases.addAll(source.getAliases().stream()
                .map(a -> a.value)
                .collect(Collectors.toList()));
        notes = source.getNotes().value;
        risk = source.getRisk().toString();
        password = source.getPassword() == null ? null : source.getPassword().toString();
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        encounters.addAll(source.getEncounters().stream()
                .map(JsonAdaptedEncounter::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final List<Alias> personAliases = new ArrayList<>();
        if (aliases != null) {
            for (String alias : aliases) {
                if (alias == null || !Alias.isValidAlias(alias.trim())) {
                    throw new IllegalValueException(Alias.MESSAGE_CONSTRAINTS);
                }
                personAliases.add(new Alias(alias));
            }
        }

        final List<Encounter> personEncounters = new ArrayList<>();
        if (encounters != null) {
            for (JsonAdaptedEncounter encounter : encounters) {
                personEncounters.add(encounter.toModelType());
            }
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Notes modelNotes;
        if (notes == null) {
            modelNotes = new Notes("");
        } else {
            if (!Notes.isValidNotes(notes)) {
                throw new IllegalValueException(Notes.MESSAGE_CONSTRAINTS);
            }
            modelNotes = new Notes(notes);
        }

        final Risk modelRisk;
        if (risk == null) {
            modelRisk = Risk.getDefault();
        } else {
            try {
                modelRisk = Risk.fromString(risk);
            } catch (IllegalArgumentException ex) {
                throw new IllegalValueException(Risk.MESSAGE_CONSTRAINTS);
            }
        }

        if (stage == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Stage.class.getSimpleName()));
        }
        final Stage modelStage;
        try {
            modelStage = Stage.fromString(stage);
        } catch (IllegalArgumentException ex) {
            throw new IllegalValueException(Stage.MESSAGE_CONSTRAINTS);
        }

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Password modelPassword;
        if (password == null) {
            modelPassword = null;
        } else if (!Password.isValidPassword(password)) {
            throw new IllegalValueException(Password.MESSAGE_CONSTRAINTS);
        } else {
            modelPassword = new Password(password);
        }

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelStage,
                personAliases, modelNotes, modelRisk, modelTags, personEncounters, modelPassword);
    }

}
