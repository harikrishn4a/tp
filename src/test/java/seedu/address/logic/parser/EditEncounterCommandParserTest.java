package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OUTCOME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditEncounterCommand;
import seedu.address.logic.commands.EditEncounterCommand.EditEncounterDescriptor;
import seedu.address.model.person.Encounter;

public class EditEncounterCommandParserTest {

    private static final String VALID_PERSON_INDEX = "1";
    private static final String VALID_ENCOUNTER_INDEX = "2";
    private static final String VALID_DATE = "2026-03-26";
    private static final String VALID_TIME = "18:30";
    private static final String VALID_LOCATION = "Maxwell Road";
    private static final String VALID_DESCRIPTION = "Met at coffee shop";
    private static final String VALID_OUTCOME = "Agreed to cooperate";

    private static final String DATE_DESC = " " + PREFIX_DATE + VALID_DATE;
    private static final String TIME_DESC = " " + PREFIX_TIME + VALID_TIME;
    private static final String LOCATION_DESC = " " + PREFIX_LOCATION + VALID_LOCATION;
    private static final String DESCRIPTION_DESC = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION;
    private static final String OUTCOME_DESC = " " + PREFIX_OUTCOME + VALID_OUTCOME;
    private static final String OUTCOME_EMPTY_DESC = " " + PREFIX_OUTCOME;

    private static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "26-03-2026";
    private static final String INVALID_TIME_DESC = " " + PREFIX_TIME + "25:00";
    private static final String INVALID_LOCATION_BLANK = " " + PREFIX_LOCATION + " ";
    private static final String INVALID_DESCRIPTION_BLANK = " " + PREFIX_DESCRIPTION + " ";

    private final EditEncounterCommandParser parser = new EditEncounterCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDate(LocalDate.of(2026, 3, 26));
        descriptor.setTime(LocalTime.of(18, 30));
        descriptor.setLocation(VALID_LOCATION);
        descriptor.setDescription(VALID_DESCRIPTION);
        descriptor.setOutcome(Optional.of(VALID_OUTCOME));

        EditEncounterCommand expectedCommand =
                new EditEncounterCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, descriptor);

        assertParseSuccess(parser,
                VALID_PERSON_INDEX + " " + VALID_ENCOUNTER_INDEX
                        + DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC + OUTCOME_DESC,
                expectedCommand);
    }

    @Test
    public void parse_someFieldsPresent_success() {
        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setDescription(VALID_DESCRIPTION);

        EditEncounterCommand expectedCommand =
                new EditEncounterCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, descriptor);

        assertParseSuccess(parser,
                VALID_PERSON_INDEX + " " + VALID_ENCOUNTER_INDEX + DESCRIPTION_DESC,
                expectedCommand);
    }

    @Test
    public void parse_clearOutcome_success() {
        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        descriptor.setOutcome(Optional.empty());

        EditEncounterCommand expectedCommand =
                new EditEncounterCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, descriptor);

        assertParseSuccess(parser,
                VALID_PERSON_INDEX + " " + VALID_ENCOUNTER_INDEX + OUTCOME_EMPTY_DESC,
                expectedCommand);
    }

    @Test
    public void parse_missingParts_failure() {
        // missing encounter index
        assertParseFailure(parser,
                VALID_PERSON_INDEX + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEncounterCommand.MESSAGE_USAGE));

        // missing person index
        assertParseFailure(parser,
                VALID_ENCOUNTER_INDEX + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEncounterCommand.MESSAGE_USAGE));

        // no fields specified
        assertParseFailure(parser,
                VALID_PERSON_INDEX + " " + VALID_ENCOUNTER_INDEX,
                EditEncounterCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidIndexes_failure() {
        assertParseFailure(parser,
                "0 1" + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEncounterCommand.MESSAGE_USAGE));
        assertParseFailure(parser,
                "1 0" + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEncounterCommand.MESSAGE_USAGE));
        assertParseFailure(parser,
                "a 1" + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEncounterCommand.MESSAGE_USAGE));
        assertParseFailure(parser,
                "1 b" + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEncounterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidValues_failure() {
        assertParseFailure(parser,
                VALID_PERSON_INDEX + " " + VALID_ENCOUNTER_INDEX + INVALID_DATE_DESC + DESCRIPTION_DESC,
                "Invalid date. Use format YYYY-MM-DD.");
        assertParseFailure(parser,
                VALID_PERSON_INDEX + " " + VALID_ENCOUNTER_INDEX + INVALID_TIME_DESC + DESCRIPTION_DESC,
                "Invalid time. Use 24-hour format HH:mm.");
        assertParseFailure(parser,
                VALID_PERSON_INDEX + " " + VALID_ENCOUNTER_INDEX + INVALID_LOCATION_BLANK + DESCRIPTION_DESC,
                Encounter.MESSAGE_LOCATION_CONSTRAINTS);
        assertParseFailure(parser,
                VALID_PERSON_INDEX + " " + VALID_ENCOUNTER_INDEX + INVALID_DESCRIPTION_BLANK,
                Encounter.MESSAGE_DESCRIPTION_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        assertParseFailure(parser,
                VALID_PERSON_INDEX + " " + VALID_ENCOUNTER_INDEX + DATE_DESC + DATE_DESC + DESCRIPTION_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));
        assertParseFailure(parser,
                VALID_PERSON_INDEX + " " + VALID_ENCOUNTER_INDEX
                        + DESCRIPTION_DESC + DESCRIPTION_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));
    }
}
