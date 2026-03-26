package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in CrimeWatch.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Stage stage;

    // Data fields
    private final Address address;
    private final List<Alias> aliases;
    private final Notes notes;
    private final Risk risk;
    private final Set<Tag> tags = new HashSet<>();
    private final List<Encounter> encounters = new ArrayList<>();
    private final Password password;

    /**
     * Full constructor - every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Stage stage,
            List<Alias> aliases, Notes notes, Risk risk, Set<Tag> tags, List<Encounter> encounters,
            Password password) {
        requireAllNonNull(name, phone, email, address, stage, aliases, notes, risk, tags, encounters);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.stage = stage;
        this.aliases = List.copyOf(aliases);
        this.notes = notes;
        this.risk = risk;
        this.tags.addAll(tags);
        this.encounters.addAll(encounters);
        this.password = password;
    }

    /**
     * Convenience constructor without password.
     */
    public Person(Name name, Phone phone, Email email, Address address, Stage stage,
            List<Alias> aliases, Notes notes, Risk risk, Set<Tag> tags, List<Encounter> encounters) {
        this(name, phone, email, address, stage, aliases, notes, risk, tags, encounters, null);
    }

    /**
     * Convenience constructor without encounters.
     */
    public Person(Name name, Phone phone, Email email, Address address, Stage stage,
            List<Alias> aliases, Notes notes, Risk risk, Set<Tag> tags) {
        this(name, phone, email, address, stage, aliases, notes, risk, tags, Collections.emptyList());
    }

    /**
     * Convenience constructor with default notes, risk, empty aliases, and no
     * encounters.
     */
    public Person(Name name, Phone phone, Email email, Address address, Stage stage, Set<Tag> tags) {
        this(name, phone, email, address, stage, List.of(), new Notes(""), Risk.getDefault(), tags,
                Collections.emptyList());
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public List<Alias> getAliases() {
        return Collections.unmodifiableList(aliases);
    }

    /**
     * Returns the first alias, or a derived alias from the name if none exist.
     */
    public Alias getAlias() {
        if (!aliases.isEmpty()) {
            return aliases.get(0);
        }
        return new Alias(name.fullName);
    }

    public Notes getNotes() {
        return notes;
    }

    public Stage getStage() {
        return stage;
    }

    public Risk getRisk() {
        return risk;
    }

    /**
     * Returns an immutable tag set, which throws
     * {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable encounter list, which throws
     * {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Encounter> getEncounters() {
        return Collections.unmodifiableList(encounters);
    }

    /**
     * Returns the password of the person.
     */
    public Password getPassword() {
        return password;
    }

    /**
     * Returns true if this person has a password configured.
     */
    public boolean hasPassword() {
        return password != null;
    }

    /**
     * Returns true if the given raw password matches this person's configured password.
     */
    public boolean isPasswordMatch(String rawPassword) {
        return hasPassword() && password.password.equals(rawPassword);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && stage.equals(otherPerson.stage)
                && aliases.equals(otherPerson.aliases)
                && notes.equals(otherPerson.notes)
                && risk.equals(otherPerson.risk)
                && tags.equals(otherPerson.tags)
                && encounters.equals(otherPerson.encounters)
                && Objects.equals(password, otherPerson.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, stage, aliases, notes, risk, tags, encounters, password);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("stage", stage)
                .add("aliases", aliases)
                .add("notes", notes)
                .add("risk", risk)
                .add("tags", tags)
                .add("encounters", encounters)
                .toString();
    }

}
