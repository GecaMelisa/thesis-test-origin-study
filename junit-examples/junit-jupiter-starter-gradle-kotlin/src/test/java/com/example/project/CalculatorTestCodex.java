package com.example.project;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("codex")
class CalculatorTestCodex {

	private Calculator newCalculator() {
		return new Calculator();
	}

	@Test
	void addsPositiveNumbersCommutatively() {
		int forward = newCalculator().add(11, 6);
		int reverse = newCalculator().add(6, 11);

		assertTrue(forward == 17 && reverse == 17 && forward == reverse && forward > 0 && reverse > 0,
				"Positive addition should total 17, be commutative, and remain positive");
	}

	@Test
	void addsMixedSignsConsistently() {
		int result = newCalculator().add(-12, 5);
		int reversed = newCalculator().add(5, -12);

		assertTrue(result == -7 && reversed == -7 && result == reversed && result < 0,
				"Mixed signs should yield -7, be order-independent, and stay negative when negative magnitude dominates");
	}

	@Test
	void zeroActsAsIdentity() {
		int base = 42;
		int leftZero = newCalculator().add(0, base);
		int rightZero = newCalculator().add(base, 0);

		assertTrue(leftZero == base && rightZero == base && leftZero == rightZero,
				"Zero must act as additive identity from either side");
	}

	@Test
	void handlesOverflowAndUnderflowWrapping() {
		int overflow = newCalculator().add(Integer.MAX_VALUE, 1);
		int underflow = newCalculator().add(Integer.MIN_VALUE, -1);

		assertTrue(overflow == Integer.MIN_VALUE && underflow == Integer.MAX_VALUE
				&& overflow < 0 && underflow > 0,
				"Overflow/underflow should wrap (MAX+1 -> MIN, MIN-1 -> MAX) with expected signs");
	}

	@Test
	void dividesWithIntegerSemantics() {
		double positive = newCalculator().div(7, 3);
		double negative = newCalculator().div(-7, 3);

		boolean ok = Math.abs(positive - 2.0) < 1e-9
				&& Math.abs(negative + 2.0) < 1e-9;
		assertTrue(ok, "Division should use integer division then scale to double (7/3 -> 2.0, -7/3 -> -2.0)");
	}

	@Test
	void divisionByZeroThrowsAssertionOrArithmetic() {
		Throwable thrown = assertThrows(Throwable.class, () -> newCalculator().div(1, 0));
		assertTrue(thrown instanceof AssertionError || thrown instanceof ArithmeticException,
				"Division by zero must throw AssertionError (with -ea) or ArithmeticException");
	}
}
