package com.example.project;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("codex")
class CalculatorTestCodex {

	private final Calculator calculator = new Calculator();

	@Test
	void addsPositiveNumbersAndIsCommutative() {
		int forward = calculator.add(12, 5);
		int reverse = calculator.add(5, 12);

		assertTrue(forward == 17 && reverse == forward && forward > 0 && reverse > 0,
				"Positive operands should sum to 17, be commutative, and stay positive");
	}

	@Test
	void addsMixedSignsWithoutOrderEffect() {
		int result = calculator.add(-9, 4);
		int reversed = calculator.add(4, -9);

		assertTrue(result == -5 && reversed == result && result < 0,
				"Mixed signs should yield -5, be order-independent, and remain negative");
	}

	@Test
	void zeroActsAsAdditiveIdentityOnBothSides() {
		int base = 27;

		int leftZero = calculator.add(0, base);
		int rightZero = calculator.add(base, 0);

		assertTrue(leftZero == base && rightZero == base && leftZero == rightZero,
				"Zero must leave the value unchanged regardless of position");
	}

	@Test
	void detectsOverflowAndUnderflowWrapping() {
		int overflow = calculator.add(Integer.MAX_VALUE, 1);
		int underflow = calculator.add(Integer.MIN_VALUE, -1);

		assertTrue(overflow == Integer.MIN_VALUE && underflow == Integer.MAX_VALUE
				&& overflow < 0 && underflow > 0,
				"Overflow/underflow should wrap (MAX+1 -> MIN, MIN-1 -> MAX) with expected signs");
	}
}
