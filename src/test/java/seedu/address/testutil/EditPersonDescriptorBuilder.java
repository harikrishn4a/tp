package seedu.address.testutil;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Risk;
import seedu.address.model.person.Stage;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setEmail(person.getEmail());
        descriptor.setAddress(person.getAddress());
        descriptor.setStage(person.getStage());
        descriptor.setAliases(person.getAliases());
        descriptor.setNotes(person.getNotes());
        descriptor.setRisk(person.getRisk());
        descriptor.setTags(person.getTags());
        if (person.getPassword() == null) {
            descriptor.clearPassword();
        } else {
            descriptor.setPassword(person.getPassword());
        }
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Sets the {@code Stage} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withStage(String stage) {
        descriptor.setStage(Stage.fromString(stage));
        return this;
    }

    /**
     * Sets the {@code Risk} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withRisk(String risk) {
        descriptor.setRisk(Risk.fromString(risk));
        return this;
    }

    /**
     * Sets the password in the descriptor.
     */
    public EditPersonDescriptorBuilder withPassword(String password) {
        descriptor.setPassword(new seedu.address.model.person.Password(password));
        return this;
    }

    /**
     * Clears the password in the descriptor.
     */
    public EditPersonDescriptorBuilder withoutPassword() {
        descriptor.clearPassword();
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        descriptor.setTags(SampleDataUtil.getTagSet(tags));
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
