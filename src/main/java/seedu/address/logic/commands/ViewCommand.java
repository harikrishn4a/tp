package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Displays the full profile of a contact identified by its displayed index.
 * v1.2: shows basic contact info only (Name, Phone, Email, Address, Tags).
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays the full profile of the contact identified by the index number used in the displayed list.\n"
            + "Parameters: INDEX (must be a positive integer) [pw/PASSWORD]\n"
            + "Example: " + COMMAND_WORD + " 1 pw/hunter2";

    public static final String MESSAGE_PASSWORD_REQUIRED =
            "This contact is password-protected. Use view INDEX pw/PASSWORD.";
    public static final String MESSAGE_INCORRECT_PASSWORD = "Incorrect password for this contact.";

    private final Index targetIndex;
    private final String providedPassword;

    public ViewCommand(Index targetIndex) {
        this(targetIndex, null);
    }

    /**
     * Creates a view command for an index with an optional password.
     */
    public ViewCommand(Index targetIndex, String providedPassword) {
        this.targetIndex = targetIndex;
        this.providedPassword = providedPassword;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(targetIndex.getZeroBased());
        if (person.hasPassword()) {
            if (providedPassword == null) {
                throw new CommandException(MESSAGE_PASSWORD_REQUIRED);
            }
            if (!person.isPasswordMatch(providedPassword)) {
                throw new CommandException(MESSAGE_INCORRECT_PASSWORD);
            }
        }
        return new CommandResult(Messages.format(person), person);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ViewCommand)) {
            return false;
        }
        ViewCommand otherViewCommand = (ViewCommand) other;
        return targetIndex.equals(otherViewCommand.targetIndex)
            && java.util.Objects.equals(providedPassword, otherViewCommand.providedPassword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
            .add("hasProvidedPassword", providedPassword != null)
                .toString();
    }
}
