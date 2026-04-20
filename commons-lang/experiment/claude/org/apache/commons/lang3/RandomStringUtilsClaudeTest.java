/*
 * Claude-generated test class for academic experiment on automated unit test generation.
 * Covers: RandomStringUtils instance API (insecure/secure singletons)
 *         next(), nextAlphabetic(), nextAlphanumeric(), nextNumeric(), nextAscii()
 * Framework: JUnit 5 (Jupiter)
 * Design: Arrange-Act-Assert, high assertion density, mutation-killing edge cases
 */
package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * High-quality, mutation-killing tests for selected RandomStringUtils methods.
 *
 * <p>Uses the insecure singleton for test determinism (no cryptographic overhead).
 * All structural/length/character-set assertions are deterministic regardless of
 * which random characters are generated.
 *
 * <p>Methods under test:
 * <ul>
 *   <li>{@link RandomStringUtils#insecure()} factory</li>
 *   <li>{@link RandomStringUtils#next(int)}</li>
 *   <li>{@link RandomStringUtils#next(int, boolean, boolean)}</li>
 *   <li>{@link RandomStringUtils#next(int, char...)}</li>
 *   <li>{@link RandomStringUtils#next(int, String)}</li>
 *   <li>{@link RandomStringUtils#nextAlphabetic(int)}</li>
 *   <li>{@link RandomStringUtils#nextAlphanumeric(int)}</li>
 *   <li>{@link RandomStringUtils#nextNumeric(int)}</li>
 *   <li>{@link RandomStringUtils#nextAscii(int)}</li>
 * </ul>
 */
public class RandomStringUtilsClaudeTest {

    /** Use the insecure (ThreadLocalRandom) singleton — no cryptographic overhead needed for tests. */
    private static RandomStringUtils rsu;

    @BeforeAll
    public static void setUpSingleton() {
        rsu = RandomStringUtils.insecure();
    }

    // -----------------------------------------------------------------------
    // next(int count)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnEmptyStringForCountZero_next() {
        // Arrange
        final int count = 0;
        // Act
        final String result = rsu.next(count);
        // Assert
        assertNotNull(result);
        assertEquals(0, result.length(), "next(0) must return an empty string");
        assertEquals("", result);
    }

    @Test
    public void shouldReturnStringOfExactLength_next() {
        // Arrange
        final int count = 10;
        // Act
        final String result = rsu.next(count);
        // Assert — length is the primary structural invariant
        assertNotNull(result);
        assertEquals(count, result.length());
    }

    @Test
    public void shouldReturnStringOfLengthOne_next() {
        // Arrange
        final int count = 1;
        // Act
        final String result = rsu.next(count);
        // Assert
        assertNotNull(result);
        assertEquals(1, result.length());
    }

    @Test
    public void shouldReturnStringOfLargeLength_next() {
        // Arrange — tests that loops run for all iterations (mutation target)
        final int count = 100;
        // Act
        final String result = rsu.next(count);
        // Assert
        assertNotNull(result);
        assertEquals(100, result.length());
    }

    @Test
    public void shouldThrowExceptionForNegativeCount_next() {
        // Act & Assert — must throw, not silently succeed
        assertThrows(IllegalArgumentException.class, () -> rsu.next(-1));
    }

    @Test
    public void shouldThrowExceptionForNegativeCountLarge_next() {
        assertThrows(IllegalArgumentException.class, () -> rsu.next(-100));
    }

    // -----------------------------------------------------------------------
    // next(int count, boolean letters, boolean numbers)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnAlphabeticOnlyWhenLettersTrueNumbersFalse() {
        // Arrange
        final int count = 50;
        // Act
        final String result = rsu.next(count, true, false);
        // Assert
        assertNotNull(result);
        assertEquals(count, result.length());
        assertTrue(result.chars().allMatch(Character::isLetter),
                "All characters must be alphabetic when letters=true, numbers=false");
    }

    @Test
    public void shouldReturnNumericOnlyWhenLettersFalseNumbersTrue() {
        // Arrange
        final int count = 50;
        // Act
        final String result = rsu.next(count, false, true);
        // Assert
        assertNotNull(result);
        assertEquals(count, result.length());
        assertTrue(result.chars().allMatch(Character::isDigit),
                "All characters must be digits when letters=false, numbers=true");
    }

    @Test
    public void shouldReturnAlphanumericWhenBothTrue() {
        // Arrange
        final int count = 100;
        // Act
        final String result = rsu.next(count, true, true);
        // Assert
        assertNotNull(result);
        assertEquals(count, result.length());
        assertTrue(result.chars().allMatch(ch -> Character.isLetterOrDigit(ch)),
                "All characters must be letters or digits when both=true");
    }

    @Test
    public void shouldReturnEmptyForCountZeroWithFlags() {
        // Arrange & Act
        final String result = rsu.next(0, true, true);
        // Assert
        assertNotNull(result);
        assertEquals(0, result.length());
        assertEquals("", result);
    }

    @Test
    public void shouldThrowForNegativeCountWithFlags() {
        assertThrows(IllegalArgumentException.class, () -> rsu.next(-1, true, false));
    }

    // -----------------------------------------------------------------------
    // next(int count, char... chars)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnOnlyCharactersFromSuppliedArray() {
        // Arrange — character set: only 'a', 'b', 'c'
        final char[] chars = {'a', 'b', 'c'};
        final int count = 30;
        // Act
        final String result = rsu.next(count, chars);
        // Assert
        assertNotNull(result);
        assertEquals(count, result.length());
        for (final char ch : result.toCharArray()) {
            assertTrue(ch == 'a' || ch == 'b' || ch == 'c',
                    "Each character must be in the supplied array; found: " + ch);
        }
    }

    @Test
    public void shouldReturnSingleRepeatedCharWhenArrayHasOneElement() {
        // Arrange
        final char[] chars = {'X'};
        final int count = 5;
        // Act
        final String result = rsu.next(count, chars);
        // Assert
        assertNotNull(result);
        assertEquals(count, result.length());
        assertEquals("XXXXX", result);
    }

    @Test
    public void shouldReturnEmptyStringForCountZeroWithCharArray() {
        // Act
        final String result = rsu.next(0, new char[]{'a'});
        // Assert
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void shouldThrowForNegativeCountWithCharArray() {
        assertThrows(IllegalArgumentException.class, () -> rsu.next(-1, new char[]{'a'}));
    }

    // -----------------------------------------------------------------------
    // next(int count, String chars)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnOnlyCharactersFromSuppliedString() {
        // Arrange
        final String chars = "AEIOU";
        final int count = 20;
        // Act
        final String result = rsu.next(count, chars);
        // Assert
        assertNotNull(result);
        assertEquals(count, result.length());
        for (final char ch : result.toCharArray()) {
            assertTrue("AEIOU".indexOf(ch) >= 0,
                    "Each character must be in 'AEIOU'; found: " + ch);
        }
    }

    @Test
    public void shouldReturnEmptyForCountZeroWithStringChars() {
        final String result = rsu.next(0, "ABC");
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void shouldThrowForEmptyStringChars() {
        // Empty string chars must trigger IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> rsu.next(5, ""));
    }

    @Test
    public void shouldReturnNullBehaviorForNullStringChars() {
        // When chars string is null the implementation falls back to full character set
        final String result = rsu.next(10, (String) null);
        assertNotNull(result);
        assertEquals(10, result.length());
    }

    // -----------------------------------------------------------------------
    // nextAlphabetic(int count)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnAlphabeticStringOfExactLength() {
        // Arrange
        final int count = 20;
        // Act
        final String result = rsu.nextAlphabetic(count);
        // Assert
        assertNotNull(result);
        assertEquals(count, result.length());
        assertTrue(result.chars().allMatch(Character::isLetter),
                "All characters must be alphabetic");
    }

    @Test
    public void shouldReturnEmptyAlphabeticStringForCountZero() {
        final String result = rsu.nextAlphabetic(0);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void shouldThrowForNegativeCountAlphabetic() {
        assertThrows(IllegalArgumentException.class, () -> rsu.nextAlphabetic(-1));
    }

    @Test
    public void shouldReturnSingleAlphabeticChar() {
        final String result = rsu.nextAlphabetic(1);
        assertNotNull(result);
        assertEquals(1, result.length());
        assertTrue(Character.isLetter(result.charAt(0)));
    }

    // -----------------------------------------------------------------------
    // nextAlphanumeric(int count)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnAlphanumericStringOfExactLength() {
        // Arrange
        final int count = 30;
        // Act
        final String result = rsu.nextAlphanumeric(count);
        // Assert
        assertNotNull(result);
        assertEquals(count, result.length());
        assertTrue(result.chars().allMatch(Character::isLetterOrDigit),
                "All characters must be alphanumeric");
    }

    @Test
    public void shouldReturnEmptyAlphanumericStringForCountZero() {
        final String result = rsu.nextAlphanumeric(0);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void shouldThrowForNegativeCountAlphanumeric() {
        assertThrows(IllegalArgumentException.class, () -> rsu.nextAlphanumeric(-1));
    }

    // -----------------------------------------------------------------------
    // nextNumeric(int count)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnNumericStringOfExactLength() {
        // Arrange
        final int count = 15;
        // Act
        final String result = rsu.nextNumeric(count);
        // Assert
        assertNotNull(result);
        assertEquals(count, result.length());
        assertTrue(result.chars().allMatch(Character::isDigit),
                "All characters must be digits");
    }

    @Test
    public void shouldReturnEmptyNumericStringForCountZero() {
        final String result = rsu.nextNumeric(0);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void shouldThrowForNegativeCountNumeric() {
        assertThrows(IllegalArgumentException.class, () -> rsu.nextNumeric(-1));
    }

    @Test
    public void numericStringShouldOnlyContainDigitChars() {
        final String result = rsu.nextNumeric(50);
        assertNotNull(result);
        assertEquals(50, result.length());
        // All chars must be '0'..'9'
        for (final char ch : result.toCharArray()) {
            assertTrue(ch >= '0' && ch <= '9',
                    "Character out of digit range: " + ch);
        }
    }

    // -----------------------------------------------------------------------
    // nextAscii(int count)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnAsciiStringOfExactLength() {
        // Arrange
        final int count = 25;
        // Act
        final String result = rsu.nextAscii(count);
        // Assert
        assertNotNull(result);
        assertEquals(count, result.length());
    }

    @Test
    public void asciiStringShouldOnlyContainPrintableAsciiChars() {
        // ASCII printable range: 32 (space) to 126 (~)
        final String result = rsu.nextAscii(100);
        assertNotNull(result);
        assertEquals(100, result.length());
        for (final char ch : result.toCharArray()) {
            assertTrue(ch >= 32 && ch <= 126,
                    "Character outside printable ASCII range: " + (int) ch);
        }
    }

    @Test
    public void shouldReturnEmptyAsciiStringForCountZero() {
        final String result = rsu.nextAscii(0);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void shouldThrowForNegativeCountAscii() {
        assertThrows(IllegalArgumentException.class, () -> rsu.nextAscii(-1));
    }

    // -----------------------------------------------------------------------
    // Factory singleton consistency
    // -----------------------------------------------------------------------

    @Test
    public void insecureFactoryShouldAlwaysReturnSameSingleton() {
        // insecure() must return the same instance (singleton pattern)
        final RandomStringUtils a = RandomStringUtils.insecure();
        final RandomStringUtils b = RandomStringUtils.insecure();
        assertTrue(a == b, "insecure() must return the same singleton instance");
    }

    @Test
    public void secureFactoryShouldAlwaysReturnSameSingleton() {
        final RandomStringUtils a = RandomStringUtils.secure();
        final RandomStringUtils b = RandomStringUtils.secure();
        assertTrue(a == b, "secure() must return the same singleton instance");
    }

    @Test
    public void insecureAndSecureShouldBeDifferentInstances() {
        final RandomStringUtils insecure = RandomStringUtils.insecure();
        final RandomStringUtils secure = RandomStringUtils.secure();
        assertTrue(insecure != secure,
                "insecure() and secure() must be different singleton instances");
    }
}
