package com.example.project

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

@Tag("codex")
class CalculatorTestCodex {

  private def newCalculator(): Calculator = new Calculator

  @Test
  def addsPositiveNumbersCommutatively(): Unit = {
    val forward = newCalculator().add(9, 8)
    val reverse = newCalculator().add(8, 9)

    assertAll(
      () => assertEquals(17, forward, "9 + 8 must equal 17"),
      () => assertEquals(forward, reverse, "Addition should be commutative"),
      () => assertTrue(forward > 0 && reverse > 0, "Positive inputs must yield positive sums")
    )
  }

  @Test
  def addsMixedSignsSymmetrically(): Unit = {
    val result = newCalculator().add(-12, 5)
    val reversed = newCalculator().add(5, -12)

    assertAll(
      () => assertEquals(-7, result, "Dominant negative magnitude should keep sum negative"),
      () => assertEquals(result, reversed, "Order should not change mixed-sign sum"),
      () => assertTrue(result < 0, "Result should be negative")
    )
  }

  @Test
  def zeroActsAsAdditiveIdentity(): Unit = {
    val base = 42
    val left = newCalculator().add(0, base)
    val right = newCalculator().add(base, 0)

    assertAll(
      () => assertEquals(base, left, "0 + n must equal n"),
      () => assertEquals(base, right, "n + 0 must equal n"),
      () => assertEquals(left, right, "Identity must be position independent")
    )
  }

  @Test
  def overflowAndUnderflowWrap(): Unit = {
    val overflow = newCalculator().add(Int.MaxValue, 1)
    val underflow = newCalculator().add(Int.MinValue, -1)

    assertAll(
      () => assertEquals(Int.MinValue, overflow, "MaxValue + 1 should wrap to MinValue"),
      () => assertEquals(Int.MaxValue, underflow, "MinValue - 1 should wrap to MaxValue"),
      () => assertTrue(overflow < 0 && underflow > 0, "Wrapped values should flip sign")
    )
  }

  @Test
  def dividesWithDoublePrecisionAndThrowsOnZero(): Unit = {
    val positive = newCalculator().div(7, 2)
    val negative = newCalculator().div(-7, 2)

    assertAll(
      () => assertEquals(3.5d, positive, 1e-9, "7 / 2 should yield 3.5"),
      () => assertEquals(-3.5d, negative, 1e-9, "-7 / 2 should yield -3.5")
    )

    val exec: Executable = () => newCalculator().div(1, 0)
    val thrown = assertThrows(classOf[IllegalArgumentException], exec, "Division by zero must throw")
    assertTrue(thrown.getMessage.contains("Division by zero"), "Exception message should mention division by zero")
  }
}
