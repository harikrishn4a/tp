package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Alias;
import seedu.address.model.person.Email;
import seedu.address.model.person.Encounter;
import seedu.address.model.person.Name;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Password;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Risk;
import seedu.address.model.person.Stage;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a raw {@code al/} string into a list of {@code Alias}.
     * Values are comma-separated. Leading/trailing whitespaces around each alias will be trimmed.
     *
     * <p>An empty input returns an empty alias list.</p>
     *
     * @throws ParseException if any alias token is invalid.
     */
    public static List<Alias> parseAliases(String rawAliases) throws ParseException {
        requireNonNull(rawAliases);
        String trimmed = rawAliases.trim();
        if (trimmed.isEmpty()) {
            return List.of();
        }

        List<String> tokens = List.of(trimmed.split(","));
        try {
            return tokens.stream()
                    .map(String::trim)
                    .map(token -> {
                        if (token.isEmpty() || !Alias.isValidAlias(token)) {
                            throw new IllegalArgumentException("Invalid alias token");
                        }
                        return new Alias(token);
                    })
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException ex) {
            throw new ParseException(Alias.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String notes} into {@code Notes}.
     *
     * @throws ParseException if the given {@code notes} is invalid.
     */
    public static Notes parseNotes(String notes) throws ParseException {
        requireNonNull(notes);
        if (!Notes.isValidNotes(notes)) {
            throw new ParseException(Notes.MESSAGE_CONSTRAINTS);
        }
        return new Notes(notes);
    }

    /**
     * Parses a {@code String risk} into a {@code Risk}.
     *
     * @throws ParseException if the given {@code risk} is invalid.
     */
    public static Risk parseRisk(String risk) throws ParseException {
        requireNonNull(risk);
        try {
            return Risk.fromString(risk);
        } catch (IllegalArgumentException ex) {
            throw new ParseException(Risk.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String alias} into an {@code Alias}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code alias} is invalid.
     */
    public static Alias parseAlias(String alias) throws ParseException {
        requireNonNull(alias);
        String trimmedAlias = alias.trim();
        if (!Alias.isValidAlias(trimmedAlias)) {
            throw new ParseException(Alias.MESSAGE_CONSTRAINTS);
        }
        return new Alias(trimmedAlias);
    }

    /**
     * Parses a raw stage string into a {@code Stage}, wrapping validation errors
     * as {@code ParseException}.
     */
    public static Stage parseStage(String stage) throws ParseException {
        try {
            return Stage.fromString(stage);
        } catch (IllegalArgumentException ex) {
            throw new ParseException(Stage.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String password} into a {@code Password}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code password} is invalid.
     */
    public static Password parsePassword(String password) throws ParseException {
        requireNonNull(password);
        String trimmedPassword = password.trim();
        if (!Password.isValidPassword(trimmedPassword)) {
            throw new ParseException(Password.MESSAGE_CONSTRAINTS);
        }
        return new Password(trimmedPassword);
    }

    /**
     * Parses a {@code String date} in {@code yyyy-MM-dd} format into a {@code LocalDate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code date} is not a valid calendar date in YYYY-MM-DD format.
     */
    public static LocalDate parseDate(String date) throws ParseException {
        requireNonNull(date);
        String trimmed = date.trim();
        try {
            return LocalDate.parse(trimmed,
                    DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT));
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid date. Use format YYYY-MM-DD.");
        }
    }

    /**
     * Parses a {@code String time} in {@code HH:mm} 24-hour format into a {@code LocalTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code time} is not a valid 24-hour time in HH:mm format.
     */
    public static LocalTime parseTime(String time) throws ParseException {
        requireNonNull(time);
        String trimmed = time.trim();
        try {
            return LocalTime.parse(trimmed, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid time. Use 24-hour format HH:mm.");
        }
    }

    /**
     * Parses and validates an encounter {@code String location}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code location} is blank.
     */
    public static String parseLocation(String location) throws ParseException {
        requireNonNull(location);
        String trimmed = location.trim();
        if (!Encounter.isValidLocation(trimmed)) {
            throw new ParseException(Encounter.MESSAGE_LOCATION_CONSTRAINTS);
        }
        return trimmed;
    }

    /**
     * Parses and validates an encounter {@code String description} (1-500 characters).
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code description} is blank or exceeds 500 characters.
     */
    public static String parseEncounterDescription(String description) throws ParseException {
        requireNonNull(description);
        String trimmed = description.trim();
        if (!Encounter.isValidDescription(trimmed)) {
            throw new ParseException(Encounter.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        return trimmed;
    }

    /**
     * Parses and validates an encounter {@code String outcome} (up to 300 characters).
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code outcome} exceeds 300 characters.
     */
    public static String parseOutcome(String outcome) throws ParseException {
        requireNonNull(outcome);
        String trimmed = outcome.trim();
        if (!Encounter.isValidOutcome(trimmed)) {
            throw new ParseException(Encounter.MESSAGE_OUTCOME_CONSTRAINTS);
        }
        return trimmed;
    }
}
