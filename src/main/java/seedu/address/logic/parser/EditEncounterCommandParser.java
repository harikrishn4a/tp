package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OUTCOME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditEncounterCommand;
import seedu.address.logic.commands.EditEncounterCommand.EditEncounterDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditEncounterCommand object.
 */
public class EditEncounterCommandParser implements Parser<EditEncounterCommand> {

    @Override
    public EditEncounterCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_TIME, PREFIX_LOCATION, PREFIX_DESCRIPTION,
                        PREFIX_OUTCOME);

        String preamble = argMultimap.getPreamble().trim();
        String[] parts = preamble.split("\\s+");
        if (parts.length != 2) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, EditEncounterCommand.MESSAGE_USAGE));
        }

        Index personIndex;
        Index encounterIndex;
        try {
            personIndex = ParserUtil.parseIndex(parts[0]);
            encounterIndex = ParserUtil.parseIndex(parts[1]);
        } catch (ParseException pe) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, EditEncounterCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE, PREFIX_TIME, PREFIX_LOCATION, PREFIX_DESCRIPTION,
                PREFIX_OUTCOME);

        EditEncounterDescriptor descriptor = new EditEncounterDescriptor();
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            descriptor.setDate(ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get()));
        }
        if (argMultimap.getValue(PREFIX_TIME).isPresent()) {
            descriptor.setTime(ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME).get()));
        }
        if (argMultimap.getValue(PREFIX_LOCATION).isPresent()) {
            descriptor.setLocation(ParserUtil.parseLocation(argMultimap.getValue(PREFIX_LOCATION).get()));
        }
        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            descriptor.setDescription(
                    ParserUtil.parseEncounterDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get()));
        }
        if (argMultimap.getValue(PREFIX_OUTCOME).isPresent()) {
            String outcome = ParserUtil.parseOutcome(argMultimap.getValue(PREFIX_OUTCOME).get());
            descriptor.setOutcome(outcome.isEmpty() ? Optional.empty() : Optional.of(outcome));
        }

        if (!descriptor.isAnyFieldEdited()) {
            throw new ParseException(EditEncounterCommand.MESSAGE_NOT_EDITED);
        }

        return new EditEncounterCommand(personIndex, encounterIndex, descriptor);
    }
}
