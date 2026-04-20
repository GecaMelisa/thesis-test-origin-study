/*
 * Claude-generated test class for academic experiment on automated unit test generation.
 * Covers: StringUtils.isEmpty(), isBlank(), reverse(), capitalize()
 * Framework: JUnit 5 (Jupiter)
 * Design: Arrange-Act-Assert, high assertion density, mutation-killing edge cases
 */
package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * High-quality, mutation-killing tests for selected StringUtils methods.
 *
 * <p>Methods under test:
 * <ul>
 *   <li>{@link StringUtils#isEmpty(CharSequence)}</li>
 *   <li>{@link StringUtils#isBlank(CharSequence)}</li>
 *   <li>{@link StringUtils#reverse(String)}</li>
 *   <li>{@link StringUtils#capitalize(String)}</li>
 * </ul>
 */
public class StringUtilsClaudeTest {

    // -----------------------------------------------------------------------
    // isEmpty()
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnTrueWhenStringIsNull_isEmpty() {
        // Arrange
        final CharSequence input = null;
        // Act
        final boolean result = StringUtils.isEmpty(input);
        // Assert
        assertTrue(result, "null must be considered empty");
    }

    @Test
    public void shouldReturnTrueWhenStringIsEmpty_isEmpty() {
        // Arrange
        final String input = "";
        // Act
        final boolean result = StringUtils.isEmpty(input);
        // Assert
        assertTrue(result, "empty string must be considered empty");
    }

    @Test
    public void shouldReturnFalseWhenStringIsSingleSpace_isEmpty() {
        // Arrange
        final String input = " ";
        // Act
        final boolean result = StringUtils.isEmpty(input);
        // Assert
        assertFalse(result, "single space is NOT empty — isEmpty does not trim");
    }

    @Test
    public void shouldReturnFalseWhenStringContainsOnlyWhitespace_isEmpty() {
        // Arrange
        final String input = "   \t\n";
        // Act
        final boolean result = StringUtils.isEmpty(input);
        // Assert
        assertFalse(result, "whitespace-only string is not considered empty by isEmpty");
    }

    @Test
    public void shouldReturnFalseWhenStringHasContent_isEmpty() {
        // Arrange
        final String input = "hello";
        // Act
        final boolean result = StringUtils.isEmpty(input);
        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenStringHasLeadingAndTrailingSpaces_isEmpty() {
        // Arrange
        final String input = "  bob  ";
        // Act
        final boolean result = StringUtils.isEmpty(input);
        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseForSingleCharacter_isEmpty() {
        // Arrange
        final String input = "a";
        // Act
        final boolean result = StringUtils.isEmpty(input);
        // Assert
        assertFalse(result, "single non-whitespace char is not empty");
    }

    @Test
    public void shouldAcceptCharSequenceNotOnlyString_isEmpty() {
        // Arrange — CharBuffer implements CharSequence
        final CharSequence emptyBuffer = new StringBuilder();
        final CharSequence nonEmptyBuffer = new StringBuilder("x");
        // Act & Assert
        assertTrue(StringUtils.isEmpty(emptyBuffer));
        assertFalse(StringUtils.isEmpty(nonEmptyBuffer));
    }

    // -----------------------------------------------------------------------
    // isBlank()
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnTrueWhenInputIsNull_isBlank() {
        // Arrange
        final CharSequence input = null;
        // Act
        final boolean result = StringUtils.isBlank(input);
        // Assert
        assertTrue(result, "null must be considered blank");
    }

    @Test
    public void shouldReturnTrueWhenInputIsEmpty_isBlank() {
        // Arrange
        final String input = "";
        // Act
        final boolean result = StringUtils.isBlank(input);
        // Assert
        assertTrue(result, "empty string must be considered blank");
    }

    @Test
    public void shouldReturnTrueWhenInputIsSingleSpace_isBlank() {
        // Arrange
        final String input = " ";
        // Act
        final boolean result = StringUtils.isBlank(input);
        // Assert
        assertTrue(result, "single space is blank");
    }

    @Test
    public void shouldReturnTrueWhenInputIsMultipleSpaces_isBlank() {
        // Arrange
        final String input = "   ";
        // Act
        final boolean result = StringUtils.isBlank(input);
        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldReturnTrueWhenInputContainsTabAndNewline_isBlank() {
        // Arrange — covers all standard Java whitespace characters
        final String input = " \t\n\r\f";
        // Act
        final boolean result = StringUtils.isBlank(input);
        // Assert
        assertTrue(result, "all-whitespace string (tab, newline, CR, FF) must be blank");
    }

    @Test
    public void shouldReturnFalseWhenInputHasNonWhitespace_isBlank() {
        // Arrange
        final String input = "bob";
        // Act
        final boolean result = StringUtils.isBlank(input);
        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenInputHasWhitespaceSurroundingContent_isBlank() {
        // Arrange
        final String input = "  bob  ";
        // Act
        final boolean result = StringUtils.isBlank(input);
        // Assert
        assertFalse(result, "surrounding spaces with real content is not blank");
    }

    @Test
    public void shouldReturnFalseWhenInputIsSingleNonWhitespaceChar_isBlank() {
        // Arrange
        final String input = "a";
        // Act
        final boolean result = StringUtils.isBlank(input);
        // Assert
        assertFalse(result);
    }

    @Test
    public void isBlankShouldBeSupersetOfIsEmpty() {
        // isBlank returns true for every input where isEmpty returns true
        // Arrange
        final String nullInput = null;
        final String emptyInput = "";
        // Act & Assert — isEmpty ⊆ isBlank
        assertTrue(StringUtils.isEmpty(nullInput));
        assertTrue(StringUtils.isBlank(nullInput));
        assertTrue(StringUtils.isEmpty(emptyInput));
        assertTrue(StringUtils.isBlank(emptyInput));
        // A space is blank but NOT empty
        assertFalse(StringUtils.isEmpty(" "));
        assertTrue(StringUtils.isBlank(" "));
    }

    @Test
    public void shouldAcceptCharSequenceSubtypes_isBlank() {
        // Arrange
        final CharSequence blankBuilder = new StringBuilder("  ");
        final CharSequence contentBuilder = new StringBuilder("x");
        // Act & Assert
        assertTrue(StringUtils.isBlank(blankBuilder));
        assertFalse(StringUtils.isBlank(contentBuilder));
    }

    // -----------------------------------------------------------------------
    // reverse()
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnNullWhenInputIsNull_reverse() {
        // Arrange
        final String input = null;
        // Act
        final String result = StringUtils.reverse(input);
        // Assert
        assertNull(result, "reverse(null) must return null");
    }

    @Test
    public void shouldReturnEmptyStringWhenInputIsEmpty_reverse() {
        // Arrange
        final String input = "";
        // Act
        final String result = StringUtils.reverse(input);
        // Assert
        assertEquals("", result, "reverse(\"\") must return \"\"");
    }

    @Test
    public void shouldReturnSameStringForSingleCharacter_reverse() {
        // Arrange
        final String input = "a";
        // Act
        final String result = StringUtils.reverse(input);
        // Assert
        assertEquals("a", result);
        assertEquals(1, result.length());
    }

    @Test
    public void shouldReturnPalindromeUnchanged_reverse() {
        // Arrange
        final String input = "abcba";
        // Act
        final String result = StringUtils.reverse(input);
        // Assert — exact value check kills mutants that alter reversal logic
        assertEquals("abcba", result);
        assertEquals(input.length(), result.length());
    }

    @Test
    public void shouldReverseSimpleWord_reverse() {
        // Arrange
        final String input = "bat";
        // Act
        final String result = StringUtils.reverse(input);
        // Assert
        assertEquals("tab", result);
        assertEquals(3, result.length());
    }

    @Test
    public void shouldReverseStringWithSpaces_reverse() {
        // Arrange
        final String input = "hello world";
        // Act
        final String result = StringUtils.reverse(input);
        // Assert
        assertEquals("dlrow olleh", result);
    }

    @Test
    public void shouldReverseStringWithMixedCase_reverse() {
        // Arrange
        final String input = "AbCdEf";
        // Act
        final String result = StringUtils.reverse(input);
        // Assert — case must be preserved exactly
        assertEquals("fEdCbA", result);
    }

    @Test
    public void shouldReverseStringWithDigitsAndSpecialChars_reverse() {
        // Arrange
        final String input = "a1!b2@";
        // Act
        final String result = StringUtils.reverse(input);
        // Assert
        assertEquals("@2b!1a", result);
    }

    @Test
    public void shouldPreserveLengthAfterReverse_reverse() {
        // Arrange
        final String input = "Apache Commons";
        // Act
        final String result = StringUtils.reverse(input);
        // Assert — length must be preserved; exact value kills length-boundary mutants
        assertEquals("snommoC ehcapA", result);
        assertEquals(input.length(), result.length());
    }

    @Test
    public void reverseOfReverseEqualsOriginal_reverse() {
        // Arrange
        final String input = "mutation testing";
        // Act
        final String doubleReversed = StringUtils.reverse(StringUtils.reverse(input));
        // Assert — idempotency check
        assertEquals(input, doubleReversed);
    }

    @Test
    public void shouldReturnSingleSpaceForSingleSpace_reverse() {
        // Arrange
        final String input = " ";
        // Act
        final String result = StringUtils.reverse(input);
        // Assert
        assertEquals(" ", result);
    }

    // -----------------------------------------------------------------------
    // capitalize()
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnNullWhenInputIsNull_capitalize() {
        // Arrange
        final String input = null;
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert
        assertNull(result, "capitalize(null) must return null");
    }

    @Test
    public void shouldReturnEmptyStringWhenInputIsEmpty_capitalize() {
        // Arrange
        final String input = "";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert
        assertEquals("", result, "capitalize(\"\") must return \"\"");
    }

    @Test
    public void shouldCapitalizeLowercaseFirstChar_capitalize() {
        // Arrange
        final String input = "hello";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert
        assertEquals("Hello", result);
    }

    @Test
    public void shouldLeaveAlreadyCapitalizedStringUnchanged_capitalize() {
        // Arrange
        final String input = "Cat";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert — same reference or equal value; the method may return the original
        assertEquals("Cat", result);
    }

    @Test
    public void shouldCapitalizeSingleLowercaseChar_capitalize() {
        // Arrange
        final String input = "a";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert
        assertEquals("A", result);
        assertEquals(1, result.length());
    }

    @Test
    public void shouldNotAlterRestOfStringAfterCapitalization_capitalize() {
        // Arrange — rest must remain exactly as supplied
        final String input = "hELLO WORLD";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert — only first char changed; rest untouched
        assertEquals("HELLO WORLD", result);
        assertEquals('H', result.charAt(0));
        assertEquals('E', result.charAt(1));
        assertEquals('L', result.charAt(2));
    }

    @Test
    public void shouldReturnSameStringWhenFirstCharIsUppercase_capitalize() {
        // Arrange
        final String input = "Already";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert
        assertEquals("Already", result);
    }

    @Test
    public void shouldCapitalizeStringStartingWithLowercaseLetter_capitalize() {
        // Arrange
        final String input = "java";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert — exact match kills mutants that uppercase more than the first char
        assertEquals("Java", result);
        assertEquals(4, result.length());
    }

    @Test
    public void shouldHandleSingleUppercaseCharInput_capitalize() {
        // Arrange
        final String input = "A";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert
        assertEquals("A", result);
    }

    @Test
    public void shouldHandleStringStartingWithDigit_capitalize() {
        // Arrange
        final String input = "1abc";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert — digit is not a letter; first char unchanged
        assertEquals("1abc", result);
    }

    @Test
    public void shouldHandleStringStartingWithSpace_capitalize() {
        // Arrange
        final String input = " hello";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert — capitalize only affects first code point; space has no titlecase
        assertEquals(" hello", result);
    }

    @Test
    public void shouldPreserveLengthAfterCapitalization_capitalize() {
        // Arrange
        final String input = "commons";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert
        assertEquals("Commons", result);
        assertEquals(input.length(), result.length());
    }

    @Test
    public void shouldHandleAllLowercaseString_capitalize() {
        // Arrange
        final String input = "abc";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert — only first char uppercased; remaining two are still lowercase
        assertEquals("Abc", result);
        assertTrue(Character.isUpperCase(result.charAt(0)));
        assertTrue(Character.isLowerCase(result.charAt(1)));
        assertTrue(Character.isLowerCase(result.charAt(2)));
    }

    @Test
    public void shouldHandleAllUppercaseString_capitalize() {
        // Arrange — first char is already uppercase, rest unchanged
        final String input = "ABC";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert
        assertEquals("ABC", result);
    }

    @Test
    public void shouldHandleWhitespaceOnlyString_capitalize() {
        // Arrange
        final String input = "   ";
        // Act
        final String result = StringUtils.capitalize(input);
        // Assert — whitespace has no uppercase; returned unchanged
        assertEquals("   ", result);
    }

    // -----------------------------------------------------------------------
    // Cross-method consistency checks
    // -----------------------------------------------------------------------

    @Test
    public void capitalizePreservesIsEmptyBehavior() {
        // capitalize(null) → null → isEmpty(null) = true
        assertNull(StringUtils.capitalize(null));
        assertTrue(StringUtils.isEmpty(StringUtils.capitalize(null)));

        // capitalize("") → "" → isEmpty("") = true
        assertEquals("", StringUtils.capitalize(""));
        assertTrue(StringUtils.isEmpty(StringUtils.capitalize("")));
    }

    @Test
    public void reverseOfEmptyIsEmpty() {
        final String result = StringUtils.reverse("");
        assertEquals("", result);
        assertTrue(StringUtils.isEmpty(result));
        assertTrue(StringUtils.isBlank(result));
    }
}
