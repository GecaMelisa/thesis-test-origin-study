package org.apache.commons.lang3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RandomStringUtilsGPTTest {

    private static final int SIZE = 10;

    @Test
    void shouldGenerateRandomStringWithCorrectLength() {
        String result = RandomStringUtils.random(SIZE);
        assertNotNull(result);
        assertEquals(SIZE, result.length());
    }

    @Test
    void shouldReturnEmptyWhenSizeZero() {
        String result = RandomStringUtils.random(0);
        assertEquals("", result);
    }

    @Test
    void shouldThrowExceptionWhenNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.random(-1));
    }

    @Test
    void shouldGenerateAlphabeticString() {
        String result = RandomStringUtils.randomAlphabetic(SIZE);
        assertEquals(SIZE, result.length());
        assertTrue(result.chars().allMatch(Character::isLetter));
    }

    @Test
    void shouldGenerateNumericString() {
        String result = RandomStringUtils.randomNumeric(SIZE);
        assertEquals(SIZE, result.length());
        assertTrue(result.chars().allMatch(Character::isDigit));
    }

    @Test
    void shouldGenerateAlphanumericString() {
        String result = RandomStringUtils.randomAlphanumeric(SIZE);
        assertEquals(SIZE, result.length());
        assertTrue(result.chars().allMatch(Character::isLetterOrDigit));
    }

    @Test
    void shouldGenerateDifferentValuesAcrossCalls() {
        String a = RandomStringUtils.random(SIZE);
        String b = RandomStringUtils.random(SIZE);
        assertNotEquals(a, b);
    }

    @Test
    void shouldHandleZeroLengthAlphabetic() {
        assertEquals("", RandomStringUtils.randomAlphabetic(0));
    }

    @Test
    void shouldHandleZeroLengthNumeric() {
        assertEquals("", RandomStringUtils.randomNumeric(0));
    }

    @Test
    void shouldHandleZeroLengthAlphanumeric() {
        assertEquals("", RandomStringUtils.randomAlphanumeric(0));
    }

    @Test
    void shouldThrowExceptionForNegativeAlphabetic() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.randomAlphabetic(-1));
    }

    @Test
    void shouldThrowExceptionForNegativeNumeric() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.randomNumeric(-1));
    }

    @Test
    void shouldThrowExceptionForNegativeAlphanumeric() {
        assertThrows(IllegalArgumentException.class, () -> RandomStringUtils.randomAlphanumeric(-1));
    }

    @Test
    void shouldContainOnlyLetters() {
        String result = RandomStringUtils.randomAlphabetic(20);
        assertTrue(result.matches("[a-zA-Z]+"));
    }

    @Test
    void shouldContainOnlyDigits() {
        String result = RandomStringUtils.randomNumeric(20);
        assertTrue(result.matches("\\d+"));
    }

    @Test
    void shouldContainLettersAndDigitsOnly() {
        String result = RandomStringUtils.randomAlphanumeric(20);
        assertTrue(result.matches("[a-zA-Z0-9]+"));
    }

    @Test
    void shouldPreserveRequestedLength() {
        int len = 15;
        String result = RandomStringUtils.random(len);
        assertEquals(len, result.length());
    }
}