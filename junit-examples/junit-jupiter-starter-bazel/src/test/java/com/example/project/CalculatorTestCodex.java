package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("codex")
class CalculatorTestCodex {

	private final Calculator calculator = new Calculator();

	@Test
	void shouldAddPositiveNumbersAndRemainCommutative() {
		int leftToRight = calculator.add(9, 4);
		int rightToLeft = calculator.add(4, 9);

		assertEquals(13, leftToRight);
		assertEquals(leftToRight, rightToLeft, "Addition must be commutative for positives");
		assertTrue(leftToRight > 0 && rightToLeft > 0, "Positive inputs should yield positive outputs");
	}

	@Test
	void shouldHandleMixedSignsWithoutOrderEffects() {
		int result = calculator.add(-6, 2);
		int reversed = calculator.add(2, -6);

		assertEquals(-4, result);
		assertEquals(result, reversed, "Order should not affect mixed sign sums");
		assertTrue(result < 0, "Dominant negative term should keep sum negative");
	}

	@Test
	void shouldKeepValueWhenAddingZeroOnEitherSide() {
		int base = 21;

		int leftZero = calculator.add(0, base);
		int rightZero = calculator.add(base, 0);

		assertEquals(base, leftZero);
		assertEquals(base, rightZero);
		assertEquals(leftZero, rightZero, "Zero must act as additive identity from either side");
	}

	@Test
	void shouldReflectIntegerOverflowAndUnderflowWrapping() {
		int overflow = calculator.add(Integer.MAX_VALUE, 1);
		int underflow = calculator.add(Integer.MIN_VALUE, -1);

		assertEquals(Integer.MIN_VALUE, overflow, "Max + 1 should wrap to Integer.MIN_VALUE");
		assertEquals(Integer.MAX_VALUE, underflow, "Min - 1 should wrap to Integer.MAX_VALUE");
		assertTrue(overflow < 0, "Overflowed sum should be negative");
		assertTrue(underflow > 0, "Underflowed sum should be positive");
	}
}
