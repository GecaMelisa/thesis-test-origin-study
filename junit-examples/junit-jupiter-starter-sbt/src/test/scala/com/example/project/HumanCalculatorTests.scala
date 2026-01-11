package com.example.project

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@Tag("human")
class HumanCalculatorTests {

  @Test
  def humanOnePlusOneEqualsTwo(): Unit = {
    val calculator = new Calculator
    assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2")
  }

  @ParameterizedTest(name = "human {0} + {1} = {2}")
  @CsvSource(
    Array(
      "0, 1, 1",
      "1, 2, 3",
      "49, 51, 100",
      "1, 100, 101"
    )
  )
  def humanAdd(first: Int, second: Int, expected: Int): Unit = {
    val calculator = new Calculator
    assertEquals(expected, calculator.add(first, second), s"$first + $second should equal $expected")
  }

  @Test
  def humanDivisionByZeroError(): Unit = {
    val calculator = new Calculator
    val thrown = assertThrows(classOf[IllegalArgumentException], () => calculator.div(1, 0))
    assertTrue(thrown.getMessage.contains("Division by zero"))
  }
}
