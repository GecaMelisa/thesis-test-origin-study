package com.example.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("gpt")
class CalculatorTests {

    private final Calculator calculator = new Calculator();

    @Test
    @DisplayName("1 + 1 = 2")
    void add_twoPositiveNumbers_returnsSum() {
        int result = calculator.add(1, 1);
        assertEquals(2, result, "1 + 1 should equal 2");
    }

    @Test
    @DisplayName("Adding negative numbers is supported")
    void add_negativeNumbers_returnsSum() {
        int result = calculator.add(-5, -7);
        assertEquals(-12, result, "-5 + -7 should equal -12");
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @DisplayName("Addition works for multiple inputs")
    @CsvSource({
            "0, 0, 0",
            "1, 2, 3",
            "49, 51, 100",
            "-5, -5, -10",
            "-2, 3, 1"
    })
    void add_variousInputs_returnsExpectedSum(int a, int b, int expected) {
        int result = calculator.add(a, b);
        assertEquals(expected, result, () -> a + " + " + b + " should equal " + expected);
    }

    @Test
    @DisplayName("Integer overflow behaves consistently")
    void add_overflow_behavesConsistently() {
        int result = calculator.add(Integer.MAX_VALUE, 1);
        int expected = Integer.MAX_VALUE + 1;
        assertEquals(expected, result, "Overflow should match Java's int overflow behavior");
    }

    @Test
    @DisplayName("Adding MIN_VALUE and -1 overflows consistently")
    void add_underflow_behavesConsistently() {
        int result = calculator.add(Integer.MIN_VALUE, -1);
        int expected = Integer.MIN_VALUE - 1;
        assertEquals(expected, result, "Underflow should match Java's int underflow behavior");
    }
}
