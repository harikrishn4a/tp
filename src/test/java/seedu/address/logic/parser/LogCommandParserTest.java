package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OUTCOME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.LogCommand;
import seedu.address.model.person.Encounter;

public class LogCommandParserTest {

    private static final String VALID_INDEX = "1";
    private static final String VALID_DATE = "2026-02-21";
    private static final String VALID_TIME = "18:30";
    private static final String VALID_LOCATION = "Maxwell Road";
    private static final String VALID_DESCRIPTION = "Met at coffee shop";
    private static final String VALID_OUTCOME = "Agreed to cooperate";

    private static final String DATE_DESC = " " + PREFIX_DATE + VALID_DATE;
    private static final String TIME_DESC = " " + PREFIX_TIME + VALID_TIME;
    private static final String LOCATION_DESC = " " + PREFIX_LOCATION + VALID_LOCATION;
    private static final String DESCRIPTION_DESC = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION;
    private static final String OUTCOME_DESC = " " + PREFIX_OUTCOME + VALID_OUTCOME;
    private static final String PW_DESC = " " + PREFIX_PASSWORD + "hunter2";

    private static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "21-02-2026"; // wrong format
    private static final String INVALID_DATE_NONEXISTENT = " " + PREFIX_DATE + "2026-02-30"; // non-existent date
    private static final String INVALID_TIME_DESC = " " + PREFIX_TIME + "25:00"; // invalid hour
    private static final String INVALID_TIME_EDGE = " " + PREFIX_TIME + "24:00"; // invalid upper bound
    private static final String INVALID_TIME_FORMAT = " " + PREFIX_TIME + "6:30pm"; // 12-hour not allowed
    private static final String INVALID_DESCRIPTION_BLANK = " " + PREFIX_DESCRIPTION + " "; // blank

    private final LogCommandParser parser = new LogCommandParser();

    // ── parse success ────────────────────────────────────────────────────────

    @Test
    public void parse_allCompulsoryFieldsPresent_success() {
        Encounter expectedEncounter = new Encounter(
                LocalDateTime.of(2026, 2, 21, 18, 30),
                VALID_LOCATION,
                VALID_DESCRIPTION,
                Optional.empty());

        assertParseSuccess(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                new LogCommand(INDEX_FIRST_PERSON, expectedEncounter));
    }

    @Test
    public void parse_passwordPresent_success() {
        Encounter expectedEncounter = new Encounter(
                LocalDateTime.of(2026, 2, 21, 18, 30),
                VALID_LOCATION,
                VALID_DESCRIPTION,
                Optional.empty());

        assertParseSuccess(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC + PW_DESC,
                new LogCommand(INDEX_FIRST_PERSON, expectedEncounter, Optional.of("hunter2")));
    }

    @Test
    public void parse_optionalOutcomePresent_success() {
        Encounter expectedEncounter = new Encounter(
                LocalDateTime.of(2026, 2, 21, 18, 30),
                VALID_LOCATION,
                VALID_DESCRIPTION,
                Optional.of(VALID_OUTCOME));

        assertParseSuccess(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC + OUTCOME_DESC,
                new LogCommand(INDEX_FIRST_PERSON, expectedEncounter));
    }

    @Test
    public void parse_prefixesInDifferentOrder_success() {
        // Prefixes in a different order than the canonical format
        Encounter expectedEncounter = new Encounter(
                LocalDateTime.of(2026, 2, 21, 18, 30),
                VALID_LOCATION,
                VALID_DESCRIPTION,
                Optional.empty());

        assertParseSuccess(
                parser,
                VALID_INDEX + DESCRIPTION_DESC + LOCATION_DESC + TIME_DESC + DATE_DESC,
                new LogCommand(INDEX_FIRST_PERSON, expectedEncounter));
    }

    @Test
    public void parse_leadingWhitespace_success() {
        Encounter expectedEncounter = new Encounter(
                LocalDateTime.of(2026, 2, 21, 18, 30),
                VALID_LOCATION,
                VALID_DESCRIPTION,
                Optional.empty());

        assertParseSuccess(
                parser,
                "  " + VALID_INDEX + DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                new LogCommand(INDEX_FIRST_PERSON, expectedEncounter));
    }

    // ── parse failure: missing index ─────────────────────────────────────────

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertParseFailure(
                parser,
                DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(
                parser,
                "0" + DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonIntegerIndex_throwsParseException() {
        assertParseFailure(
                parser,
                "abc" + DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE));
    }

    // ── parse failure: missing compulsory prefixes ───────────────────────────

    @Test
    public void parse_missingDate_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingTime_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingLocation_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + DESCRIPTION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingDescription_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + LOCATION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_allFieldsMissing_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE));
    }

    // ── parse failure: invalid field values ──────────────────────────────────

    @Test
    public void parse_invalidDateFormat_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + INVALID_DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                "Invalid date. Use format YYYY-MM-DD.");
    }

    @Test
    public void parse_nonExistentDate_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + INVALID_DATE_NONEXISTENT + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                "Invalid date. Use format YYYY-MM-DD.");
    }

    @Test
    public void parse_invalidTimeValue_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + INVALID_TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                "Invalid time. Use 24-hour format HH:mm.");
    }

    @Test
    public void parse_edgeTime2400_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + INVALID_TIME_EDGE + LOCATION_DESC + DESCRIPTION_DESC,
                "Invalid time. Use 24-hour format HH:mm.");
    }

    @Test
    public void parse_invalidTimeFormat_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + INVALID_TIME_FORMAT + LOCATION_DESC + DESCRIPTION_DESC,
                "Invalid time. Use 24-hour format HH:mm.");
    }

    @Test
    public void parse_blankDescription_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + LOCATION_DESC + INVALID_DESCRIPTION_BLANK,
                Encounter.MESSAGE_DESCRIPTION_CONSTRAINTS);
    }

    @Test
    public void parse_descriptionTooLong_throwsParseException() {
        String tooLong = " " + PREFIX_DESCRIPTION + "a".repeat(Encounter.DESCRIPTION_MAX_LENGTH + 1);
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + LOCATION_DESC + tooLong,
                Encounter.MESSAGE_DESCRIPTION_CONSTRAINTS);
    }

    @Test
    public void parse_outcomeTooLong_throwsParseException() {
        String tooLong = " " + PREFIX_OUTCOME + "a".repeat(Encounter.OUTCOME_MAX_LENGTH + 1);
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC + tooLong,
                Encounter.MESSAGE_OUTCOME_CONSTRAINTS);
    }

    // ── parse failure: duplicate prefixes ────────────────────────────────────

    @Test
    public void parse_duplicateDatePrefix_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));
    }

    @Test
    public void parse_duplicateTimePrefix_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC,
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME));
    }

    @Test
    public void parse_duplicateDescriptionPrefix_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + LOCATION_DESC
                        + DESCRIPTION_DESC + DESCRIPTION_DESC,
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));
    }

    @Test
    public void parse_duplicatePasswordPrefix_throwsParseException() {
        assertParseFailure(
                parser,
                VALID_INDEX + DATE_DESC + TIME_DESC + LOCATION_DESC + DESCRIPTION_DESC + PW_DESC + PW_DESC,
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PASSWORD));
    }
}
