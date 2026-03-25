package seedu.address.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' CrimeWatch file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' CrimeWatch file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces CrimeWatch data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the CrimeWatch.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the CrimeWatch.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the CrimeWatch.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the CrimeWatch.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the CrimeWatch.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Sets the comparator used to sort the filtered person list view.
     *
     * <p>Sorting is view-only and does not mutate persisted address book ordering.</p>
     *
     * @throws NullPointerException if {@code comparator} is null.
     */
    void setPersonSortComparator(Comparator<Person> comparator);

    /**
     * Clears any active comparator and restores insertion order for the filtered person list view.
     */
    void clearPersonSortComparator();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);
}
