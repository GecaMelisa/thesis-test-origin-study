package org.apache.commons.lang3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilsGPTTest {

    @Test
    void shouldReturnTrueForNullAndEmpty_isEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
    }

    @Test
    void shouldReturnFalseForNonEmpty_isEmpty() {
        assertFalse(StringUtils.isEmpty("a"));
        assertFalse(StringUtils.isEmpty("abc"));
    }

    @Test
    void shouldReturnTrueForBlankInputs_isBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank("   "));
        assertTrue(StringUtils.isBlank("\t\n"));
    }

    @Test
    void shouldReturnFalseForNonBlankInputs_isBlank() {
        assertFalse(StringUtils.isBlank("a"));
        assertFalse(StringUtils.isBlank(" a "));
    }

    @Test
    void isBlankShouldBeSupersetOfIsEmpty() {
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isBlank(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertTrue(StringUtils.isBlank(" "));
    }

    @Test
    void shouldReverseNullAndEmpty() {
        assertNull(StringUtils.reverse(null));
        assertEquals("", StringUtils.reverse(""));
    }

    @Test
    void shouldReverseSimpleStrings() {
        assertEquals("cba", StringUtils.reverse("abc"));
        assertEquals("a", StringUtils.reverse("a"));
    }

    @Test
    void shouldReverseWithWhitespaceAndSymbols() {
        assertEquals(" cba ", StringUtils.reverse(" abc "));
        assertEquals("!1a", StringUtils.reverse("a1!"));
    }

    @Test
    void reverseOfReverseShouldReturnOriginal() {
        String input = "mutation";
        String reversedTwice = StringUtils.reverse(StringUtils.reverse(input));
        assertEquals(input, reversedTwice);
    }

    @Test
    void shouldPreserveLengthAfterReverse() {
        String input = "Apache";
        String result = StringUtils.reverse(input);
        assertEquals(input.length(), result.length());
    }

    @Test
    void shouldCapitalizeNullAndEmpty() {
        assertNull(StringUtils.capitalize(null));
        assertEquals("", StringUtils.capitalize(""));
    }

    @Test
    void shouldCapitalizeLowercaseWord() {
        assertEquals("Hello", StringUtils.capitalize("hello"));
    }

    @Test
    void shouldNotChangeAlreadyCapitalized() {
        assertEquals("Hello", StringUtils.capitalize("Hello"));
    }

    @Test
    void shouldCapitalizeSingleCharacter() {
        assertEquals("A", StringUtils.capitalize("a"));
        assertEquals("A", StringUtils.capitalize("A"));
    }

    @Test
    void shouldNotModifyNonLetterStart() {
        assertEquals("1abc", StringUtils.capitalize("1abc"));
        assertEquals(" abc", StringUtils.capitalize(" abc"));
    }

    @Test
    void shouldOnlyChangeFirstCharacter() {
        String result = StringUtils.capitalize("hELLO");
        assertEquals("HELLO", result);
        assertEquals('H', result.charAt(0));
        assertEquals('E', result.charAt(1));
    }

    @Test
    void shouldPreserveLengthAfterCapitalize() {
        String input = "commons";
        String result = StringUtils.capitalize(input);
        assertEquals(input.length(), result.length());
    }
}