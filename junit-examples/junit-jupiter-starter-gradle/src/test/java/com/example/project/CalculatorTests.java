package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Calculator} in the junit-jupiter-starter-gradle module.
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
    @DisplayName("-5 + -3 = -8")
    void add_twoNegativeNumbers_returnsSum() {
        int result = calculator.add(-5, -3);
        assertEquals(-8, result, "-5 + -3 should equal -8");
    }

    @Test
    @DisplayName("0 + n = n (zero on left)")
    void add_zeroOnLeft_returnsNumber() {
        int result = calculator.add(0, 7);

        assertEquals(7, result, "0 + 7 should equal 7");
    }

    @Test
    @DisplayName("n + 0 = n (zero on right)")
    void add_zeroOnRight_returnsNumber() {
        int result = calculator.add(9, 0);

        assertEquals(9, result, "9 + 0 should equal 9");
    }

    @Test
    @DisplayName("Mixed signs: 10 + (-3) = 7")
    void add_positiveAndNegative_returnsSum() {
        int result = calculator.add(10, -3);
        assertEquals(7, result, "10 + (-3) should equal 7");
    }

    @Test
    @DisplayName("Commutativity: a + b = b + a")
    void add_isCommutative() {
        int a = 4;
        int b = 9;

        int result1 = calculator.add(a, b);
        int result2 = calculator.add(b, a);

        assertEquals(result1, result2, "Addition should be commutative");
    }

    @Test
    @DisplayName("Associativity: (a + b) + c = a + (b + c)")
    void add_isAssociative() {
        int a = 2;
        int b = 3;
        int c = 4;

        int left = calculator.add(calculator.add(a, b), c);
        int right = calculator.add(a, calculator.add(b, c));

        assertEquals(left, right, "Addition should be associative");
    }

    @Test
    @DisplayName("Large numbers within int range")
    void add_largeNumbers_withinIntRange() {
        int a = 1_000_000_000;
        int b = 200_000_000;
        int result = calculator.add(a, b);

        assertEquals(1_200_000_000, result, "1,000,000,000 + 200,000,000 should equal 1,200,000,000");
    }

    // If your Calculator has other operations (subtract, multiply, divide),
    // you can add similar tests here, e.g.:
    //
    // @Test
    // void multiply_twoNumbers_returnsProduct() { ... }
    //
    // @Test
    // void divide_byZero_throwsException() { ... }
}