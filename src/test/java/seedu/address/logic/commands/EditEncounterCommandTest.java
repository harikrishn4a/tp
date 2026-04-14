package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditEncounterCommand.EditEncounterDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;
import seedu.address.model.person.Reminder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditEncounterCommand.
 */
public class EditEncounterCommandTest {

    private static final Encounter OLDER_ENCOUNTER = new Encounter(
            LocalDateTime.of(2026, 3, 20, 10, 30),
            "Chinatown",
            "Brief interaction",
            Optional.of("Observed"));

    private static final Encounter NEWER_ENCOUNTER = new Encounter(
            LocalDateTime.of(2026, 3, 25, 19, 15),
            "Marina Bay",
            "Long discussion",
            Optional.of("Pending"));
    private static final Reminder REMINDER = new Reminder(
            LocalDate.of(2026, 4, 1),
            LocalTime.of(9, 0),
            "Follow up");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexesAndFields_success() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withEncounters = new PersonBuilder(original).withEncounters(OLDER_ENCOUNTER, NEWER_ENCOUNTER).build();
        model.setPerson(original, withEncounters);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setLocation("Maxwell Road");
        descriptor.setDescription("Met at coffee shop");
        descriptor.setOutcome(Optional.of("Agreed to follow up"));
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor);

        Encounter editedNewer = new Encounter(
                LocalDateTime.of(2026, 3, 25, 19, 15),
                "Maxwell Road",
                "Met at coffee shop",
                Optional.of("Agreed to follow up"));

        Person expectedEditedPerson = new PersonBuilder(withEncounters)
                .withEncounters(OLDER_ENCOUNTER, editedNewer)
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(withEncounters, expectedEditedPerson);

        CommandResult expectedResult = new CommandResult(
                String.format(EditEncounterCommand.MESSAGE_SUCCESS, 1, expectedEditedPerson.getName()),
                expectedEditedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_clearOutcome_success() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withEncounters = new PersonBuilder(original).withEncounters(OLDER_ENCOUNTER).build();
        model.setPerson(original, withEncounters);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setOutcome(Optional.empty());
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor);

        Encounter edited = new Encounter(
                LocalDateTime.of(2026, 3, 20, 10, 30),
                "Chinatown",
                "Brief interaction",
                Optional.empty());

        Person expectedEditedPerson = new PersonBuilder(withEncounters).withEncounters(edited).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(withEncounters, expectedEditedPerson);

        CommandResult expectedResult = new CommandResult(
                String.format(EditEncounterCommand.MESSAGE_SUCCESS, 1, expectedEditedPerson.getName()),
                expectedEditedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_editDateOnly_preservesTime() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withEncounters = new PersonBuilder(original).withEncounters(OLDER_ENCOUNTER).build();
        model.setPerson(original, withEncounters);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDate(LocalDate.of(2026, 4, 1));
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor);

        Encounter edited = new Encounter(
                LocalDateTime.of(2026, 4, 1, 10, 30),
                "Chinatown",
                "Brief interaction",
                Optional.of("Observed"));

        Person expectedEditedPerson = new PersonBuilder(withEncounters).withEncounters(edited).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(withEncounters, expectedEditedPerson);

        CommandResult expectedResult = new CommandResult(
                String.format(EditEncounterCommand.MESSAGE_SUCCESS, 1, expectedEditedPerson.getName()),
                expectedEditedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_editTimeOnly_preservesDate() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withEncounters = new PersonBuilder(original).withEncounters(OLDER_ENCOUNTER).build();
        model.setPerson(original, withEncounters);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setTime(LocalTime.of(11, 45));
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor);

        Encounter edited = new Encounter(
                LocalDateTime.of(2026, 3, 20, 11, 45),
                "Chinatown",
                "Brief interaction",
                Optional.of("Observed"));

        Person expectedEditedPerson = new PersonBuilder(withEncounters).withEncounters(edited).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(withEncounters, expectedEditedPerson);

        CommandResult expectedResult = new CommandResult(
                String.format(EditEncounterCommand.MESSAGE_SUCCESS, 1, expectedEditedPerson.getName()),
                expectedEditedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_failure() {
        Index outOfBound = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDescription("Updated");
        EditEncounterCommand command = new EditEncounterCommand(outOfBound, Index.fromOneBased(1), descriptor);
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidEncounterIndex_failure() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withEncounters = new PersonBuilder(original).withEncounters(OLDER_ENCOUNTER).build();
        model.setPerson(original, withEncounters);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDescription("Updated");
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2), descriptor);

        assertCommandFailure(command, model, EditEncounterCommand.MESSAGE_INVALID_ENCOUNTER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_preservesEncounterOrder() throws Exception {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withEncounters = new PersonBuilder(original).withEncounters(OLDER_ENCOUNTER, NEWER_ENCOUNTER).build();
        model.setPerson(original, withEncounters);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDescription("Updated newest");
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor);

        command.execute(model);

        List<Encounter> result = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getEncounters();
        assertEquals("Brief interaction", result.get(0).description);
        assertEquals("Updated newest", result.get(1).description);
    }

    @Test
    public void execute_indexingUsesChronologicalOrder_success() throws Exception {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Simulate logging order that does not match chronological order.
        Encounter mostRecentByDateTime = new Encounter(
                LocalDateTime.of(2026, 3, 25, 19, 15),
                "Marina Bay",
                "Chronologically newest",
                Optional.of("Pending"));
        Encounter oldestByDateTime = new Encounter(
                LocalDateTime.of(2026, 3, 20, 10, 30),
                "Chinatown",
                "Chronologically oldest",
                Optional.of("Observed"));

        Person withEncounters = new PersonBuilder(original)
                .withEncounters(mostRecentByDateTime, oldestByDateTime)
                .build();
        model.setPerson(original, withEncounters);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDescription("Edited by index #1");
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor);

        command.execute(model);

        List<Encounter> result = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getEncounters();
        assertEquals("Edited by index #1", result.get(0).description);
        assertEquals("Chronologically oldest", result.get(1).description);
    }

    @Test
    public void execute_preservesReminders() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withEncounterAndReminder = new Person(
                original.getName(),
                original.getPhone(),
                original.getEmail(),
                original.getAddress(),
                original.getStage(),
                original.getAliases(),
                original.getNotes(),
                original.getRisk(),
                original.getTags(),
                List.of(OLDER_ENCOUNTER),
                List.of(REMINDER));
        model.setPerson(original, withEncounterAndReminder);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDescription("Updated description");
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor);

        Encounter edited = new Encounter(
                LocalDateTime.of(2026, 3, 20, 10, 30),
                "Chinatown",
                "Updated description",
                Optional.of("Observed"));
        Person expectedEditedPerson = new Person(
                withEncounterAndReminder.getName(),
                withEncounterAndReminder.getPhone(),
                withEncounterAndReminder.getEmail(),
                withEncounterAndReminder.getAddress(),
                withEncounterAndReminder.getStage(),
                withEncounterAndReminder.getAliases(),
                withEncounterAndReminder.getNotes(),
                withEncounterAndReminder.getRisk(),
                withEncounterAndReminder.getTags(),
                List.of(edited),
                List.of(REMINDER));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(withEncounterAndReminder, expectedEditedPerson);

        CommandResult expectedResult = new CommandResult(
                String.format(EditEncounterCommand.MESSAGE_SUCCESS, 1, expectedEditedPerson.getName()),
                expectedEditedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_protectedContact_passwordPreserved() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person protectedPerson = new PersonBuilder(original)
                .withEncounters(OLDER_ENCOUNTER)
                .withPassword("hunter2")
                .build();
        model.setPerson(original, protectedPerson);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDescription("Updated description");
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor);

        Encounter edited = new Encounter(
                LocalDateTime.of(2026, 3, 20, 10, 30),
                "Chinatown",
                "Updated description",
                Optional.of("Observed"));
        Person expectedEditedPerson = new Person(
                protectedPerson.getName(),
                protectedPerson.getPhone(),
                protectedPerson.getEmail(),
                protectedPerson.getAddress(),
                protectedPerson.getStage(),
                protectedPerson.getAliases(),
                protectedPerson.getNotes(),
                protectedPerson.getRisk(),
                protectedPerson.getTags(),
                List.of(edited),
                protectedPerson.getReminders(),
                protectedPerson.getPassword());
        assertTrue(expectedEditedPerson.hasPassword());
        assertTrue(expectedEditedPerson.isPasswordMatch("hunter2"));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(protectedPerson, expectedEditedPerson);

        CommandResult expectedResult = new CommandResult(
                String.format(EditEncounterCommand.MESSAGE_SUCCESS, 1, expectedEditedPerson.getName()),
                expectedEditedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_success_returnsEditedPersonToView() throws Exception {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withEncounter = new PersonBuilder(original).withEncounters(OLDER_ENCOUNTER).build();
        model.setPerson(original, withEncounter);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDescription("Updated description");
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor);

        CommandResult result = command.execute(model);
        assertTrue(result.getPersonToView().isPresent());
        assertEquals(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()),
                result.getPersonToView().get());
    }

    @Test
    public void equals() {
        EditEncounterDescriptor descriptor1 = new EditEncounterDescriptor();
        descriptor1.setDescription("Desc 1");
        EditEncounterDescriptor descriptor2 = new EditEncounterDescriptor();
        descriptor2.setDescription("Desc 2");

        EditEncounterCommand first = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor1);
        EditEncounterCommand second = new EditEncounterCommand(INDEX_SECOND_PERSON, Index.fromOneBased(1), descriptor1);
        EditEncounterCommand differentDescriptor =
                new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor2);

        assertTrue(first.equals(first));
        assertTrue(first.equals(new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor1)));
        assertFalse(first.equals(null));
        assertFalse(first.equals(5));
        assertFalse(first.equals(second));
        assertFalse(first.equals(differentDescriptor));
    }

    @Test
    public void toStringMethod() {
        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDescription("Updated description");
        EditEncounterCommand command = new EditEncounterCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), descriptor);

        String expected = EditEncounterCommand.class.getCanonicalName()
                + "{personIndex=" + INDEX_FIRST_PERSON
                + ", encounterIndex=" + Index.fromOneBased(1)
                + ", editEncounterDescriptor=" + descriptor + "}";
        assertEquals(expected, command.toString());
    }
}
