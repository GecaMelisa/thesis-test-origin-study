package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Basic JUnit Jupiter tests for {@link Calculator}.
 *
 * This is enough to:
 *  - verify the Gradle/JUnit setup
 *  - give you some coverage for the "starter-ant" module
 */
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
    @DisplayName("Adding negative numbers")
    void add_negativeNumbers_returnsSum() {
        int result = calculator.add(-3, -7);
        assertEquals(-10, result, "-3 + -7 should equal -10");
    }

    @Test
    @DisplayName("Adding zero keeps the number")
    void add_zero_returnsSameNumber() {
        int result = calculator.add(5, 0);
        assertEquals(5, result, "5 + 0 should equal 5");
    }
}
