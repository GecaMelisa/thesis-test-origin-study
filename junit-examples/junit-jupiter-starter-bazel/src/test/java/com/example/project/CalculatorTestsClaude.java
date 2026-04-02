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
    void addsTwoPositiveNumbers() {
        assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
    }

    @Test
    @DisplayName("Adding two negative numbers")
    void addsTwoNegativeNumbers() {
        assertEquals(-10, calculator.add(-3, -7), "-3 + -7 should equal -10");
    }

    @Test
    @DisplayName("Adding zero keeps the number unchanged")
    void addZeroKeepsNumber() {
        assertEquals(5, calculator.add(5, 0), "5 + 0 should equal 5");
        assertEquals(5, calculator.add(0, 5), "0 + 5 should equal 5");
    }

    @Test
    @DisplayName("Adding positive and negative number")
    void addPositiveAndNegative() {
        assertEquals(-1, calculator.add(2, -3), "2 + (-3) should equal -1");
    }

    @Test
    @DisplayName("Adding zero and zero")
    void addZeroAndZero() {
        assertEquals(0, calculator.add(0, 0), "0 + 0 should equal 0");
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "0, 1, 1",
            "1, 2, 3",
            "49, 51, 100",
            "1, 100, 101",
            "-5, 5, 0",
            "-10, -20, -30"
    })
    void addParameterized(int first, int second, int expectedResult) {
        assertEquals(expectedResult, calculator.add(first, second),
                first + " + " + second + " should equal " + expectedResult);
    }

    @Test
    @DisplayName("Commutativity: a + b = b + a")
    void addIsCommutative() {
        assertEquals(calculator.add(3, 7), calculator.add(7, 3),
                "Addition should be commutative");
    }

    @Test
    @DisplayName("Large numbers within int range")
    void addLargeNumbers() {
        assertEquals(1_200_000_000, calculator.add(1_000_000_000, 200_000_000),
                "1,000,000,000 + 200,000,000 should equal 1,200,000,000");
    }

    @Test
    @DisplayName("MAX_VALUE + 0 = MAX_VALUE")
    void addMaxValueAndZero() {
        assertEquals(Integer.MAX_VALUE, calculator.add(Integer.MAX_VALUE, 0),
                "MAX_VALUE + 0 should equal MAX_VALUE");
    }

    @Test
    @DisplayName("MIN_VALUE + 0 = MIN_VALUE")
    void addMinValueAndZero() {
        assertEquals(Integer.MIN_VALUE, calculator.add(Integer.MIN_VALUE, 0),
                "MIN_VALUE + 0 should equal MIN_VALUE");
    }

    @Test
    @DisplayName("MAX_VALUE + MIN_VALUE = -1")
    void addMaxAndMin() {
        assertEquals(-1, calculator.add(Integer.MAX_VALUE, Integer.MIN_VALUE),
                "MAX_VALUE + MIN_VALUE should equal -1");
    }
}
