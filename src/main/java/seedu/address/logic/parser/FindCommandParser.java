package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        List<String> nameKeywords = extractNameKeywords(args);

        if (nameKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new NameContainsKeywordsPredicate(nameKeywords));
    }

    private List<String> extractNameKeywords(String preamble) {
        String trimmedPreamble = preamble.trim();
        if (trimmedPreamble.isEmpty()) {
            return List.of();
        }

        return Arrays.stream(trimmedPreamble.split("\\s+"))
                .filter(keyword -> !keyword.isBlank())
                .collect(Collectors.toList());
    }

}
