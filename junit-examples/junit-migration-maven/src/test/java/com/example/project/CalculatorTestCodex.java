package com.example.project;

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
		int sum = newCalculator().add(8, 9);
		assertTrue(sum == 17 && sum == newCalculator().add(9, 8) && sum > 0,
				"Positive addition should total 17, be commutative, and remain positive");
	}

	@Test
	void addsMixedSignsSymmetrically() {
		int sum = newCalculator().add(-12, 5);
		assertTrue(sum == -7 && sum == newCalculator().add(5, -12) && sum < 0,
				"Mixed signs should yield -7 and be order independent");
	}

	@Test
	void zeroActsAsIdentityEitherSide() {
		int base = 64;
		int left = newCalculator().add(0, base);
		int right = newCalculator().add(base, 0);
		assertTrue(left == base && right == base && left == right,
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
}
