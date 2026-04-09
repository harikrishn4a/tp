package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OUTCOME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.LogCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Encounter;

/**
 * Parses input arguments and creates a new {@link LogCommand} object.
 */
public class LogCommandParser implements Parser<LogCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of {@code LogCommand}
     * and returns a {@code LogCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public LogCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_DATE, PREFIX_TIME, PREFIX_LOCATION, PREFIX_DESCRIPTION, PREFIX_OUTCOME, PREFIX_PASSWORD);

        if (!arePrefixesPresent(argMultimap, PREFIX_DATE, PREFIX_TIME, PREFIX_LOCATION, PREFIX_DESCRIPTION)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_DATE, PREFIX_TIME, PREFIX_LOCATION, PREFIX_DESCRIPTION, PREFIX_OUTCOME, PREFIX_PASSWORD);

        LocalDate date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        LocalTime time = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME).get());
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        String location = ParserUtil.parseLocation(argMultimap.getValue(PREFIX_LOCATION).get());
        String description =
                ParserUtil.parseEncounterDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());

        Optional<String> outcome = Optional.empty();
        if (argMultimap.getValue(PREFIX_OUTCOME).isPresent()) {
            outcome = Optional.of(
                    ParserUtil.parseOutcome(argMultimap.getValue(PREFIX_OUTCOME).get()));
        }

        Encounter encounter = new Encounter(dateTime, location, description, outcome);

        boolean passwordPrefixPresent = argMultimap.getValue(PREFIX_PASSWORD).isPresent();
        Optional<String> passwordPrefix = passwordPrefixPresent
                ? Optional.of(argMultimap.getValue(PREFIX_PASSWORD).get())
                : Optional.empty();

        return new LogCommand(index, encounter, passwordPrefix);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
