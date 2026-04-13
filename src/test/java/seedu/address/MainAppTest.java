package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.Storage;

public class MainAppTest {

    @Test
    public void initModelManager_noAddressBookFile_usesSampleData() throws Exception {
        MainApp mainApp = new MainApp();
        UserPrefs userPrefs = new UserPrefs();
        Storage storage = new StubStorage(Optional.empty(), userPrefs.getAddressBookFilePath());

        Model model = invokeInitModelManager(mainApp, storage, userPrefs);

        assertEquals(SampleDataUtil.getSamplePersons().length, model.getAddressBook().getPersonList().size());
    }

    private static Model invokeInitModelManager(MainApp mainApp, Storage storage, ReadOnlyUserPrefs userPrefs)
            throws Exception {
        Method method = MainApp.class.getDeclaredMethod("initModelManager", Storage.class, ReadOnlyUserPrefs.class);
        method.setAccessible(true);
        return (Model) method.invoke(mainApp, storage, userPrefs);
    }

    private static class StubStorage implements Storage {
        private final Optional<ReadOnlyAddressBook> addressBookOptional;
        private final Path addressBookFilePath;

        StubStorage(Optional<ReadOnlyAddressBook> addressBookOptional, Path addressBookFilePath) {
            this.addressBookOptional = addressBookOptional;
            this.addressBookFilePath = addressBookFilePath;
        }

        @Override
        public Path getAddressBookFilePath() {
            return addressBookFilePath;
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
            return addressBookOptional;
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
            return addressBookOptional;
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Path getUserPrefsFilePath() {
            return Paths.get("preferences.json");
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            throw new UnsupportedOperationException();
        }

    }
}
