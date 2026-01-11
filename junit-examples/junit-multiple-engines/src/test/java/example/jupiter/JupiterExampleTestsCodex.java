package example.jupiter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("codex")
class JupiterExampleTestsCodex {

	@Test
	@Tag("fast")
	@DisplayName("adds positives commutatively")
	void addsPositivesCommutatively() {
		int sum = 4 + 9;
		assertTrue(sum == 13 && sum == (9 + 4) && sum > 0,
				"Positive addition should be commutative and positive");
	}

	@Test
	@Tag("edge")
	@DisplayName("overflow and underflow wrap as ints")
	void overflowAndUnderflowWrap() {
		int overflow = Integer.MAX_VALUE + 1;
		int underflow = Integer.MIN_VALUE - 1;

		assertTrue(overflow == Integer.MIN_VALUE && underflow == Integer.MAX_VALUE,
				"int overflow/underflow should wrap between MIN and MAX");
	}
}
