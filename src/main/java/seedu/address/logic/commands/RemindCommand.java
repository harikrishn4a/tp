package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Reminder;

/**
 * Adds a reminder to a contact identified by index in the displayed contact list.
 */
public class RemindCommand extends Command {

    public static final String COMMAND_WORD = "remind";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets a reminder for the contact identified by the index number "
            + "used in the displayed contact list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME + "TIME "
            + PREFIX_NOTES + "NOTE "
            + "[" + PREFIX_PASSWORD + "PASSWORD]\n"
            + "If the contact is password-protected, " + PREFIX_PASSWORD + " must be the current password.\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2026-03-28 "
            + PREFIX_TIME + "20:00 "
            + PREFIX_NOTES + "Meet informant\n"
            + "Example (protected): " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2026-03-28 "
            + PREFIX_TIME + "20:00 "
            + PREFIX_NOTES + "Meet informant "
            + PREFIX_PASSWORD + "hunter2";

    public static final String MESSAGE_SUCCESS = "Reminder set for %1$s on %2$s %3$s.";
    public static final String MESSAGE_DUPLICATE_REMINDER = "This reminder already exists for this contact.";
    public static final String MESSAGE_PASSWORD_REQUIRED_FOR_REMIND =
            "This contact is password-protected. Include " + PREFIX_PASSWORD + "CURRENT_PASSWORD in your remind.";
    public static final String MESSAGE_UNEXPECTED_PASSWORD_FOR_REMIND =
            "This contact is not password-protected. Remove " + PREFIX_PASSWORD + ".";

    private final Index index;
    private final Reminder reminder;
    private final Optional<String> passwordPrefix;

    /**
     * @param index index of person in the filtered person list to set the reminder for
     * @param reminder reminder to add to the person's reminders list
     */
    public RemindCommand(Index index, Reminder reminder) {
        this(index, reminder, Optional.empty());
    }

    /**
     * @param passwordPrefix raw string after {@code pw/}, or empty if {@code pw/} was not in the command
     */
    public RemindCommand(Index index, Reminder reminder, Optional<String> passwordPrefix) {
        requireNonNull(index);
        requireNonNull(reminder);
        requireNonNull(passwordPrefix);
        this.index = index;
        this.reminder = reminder;
        this.passwordPrefix = passwordPrefix;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person targetPerson = lastShownList.get(index.getZeroBased());
        if (targetPerson.getReminders().contains(reminder)) {
            throw new CommandException(MESSAGE_DUPLICATE_REMINDER);
        }

        if (targetPerson.hasPassword()) {
            if (passwordPrefix.isEmpty()) {
                throw new CommandException(MESSAGE_PASSWORD_REQUIRED_FOR_REMIND);
            }
            if (!targetPerson.isPasswordMatch(passwordPrefix.get())) {
                throw new CommandException(ViewCommand.MESSAGE_INCORRECT_PASSWORD);
            }
        } else if (passwordPrefix.isPresent()) {
            throw new CommandException(MESSAGE_UNEXPECTED_PASSWORD_FOR_REMIND);
        }

        model.addReminderToContact(index, reminder);
        Person updatedPerson = model.getFilteredPersonList().get(index.getZeroBased());

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                targetPerson.getName(),
                reminder.getDate(),
                reminder.getTime()),
                updatedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemindCommand)) {
            return false;
        }

        RemindCommand otherCommand = (RemindCommand) other;
        return index.equals(otherCommand.index)
                && reminder.equals(otherCommand.reminder)
                && passwordPrefix.equals(otherCommand.passwordPrefix);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("reminder", reminder)
                .add("passwordPrefixProvided", passwordPrefix.isPresent())
                .toString();
    }
}
