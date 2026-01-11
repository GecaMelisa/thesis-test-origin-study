package com.example.project

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("codex")
class CalculatorTestCodex {

    private fun newCalculator() = Calculator()

    @Test
    fun `adds positive numbers and is commutative`() {
        val forward = newCalculator().add(10, 7)
        val reverse = newCalculator().add(7, 10)

        assertAll(
            { assertEquals(17, forward, "10 + 7 should equal 17") },
            { assertEquals(forward, reverse, "Addition must be commutative") },
            { assertTrue(forward > 0 && reverse > 0, "Positive inputs should produce positive sums") }
        )
    }

    @Test
    fun `adds mixed signs symmetrically`() {
        val result = newCalculator().add(-9, 4)
        val reversed = newCalculator().add(4, -9)

        assertAll(
            { assertEquals(-5, result, "Dominant negative magnitude should keep sum negative") },
            { assertEquals(result, reversed, "Order must not change mixed-sign sums") },
            { assertTrue(result < 0, "Result should be negative") }
        )
    }

    @Test
    fun `zero acts as identity either side`() {
        val base = 64
        val leftZero = newCalculator().add(0, base)
        val rightZero = newCalculator().add(base, 0)

        assertAll(
            { assertEquals(base, leftZero, "0 + n must equal n") },
            { assertEquals(base, rightZero, "n + 0 must equal n") },
            { assertEquals(leftZero, rightZero, "Identity must be position-independent") }
        )
    }

    @Test
    fun `handles overflow and underflow wrapping`() {
        val overflow = newCalculator().add(Int.MAX_VALUE, 1)
        val underflow = newCalculator().add(Int.MIN_VALUE, -1)

        assertAll(
            { assertEquals(Int.MIN_VALUE, overflow, "MAX_VALUE + 1 should wrap to MIN_VALUE") },
            { assertEquals(Int.MAX_VALUE, underflow, "MIN_VALUE - 1 should wrap to MAX_VALUE") },
            { assertTrue(overflow < 0 && underflow > 0, "Wrapped values should flip sign as expected") }
        )
    }
}
