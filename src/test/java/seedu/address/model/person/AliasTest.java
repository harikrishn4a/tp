package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AliasTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Alias(null));
    }

    @Test
    public void constructor_invalidAlias_throwsIllegalArgumentException() {
        String invalidAlias = "";
        assertThrows(IllegalArgumentException.class, () -> new Alias(invalidAlias));
    }

    @Test
    public void isValidAlias() {
        // null alias
        assertThrows(NullPointerException.class, () -> Alias.isValidAlias(null));

        // invalid aliases
        assertFalse(Alias.isValidAlias("")); // empty string
        assertFalse(Alias.isValidAlias(" ")); // spaces only

        // valid aliases
        assertTrue(Alias.isValidAlias("John Doe"));
        assertTrue(Alias.isValidAlias("JD"));
    }

    @Test
    public void equals() {
        Alias alias = new Alias("John Doe");

        // same values -> returns true
        assertTrue(alias.equals(new Alias("John Doe")));

        // same object -> returns true
        assertTrue(alias.equals(alias));

        // null -> returns false
        assertFalse(alias.equals(null));

        // different types -> returns false
        assertFalse(alias.equals(5.0f));

        // different values -> returns false
        assertFalse(alias.equals(new Alias("Other Valid Alias")));
    }
}
