package com.example.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("gpt")
class CalculatorTests {

    private final Calculator calculator = new Calculator();

    @Test
    @DisplayName("1 + 1 = 2")
    void add_twoPositiveNumbers_returnsSum() {
        int result = calculator.add(1, 1);
        assertEquals(2, result);
    }
}
