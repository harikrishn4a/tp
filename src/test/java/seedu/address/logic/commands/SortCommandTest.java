package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code SortCommand}.
 */
public class SortCommandTest {

    @Test
    public void execute_locationSort_success() {
        Person zed = new PersonBuilder()
                .withName("Zed One")
                .withEncounters(new Encounter(LocalDateTime.of(2026, 3, 10, 10, 0),
                        "Zurich", "desc", Optional.empty()))
                .build();
        Person amy = new PersonBuilder()
                .withName("Amy Two")
                .withEncounters(new Encounter(LocalDateTime.of(2026, 3, 11, 10, 0),
                        "Amsterdam", "desc", Optional.empty()))
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(zed);
        addressBook.addPerson(amy);
        Model model = new ModelManager(addressBook, new UserPrefs());

        SortCommand command = new SortCommand(SortCommand.SortCriterion.LOCATION);
        CommandResult result = command.execute(model);

        assertEquals(String.format(SortCommand.MESSAGE_SUCCESS, "location"), result.getFeedbackToUser());
        assertEquals(List.of(amy, zed), model.getFilteredPersonList());
    }

    @Test
    public void execute_recentSort_success() {
        Person older = new PersonBuilder()
                .withName("Older Person")
                .withEncounters(new Encounter(LocalDateTime.of(2026, 3, 1, 10, 0),
                        "Athens", "desc", Optional.empty()))
                .build();
        Person newer = new PersonBuilder()
                .withName("Newer Person")
                .withEncounters(new Encounter(LocalDateTime.of(2026, 3, 20, 10, 0),
                        "Berlin", "desc", Optional.empty()))
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(older);
        addressBook.addPerson(newer);
        Model model = new ModelManager(addressBook, new UserPrefs());

        SortCommand command = new SortCommand(SortCommand.SortCriterion.RECENT);
        command.execute(model);

        assertEquals(List.of(newer, older), model.getFilteredPersonList());
    }

    @Test
    public void equals() {
        SortCommand locationSort = new SortCommand(SortCommand.SortCriterion.LOCATION);
        SortCommand recentSort = new SortCommand(SortCommand.SortCriterion.RECENT);

        assertTrue(locationSort.equals(locationSort));
        assertTrue(locationSort.equals(new SortCommand(SortCommand.SortCriterion.LOCATION)));
        assertFalse(locationSort.equals(1));
        assertFalse(locationSort.equals(null));
        assertFalse(locationSort.equals(recentSort));
    }
}
