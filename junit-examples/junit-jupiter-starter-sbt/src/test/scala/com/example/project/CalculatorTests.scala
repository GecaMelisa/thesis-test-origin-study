package com.example.project

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("Calculator (Scala + JUnit Jupiter)")
@Tag("gpt")
class CalculatorTests {

  private val calculator = new Calculator

  @Nested
  @DisplayName("add(a, b)")
  class AddTests {

    @Test
    @DisplayName("adds two positive numbers")
    def addsTwoPositiveNumbers(): Unit = {
      val result = calculator.add(2, 5)
      assertEquals(7, result)
    }

    @Test
    @DisplayName("is commutative: a + b == b + a")
    def addIsCommutative(): Unit = {
      assertAll(
        () => assertEquals(calculator.add(1, 2), calculator.add(2, 1)),
        () => assertEquals(calculator.add(-3, 7), calculator.add(7, -3)),
        () => assertEquals(calculator.add(0, 5), calculator.add(5, 0))
      )
    }

    @Test
    @DisplayName("adding zero does not change the value")
    def addingZeroDoesNotChangeValue(): Unit = {
      assertAll(
        () => assertEquals(5, calculator.add(5, 0)),
        () => assertEquals(-4, calculator.add(-4, 0)),
        () => assertEquals(0, calculator.add(0, 0))
      )
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource(
      Array(
        "1, 1, 2",
        "0, 5, 5",
        "-3, 7, 4",
        "10, -2, 8"
      )
    )
    @DisplayName("works for several input combinations (parameterized)")
    def addParameterized(a: Int, b: Int, expected: Int): Unit = {
      val result = calculator.add(a, b)
      assertEquals(expected, result)
    }
  }
}
