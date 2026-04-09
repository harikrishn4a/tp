package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;
import seedu.address.model.person.Reminder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code LogCommand}.
 */
public class LogCommandTest {

    private static final String SAMPLE_PASSWORD = "hunter2";

    private static final Encounter ENCOUNTER_NO_OUTCOME = new Encounter(
            LocalDateTime.of(2026, 2, 21, 18, 30),
            "Maxwell Road",
            "Met at coffee shop",
            Optional.empty());

    private static final Encounter ENCOUNTER_WITH_OUTCOME = new Encounter(
            LocalDateTime.of(2026, 2, 21, 18, 30),
            "Maxwell Road",
            "Met at coffee shop",
            Optional.of("Agreed to cooperate"));

    private static final Encounter ENCOUNTER_OTHER = new Encounter(
            LocalDateTime.of(2025, 6, 1, 9, 0),
            "Orchard Road",
            "Brief sighting near mall entrance",
            Optional.empty());
    private static final Reminder REMINDER = new Reminder(
            java.time.LocalDate.of(2026, 3, 28),
            java.time.LocalTime.of(20, 0),
            "Meet informant");

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    // ── execute: success ─────────────────────────────────────────────────────

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToLog = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME);

        String expectedMessage = String.format(
                LogCommand.MESSAGE_SUCCESS,
                personToLog.getName(),
                ENCOUNTER_NO_OUTCOME.getFormattedDateTime());

        Person updatedPerson = new PersonBuilder(personToLog)
                .withEncounters(ENCOUNTER_NO_OUTCOME)
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToLog, updatedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, updatedPerson);
        assertCommandSuccess(logCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_withOutcome_success() {
        Person personToLog = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_WITH_OUTCOME);

        String expectedMessage = String.format(
                LogCommand.MESSAGE_SUCCESS,
                personToLog.getName(),
                ENCOUNTER_WITH_OUTCOME.getFormattedDateTime());

        Person updatedPerson = new PersonBuilder(personToLog)
                .withEncounters(ENCOUNTER_WITH_OUTCOME)
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToLog, updatedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, updatedPerson);
        assertCommandSuccess(logCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_validIndexUnfilteredList_appendsToExistingEncounters() {
        // Prime the person with one existing encounter
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithOne = new PersonBuilder(original)
                .withEncounters(ENCOUNTER_NO_OUTCOME)
                .build();
        model.setPerson(original, personWithOne);

        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_OTHER);

        String expectedMessage = String.format(
                LogCommand.MESSAGE_SUCCESS,
                personWithOne.getName(),
                ENCOUNTER_OTHER.getFormattedDateTime());

        Person updatedPerson = new PersonBuilder(personWithOne)
                .withEncounters(ENCOUNTER_NO_OUTCOME, ENCOUNTER_OTHER)
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithOne, updatedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, updatedPerson);
        assertCommandSuccess(logCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_validIndexUnfilteredList_preservesReminders() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithReminder = new Person(
                original.getName(),
                original.getPhone(),
                original.getEmail(),
                original.getAddress(),
                original.getStage(),
                original.getAliases(),
                original.getNotes(),
                original.getRisk(),
                original.getTags(),
                original.getEncounters(),
                java.util.List.of(REMINDER));
        model.setPerson(original, personWithReminder);

        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_WITH_OUTCOME);
        String expectedMessage = String.format(
                LogCommand.MESSAGE_SUCCESS,
                personWithReminder.getName(),
                ENCOUNTER_WITH_OUTCOME.getFormattedDateTime());

        Person updatedPerson = new Person(
                personWithReminder.getName(),
                personWithReminder.getPhone(),
                personWithReminder.getEmail(),
                personWithReminder.getAddress(),
                personWithReminder.getStage(),
                personWithReminder.getAliases(),
                personWithReminder.getNotes(),
                personWithReminder.getRisk(),
                personWithReminder.getTags(),
                java.util.List.of(ENCOUNTER_WITH_OUTCOME),
                java.util.List.of(REMINDER));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithReminder, updatedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, updatedPerson);
        assertCommandSuccess(logCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        CommandTestUtil.showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToLog = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME);

        String expectedMessage = String.format(
                LogCommand.MESSAGE_SUCCESS,
                personToLog.getName(),
                ENCOUNTER_NO_OUTCOME.getFormattedDateTime());

        Person updatedPerson = new PersonBuilder(personToLog)
                .withEncounters(ENCOUNTER_NO_OUTCOME)
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        CommandTestUtil.showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(personToLog, updatedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, updatedPerson);
        assertCommandSuccess(logCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_protectedContact_passwordPreserved() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person protectedPerson = new PersonBuilder(original)
                .withPassword("hunter2")
                .build();
        model.setPerson(original, protectedPerson);

        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME, Optional.of("hunter2"));
        String expectedMessage = String.format(
                LogCommand.MESSAGE_SUCCESS,
                protectedPerson.getName(),
                ENCOUNTER_NO_OUTCOME.getFormattedDateTime());

        Person updatedPerson = new PersonBuilder(protectedPerson)
                .withEncounters(ENCOUNTER_NO_OUTCOME)
                .build();
        assertTrue(updatedPerson.hasPassword());
        assertTrue(updatedPerson.isPasswordMatch("hunter2"));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(protectedPerson, updatedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, updatedPerson);
        assertCommandSuccess(logCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_success_returnsLoggedPersonToView() throws Exception {
        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME);

        CommandResult result = logCommand.execute(model);

        assertTrue(result.getPersonToView().isPresent());
        assertEquals(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()),
                result.getPersonToView().get());
    }

    @Test
    public void execute_protectedPassword_success() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person passwordProtected = new PersonBuilder(original).withPassword(SAMPLE_PASSWORD).build();
        model.setPerson(original, passwordProtected);

        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME, Optional.of(SAMPLE_PASSWORD));

        String expectedMessage = String.format(
                LogCommand.MESSAGE_SUCCESS,
                passwordProtected.getName(),
                ENCOUNTER_NO_OUTCOME.getFormattedDateTime());

        Person updatedPerson = new PersonBuilder(passwordProtected)
                .withEncounters(ENCOUNTER_NO_OUTCOME)
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(passwordProtected, updatedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, updatedPerson);
        assertCommandSuccess(logCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_protectedPasswordRequired_failure() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person passwordProtected = new PersonBuilder(original).withPassword(SAMPLE_PASSWORD).build();
        model.setPerson(original, passwordProtected);

        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME);
        assertCommandFailure(logCommand, model, LogCommand.MESSAGE_PASSWORD_REQUIRED_FOR_LOG);
    }

    @Test
    public void execute_protectedWrongPassword_failure() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person passwordProtected = new PersonBuilder(original).withPassword(SAMPLE_PASSWORD).build();
        model.setPerson(original, passwordProtected);

        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME, Optional.of("wrong"));
        assertCommandFailure(logCommand, model, ViewCommand.MESSAGE_INCORRECT_PASSWORD);
    }

    @Test
    public void execute_unprotectedUnexpectedPassword_failure() {
        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME, Optional.of("x"));
        assertCommandFailure(logCommand, model, LogCommand.MESSAGE_UNEXPECTED_PASSWORD_FOR_LOG);
    }

    // ── execute: failures ────────────────────────────────────────────────────

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBound = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LogCommand logCommand = new LogCommand(outOfBound, ENCOUNTER_NO_OUTCOME);
        assertCommandFailure(logCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        CommandTestUtil.showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBound = INDEX_SECOND_PERSON;
        // Confirm the index is still within the full address book
        assertTrue(outOfBound.getZeroBased() < model.getAddressBook().getPersonList().size());

        LogCommand logCommand = new LogCommand(outOfBound, ENCOUNTER_NO_OUTCOME);
        assertCommandFailure(logCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    // ── equals ───────────────────────────────────────────────────────────────

    @Test
    public void equals() {
        LogCommand logFirst = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME);
        LogCommand logSecond = new LogCommand(INDEX_SECOND_PERSON, ENCOUNTER_NO_OUTCOME);
        LogCommand logFirstOtherEncounter = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_OTHER);

        // same object -> returns true
        assertTrue(logFirst.equals(logFirst));

        // same values -> returns true
        assertTrue(logFirst.equals(new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME)));

        // null -> returns false
        assertFalse(logFirst.equals(null));

        // different type -> returns false
        assertFalse(logFirst.equals(5));

        // different index -> returns false
        assertFalse(logFirst.equals(logSecond));

        // different encounter -> returns false
        assertFalse(logFirst.equals(logFirstOtherEncounter));

        assertFalse(logFirst.equals(new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME, Optional.of("a"))));
    }

    // ── toString ─────────────────────────────────────────────────────────────

    @Test
    public void toStringMethod() {
        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME);
        String expected = LogCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON
                + ", encounter=" + ENCOUNTER_NO_OUTCOME
                + ", passwordPrefixProvided=false}";
        assertEquals(expected, logCommand.toString());
    }
}
