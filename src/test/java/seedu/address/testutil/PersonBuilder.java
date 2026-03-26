package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private seedu.address.model.person.Stage stage;
    private List<Alias> aliases;
    private Notes notes;
    private Risk risk;
    private Set<Tag> tags;
    private List<Encounter> encounters;
    private Password password;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        stage = seedu.address.model.person.Stage.SURVEILLANCE;
        aliases = new ArrayList<>();
        notes = new Notes("");
        risk = Risk.getDefault();
        tags = new HashSet<>();
        encounters = new ArrayList<>();
        password = null;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        stage = personToCopy.getStage();
        aliases = new ArrayList<>(personToCopy.getAliases());
        notes = personToCopy.getNotes();
        risk = personToCopy.getRisk();
        tags = new HashSet<>(personToCopy.getTags());
        encounters = new ArrayList<>(personToCopy.getEncounters());
        password = personToCopy.getPassword();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Stage} of the {@code Person} that we are building.
     */
    public PersonBuilder withStage(String stage) {
        this.stage = seedu.address.model.person.Stage.fromString(stage);
        return this;
    }

    /**
     * Sets a single alias shortcut.
     */
    public PersonBuilder withAlias(String alias) {
        this.aliases = List.of(new Alias(alias));
        return this;
    }

    /**
     * Sets the aliases of the {@code Person} that we are building.
     */
    public PersonBuilder withAliases(String... aliases) {
        this.aliases = Arrays.stream(aliases).map(Alias::new).collect(Collectors.toList());
        return this;
    }

    /**
     * Sets the {@code Notes} of the {@code Person} that we are building.
     */
    public PersonBuilder withNotes(String notes) {
        this.notes = new Notes(notes);
        return this;
    }

    /**
     * Sets the {@code Risk} of the {@code Person} that we are building.
     */
    public PersonBuilder withRisk(String risk) {
        this.risk = Risk.fromString(risk);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code encounters} of the {@code Person} that we are building.
     */
    public PersonBuilder withEncounters(Encounter... encounters) {
        this.encounters = new ArrayList<>(Arrays.asList(encounters));
        return this;
    }

    /**
     * Sets the password for the person.
     */
    public PersonBuilder withPassword(String password) {
        this.password = new Password(password);
        return this;
    }

    /**
     * Clears password protection for the person.
     */
    public PersonBuilder withoutPassword() {
        this.password = null;
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, address, stage, aliases, notes, risk, tags, encounters, password);
    }
}
