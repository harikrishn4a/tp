package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ExportCommandTest {

    private static final Encounter BOB_ENCOUNTER = new Encounter(
            LocalDateTime.of(2025, 6, 1, 9, 0),
            "Harbor District",
            "Spotted \"suspect\", leaving",
            Optional.empty());

    private static final Encounter ALICE_ENCOUNTER = new Encounter(
            LocalDateTime.of(2026, 2, 21, 18, 30),
            "Harbor District",
            "Met \"suspect\", near docks",
            Optional.of("Agreed \"cooperate\""));

    private static final Encounter NON_MATCHING_ENCOUNTER = new Encounter(
            LocalDateTime.of(2026, 3, 1, 10, 0),
            "Other Location",
            "Ignored encounter",
            Optional.empty());

    private static final String EXPECTED_HEADER =
            "encounterTimestamp,encounterDescription,encounterOutcome,contactName,contactTags";

    private static final String EXPECTED_BOB_ROW =
            "\"2025-06-01 09:00\",\"Spotted \"\"suspect\"\", leaving\",\"\",\"Bob Brown\",\"armed, friends\"";

    private static final String EXPECTED_ALICE_ROW =
            "\"2026-02-21 18:30\",\"Met \"\"suspect\"\", near docks\",\"Agreed \"\"cooperate\"\"\",\"Alice Pauline\",\"armed, friends\"";

    @Test
    public void execute_exportsMatchingEncounters_sortedAndEscaped() throws Exception {
        ExportCommand command = new ExportCommand("hArBoR district");

        Person bob = new PersonBuilder().withName("Bob Brown")
                .withTags("friends", "armed")
                .withEncounters(BOB_ENCOUNTER)
                .build();

        Person alice = new PersonBuilder().withName("Alice Pauline")
                .withTags("friends", "armed")
                .withEncounters(ALICE_ENCOUNTER, NON_MATCHING_ENCOUNTER)
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(alice);
        addressBook.addPerson(bob);

        Model model = new ModelManager(addressBook, new UserPrefs());

        Path exportDir = Paths.get("exports");
        Files.createDirectories(exportDir);

        Set<String> existingFiles = listExistingExportFiles(exportDir);

        Path exportedFile = null;
        try {
            CommandResult result = command.execute(model);

            List<Path> newFiles = listNewExportFiles(exportDir, existingFiles);
            assertEquals(1, newFiles.size());
            exportedFile = newFiles.get(0);

            List<String> lines = Files.readAllLines(exportedFile, StandardCharsets.UTF_8);
            assertEquals(3, lines.size());
            assertEquals(EXPECTED_HEADER, lines.get(0));
            assertEquals(EXPECTED_BOB_ROW, lines.get(1));
            assertEquals(EXPECTED_ALICE_ROW, lines.get(2));

            String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, 2, exportedFile);
            assertEquals(expectedMessage, result.getFeedbackToUser());
        } finally {
            if (exportedFile != null) {
                Files.deleteIfExists(exportedFile);
            }
        }
    }

    @Test
    public void execute_noMatchingEncounters_throwsCommandExceptionAndDoesNotCreateFile() throws Exception {
        ExportCommand command = new ExportCommand("Nowhere Land");

        Person bob = new PersonBuilder().withName("Bob Brown")
                .withTags("friends", "armed")
                .withEncounters(BOB_ENCOUNTER)
                .build();

        Person alice = new PersonBuilder().withName("Alice Pauline")
                .withTags("friends", "armed")
                .withEncounters(ALICE_ENCOUNTER, NON_MATCHING_ENCOUNTER)
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(alice);
        addressBook.addPerson(bob);

        Model model = new ModelManager(addressBook, new UserPrefs());

        Path exportDir = Paths.get("exports");
        Files.createDirectories(exportDir);
        Set<String> existingFiles = listExistingExportFiles(exportDir);

        String expectedMessage = String.format(ExportCommand.MESSAGE_NO_SUCH_LOCATION, "Nowhere Land");
        assertCommandFailure(command, model, expectedMessage);

        List<Path> newFiles = listNewExportFiles(exportDir, existingFiles);
        assertEquals(0, newFiles.size());
    }

    private static Set<String> listExistingExportFiles(Path exportDir) throws Exception {
        if (!Files.exists(exportDir)) {
            return Set.of();
        }

        Set<String> existing = new HashSet<>();
        try (Stream<Path> stream = Files.list(exportDir)) {
            stream.map(p -> p.getFileName().toString()).forEach(existing::add);
        }
        return existing;
    }

    private static List<Path> listNewExportFiles(Path exportDir, Set<String> existingFiles)
            throws Exception {
        List<Path> newFiles = new ArrayList<>();
        try (Stream<Path> stream = Files.list(exportDir)) {
            stream.filter(p -> p.getFileName().toString().startsWith("CrimeWatch-export-"))
                    .filter(p -> !existingFiles.contains(p.getFileName().toString()))
                    .forEach(newFiles::add);
        }
        return newFiles;
    }
}

