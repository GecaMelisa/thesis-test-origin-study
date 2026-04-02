package com.example.project

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("Calculator tests (Claude, Kotlin + JUnit Jupiter)")
@Tag("claude")
class CalculatorTestsClaude {

    private val calculator = Calculator()

    @Nested
    @DisplayName("add()")
    inner class AddTests {

        @ParameterizedTest(name = "{0} + {1} = {2}")
        @CsvSource(
            "1, 1, 2",
            "2, 3, 5",
            "-1, 1, 0",
            "-2, -3, -5",
            "0, 0, 0"
        )
        fun add_returns_sum(a: Int, b: Int, expected: Int) {
            val result = calculator.add(a, b)
            assertEquals(expected, result)
        }

        @Test
        fun add_is_commutative() {
            val r1 = calculator.add(1, 2)
            val r2 = calculator.add(2, 1)
            assertEquals(r1, r2)

            val r3 = calculator.add(-3, 7)
            val r4 = calculator.add(7, -3)
            assertEquals(r3, r4)
        }

        @Test
        fun adding_zero_does_not_change_value() {
            assertEquals(5, calculator.add(5, 0))
            assertEquals(5, calculator.add(0, 5))
            assertEquals(-3, calculator.add(-3, 0))
        }

        @Test
        fun add_handles_negative_result() {
            assertEquals(-1, calculator.add(2, -3), "2 + (-3) should equal -1")
        }

        @Test
        fun add_large_numbers() {
            assertEquals(1_200_000_000, calculator.add(1_000_000_000, 200_000_000))
        }

        @Test
        fun add_max_and_min_value() {
            assertEquals(-1, calculator.add(Int.MAX_VALUE, Int.MIN_VALUE))
        }
    }
}
