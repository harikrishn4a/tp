package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Person;

/**
 * Exports encounters filtered by location to a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports encounters matching the given "
            + "location to a CSV file.\n"
            + "Parameters: " + "l/" + "LOCATION\n"
            + "Example: " + COMMAND_WORD + " l/Harbor District";

    public static final String MESSAGE_SUCCESS = "Exported %1$d matching encounters to %2$s.";
    public static final String MESSAGE_NO_SUCH_LOCATION =
            "No encounters found at location %1$s.";
    public static final String MESSAGE_FILE_WRITE_FAILED = "Failed to export to %1$s: %2$s";

    private static final DateTimeFormatter EXPORT_TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final String location;

    /**
     * A single CSV row representing one matching encounter.
     */
    private static class ExportRow {
        private final LocalDateTime encounterDateTime;
        private final String encounterTimestamp;
        private final String encounterDescription;
        private final String encounterOutcome;
        private final String contactName;
        private final String contactTags;

        ExportRow(LocalDateTime encounterDateTime, String encounterTimestamp,
                  String encounterDescription, String encounterOutcome,
                  String contactName, String contactTags) {
            this.encounterDateTime = encounterDateTime;
            this.encounterTimestamp = encounterTimestamp;
            this.encounterDescription = encounterDescription;
            this.encounterOutcome = encounterOutcome;
            this.contactName = contactName;
            this.contactTags = contactTags;
        }
    }

    /**
     * Creates an export command for encounters matching the given location.
     *
     * @param location encounter location to match (must not be blank)
     */
    public ExportCommand(String location) {
        requireNonNull(location);
        this.location = location.trim();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Export should be saved in the app home directory (same base as data/addressbook.json),
        // so we write to "./exports/".
        Path exportDir = Paths.get("exports");
        String timestamp = LocalDateTime.now().format(EXPORT_TIMESTAMP_FORMAT);
        Path exportFile = exportDir.resolve("CrimeWatch-export-" + timestamp + ".csv");

        List<Person> people = model.getAddressBook().getPersonList();

        // Collect matching rows first so we can fail early without creating an empty CSV.
        List<ExportRow> rows = new ArrayList<>();
        for (Person person : people) {
            String contactName = person.getName().fullName;
            String contactTags = person.getTags().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .map(tag -> tag.tagName)
                    .collect(Collectors.joining(", "));

            for (Encounter encounter : person.getEncounters()) {
                if (matchesLocation(encounter, location)) {
                    rows.add(new ExportRow(
                            encounter.dateTime,
                            encounter.getFormattedDateTime(),
                            encounter.description,
                            encounter.outcome.orElse(""),
                            contactName,
                            contactTags));
                }
            }
        }

        if (rows.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_SUCH_LOCATION, location));
        }

        rows.sort(Comparator.comparing(row -> row.encounterDateTime));

        try {
            Files.createDirectories(exportDir);

            try (BufferedWriter writer = Files.newBufferedWriter(exportFile, StandardCharsets.UTF_8)) {
                writer.write("encounterTimestamp,encounterDescription,encounterOutcome,contactName,contactTags");
                writer.newLine();

                for (ExportRow row : rows) {
                    writer.write(csvField(row.encounterTimestamp) + ","
                            + csvField(row.encounterDescription) + ","
                            + csvField(row.encounterOutcome) + ","
                            + csvField(row.contactName) + ","
                            + csvField(row.contactTags));
                    writer.newLine();
                }
            }

            return new CommandResult(String.format(MESSAGE_SUCCESS, rows.size(), exportFile));
        } catch (IOException ioe) {
            throw new CommandException(
                    String.format(MESSAGE_FILE_WRITE_FAILED, exportFile, ioe.getMessage()), ioe);
        }
    }

    private static boolean matchesLocation(Encounter encounter, String targetLocation) {
        // Case-insensitive match; trim both sides to be forgiving of user input spacing.
        return encounter.location.trim().equalsIgnoreCase(targetLocation.trim());
    }

    private static String csvField(String value) {
        String safeValue = value == null ? "" : value;
        String escaped = safeValue.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherExportCommand = (ExportCommand) other;
        return location.equals(otherExportCommand.location);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("location", location)
                .toString();
    }
}

