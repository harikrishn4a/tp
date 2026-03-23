package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RISK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Person;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_PHONE + person.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + person.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + person.getAddress().value + " ");
        sb.append(PREFIX_STAGE + person.getStage().toString() + " ");
        if (!person.getAliases().isEmpty()) {
            sb.append(PREFIX_ALIAS)
                    .append(String.join(", ", person.getAliases().stream().map(a -> a.value).toList()))
                    .append(" ");
        }
        if (!person.getNotes().value.isEmpty()) {
            sb.append(PREFIX_NOTES).append(person.getNotes().value).append(" ");
        }
        sb.append(PREFIX_RISK).append(person.getRisk().toString()).append(" ");
        person.getTags().forEach(tag -> sb.append(PREFIX_TAG).append(tag.tagName).append(" "));
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        descriptor.getAliases().ifPresent(aliases -> sb.append(PREFIX_ALIAS)
                .append(String.join(", ", aliases.stream().map(a -> a.value).toList()))
                .append(" "));
        descriptor.getNotes().ifPresent(notes -> sb.append(PREFIX_NOTES).append(notes.value).append(" "));
        descriptor.getRisk().ifPresent(risk -> sb.append(PREFIX_RISK).append(risk.toString()).append(" "));
        descriptor.getTags().ifPresent(tags ->
                tags.forEach(tag -> sb.append(PREFIX_TAG).append(tag.tagName).append(" ")));
        return sb.toString();
    }
}
