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
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Reminder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code RemindCommand}.
 */
public class RemindCommandTest {

    private static final String SAMPLE_PASSWORD = "hunter2";

    private static final Reminder REMINDER_LATER = new Reminder(
            LocalDate.of(2026, 3, 28), LocalTime.of(20, 0), "Meet informant");
    private static final Reminder REMINDER_EARLIER = new Reminder(
            LocalDate.of(2026, 3, 20), LocalTime.of(9, 30), "Check case file");

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToRemind = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        RemindCommand remindCommand = new RemindCommand(INDEX_FIRST_PERSON, REMINDER_LATER);

        String expectedMessage = String.format(RemindCommand.MESSAGE_SUCCESS,
                personToRemind.getName(), REMINDER_LATER.getDate(), REMINDER_LATER.getTime());

        Person updatedPerson = new Person(
                personToRemind.getName(), personToRemind.getPhone(), personToRemind.getEmail(),
                personToRemind.getAddress(), personToRemind.getStage(), personToRemind.getAliases(),
                personToRemind.getNotes(), personToRemind.getRisk(), personToRemind.getTags(),
                personToRemind.getEncounters(), java.util.List.of(REMINDER_LATER));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToRemind, updatedPerson);

        assertCommandSuccess(remindCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexUnfilteredList_remindersSortedAscending() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withOneReminder = new PersonBuilder(original).build();
        withOneReminder = new Person(withOneReminder.getName(), withOneReminder.getPhone(), withOneReminder.getEmail(),
                withOneReminder.getAddress(), withOneReminder.getStage(), withOneReminder.getAliases(),
                withOneReminder.getNotes(), withOneReminder.getRisk(), withOneReminder.getTags(),
                withOneReminder.getEncounters(), java.util.List.of(REMINDER_LATER));
        model.setPerson(original, withOneReminder);

        RemindCommand remindCommand = new RemindCommand(INDEX_FIRST_PERSON, REMINDER_EARLIER);

        Person expectedUpdated = new Person(
                withOneReminder.getName(), withOneReminder.getPhone(), withOneReminder.getEmail(),
                withOneReminder.getAddress(), withOneReminder.getStage(), withOneReminder.getAliases(),
                withOneReminder.getNotes(), withOneReminder.getRisk(), withOneReminder.getTags(),
                withOneReminder.getEncounters(), java.util.List.of(REMINDER_EARLIER, REMINDER_LATER));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(withOneReminder, expectedUpdated);

        String expectedMessage = String.format(RemindCommand.MESSAGE_SUCCESS,
                withOneReminder.getName(), REMINDER_EARLIER.getDate(), REMINDER_EARLIER.getTime());
        assertCommandSuccess(remindCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexUnfilteredList_passwordPreserved() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person passwordProtected = new PersonBuilder(original).withPassword(SAMPLE_PASSWORD).build();
        model.setPerson(original, passwordProtected);

        RemindCommand remindCommand = new RemindCommand(INDEX_FIRST_PERSON, REMINDER_LATER,
                Optional.of(SAMPLE_PASSWORD));

        Person expectedUpdated = new Person(
                passwordProtected.getName(),
                passwordProtected.getPhone(),
                passwordProtected.getEmail(),
                passwordProtected.getAddress(),
                passwordProtected.getStage(),
                passwordProtected.getAliases(),
                passwordProtected.getNotes(),
                passwordProtected.getRisk(),
                passwordProtected.getTags(),
                passwordProtected.getEncounters(),
                java.util.List.of(REMINDER_LATER),
                passwordProtected.getPassword());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(passwordProtected, expectedUpdated);

        String expectedMessage = String.format(RemindCommand.MESSAGE_SUCCESS,
                passwordProtected.getName(), REMINDER_LATER.getDate(), REMINDER_LATER.getTime());
        assertCommandSuccess(remindCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_protectedPasswordRequired_failure() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person passwordProtected = new PersonBuilder(original).withPassword(SAMPLE_PASSWORD).build();
        model.setPerson(original, passwordProtected);

        RemindCommand remindCommand = new RemindCommand(INDEX_FIRST_PERSON, REMINDER_LATER);
        assertCommandFailure(remindCommand, model, RemindCommand.MESSAGE_PASSWORD_REQUIRED_FOR_REMIND);
    }

    @Test
    public void execute_protectedWrongPassword_failure() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person passwordProtected = new PersonBuilder(original).withPassword(SAMPLE_PASSWORD).build();
        model.setPerson(original, passwordProtected);

        RemindCommand remindCommand = new RemindCommand(INDEX_FIRST_PERSON, REMINDER_LATER, Optional.of("wrong"));
        assertCommandFailure(remindCommand, model, ViewCommand.MESSAGE_INCORRECT_PASSWORD);
    }

    @Test
    public void execute_unprotectedUnexpectedPassword_failure() {
        RemindCommand remindCommand = new RemindCommand(INDEX_FIRST_PERSON, REMINDER_LATER, Optional.of("x"));
        assertCommandFailure(remindCommand, model, RemindCommand.MESSAGE_UNEXPECTED_PASSWORD_FOR_REMIND);
    }

    @Test
    public void execute_sortedList_usesDisplayedIndex() {
        model.setPersonSortComparator(
                Comparator.comparing((Person person) -> person.getName().fullName).reversed());
        Person personToRemind = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        RemindCommand remindCommand = new RemindCommand(INDEX_FIRST_PERSON, REMINDER_LATER);

        Person expectedUpdated = new Person(
                personToRemind.getName(),
                personToRemind.getPhone(),
                personToRemind.getEmail(),
                personToRemind.getAddress(),
                personToRemind.getStage(),
                personToRemind.getAliases(),
                personToRemind.getNotes(),
                personToRemind.getRisk(),
                personToRemind.getTags(),
                personToRemind.getEncounters(),
                java.util.List.of(REMINDER_LATER),
                personToRemind.getPassword());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPersonSortComparator(
                Comparator.comparing((Person person) -> person.getName().fullName).reversed());
        expectedModel.setPerson(personToRemind, expectedUpdated);

        String expectedMessage = String.format(RemindCommand.MESSAGE_SUCCESS,
                personToRemind.getName(), REMINDER_LATER.getDate(), REMINDER_LATER.getTime());
        assertCommandSuccess(remindCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBound = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemindCommand remindCommand = new RemindCommand(outOfBound, REMINDER_LATER);
        assertCommandFailure(remindCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        CommandTestUtil.showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBound = INDEX_SECOND_PERSON;
        assertTrue(outOfBound.getZeroBased() < model.getAddressBook().getPersonList().size());

        RemindCommand remindCommand = new RemindCommand(outOfBound, REMINDER_LATER);
        assertCommandFailure(remindCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        RemindCommand first = new RemindCommand(INDEX_FIRST_PERSON, REMINDER_LATER);
        RemindCommand second = new RemindCommand(INDEX_SECOND_PERSON, REMINDER_LATER);
        RemindCommand firstDifferentReminder = new RemindCommand(INDEX_FIRST_PERSON, REMINDER_EARLIER);

        assertTrue(first.equals(first));
        assertTrue(first.equals(new RemindCommand(INDEX_FIRST_PERSON, REMINDER_LATER)));
        assertFalse(first.equals(null));
        assertFalse(first.equals(5));
        assertFalse(first.equals(second));
        assertFalse(first.equals(firstDifferentReminder));
        assertFalse(first.equals(new RemindCommand(INDEX_FIRST_PERSON, REMINDER_LATER, Optional.of("a"))));
    }

    @Test
    public void toStringMethod() {
        RemindCommand remindCommand = new RemindCommand(INDEX_FIRST_PERSON, REMINDER_LATER);
        String expected = RemindCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON + ", reminder=" + REMINDER_LATER
                + ", passwordPrefixProvided=false}";
        assertEquals(expected, remindCommand.toString());
    }
}
