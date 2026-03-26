package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link ExportCommand} object.
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@link ExportCommand}.
     *
     * Expected format:
     * {@code export l/LOCATION}
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ExportCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_LOCATION);

        if (!argMultimap.getPreamble().isEmpty()
                || argMultimap.getAllValues(PREFIX_LOCATION).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_LOCATION);

        String location = ParserUtil.parseLocation(argMultimap.getValue(PREFIX_LOCATION).get());

        return new ExportCommand(location);
    }
}

