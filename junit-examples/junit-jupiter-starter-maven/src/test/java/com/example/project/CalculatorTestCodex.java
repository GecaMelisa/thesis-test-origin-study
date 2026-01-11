package com.example.project;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("codex")
class CalculatorTestCodex {

	private Calculator newCalculator() {
		return new Calculator();
	}

	@Test
	void addsPositiveNumbersAndIsCommutative() {
		int forward = newCalculator().add(8, 9);
		int reverse = newCalculator().add(9, 8);

		assertAll(
				() -> assertEquals(17, forward, "8 + 9 must equal 17"),
				() -> assertEquals(forward, reverse, "Addition should be commutative"),
				() -> assertTrue(forward > 0 && reverse > 0, "Positive inputs yield positive sums")
		);
	}

	@Test
	void addsMixedSignsSymmetrically() {
		int result = newCalculator().add(-15, 6);
		int reversed = newCalculator().add(6, -15);

		assertAll(
				() -> assertEquals(-9, result, "Dominant negative term keeps sum negative"),
				() -> assertEquals(result, reversed, "Order should not affect mixed-sign sums"),
				() -> assertTrue(result < 0, "Result should be negative")
		);
	}

	@Test
	void zeroActsAsIdentityOnBothSides() {
		int base = 123;
		int leftZero = newCalculator().add(0, base);
		int rightZero = newCalculator().add(base, 0);

		assertAll(
				() -> assertEquals(base, leftZero, "0 + n must equal n"),
				() -> assertEquals(base, rightZero, "n + 0 must equal n"),
				() -> assertEquals(leftZero, rightZero, "Identity should be position-independent")
		);
	}

	@Test
	void handlesOverflowAndUnderflowWrapping() {
		int overflow = newCalculator().add(Integer.MAX_VALUE, 1);
		int underflow = newCalculator().add(Integer.MIN_VALUE, -1);

		assertAll(
				() -> assertEquals(Integer.MIN_VALUE, overflow, "MAX_VALUE + 1 should wrap to MIN_VALUE"),
				() -> assertEquals(Integer.MAX_VALUE, underflow, "MIN_VALUE - 1 should wrap to MAX_VALUE"),
				() -> assertTrue(overflow < 0 && underflow > 0, "Wrapped values should flip sign")
		);
	}
}
