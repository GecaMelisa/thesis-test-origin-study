package com.example.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("human")
class HumanCalculatorTestsJava {

    private transient final Calculator calculator = new Calculator();

    @Test
    @DisplayName("add: 2 + 3 = 5")
    void addPositiveNumbers() {
        int result = calculator.add(2, 3);
        assertEquals(5, result, "2 + 3 should equal 5");
    }

    @Test
    @DisplayName("add: -4 + -6 = -10")
    void addNegativeNumbers() {
        int result = calculator.add(-4, -6);
        assertEquals(-10, result, "-4 + -6 should equal -10");
    }

    @Test
    @DisplayName("add: 7 + 0 = 7")
    void addWithZero() {
        int result = calculator.add(7, 0);
        assertEquals(7, result, "7 + 0 should equal 7");
    }

    @Test
    @DisplayName("add: MAX_VALUE + 0 = MAX_VALUE (no overflow)")
    void addWithIntegerMaxValue() {
        int result = calculator.add(Integer.MAX_VALUE, 0);
        assertEquals(Integer.MAX_VALUE, result,
                "MAX_VALUE + 0 should equal MAX_VALUE");
    }

    @Test
    @DisplayName("add: MIN_VALUE + 0 = MIN_VALUE (no overflow)")
    void addWithIntegerMinValue() {
        int result = calculator.add(Integer.MIN_VALUE, 0);
        assertEquals(Integer.MIN_VALUE, result,
                "MIN_VALUE + 0 should equal MIN_VALUE");
    }
}
