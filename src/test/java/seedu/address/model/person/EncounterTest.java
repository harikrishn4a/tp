package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class EncounterTest {

    private static final LocalDateTime VALID_DATE_TIME = LocalDateTime.of(
        2024,
        1,
        15,
        14,
        30
    );
    private static final String VALID_LOCATION = "Maxwell Road";
    private static final String VALID_DESCRIPTION =
        "Discussed project details over coffee";
    private static final Optional<String> NO_OUTCOME = Optional.empty();
    private static final Optional<String> VALID_OUTCOME = Optional.of(
        "Agreed to cooperate"
    );

    // ── constructor null-safety ──────────────────────────────────────────────

    @Test
    public void constructor_nullDateTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new Encounter(null, VALID_LOCATION, VALID_DESCRIPTION, NO_OUTCOME)
        );
    }

    @Test
    public void constructor_nullLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new Encounter(VALID_DATE_TIME, null, VALID_DESCRIPTION, NO_OUTCOME)
        );
    }

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new Encounter(VALID_DATE_TIME, VALID_LOCATION, null, NO_OUTCOME)
        );
    }

    @Test
    public void constructor_nullOutcome_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new Encounter(
                VALID_DATE_TIME,
                VALID_LOCATION,
                VALID_DESCRIPTION,
                null
            )
        );
    }

    // ── constructor validation ───────────────────────────────────────────────

    @Test
    public void constructor_invalidLocation_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            new Encounter(VALID_DATE_TIME, "", VALID_DESCRIPTION, NO_OUTCOME)
        );
        assertThrows(IllegalArgumentException.class, () ->
            new Encounter(VALID_DATE_TIME, " ", VALID_DESCRIPTION, NO_OUTCOME)
        );
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            new Encounter(VALID_DATE_TIME, VALID_LOCATION, "", NO_OUTCOME)
        );
        assertThrows(IllegalArgumentException.class, () ->
            new Encounter(VALID_DATE_TIME, VALID_LOCATION, " ", NO_OUTCOME)
        );
        // Exceeds 500 characters
        String tooLong = "a".repeat(Encounter.DESCRIPTION_MAX_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () ->
            new Encounter(VALID_DATE_TIME, VALID_LOCATION, tooLong, NO_OUTCOME)
        );
    }

    @Test
    public void constructor_invalidOutcome_throwsIllegalArgumentException() {
        // Exceeds 300 characters
        String tooLong = "a".repeat(Encounter.OUTCOME_MAX_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () ->
            new Encounter(
                VALID_DATE_TIME,
                VALID_LOCATION,
                VALID_DESCRIPTION,
                Optional.of(tooLong)
            )
        );
    }

    @Test
    public void constructor_validWithOutcome_success() {
        Encounter encounter = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            VALID_OUTCOME
        );
        assertEquals(VALID_DATE_TIME, encounter.dateTime);
        assertEquals(VALID_LOCATION, encounter.location);
        assertEquals(VALID_DESCRIPTION, encounter.description);
        assertEquals(VALID_OUTCOME, encounter.outcome);
    }

    @Test
    public void constructor_validWithoutOutcome_success() {
        Encounter encounter = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            NO_OUTCOME
        );
        assertEquals(NO_OUTCOME, encounter.outcome);
    }

    // ── isValidLocation ──────────────────────────────────────────────────────

    @Test
    public void isValidLocation() {
        assertThrows(NullPointerException.class, () ->
            Encounter.isValidLocation(null)
        );

        assertFalse(Encounter.isValidLocation(""));
        assertFalse(Encounter.isValidLocation(" "));
        assertFalse(Encounter.isValidLocation("   "));

        assertTrue(Encounter.isValidLocation("Maxwell Road"));
        assertTrue(Encounter.isValidLocation("-"));
        assertTrue(Encounter.isValidLocation("1"));
        assertTrue(
            Encounter.isValidLocation("Blk 47 Tampines Street 20, #17-35")
        );
    }

    // ── isValidDescription ───────────────────────────────────────────────────

    @Test
    public void isValidDescription() {
        assertThrows(NullPointerException.class, () ->
            Encounter.isValidDescription(null)
        );

        assertFalse(Encounter.isValidDescription(""));
        assertFalse(Encounter.isValidDescription(" "));
        assertFalse(Encounter.isValidDescription("   "));
        assertFalse(
            Encounter.isValidDescription(
                "a".repeat(Encounter.DESCRIPTION_MAX_LENGTH + 1)
            )
        );

        assertTrue(Encounter.isValidDescription("Had lunch"));
        assertTrue(Encounter.isValidDescription("-"));
        assertTrue(Encounter.isValidDescription("1"));
        assertTrue(
            Encounter.isValidDescription(
                "a".repeat(Encounter.DESCRIPTION_MAX_LENGTH)
            )
        );
        assertTrue(
            Encounter.isValidDescription(
                "Met at conference and discussed new project ideas"
            )
        );
        assertTrue(
            Encounter.isValidDescription(
                "Catch-up; discussed Q4 plans & budget."
            )
        );
    }

    // ── isValidOutcome ───────────────────────────────────────────────────────

    @Test
    public void isValidOutcome() {
        assertThrows(NullPointerException.class, () ->
            Encounter.isValidOutcome(null)
        );

        assertFalse(
            Encounter.isValidOutcome(
                "a".repeat(Encounter.OUTCOME_MAX_LENGTH + 1)
            )
        );

        assertTrue(Encounter.isValidOutcome("")); // empty string is allowed (optional field)
        assertTrue(Encounter.isValidOutcome("Cooperating"));
        assertTrue(
            Encounter.isValidOutcome("a".repeat(Encounter.OUTCOME_MAX_LENGTH))
        );
    }

    // ── getFormattedDateTime ─────────────────────────────────────────────────

    @Test
    public void getFormattedDateTime() {
        Encounter encounter = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            NO_OUTCOME
        );
        assertEquals("2024-01-15 14:30", encounter.getFormattedDateTime());
    }

    @Test
    public void getFormattedDateTime_paddedValues() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 3, 5, 9, 7);
        Encounter encounter = new Encounter(
            dateTime,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            NO_OUTCOME
        );
        assertEquals("2024-03-05 09:07", encounter.getFormattedDateTime());
    }

    // ── equals ───────────────────────────────────────────────────────────────

    @Test
    public void equals() {
        Encounter encounter = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            NO_OUTCOME
        );

        // same values -> returns true
        assertTrue(
            encounter.equals(
                new Encounter(
                    VALID_DATE_TIME,
                    VALID_LOCATION,
                    VALID_DESCRIPTION,
                    NO_OUTCOME
                )
            )
        );

        // same object -> returns true
        assertTrue(encounter.equals(encounter));

        // null -> returns false
        assertFalse(encounter.equals(null));

        // different type -> returns false
        assertFalse(encounter.equals(5));

        // different dateTime -> returns false
        assertFalse(
            encounter.equals(
                new Encounter(
                    VALID_DATE_TIME.plusHours(1),
                    VALID_LOCATION,
                    VALID_DESCRIPTION,
                    NO_OUTCOME
                )
            )
        );

        // different location -> returns false
        assertFalse(
            encounter.equals(
                new Encounter(
                    VALID_DATE_TIME,
                    "Other Location",
                    VALID_DESCRIPTION,
                    NO_OUTCOME
                )
            )
        );

        // different description -> returns false
        assertFalse(
            encounter.equals(
                new Encounter(
                    VALID_DATE_TIME,
                    VALID_LOCATION,
                    "Different description",
                    NO_OUTCOME
                )
            )
        );

        // different outcome -> returns false
        assertFalse(
            encounter.equals(
                new Encounter(
                    VALID_DATE_TIME,
                    VALID_LOCATION,
                    VALID_DESCRIPTION,
                    VALID_OUTCOME
                )
            )
        );

        // with outcome vs without outcome -> returns false
        Encounter withOutcome = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            VALID_OUTCOME
        );
        assertFalse(encounter.equals(withOutcome));

        // both same with outcome -> returns true
        assertTrue(
            withOutcome.equals(
                new Encounter(
                    VALID_DATE_TIME,
                    VALID_LOCATION,
                    VALID_DESCRIPTION,
                    VALID_OUTCOME
                )
            )
        );
    }

    // ── hashCode ─────────────────────────────────────────────────────────────

    @Test
    public void hashCode_sameValues_sameHashCode() {
        Encounter e1 = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            NO_OUTCOME
        );
        Encounter e2 = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            NO_OUTCOME
        );
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    public void hashCode_withOutcome_sameHashCode() {
        Encounter e1 = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            VALID_OUTCOME
        );
        Encounter e2 = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            VALID_OUTCOME
        );
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    // ── toString ─────────────────────────────────────────────────────────────

    @Test
    public void toStringMethod_withoutOutcome() {
        Encounter encounter = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            NO_OUTCOME
        );
        String expected = Encounter.class.getCanonicalName() + "{dateTime="
                + encounter.getFormattedDateTime() + ", location=" + VALID_LOCATION
                + ", description=" + VALID_DESCRIPTION + ", outcome=}";
        assertEquals(expected, encounter.toString());
    }

    @Test
    public void toStringMethod_withOutcome() {
        Encounter encounter = new Encounter(
            VALID_DATE_TIME,
            VALID_LOCATION,
            VALID_DESCRIPTION,
            VALID_OUTCOME
        );
        String expected = Encounter.class.getCanonicalName() + "{dateTime="
                + encounter.getFormattedDateTime() + ", location=" + VALID_LOCATION
                + ", description=" + VALID_DESCRIPTION
                + ", outcome=" + VALID_OUTCOME.get() + "}";
        assertEquals(expected, encounter.toString());
    }
}
