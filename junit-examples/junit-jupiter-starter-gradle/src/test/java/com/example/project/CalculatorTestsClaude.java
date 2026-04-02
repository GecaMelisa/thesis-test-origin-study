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
    @DisplayName("-5 + -3 = -8")
    void addTwoNegativeNumbers() {
        int result = calculator.add(-5, -3);
        assertEquals(-8, result, "-5 + -3 should equal -8");
    }

    @Test
    @DisplayName("0 + n = n (zero on left)")
    void addZeroOnLeft() {
        int result = calculator.add(0, 7);
        assertEquals(7, result, "0 + 7 should equal 7");
    }

    @Test
    @DisplayName("n + 0 = n (zero on right)")
    void addZeroOnRight() {
        int result = calculator.add(9, 0);
        assertEquals(9, result, "9 + 0 should equal 9");
    }

    @Test
    @DisplayName("Mixed signs: 10 + (-3) = 7")
    void addPositiveAndNegative() {
        int result = calculator.add(10, -3);
        assertEquals(7, result, "10 + (-3) should equal 7");
    }

    @Test
    @DisplayName("Commutativity: a + b = b + a")
    void addIsCommutative() {
        int a = 4;
        int b = 9;
        assertEquals(calculator.add(a, b), calculator.add(b, a),
                "Addition should be commutative");
    }

    @Test
    @DisplayName("Associativity: (a + b) + c = a + (b + c)")
    void addIsAssociative() {
        int a = 2, b = 3, c = 4;
        int left = calculator.add(calculator.add(a, b), c);
        int right = calculator.add(a, calculator.add(b, c));
        assertEquals(left, right, "Addition should be associative");
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "0, 0, 0",
            "1, 2, 3",
            "49, 51, 100",
            "-5, -5, -10",
            "-2, 3, 1",
            "100, -100, 0"
    })
    void addParameterized(int first, int second, int expectedResult) {
        int result = calculator.add(first, second);
        assertEquals(expectedResult, result,
                () -> first + " + " + second + " should equal " + expectedResult);
    }

    @Test
    @DisplayName("Large numbers within int range")
    void addLargeNumbers() {
        int result = calculator.add(1_000_000_000, 200_000_000);
        assertEquals(1_200_000_000, result,
                "1,000,000,000 + 200,000,000 should equal 1,200,000,000");
    }

    @Test
    @DisplayName("MAX_VALUE + MIN_VALUE = -1")
    void addMaxAndMin() {
        assertEquals(-1, calculator.add(Integer.MAX_VALUE, Integer.MIN_VALUE),
                "MAX_VALUE + MIN_VALUE should equal -1");
    }
}
