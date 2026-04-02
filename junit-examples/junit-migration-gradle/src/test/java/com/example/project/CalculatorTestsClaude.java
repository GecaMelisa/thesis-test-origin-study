package com.example.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("claude")
class CalculatorTestsClaude {

    private final Calculator calculator = new Calculator();

    // --- Positive numbers ---

    @Test
    @DisplayName("add() 2 + 3 = 5")
    void addTwoPositiveNumbers() {
        assertEquals(5, calculator.add(2, 3),
                "Adding 2 and 3 should result in 5");
    }

    @Test
    @DisplayName("add() 40 + 2 = 42")
    void addFortyAndTwo() {
        assertEquals(42, calculator.add(40, 2),
                "Adding 40 and 2 should result in 42");
    }

    @Test
    @DisplayName("add() 70 + 30 = 100")
    void addSeventyAndThirty() {
        assertEquals(100, calculator.add(70, 30),
                "Adding 70 and 30 should result in 100");
    }

    // --- Zero cases ---

    @Test
    @DisplayName("add() 5 + 0 = 5")
    void addWithZeroRight() {
        assertEquals(5, calculator.add(5, 0),
                "Adding 0 to 5 should keep the value 5");
    }

    @Test
    @DisplayName("add() 0 + 5 = 5")
    void addWithZeroLeft() {
        assertEquals(5, calculator.add(0, 5),
                "Adding 5 to 0 should result in 5");
    }

    @Test
    @DisplayName("add() 0 + 0 = 0")
    void addZeroAndZero() {
        assertEquals(0, calculator.add(0, 0),
                "Adding 0 and 0 should result in 0");
    }

    // --- Negative numbers ---

    @Test
    @DisplayName("add() -3 + 2 = -1")
    void addNegativeAndPositive() {
        assertEquals(-1, calculator.add(-3, 2),
                "Adding -3 and 2 should result in -1");
    }

    @Test
    @DisplayName("add() -2 + -3 = -5")
    void addTwoNegativeNumbers() {
        assertEquals(-5, calculator.add(-2, -3),
                "Adding -2 and -3 should result in -5");
    }

    @Test
    @DisplayName("add() 5 + -3 = 2")
    void addPositiveAndNegative() {
        assertEquals(2, calculator.add(5, -3),
                "Adding 5 and -3 should result in 2");
    }

    // --- Boundary cases ---

    @Test
    @DisplayName("add() MAX_VALUE + 0 = MAX_VALUE")
    void addMaxAndZero() {
        assertEquals(Integer.MAX_VALUE, calculator.add(Integer.MAX_VALUE, 0),
                "Adding 0 to Integer.MAX_VALUE should keep the same value");
    }

    @Test
    @DisplayName("add() MIN_VALUE + 0 = MIN_VALUE")
    void addMinAndZero() {
        assertEquals(Integer.MIN_VALUE, calculator.add(Integer.MIN_VALUE, 0),
                "Adding 0 to Integer.MIN_VALUE should keep the same value");
    }

    @Test
    @DisplayName("add() MAX_VALUE + MIN_VALUE = -1")
    void addMaxAndMin() {
        assertEquals(-1, calculator.add(Integer.MAX_VALUE, Integer.MIN_VALUE),
                "Adding Integer.MAX_VALUE and Integer.MIN_VALUE should result in -1");
    }

    // --- Commutativity ---

    @Test
    @DisplayName("add() is commutative: a + b = b + a")
    void addIsCommutative() {
        assertEquals(calculator.add(7, 3), calculator.add(3, 7),
                "Addition should be commutative");
    }
}
