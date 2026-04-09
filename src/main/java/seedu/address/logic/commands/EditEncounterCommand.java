package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OUTCOME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;

/**
 * Edits an existing encounter for a contact in CrimeWatch.
 */
public class EditEncounterCommand extends Command {

    public static final String COMMAND_WORD = "editencounter";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of an encounter identified by person index and encounter index.\n"
            + "Parameters: PERSON_INDEX ENCOUNTER_INDEX "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TIME + "TIME] "
            + "[" + PREFIX_LOCATION + "LOCATION] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_OUTCOME + "OUTCOME]\n"
            + "Example: " + COMMAND_WORD + " 1 2 "
            + PREFIX_LOCATION + "Maxwell Road "
            + PREFIX_DESCRIPTION + "Met at coffee shop";

    public static final String MESSAGE_SUCCESS = "Edited encounter #%1$d for %2$s.";
    public static final String MESSAGE_INVALID_ENCOUNTER_DISPLAYED_INDEX =
            "The encounter index provided is invalid.";
    public static final String MESSAGE_NOT_EDITED = "At least one encounter field to edit must be provided.";

    private final Index personIndex;
    private final Index encounterIndex;
    private final EditEncounterDescriptor editEncounterDescriptor;

    /**
     * @param personIndex of the person in the filtered person list
     * @param encounterIndex display index of encounter shown in view panel (#1 is most recent)
     * @param editEncounterDescriptor encounter fields to edit
     */
    public EditEncounterCommand(
            Index personIndex,
            Index encounterIndex,
            EditEncounterDescriptor editEncounterDescriptor) {
        requireNonNull(personIndex);
        requireNonNull(encounterIndex);
        requireNonNull(editEncounterDescriptor);
        this.personIndex = personIndex;
        this.encounterIndex = encounterIndex;
        this.editEncounterDescriptor = new EditEncounterDescriptor(editEncounterDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(personIndex.getZeroBased());
        List<Encounter> existingEncounters = personToEdit.getEncounters();
        int encounterDisplayOneBased = encounterIndex.getOneBased();
        if (encounterDisplayOneBased > existingEncounters.size()) {
            throw new CommandException(MESSAGE_INVALID_ENCOUNTER_DISPLAYED_INDEX);
        }

        // Encounter cards are shown in reverse-chronological order where #1 is the most recent.
        int encounterZeroBased = existingEncounters.size() - encounterDisplayOneBased;
        Encounter encounterToEdit = existingEncounters.get(encounterZeroBased);
        Encounter editedEncounter = createEditedEncounter(encounterToEdit, editEncounterDescriptor);

        List<Encounter> updatedEncounters = new ArrayList<>(existingEncounters);
        updatedEncounters.set(encounterZeroBased, editedEncounter);

        Person editedPerson = createEditedPerson(personToEdit, updatedEncounters);

        model.setPerson(personToEdit, editedPerson);
        return createSuccessResult(encounterDisplayOneBased, editedPerson);
    }

    /**
     * Rebuilds the contact with updated encounter history after an encounter edit.
     */
    private Person createEditedPerson(Person personToEdit, List<Encounter> updatedEncounters) {
        var name = personToEdit.getName();
        var phone = personToEdit.getPhone();
        var email = personToEdit.getEmail();
        var address = personToEdit.getAddress();
        var stage = personToEdit.getStage();
        var aliases = personToEdit.getAliases();
        var notes = personToEdit.getNotes();
        var risk = personToEdit.getRisk();
        var tags = personToEdit.getTags();
        var reminders = personToEdit.getReminders();
        var password = personToEdit.getPassword();

        return new Person(
                name,
                phone,
                email,
                address,
                stage,
                aliases,
                notes,
                risk,
                tags,
                updatedEncounters,
                reminders,
                password);
    }

    /**
     * Creates the success result for a completed encounter edit.
     */
    private CommandResult createSuccessResult(int encounterDisplayOneBased, Person editedPerson) {
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, encounterDisplayOneBased, editedPerson.getName()),
                editedPerson);
    }

    private static Encounter createEditedEncounter(Encounter encounterToEdit, EditEncounterDescriptor descriptor) {
        LocalDate date = descriptor.getDate().orElse(encounterToEdit.dateTime.toLocalDate());
        LocalTime time = descriptor.getTime().orElse(encounterToEdit.dateTime.toLocalTime());
        LocalDateTime updatedDateTime = LocalDateTime.of(date, time);
        String updatedLocation = descriptor.getLocation().orElse(encounterToEdit.location);
        String updatedDescription = descriptor.getDescription().orElse(encounterToEdit.description);
        Optional<String> updatedOutcome = descriptor.getOutcome().orElse(encounterToEdit.outcome);

        return new Encounter(updatedDateTime, updatedLocation, updatedDescription, updatedOutcome);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EditEncounterCommand)) {
            return false;
        }
        EditEncounterCommand otherCommand = (EditEncounterCommand) other;
        return personIndex.equals(otherCommand.personIndex)
                && encounterIndex.equals(otherCommand.encounterIndex)
                && editEncounterDescriptor.equals(otherCommand.editEncounterDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", personIndex)
                .add("encounterIndex", encounterIndex)
                .add("editEncounterDescriptor", editEncounterDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the encounter with.
     */
    public static class EditEncounterDescriptor {
        private LocalDate date;
        private LocalTime time;
        private String location;
        private String description;
        private Optional<String> outcome;

        public EditEncounterDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditEncounterDescriptor(EditEncounterDescriptor toCopy) {
            setDate(toCopy.date);
            setTime(toCopy.time);
            setLocation(toCopy.location);
            setDescription(toCopy.description);
            setOutcome(toCopy.outcome);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(date, time, location, description, outcome);
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Optional<LocalDate> getDate() {
            return Optional.ofNullable(date);
        }

        public void setTime(LocalTime time) {
            this.time = time;
        }

        public Optional<LocalTime> getTime() {
            return Optional.ofNullable(time);
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Optional<String> getLocation() {
            return Optional.ofNullable(location);
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Optional<String> getDescription() {
            return Optional.ofNullable(description);
        }

        /**
         * Sets outcome to this descriptor. Use Optional.empty() to clear outcome.
         */
        public void setOutcome(Optional<String> outcome) {
            this.outcome = (outcome != null) ? Optional.ofNullable(outcome.orElse(null)) : null;
        }

        /**
         * Returns optional outcome wrapped in Optional, or empty if not edited.
         */
        public Optional<Optional<String>> getOutcome() {
            return Optional.ofNullable(outcome);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof EditEncounterDescriptor)) {
                return false;
            }
            EditEncounterDescriptor otherDescriptor = (EditEncounterDescriptor) other;
            return Objects.equals(date, otherDescriptor.date)
                    && Objects.equals(time, otherDescriptor.time)
                    && Objects.equals(location, otherDescriptor.location)
                    && Objects.equals(description, otherDescriptor.description)
                    && Objects.equals(outcome, otherDescriptor.outcome);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("date", date)
                    .add("time", time)
                    .add("location", location)
                    .add("description", description)
                    .add("outcome", outcome)
                    .toString();
        }
    }
}
