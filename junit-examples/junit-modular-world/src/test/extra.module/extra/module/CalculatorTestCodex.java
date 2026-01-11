package extra.module;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.tool.Calculator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("codex")
class CalculatorTestCodex {

	private Calculator newCalculator() {
		return new Calculator();
	}

	@Test
	void addsPositiveNumbersAndIsCommutative() {
		int sum = newCalculator().add(7, 8);
		assertTrue(sum == 15 && sum == newCalculator().add(8, 7) && sum > 0,
				"7+8 should be 15, commutative, and positive");
	}

	@Test
	void mixedSignsRemainOrderIndependent() {
		int sum = newCalculator().add(-9, 4);
		assertTrue(sum == -5 && sum == newCalculator().add(4, -9) && sum < 0,
				"Mixed signs should yield -5 and be order independent");
	}

	@Test
	void zeroActsAsIdentity() {
		int base = 42;
		int left = newCalculator().add(0, base);
		int right = newCalculator().add(base, 0);
		assertTrue(left == base && right == base && left == right,
				"Zero must be additive identity on both sides");
	}

	@Test
	void overflowAndUnderflowWrap() {
		int overflow = newCalculator().add(Integer.MAX_VALUE, 1);
		int underflow = newCalculator().add(Integer.MIN_VALUE, -1);
		assertTrue(overflow == Integer.MIN_VALUE && underflow == Integer.MAX_VALUE
				&& overflow < 0 && underflow > 0,
				"Overflow/underflow should wrap and flip sign as expected");
	}
}
