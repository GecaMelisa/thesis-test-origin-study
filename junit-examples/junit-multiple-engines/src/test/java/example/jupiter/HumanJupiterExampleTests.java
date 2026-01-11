package example.jupiter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("human")
class HumanJupiterExampleTests {

	@Test
	@DisplayName("human 1 + 1 = 2")
	void humanOnePlusOneEqualsTwo() {
		assertEquals(2, 1 + 1, "1 + 1 should equal 2");
	}

	@Test
	@DisplayName("human subtraction keeps negatives")
	void humanSubtractionKeepsNegatives() {
		assertEquals(-2, 3 - 5, "3 - 5 should equal -2");
	}
}
