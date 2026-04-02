package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("claude")
class CalculatorTestsClaude {

    private final Calculator calculator = new Calculator();

    @Test
    @DisplayName("1 + 1 = 2")
    void addTwoPositiveNumbers() {
        int result = calculator.add(1, 1);
        assertEquals(2, result, "1 + 1 should equal 2");
    }

    @Test
    @DisplayName("Adding negative numbers is supported")
    void addNegativeNumbers() {
        int result = calculator.add(-5, -7);
        assertEquals(-12, result, "-5 + -7 should equal -12");
    }

    @Test
    @DisplayName("Adding zero does not change the value")
    void addZeroDoesNotChangeValue() {
        assertEquals(5, calculator.add(5, 0), "5 + 0 should equal 5");
        assertEquals(5, calculator.add(0, 5), "0 + 5 should equal 5");
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
    void addVariousInputs(int a, int b, int expected) {
        int result = calculator.add(a, b);
        assertEquals(expected, result, () -> a + " + " + b + " should equal " + expected);
    }

    @Test
    @DisplayName("Integer overflow behaves consistently")
    void addOverflow() {
        int result = calculator.add(Integer.MAX_VALUE, 1);
        int expected = Integer.MAX_VALUE + 1;
        assertEquals(expected, result, "Overflow should match Java's int overflow behavior");
    }

    @Test
    @DisplayName("Adding MIN_VALUE and -1 overflows consistently")
    void addUnderflow() {
        int result = calculator.add(Integer.MIN_VALUE, -1);
        int expected = Integer.MIN_VALUE - 1;
        assertEquals(expected, result, "Underflow should match Java's int underflow behavior");
    }

    @Test
    @DisplayName("Commutativity: a + b = b + a")
    void addIsCommutative() {
        assertEquals(calculator.add(3, 7), calculator.add(7, 3),
                "Addition should be commutative");
    }

    @Test
    @DisplayName("Adding positive and negative yields correct sign")
    void addMixedSigns() {
        assertEquals(-1, calculator.add(2, -3), "2 + (-3) should equal -1");
        assertEquals(1, calculator.add(-2, 3), "-2 + 3 should equal 1");
    }
}
