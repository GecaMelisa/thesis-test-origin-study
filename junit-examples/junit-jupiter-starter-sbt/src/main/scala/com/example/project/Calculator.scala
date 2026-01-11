package com.example.project

class Calculator {

  def add(a: Int, b: Int): Int =
    a + b

  def div(a: Int, b: Int): Double = {
    require(b != 0, "Division by zero is not allowed")
    a.toDouble / b.toDouble
  }
}
