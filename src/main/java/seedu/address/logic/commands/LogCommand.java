package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OUTCOME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.ArrayList;
import java.util.List;

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
            + "[" + PREFIX_OUTCOME + "OUTCOME]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2026-02-21 "
            + PREFIX_TIME + "18:30 "
            + PREFIX_LOCATION + "Maxwell Road "
            + PREFIX_DESCRIPTION + "Met at coffee shop "
            + PREFIX_OUTCOME + "Agreed to cooperate";

    public static final String MESSAGE_SUCCESS = "Encounter logged for %1$s on %2$s.";

    private final Index index;
    private final Encounter encounter;

    /**
     * @param index     of the person in the filtered person list to log the encounter for
     * @param encounter the encounter to append to the person's history
     */
    public LogCommand(Index index, Encounter encounter) {
        requireNonNull(index);
        requireNonNull(encounter);
        this.index = index;
        this.encounter = encounter;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToLog = lastShownList.get(index.getZeroBased());

        List<Encounter> updatedEncounters = new ArrayList<>(personToLog.getEncounters());
        updatedEncounters.add(encounter);

        Person updatedPerson = new Person(
                personToLog.getName(),
                personToLog.getPhone(),
                personToLog.getEmail(),
                personToLog.getAddress(),
                personToLog.getStage(),
                personToLog.getTags(),
                updatedEncounters);

        model.setPerson(personToLog, updatedPerson);

        return new CommandResult(
                String.format(MESSAGE_SUCCESS,
                        personToLog.getName(),
                        encounter.getFormattedDateTime()));
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
                && encounter.equals(otherLogCommand.encounter);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("encounter", encounter)
                .toString();
    }
}
