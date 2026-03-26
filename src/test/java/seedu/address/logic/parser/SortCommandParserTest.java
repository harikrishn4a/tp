package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.SortCommand.SortCriterion;

public class SortCommandParserTest {

    private final SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArg_throwsParseException() {
        assertParseFailure(parser, "unknown",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleArgs_throwsParseException() {
        assertParseFailure(parser, "location extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsSortCommand() {
        assertParseSuccess(parser, "location", new SortCommand(SortCriterion.LOCATION));
        assertParseSuccess(parser, "tag", new SortCommand(SortCriterion.TAG));
        assertParseSuccess(parser, "alphabetical", new SortCommand(SortCriterion.ALPHABETICAL));
        assertParseSuccess(parser, "status", new SortCommand(SortCriterion.STATUS));
        assertParseSuccess(parser, "recent", new SortCommand(SortCriterion.RECENT));
        assertParseSuccess(parser, "  LoCaTiOn  ", new SortCommand(SortCriterion.LOCATION));
    }
}
