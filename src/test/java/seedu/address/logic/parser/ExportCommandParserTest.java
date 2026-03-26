package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ExportCommand;

/**
 * Tests for {@link ExportCommandParser}.
 */
public class ExportCommandParserTest {
    private static final String VALID_LOCATION = "Harbor District";
    private static final String VALID_LOCATION_WITH_EXTRA_SPACES = "  Harbor District  ";

    private static final String LOCATION_DESC = " " + PREFIX_LOCATION + VALID_LOCATION;

    private static final String INVALID_LOCATION_BLANK_DESC = " " + PREFIX_LOCATION + " ";

    private static final ExportCommandParser parser = new ExportCommandParser();

    // ── parse success ────────────────────────────────────────────────────────

    @Test
    public void parse_validArgs_returnsExportCommand() {
        assertParseSuccess(parser, LOCATION_DESC, new ExportCommand(VALID_LOCATION));
        assertParseSuccess(parser, " " + PREFIX_LOCATION + VALID_LOCATION_WITH_EXTRA_SPACES,
                new ExportCommand(VALID_LOCATION));
        assertParseSuccess(parser, "  " + LOCATION_DESC + "  ", new ExportCommand(VALID_LOCATION));
    }

    // ── parse failure: missing location / invalid command format ───────────

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingLocation_throwsParseException() {
        assertParseFailure(parser, "NonEmptyPreamble",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser, "NonEmptyPreamble" + LOCATION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

    // ── parse failure: invalid location values ──────────────────────────────

    @Test
    public void parse_blankLocation_throwsParseException() {
        assertParseFailure(parser, INVALID_LOCATION_BLANK_DESC, seedu.address.model.person.Encounter
                .MESSAGE_LOCATION_CONSTRAINTS);
    }

    // ── parse failure: duplicate location prefixes ──────────────────────────

    @Test
    public void parse_duplicateLocationPrefix_throwsParseException() {
        // ArgumentTokenizer requires prefixes to be preceded by whitespace.
        String input = LOCATION_DESC + " " + PREFIX_LOCATION + VALID_LOCATION;
        assertParseFailure(parser, input, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LOCATION));
    }
}

