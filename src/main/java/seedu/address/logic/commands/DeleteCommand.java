package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from CrimeWatch.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) [" + PREFIX_PASSWORD + "PASSWORD]\n"
            + "If the contact is password-protected, " + PREFIX_PASSWORD + " must be the current password.\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "Example (protected): " + COMMAND_WORD + " 1 " + PREFIX_PASSWORD + "hunter2";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_PASSWORD_REQUIRED_FOR_DELETE =
            "This contact is password-protected. Include " + PREFIX_PASSWORD + "CURRENT_PASSWORD in your delete.";
    public static final String MESSAGE_UNEXPECTED_PASSWORD_FOR_DELETE =
            "This contact is not password-protected. Remove " + PREFIX_PASSWORD + ".";

    private final Index targetIndex;
    private final String providedPassword;

    public DeleteCommand(Index targetIndex) {
        this(targetIndex, null);
    }

    /**
     * Creates a delete command for an index with an optional password.
     */
    public DeleteCommand(Index targetIndex, String providedPassword) {
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

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        if (personToDelete.hasPassword()) {
            if (providedPassword == null) {
                throw new CommandException(MESSAGE_PASSWORD_REQUIRED_FOR_DELETE);
            }
            if (!personToDelete.isPasswordMatch(providedPassword)) {
                throw new CommandException(ViewCommand.MESSAGE_INCORRECT_PASSWORD);
            }
        } else if (providedPassword != null) {
            throw new CommandException(MESSAGE_UNEXPECTED_PASSWORD_FOR_DELETE);
        }

        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex)
            && Objects.equals(providedPassword, otherDeleteCommand.providedPassword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
            .add("hasProvidedPassword", providedPassword != null)
                .toString();
    }
}
