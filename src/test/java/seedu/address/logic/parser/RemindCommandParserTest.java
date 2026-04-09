package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.RemindCommand;
import seedu.address.model.person.Reminder;

public class RemindCommandParserTest {

    private static final String VALID_INDEX = "1";
    private static final String VALID_DATE = "2026-03-28";
    private static final String VALID_TIME = "20:00";
    private static final String VALID_NOTE = "Meet informant";

    private static final String DATE_DESC = " " + PREFIX_DATE + VALID_DATE;
    private static final String TIME_DESC = " " + PREFIX_TIME + VALID_TIME;
    private static final String NOTE_DESC = " " + PREFIX_NOTES + VALID_NOTE;

    private static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "28-03-2026";
    private static final String INVALID_TIME_DESC = " " + PREFIX_TIME + "8pm";
    private static final String INVALID_NOTE_DESC = " " + PREFIX_NOTES + "   ";
    private static final String PW_DESC = " " + PREFIX_PASSWORD + "hunter2";

    private final RemindCommandParser parser = new RemindCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Reminder reminder = new Reminder(LocalDate.of(2026, 3, 28), LocalTime.of(20, 0), VALID_NOTE);
        assertParseSuccess(parser, VALID_INDEX + DATE_DESC + TIME_DESC + NOTE_DESC,
                new RemindCommand(INDEX_FIRST_PERSON, reminder));
    }

    @Test
    public void parse_passwordPresent_success() {
        Reminder reminder = new Reminder(LocalDate.of(2026, 3, 28), LocalTime.of(20, 0), VALID_NOTE);
        assertParseSuccess(parser, VALID_INDEX + DATE_DESC + TIME_DESC + NOTE_DESC + PW_DESC,
                new RemindCommand(INDEX_FIRST_PERSON, reminder, Optional.of("hunter2")));
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, DATE_DESC + TIME_DESC + NOTE_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingDate_failure() {
        assertParseFailure(parser, VALID_INDEX + TIME_DESC + NOTE_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingTime_failure() {
        assertParseFailure(parser, VALID_INDEX + DATE_DESC + NOTE_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingNote_failure() {
        assertParseFailure(parser, VALID_INDEX + DATE_DESC + TIME_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, VALID_INDEX + INVALID_DATE_DESC + TIME_DESC + NOTE_DESC,
                "Invalid date. Use format YYYY-MM-DD.");
    }

    @Test
    public void parse_invalidTime_failure() {
        assertParseFailure(parser, VALID_INDEX + DATE_DESC + INVALID_TIME_DESC + NOTE_DESC,
                "Invalid time. Use 24-hour format HH:mm.");
    }

    @Test
    public void parse_blankNote_failure() {
        assertParseFailure(parser, VALID_INDEX + DATE_DESC + TIME_DESC + INVALID_NOTE_DESC,
                Reminder.MESSAGE_NOTE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicateDatePrefix_failure() {
        assertParseFailure(parser, VALID_INDEX + DATE_DESC + DATE_DESC + TIME_DESC + NOTE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));
    }

    @Test
    public void parse_duplicateTimePrefix_failure() {
        assertParseFailure(parser, VALID_INDEX + DATE_DESC + TIME_DESC + TIME_DESC + NOTE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME));
    }

    @Test
    public void parse_duplicateNotePrefix_failure() {
        assertParseFailure(parser, VALID_INDEX + DATE_DESC + TIME_DESC + NOTE_DESC + NOTE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NOTES));
    }

    @Test
    public void parse_duplicatePasswordPrefix_failure() {
        assertParseFailure(parser, VALID_INDEX + DATE_DESC + TIME_DESC + NOTE_DESC + PW_DESC + PW_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PASSWORD));
    }
}
