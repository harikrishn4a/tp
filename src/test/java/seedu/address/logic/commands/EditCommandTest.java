package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PASSWORD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RISK_HIGH;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;
import seedu.address.model.person.Reminder;
import seedu.address.model.person.Risk;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB)
                .withRisk(VALID_RISK_HIGH).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withRisk(VALID_RISK_HIGH).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        CommandResult expectedResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_protectedContactWithoutPasswordPrefix_failure() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person protectedPerson = new PersonBuilder(first).withPassword(VALID_PASSWORD).build();
        model.setPerson(first, protectedPerson);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_PASSWORD_REQUIRED_FOR_EDIT);
    }

    @Test
    public void execute_protectedContactWrongPassword_failure() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person protectedPerson = new PersonBuilder(first).withPassword(VALID_PASSWORD).build();
        model.setPerson(first, protectedPerson);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor, Optional.of("wrongpass"));

        assertCommandFailure(editCommand, model, ViewCommand.MESSAGE_INCORRECT_PASSWORD);
    }

    @Test
    public void execute_protectedContactWithPasswordPrefix_success() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person protectedPerson = new PersonBuilder(first).withPassword(VALID_PASSWORD).build();
        model.setPerson(first, protectedPerson);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor, Optional.of(VALID_PASSWORD));

        Person expectedEdited = new PersonBuilder(protectedPerson).withName(VALID_NAME_BOB).build();
        String expectedMessage =
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(expectedEdited));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(protectedPerson, expectedEdited);

        CommandResult expectedResult = new CommandResult(expectedMessage, expectedEdited);
        assertCommandSuccess(editCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_unprotectedPassword_success() {
        Person firstBefore = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor, Optional.of(VALID_PASSWORD));

        Person expectedEdited = new PersonBuilder(firstBefore).withName(VALID_NAME_BOB).withPassword(VALID_PASSWORD)
                .build();
        String expectedMessage =
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(expectedEdited));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstBefore, expectedEdited);

        CommandResult expectedResult = new CommandResult(expectedMessage, expectedEdited);
        assertCommandSuccess(editCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_editFieldsSpecified_preservesEncounters() {
        Encounter existingEncounter = new Encounter(
                LocalDateTime.of(2026, 3, 10, 9, 30),
                "Maxwell Road",
                "Observed handoff",
                Optional.of("Follow-up required"));
        Person personWithEncounter = new PersonBuilder(
                model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withEncounters(existingEncounter)
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personWithEncounter);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withRisk(VALID_RISK_HIGH).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        Person expectedEditedPerson = new PersonBuilder(personWithEncounter).withRisk(VALID_RISK_HIGH).build();
        String expectedMessage =
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(expectedEditedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithEncounter, expectedEditedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, expectedEditedPerson);
        assertCommandSuccess(editCommand, model, expectedResult, expectedModel);
        assertEquals(1, model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getEncounters().size());
        assertEquals(existingEncounter, model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased())
                .getEncounters().get(0));
    }

    @Test
    public void execute_editFieldsSpecified_preservesReminders() {
        Reminder existingReminder = new Reminder(
                LocalDate.of(2026, 4, 1),
                LocalTime.of(8, 45),
                "Briefing");
        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithReminder = new Person(
                originalPerson.getName(),
                originalPerson.getPhone(),
                originalPerson.getEmail(),
                originalPerson.getAddress(),
                originalPerson.getStage(),
                originalPerson.getAliases(),
                originalPerson.getNotes(),
                originalPerson.getRisk(),
                originalPerson.getTags(),
                originalPerson.getEncounters(),
                java.util.List.of(existingReminder),
                originalPerson.getPassword());
        model.setPerson(originalPerson, personWithReminder);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withRisk(VALID_RISK_HIGH).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        Person expectedEditedPerson = new Person(
                personWithReminder.getName(),
                personWithReminder.getPhone(),
                personWithReminder.getEmail(),
                personWithReminder.getAddress(),
                personWithReminder.getStage(),
                personWithReminder.getAliases(),
                personWithReminder.getNotes(),
                Risk.fromString(VALID_RISK_HIGH),
                personWithReminder.getTags(),
                personWithReminder.getEncounters(),
                java.util.List.of(existingReminder),
                personWithReminder.getPassword());
        String expectedMessage =
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(expectedEditedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithReminder, expectedEditedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, expectedEditedPerson);
        assertCommandSuccess(editCommand, model, expectedResult, expectedModel);
        assertEquals(java.util.List.of(existingReminder),
                model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getReminders());
    }

    @Test
    public void execute_success_returnsEditedPersonToView() throws Exception {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withRisk(VALID_RISK_HIGH).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        CommandResult result = editCommand.execute(model);

        assertTrue(result.getPersonToView().isPresent());
        assertEquals(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()),
                result.getPersonToView().get());
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));

        // different authentication password -> returns false
        assertFalse(standardCommand.equals(
                new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor(DESC_AMY), Optional.of("x"))));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + ", passwordPrefixProvided=false}";
        assertEquals(expected, editCommand.toString());
    }

}
