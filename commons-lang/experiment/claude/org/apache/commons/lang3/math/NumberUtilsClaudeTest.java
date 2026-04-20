/*
 * Claude-generated test class for academic experiment on automated unit test generation.
 * Covers: NumberUtils.toInt(), toLong(), toDouble(), isCreatable(), isDigits(), isParsable()
 * Framework: JUnit 5 (Jupiter)
 * Design: Arrange-Act-Assert, high assertion density, mutation-killing edge cases
 */
package org.apache.commons.lang3.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * High-quality, mutation-killing tests for selected NumberUtils methods.
 *
 * <p>Methods under test:
 * <ul>
 *   <li>{@link NumberUtils#toInt(String)}</li>
 *   <li>{@link NumberUtils#toInt(String, int)}</li>
 *   <li>{@link NumberUtils#toLong(String)}</li>
 *   <li>{@link NumberUtils#toLong(String, long)}</li>
 *   <li>{@link NumberUtils#toDouble(String)}</li>
 *   <li>{@link NumberUtils#isCreatable(String)}</li>
 *   <li>{@link NumberUtils#isDigits(String)}</li>
 *   <li>{@link NumberUtils#isParsable(String)}</li>
 * </ul>
 */
public class NumberUtilsClaudeTest {

    // -----------------------------------------------------------------------
    // toInt(String)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnZeroWhenInputIsNull_toInt() {
        // Arrange
        final String input = null;
        // Act
        final int result = NumberUtils.toInt(input);
        // Assert
        assertEquals(0, result, "toInt(null) must return 0");
    }

    @Test
    public void shouldReturnZeroWhenInputIsEmpty_toInt() {
        // Arrange
        final String input = "";
        // Act
        final int result = NumberUtils.toInt(input);
        // Assert
        assertEquals(0, result, "toInt(\"\") must return 0");
    }

    @Test
    public void shouldParsePositiveInteger_toInt() {
        // Arrange
        final String input = "42";
        // Act
        final int result = NumberUtils.toInt(input);
        // Assert — exact value kills off-by-one mutants
        assertEquals(42, result);
    }

    @Test
    public void shouldParseNegativeInteger_toInt() {
        // Arrange
        final String input = "-100";
        // Act
        final int result = NumberUtils.toInt(input);
        // Assert
        assertEquals(-100, result);
    }

    @Test
    public void shouldParseZeroString_toInt() {
        // Arrange
        final String input = "0";
        // Act
        final int result = NumberUtils.toInt(input);
        // Assert
        assertEquals(0, result);
    }

    @Test
    public void shouldReturnZeroForNonNumericInput_toInt() {
        // Arrange
        final String input = "abc";
        // Act
        final int result = NumberUtils.toInt(input);
        // Assert
        assertEquals(0, result, "unparsable string must fall back to 0");
    }

    @Test
    public void shouldParseIntMaxValue_toInt() {
        // Arrange — boundary: Integer.MAX_VALUE
        final String input = String.valueOf(Integer.MAX_VALUE);
        // Act
        final int result = NumberUtils.toInt(input);
        // Assert
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void shouldParseIntMinValue_toInt() {
        // Arrange — boundary: Integer.MIN_VALUE
        final String input = String.valueOf(Integer.MIN_VALUE);
        // Act
        final int result = NumberUtils.toInt(input);
        // Assert
        assertEquals(Integer.MIN_VALUE, result);
    }

    @Test
    public void shouldReturnZeroForFloatString_toInt() {
        // Arrange — floats are not valid ints
        final String input = "3.14";
        // Act
        final int result = NumberUtils.toInt(input);
        // Assert
        assertEquals(0, result);
    }

    // -----------------------------------------------------------------------
    // toInt(String, int defaultValue)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnDefaultWhenInputIsNull_toIntWithDefault() {
        // Arrange
        final int defaultValue = 99;
        // Act
        final int result = NumberUtils.toInt(null, defaultValue);
        // Assert
        assertEquals(99, result);
    }

    @Test
    public void shouldReturnDefaultWhenInputIsEmpty_toIntWithDefault() {
        // Arrange
        final int defaultValue = -7;
        // Act
        final int result = NumberUtils.toInt("", defaultValue);
        // Assert
        assertEquals(-7, result);
    }

    @Test
    public void shouldReturnParsedValueNotDefault_toIntWithDefault() {
        // Arrange
        final int defaultValue = 99;
        // Act
        final int result = NumberUtils.toInt("5", defaultValue);
        // Assert — must return parsed value, not default
        assertEquals(5, result);
    }

    @Test
    public void shouldReturnDefaultForNonNumericInput_toIntWithDefault() {
        // Arrange
        final int defaultValue = 42;
        // Act
        final int result = NumberUtils.toInt("xyz", defaultValue);
        // Assert
        assertEquals(42, result);
    }

    // -----------------------------------------------------------------------
    // toLong(String)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnZeroWhenInputIsNull_toLong() {
        // Act
        final long result = NumberUtils.toLong(null);
        // Assert
        assertEquals(0L, result);
    }

    @Test
    public void shouldReturnZeroWhenInputIsEmpty_toLong() {
        // Act
        final long result = NumberUtils.toLong("");
        // Assert
        assertEquals(0L, result);
    }

    @Test
    public void shouldParseLongValue_toLong() {
        // Arrange — value larger than Integer.MAX_VALUE
        final String input = "10000000000";
        // Act
        final long result = NumberUtils.toLong(input);
        // Assert — exact value check
        assertEquals(10_000_000_000L, result);
    }

    @Test
    public void shouldParseNegativeLong_toLong() {
        // Arrange
        final String input = "-9999999999";
        // Act
        final long result = NumberUtils.toLong(input);
        // Assert
        assertEquals(-9_999_999_999L, result);
    }

    @Test
    public void shouldParseLongMaxValue_toLong() {
        // Arrange
        final String input = String.valueOf(Long.MAX_VALUE);
        // Act
        final long result = NumberUtils.toLong(input);
        // Assert
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void shouldParseLongMinValue_toLong() {
        // Arrange
        final String input = String.valueOf(Long.MIN_VALUE);
        // Act
        final long result = NumberUtils.toLong(input);
        // Assert
        assertEquals(Long.MIN_VALUE, result);
    }

    @Test
    public void shouldReturnZeroForNonNumericInput_toLong() {
        // Act
        final long result = NumberUtils.toLong("notanumber");
        // Assert
        assertEquals(0L, result);
    }

    @Test
    public void shouldReturnDefaultWhenInputIsNullOrInvalid_toLongWithDefault() {
        // Act & Assert
        assertEquals(99L, NumberUtils.toLong(null, 99L));
        assertEquals(99L, NumberUtils.toLong("", 99L));
        assertEquals(99L, NumberUtils.toLong("bad", 99L));
        // Parsed value overrides default
        assertEquals(7L, NumberUtils.toLong("7", 99L));
    }

    // -----------------------------------------------------------------------
    // toDouble(String)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnZeroWhenInputIsNull_toDouble() {
        // Act
        final double result = NumberUtils.toDouble((String) null);
        // Assert
        assertEquals(0.0d, result, 0.0d);
    }

    @Test
    public void shouldReturnZeroWhenInputIsEmpty_toDouble() {
        // Act
        final double result = NumberUtils.toDouble("");
        // Assert
        assertEquals(0.0d, result, 0.0d);
    }

    @Test
    public void shouldParsePositiveDouble_toDouble() {
        // Arrange
        final String input = "1.6";
        // Act
        final double result = NumberUtils.toDouble(input);
        // Assert — use delta for floating-point equality
        assertEquals(1.6d, result, 1e-10d);
    }

    @Test
    public void shouldParseNegativeDouble_toDouble() {
        // Arrange
        final String input = "-2.5";
        // Act
        final double result = NumberUtils.toDouble(input);
        // Assert
        assertEquals(-2.5d, result, 1e-10d);
    }

    @Test
    public void shouldParseScientificNotation_toDouble() {
        // Arrange
        final String input = "1.2e3";
        // Act
        final double result = NumberUtils.toDouble(input);
        // Assert
        assertEquals(1200.0d, result, 1e-10d);
    }

    @Test
    public void shouldReturnZeroForNonNumericInput_toDouble() {
        // Act
        final double result = NumberUtils.toDouble("abc");
        // Assert
        assertEquals(0.0d, result, 0.0d);
    }

    @Test
    public void shouldReturnDefaultWhenParsingFails_toDoubleWithDefault() {
        // Arrange
        final double defaultValue = 3.14d;
        // Act & Assert
        assertEquals(3.14d, NumberUtils.toDouble((String) null, defaultValue), 1e-10d);
        assertEquals(3.14d, NumberUtils.toDouble("", defaultValue), 1e-10d);
        assertEquals(3.14d, NumberUtils.toDouble("invalid", defaultValue), 1e-10d);
        // Parsed value must override default
        assertEquals(2.0d, NumberUtils.toDouble("2.0", defaultValue), 1e-10d);
    }

    // -----------------------------------------------------------------------
    // isCreatable(String)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnFalseForNull_isCreatable() {
        assertFalse(NumberUtils.isCreatable(null));
    }

    @Test
    public void shouldReturnFalseForEmpty_isCreatable() {
        assertFalse(NumberUtils.isCreatable(""));
    }

    @Test
    public void shouldReturnFalseForBlank_isCreatable() {
        assertFalse(NumberUtils.isCreatable("   "));
    }

    @Test
    public void shouldReturnTrueForInteger_isCreatable() {
        assertTrue(NumberUtils.isCreatable("123"));
    }

    @Test
    public void shouldReturnTrueForNegativeInteger_isCreatable() {
        assertTrue(NumberUtils.isCreatable("-456"));
    }

    @Test
    public void shouldReturnTrueForPositiveWithPlus_isCreatable() {
        assertTrue(NumberUtils.isCreatable("+123"));
    }

    @Test
    public void shouldReturnTrueForDecimal_isCreatable() {
        assertTrue(NumberUtils.isCreatable("3.14"));
    }

    @Test
    public void shouldReturnTrueForHexadecimal_isCreatable() {
        assertTrue(NumberUtils.isCreatable("0x1A"));
        assertTrue(NumberUtils.isCreatable("0X1a"));
    }

    @Test
    public void shouldReturnTrueForLongLiteral_isCreatable() {
        // L suffix marks a long literal
        assertTrue(NumberUtils.isCreatable("100L"));
        assertTrue(NumberUtils.isCreatable("100l"));
    }

    @Test
    public void shouldReturnTrueForFloatLiteral_isCreatable() {
        assertTrue(NumberUtils.isCreatable("1.5f"));
        assertTrue(NumberUtils.isCreatable("1.5F"));
    }

    @Test
    public void shouldReturnTrueForDoubleLiteral_isCreatable() {
        assertTrue(NumberUtils.isCreatable("1.5d"));
        assertTrue(NumberUtils.isCreatable("1.5D"));
    }

    @Test
    public void shouldReturnFalseForAlphabeticInput_isCreatable() {
        assertFalse(NumberUtils.isCreatable("abc"));
    }

    @Test
    public void shouldReturnFalseForAlphanumericInput_isCreatable() {
        assertFalse(NumberUtils.isCreatable("1a2b"));
    }

    @Test
    public void shouldReturnFalseForMultipleDecimalPoints_isCreatable() {
        assertFalse(NumberUtils.isCreatable("1.2.3"));
    }

    @Test
    public void shouldReturnTrueForZero_isCreatable() {
        assertTrue(NumberUtils.isCreatable("0"));
    }

    @Test
    public void shouldReturnTrueForNegativeDecimal_isCreatable() {
        assertTrue(NumberUtils.isCreatable("-1.5"));
    }

    // -----------------------------------------------------------------------
    // isDigits(String)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnFalseForNull_isDigits() {
        assertFalse(NumberUtils.isDigits(null));
    }

    @Test
    public void shouldReturnFalseForEmpty_isDigits() {
        assertFalse(NumberUtils.isDigits(""));
    }

    @Test
    public void shouldReturnTrueForAllDigits_isDigits() {
        assertTrue(NumberUtils.isDigits("12345"));
    }

    @Test
    public void shouldReturnFalseWhenContainsLetter_isDigits() {
        assertFalse(NumberUtils.isDigits("123a"));
    }

    @Test
    public void shouldReturnFalseForNegativeSign_isDigits() {
        // minus sign is not a digit
        assertFalse(NumberUtils.isDigits("-123"));
    }

    @Test
    public void shouldReturnFalseForDecimalPoint_isDigits() {
        assertFalse(NumberUtils.isDigits("1.2"));
    }

    @Test
    public void shouldReturnTrueForSingleDigit_isDigits() {
        assertTrue(NumberUtils.isDigits("7"));
    }

    @Test
    public void shouldReturnFalseForWhitespace_isDigits() {
        assertFalse(NumberUtils.isDigits(" 12"));
    }

    // -----------------------------------------------------------------------
    // isParsable(String)
    // -----------------------------------------------------------------------

    @Test
    public void shouldReturnFalseForNull_isParsable() {
        assertFalse(NumberUtils.isParsable(null));
    }

    @Test
    public void shouldReturnFalseForEmpty_isParsable() {
        assertFalse(NumberUtils.isParsable(""));
    }

    @Test
    public void shouldReturnTrueForInteger_isParsable() {
        assertTrue(NumberUtils.isParsable("100"));
    }

    @Test
    public void shouldReturnTrueForNegativeInteger_isParsable() {
        assertTrue(NumberUtils.isParsable("-100"));
    }

    @Test
    public void shouldReturnTrueForDecimal_isParsable() {
        assertTrue(NumberUtils.isParsable("1.23"));
    }

    @Test
    public void shouldReturnFalseForHexPrefix_isParsable() {
        // 0x notation is NOT parsable by Integer/Long/Float/Double
        assertFalse(NumberUtils.isParsable("0x10"));
    }

    @Test
    public void shouldReturnFalseForLongSuffix_isParsable() {
        // "L" suffix not handled by parseDouble/parseLong directly in isParsable
        assertFalse(NumberUtils.isParsable("10L"));
    }

    @Test
    public void shouldReturnFalseForAlphabeticInput_isParsable() {
        assertFalse(NumberUtils.isParsable("abc"));
    }

    @Test
    public void shouldReturnTrueForScientificNotation_isParsable() {
        // parseDouble handles scientific notation
        assertTrue(NumberUtils.isParsable("1.2e5"));
    }

    @Test
    public void shouldReturnFalseForBlankString_isParsable() {
        assertFalse(NumberUtils.isParsable("   "));
    }

    // -----------------------------------------------------------------------
    // Boundary / integration checks
    // -----------------------------------------------------------------------

    @Test
    public void toIntAndToLongConsistencyForSmallValues() {
        // For values in int range, toInt and toLong should agree
        final String input = "12345";
        final int intResult = NumberUtils.toInt(input);
        final long longResult = NumberUtils.toLong(input);
        assertEquals(12345, intResult);
        assertEquals(12345L, longResult);
        assertEquals((long) intResult, longResult);
    }

    @Test
    public void isCreatableImpliesParseableForSimpleIntegers() {
        // A simple integer must be both creatable and parsable
        final String input = "42";
        assertTrue(NumberUtils.isCreatable(input));
        assertTrue(NumberUtils.isParsable(input));
    }

    @Test
    public void isDigitsSubsetOfIsCreatable() {
        // Any all-digit string is creatable
        final String input = "9876";
        assertTrue(NumberUtils.isDigits(input));
        assertTrue(NumberUtils.isCreatable(input));
    }
}
