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

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code LogCommand}.
 */
public class LogCommandTest {

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

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

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

        assertCommandSuccess(logCommand, model, expectedMessage, expectedModel);
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

        assertCommandSuccess(logCommand, model, expectedMessage, expectedModel);
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

        assertCommandSuccess(logCommand, model, expectedMessage, expectedModel);
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

        assertCommandSuccess(logCommand, model, expectedMessage, expectedModel);
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
    }

    // ── toString ─────────────────────────────────────────────────────────────

    @Test
    public void toStringMethod() {
        LogCommand logCommand = new LogCommand(INDEX_FIRST_PERSON, ENCOUNTER_NO_OUTCOME);
        String expected = LogCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON
                + ", encounter=" + ENCOUNTER_NO_OUTCOME + "}";
        assertEquals(expected, logCommand.toString());
    }
}
