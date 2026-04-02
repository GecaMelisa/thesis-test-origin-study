package com.example.project

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("Calculator tests (Claude)")
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
                "0, 0, 0",
                "100, -100, 0"
        )
        fun `add various integers`(a: Int, b: Int, expected: Int) {
            val result = calculator.add(a, b)
            assertEquals(expected, result)
        }

        @Test
        fun `adding zero does not change the value`() {
            assertEquals(5, calculator.add(5, 0))
            assertEquals(5, calculator.add(0, 5))
            assertEquals(-3, calculator.add(-3, 0))
        }

        @Test
        fun `addition is commutative`() {
            assertEquals(calculator.add(3, 7), calculator.add(7, 3))
            assertEquals(calculator.add(-3, 7), calculator.add(7, -3))
        }

        @Test
        fun `large numbers within int range`() {
            val result = calculator.add(1_000_000_000, 200_000_000)
            assertEquals(1_200_000_000, result)
        }

        @Test
        fun `MAX_VALUE + MIN_VALUE = -1`() {
            assertEquals(-1, calculator.add(Int.MAX_VALUE, Int.MIN_VALUE))
        }
    }

    @Nested
    @DisplayName("div()")
    inner class DivTests {

        @Test
        fun `divide positive numbers`() {
            val result = calculator.div(4, 2)
            assertEquals(2.0, result, 0.0001)
        }

        @Test
        fun `divide negative and positive numbers`() {
            assertEquals(-2.0, calculator.div(-6, 3), 0.0001)
            assertEquals(-2.0, calculator.div(6, -3), 0.0001)
            assertEquals(2.0, calculator.div(-6, -3), 0.0001)
        }

        @Test
        fun `division uses integer division semantics`() {
            val result = calculator.div(5, 2)
            assertEquals(2.0, result, 0.0001)
        }

        @Test
        fun `division by zero should fail via assertion`() {
            val exception = assertThrows(AssertionError::class.java) {
                calculator.div(1, 0)
            }
            assertTrue(exception.message?.contains("Division by Zero") == true)
        }

        @Test
        fun `divide one by one`() {
            val result = calculator.div(1, 1)
            assertEquals(1.0, result, 0.0001)
        }
    }
}
