package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("codex")
class CalculatorTestCodex {

	private final Calculator calculator = new Calculator();

	@Test
	void addsPositiveNumbers_returnsExpectedSum() {
		int sumForward = calculator.add(6, 8);

		assertEquals(14, sumForward, "6 + 8 should equal 14");
	}

	@Test
	void addsPositiveNumbers_commutative() {
		int sumForward = calculator.add(6, 8);
		int sumReverse = calculator.add(8, 6);

		assertEquals(sumForward, sumReverse, "Addition should be commutative");
	}

	@Test
	void addsPositiveNumbers_resultPositive() {
		int sum = calculator.add(6, 8);

		assertTrue(sum > 0, "Positive operands must yield a positive sum");
	}

	@Test
	void addsMixedSigns_returnsExpectedSum() {
		int result = calculator.add(-10, 4);

		assertEquals(-6, result, "-10 + 4 should equal -6");
	}

	@Test
	void addsMixedSigns_commutative() {
		int result = calculator.add(-10, 4);
		int reversed = calculator.add(4, -10);

		assertEquals(result, reversed, "Order should not matter for mixed-sign addition");
	}

	@Test
	void addsMixedSigns_negativeResult() {
		int result = calculator.add(-10, 4);

		assertTrue(result < 0, "Larger magnitude negative should keep sum negative");
	}

	@Test
	void zeroActsAsAdditiveIdentity_onLeft() {
		int value = 33;
		int leftZero = calculator.add(0, value);

		assertEquals(value, leftZero, "0 + value should equal value");
	}

	@Test
	void zeroActsAsAdditiveIdentity_onRight() {
		int value = 33;
		int rightZero = calculator.add(value, 0);

		assertEquals(value, rightZero, "value + 0 should equal value");
	}

	@Test
	void zeroActsAsAdditiveIdentity_sameResult() {
		int value = 33;
		int leftZero = calculator.add(0, value);
		int rightZero = calculator.add(value, 0);

		assertEquals(leftZero, rightZero, "Zero must be additive identity regardless of operand position");
	}

	@Test
	void integerOverflow_wrapsToMinValue() {
		int overflow = calculator.add(Integer.MAX_VALUE, 1);

		assertEquals(Integer.MIN_VALUE, overflow, "MAX_VALUE + 1 should wrap to MIN_VALUE");
	}

	@Test
	void integerUnderflow_wrapsToMaxValue() {
		int underflow = calculator.add(Integer.MIN_VALUE, -1);

		assertEquals(Integer.MAX_VALUE, underflow, "MIN_VALUE - 1 should wrap to MAX_VALUE");
	}

	@Test
	void integerOverflow_resultNegative() {
		int overflow = calculator.add(Integer.MAX_VALUE, 1);

		assertTrue(overflow < 0, "Overflowed result should be negative");
	}

	@Test
	void integerUnderflow_resultPositive() {
		int underflow = calculator.add(Integer.MIN_VALUE, -1);

		assertTrue(underflow > 0, "Underflowed result should be positive");
	}
}