package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ExportCommandTest {

    private static final Path EXPORT_DIR = Paths.get("exports");
    private static final String EXPORT_PREFIX = "CrimeWatch-export-";

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
            "\"2026-02-21 18:30\",\"Met \"\"suspect\"\", near docks\",\"Agreed \"\"cooperate\"\"\","
            + "\"Alice Pauline\",\"armed, friends\"";

    @BeforeEach
    public void clearExports() throws Exception {
        Files.createDirectories(EXPORT_DIR);
        try (Stream<Path> stream = Files.list(EXPORT_DIR)) {
            stream.filter(p -> p.getFileName().toString().startsWith(EXPORT_PREFIX))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete export file " + p, e);
                        }
                    });
        }
    }

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
        CommandResult result = command.execute(model);

        List<Path> exportedFiles = listExportFiles();
        assertEquals(1, exportedFiles.size());
        Path exportedFile = exportedFiles.get(0);

        List<String> lines = Files.readAllLines(exportedFile, StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertEquals(EXPECTED_HEADER, lines.get(0));
        assertEquals(EXPECTED_BOB_ROW, lines.get(1));
        assertEquals(EXPECTED_ALICE_ROW, lines.get(2));

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, 2, exportedFile);
        assertEquals(expectedMessage, result.getFeedbackToUser());
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

        String expectedMessage = String.format(ExportCommand.MESSAGE_NO_SUCH_LOCATION, "Nowhere Land");
        assertCommandFailure(command, model, expectedMessage);
        List<Path> exportedFiles = listExportFiles();
        assertEquals(0, exportedFiles.size());
    }

    @Test
    public void execute_locationTrimAndCaseInsensitive_matchCreated() throws Exception {
        ExportCommand command = new ExportCommand("  harbor district  ");

        Encounter trailingLocationEncounter = new Encounter(
                LocalDateTime.of(2026, 2, 21, 18, 30),
                "Harbor District   ",
                "Trailing-space location match",
                Optional.empty());

        Person person = new PersonBuilder().withName("Alice Pauline")
                .withTags("friends", "armed")
                .withEncounters(trailingLocationEncounter)
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(person);
        Model model = new ModelManager(addressBook, new UserPrefs());
        CommandResult result = command.execute(model);
        List<Path> exportedFiles = listExportFiles();
        assertEquals(1, exportedFiles.size());
        Path exportedFile = exportedFiles.get(0);

        List<String> lines = Files.readAllLines(exportedFile, StandardCharsets.UTF_8);
        assertEquals(2, lines.size());
        assertEquals(EXPECTED_HEADER, lines.get(0));
        assertEquals(
                "\"2026-02-21 18:30\",\"Trailing-space location match\",\"\",\"Alice Pauline\",\"armed, friends\"",
                lines.get(1));

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, 1, exportedFile);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_emptyTags_stillExportsAndLeavesContactTagsBlank() throws Exception {
        ExportCommand command = new ExportCommand("Harbor District");

        Person personWithNoTags = new PersonBuilder().withName("Bob Brown")
                .withEncounters(BOB_ENCOUNTER)
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(personWithNoTags);
        Model model = new ModelManager(addressBook, new UserPrefs());
        CommandResult result = command.execute(model);
        List<Path> exportedFiles = listExportFiles();
        assertEquals(1, exportedFiles.size());
        Path exportedFile = exportedFiles.get(0);

        List<String> lines = Files.readAllLines(exportedFile, StandardCharsets.UTF_8);
        assertEquals(2, lines.size());
        assertEquals(EXPECTED_HEADER, lines.get(0));
        assertEquals(
                "\"2025-06-01 09:00\",\"Spotted \"\"suspect\"\", leaving\",\"\",\"Bob Brown\",\"\"",
                lines.get(1));

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, 1, exportedFile);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_multipleEncountersSortedByTimestamp() throws Exception {
        ExportCommand command = new ExportCommand("Harbor District");

        Encounter e1 = new Encounter(
                LocalDateTime.of(2025, 6, 1, 8, 0),
                "Harbor District",
                "First",
                Optional.empty());
        Encounter eMid = new Encounter(
                LocalDateTime.of(2025, 6, 1, 9, 0),
                "Harbor District",
                "Second",
                Optional.empty());
        Encounter e2 = new Encounter(
                LocalDateTime.of(2025, 6, 1, 10, 0),
                "Harbor District",
                "Third",
                Optional.empty());

        Person p1 = new PersonBuilder().withName("Person One")
                .withTags("b")
                .withEncounters(e1, e2)
                .build();
        Person p2 = new PersonBuilder().withName("Person Two")
                .withTags("a")
                .withEncounters(eMid)
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(p1);
        addressBook.addPerson(p2);
        Model model = new ModelManager(addressBook, new UserPrefs());
        CommandResult result = command.execute(model);
        List<Path> exportedFiles = listExportFiles();
        assertEquals(1, exportedFiles.size());
        Path exportedFile = exportedFiles.get(0);

        List<String> lines = Files.readAllLines(exportedFile, StandardCharsets.UTF_8);
        assertEquals(4, lines.size());
        assertEquals(EXPECTED_HEADER, lines.get(0));
        assertEquals(
                "\"2025-06-01 08:00\",\"First\",\"\",\"Person One\",\"b\"",
                lines.get(1));
        assertEquals(
                "\"2025-06-01 09:00\",\"Second\",\"\",\"Person Two\",\"a\"",
                lines.get(2));
        assertEquals(
                "\"2025-06-01 10:00\",\"Third\",\"\",\"Person One\",\"b\"",
                lines.get(3));

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, 3, exportedFile);
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    private static List<Path> listExportFiles() throws Exception {
        if (!Files.exists(EXPORT_DIR)) {
            return List.of();
        }

        try (Stream<Path> stream = Files.list(EXPORT_DIR)) {
            return stream.filter(p -> p.getFileName().toString().startsWith(EXPORT_PREFIX))
                    .collect(Collectors.toList());
        }
    }
}

