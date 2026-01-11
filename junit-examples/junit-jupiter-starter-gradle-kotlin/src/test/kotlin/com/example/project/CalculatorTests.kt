package com.example.project

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("Calculator tests")
@Tag("gpt")
class CalculatorTests {

    private val calculator = Calculator()

    @Nested
    @DisplayName("add()")
    inner class AddTests {

        @ParameterizedTest
        @CsvSource(
                "1, 1, 2",
                "2, 3, 5",
                "-1, 1, 0",
                "-2, -3, -5",
                "0, 0, 0"
        )
        fun `add various integers`(a: Int, b: Int, expected: Int) {
            val result = calculator.add(a, b)
            assertEquals(expected, result)
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
            val result1 = calculator.div(-6, 3)
            val result2 = calculator.div(6, -3)
            val result3 = calculator.div(-6, -3)

            assertEquals(-2.0, result1, 0.0001)
            assertEquals(-2.0, result2, 0.0001)
            assertEquals(2.0, result3, 0.0001)
        }

        @Test
        fun `division uses integer division semantics`() {
            // Because of a / b * 1_0, 5 / 2 becomes (5 / 2) * 1_0 = 2_0
            val result = calculator.div(5, 2)
            assertEquals(2.0, result, 0.0001)
        }

        @Test
        fun `division by zero should fail via assertion`() {
            // NOTE: this test will only pass if JVM assertions are enabled (-ea).
            val exception = assertThrows(AssertionError::class.java) {
                calculator.div(1, 0)
            }
            assertTrue(exception.message?.contains("Division by Zero") == true)
        }
    }
}
