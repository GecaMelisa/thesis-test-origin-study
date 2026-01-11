package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("codex")
class CalculatorTestCodex {

	private final Calculator calculator = new Calculator();

	@Test
	void shouldAddPositiveNumbersAndBeCommutative() {
		int leftToRight = calculator.add(7, 5);
		int rightToLeft = calculator.add(5, 7);

		assertEquals(12, leftToRight);
		assertEquals(leftToRight, rightToLeft, "Addition should be commutative");
		assertTrue(leftToRight > 0 && rightToLeft > 0, "Positive inputs must yield positive sums");
	}

	@Test
	void shouldHandleNegativeAndPositiveMix() {
		int result = calculator.add(-8, 3);

		assertEquals(-5, result);
		assertTrue(result < 0, "Sum should stay negative when magnitude of negative term is larger");
		assertEquals(calculator.add(3, -8), result, "Order should not change mixed-sign sums");
	}

	@Test
	void shouldPreserveValueWhenAddingZeroEitherSide() {
		int base = 42;

		assertEquals(base, calculator.add(base, 0));
		assertEquals(base, calculator.add(0, base));
		assertEquals(calculator.add(base, 0), calculator.add(0, base), "Zero should be additive identity");
	}

	@Test
	void shouldOverflowWithTwoLargeMagnitudes() {
		int overflow = calculator.add(Integer.MAX_VALUE, 1);
		int underflow = calculator.add(Integer.MIN_VALUE, -1);

		assertEquals(Integer.MIN_VALUE, overflow, "Max + 1 should wrap to Integer.MIN_VALUE");
		assertEquals(Integer.MAX_VALUE, underflow, "Min - 1 should wrap to Integer.MAX_VALUE");
		assertTrue(overflow < 0, "Overflowed sum should be negative");
		assertTrue(underflow > 0, "Underflowed sum should be positive");
	}
}


