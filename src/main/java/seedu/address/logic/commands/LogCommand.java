package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OUTCOME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;

/**
 * Logs an encounter with a contact identified by index in the displayed contact list.
 * The encounter is appended to the contact's existing encounter history.
 */
public class LogCommand extends Command {

    public static final String COMMAND_WORD = "log";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Logs an encounter with the contact identified by the index number"
            + " used in the displayed contact list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME + "TIME "
            + PREFIX_LOCATION + "LOCATION "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + "[" + PREFIX_OUTCOME + "OUTCOME] "
            + "[" + PREFIX_PASSWORD + "PASSWORD]\n"
            + "If the contact is password-protected, " + PREFIX_PASSWORD + " must be the current password.\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2026-02-21 "
            + PREFIX_TIME + "18:30 "
            + PREFIX_LOCATION + "Maxwell Road "
            + PREFIX_DESCRIPTION + "Met at coffee shop "
            + PREFIX_OUTCOME + "Agreed to cooperate\n"
            + "Example (protected): " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2026-02-21 "
            + PREFIX_TIME + "18:30 "
            + PREFIX_LOCATION + "Maxwell Road "
            + PREFIX_DESCRIPTION + "Met at coffee shop "
            + PREFIX_PASSWORD + "hunter2";

    public static final String MESSAGE_SUCCESS = "Encounter logged for %1$s on %2$s.";
    public static final String MESSAGE_PASSWORD_REQUIRED_FOR_LOG =
            "This contact is password-protected. Include " + PREFIX_PASSWORD + "CURRENT_PASSWORD in your log.";
    public static final String MESSAGE_UNEXPECTED_PASSWORD_FOR_LOG =
            "This contact is not password-protected. Remove " + PREFIX_PASSWORD + ".";

    private final Index index;
    private final Encounter encounter;
    private final Optional<String> passwordPrefix;

    /**
     * @param index     of the person in the filtered person list to log the encounter for
     * @param encounter the encounter to append to the person's history
     */
    public LogCommand(Index index, Encounter encounter) {
        this(index, encounter, Optional.empty());
    }

    /**
     * @param passwordPrefix raw string after {@code pw/}, or empty if {@code pw/} was not in the command
     */
    public LogCommand(Index index, Encounter encounter, Optional<String> passwordPrefix) {
        requireNonNull(index);
        requireNonNull(encounter);
        requireNonNull(passwordPrefix);
        this.index = index;
        this.encounter = encounter;
        this.passwordPrefix = passwordPrefix;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToLog = lastShownList.get(index.getZeroBased());

        if (personToLog.hasPassword()) {
            if (passwordPrefix.isEmpty()) {
                throw new CommandException(MESSAGE_PASSWORD_REQUIRED_FOR_LOG);
            }
            if (!personToLog.isPasswordMatch(passwordPrefix.get())) {
                throw new CommandException(ViewCommand.MESSAGE_INCORRECT_PASSWORD);
            }
        } else if (passwordPrefix.isPresent()) {
            throw new CommandException(MESSAGE_UNEXPECTED_PASSWORD_FOR_LOG);
        }

        List<Encounter> updatedEncounters = new ArrayList<>(personToLog.getEncounters());
        updatedEncounters.add(encounter);

        Person updatedPerson = createLoggedPerson(personToLog, updatedEncounters);

        model.setPerson(personToLog, updatedPerson);

        return new CommandResult(
                String.format(MESSAGE_SUCCESS,
                        personToLog.getName(),
                        encounter.getFormattedDateTime()),
                updatedPerson);
    }

    /**
     * Rebuilds the logged contact with updated encounter history.
     */
    private Person createLoggedPerson(Person personToLog, List<Encounter> updatedEncounters) {
        var name = personToLog.getName();
        var phone = personToLog.getPhone();
        var email = personToLog.getEmail();
        var address = personToLog.getAddress();
        var stage = personToLog.getStage();
        var aliases = personToLog.getAliases();
        var notes = personToLog.getNotes();
        var risk = personToLog.getRisk();
        var tags = personToLog.getTags();
        var reminders = personToLog.getReminders();
        var password = personToLog.getPassword();

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

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LogCommand)) {
            return false;
        }

        LogCommand otherLogCommand = (LogCommand) other;
        return index.equals(otherLogCommand.index)
                && encounter.equals(otherLogCommand.encounter)
                && passwordPrefix.equals(otherLogCommand.passwordPrefix);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("encounter", encounter)
                .add("passwordPrefixProvided", passwordPrefix.isPresent())
                .toString();
    }
}
