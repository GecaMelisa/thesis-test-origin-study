package example.jupiter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("gpt")
class JupiterExampleTests {

	@Test
	@DisplayName("adds integers in a simple example")
	void addsIntegers() {
		assertEquals(7, 3 + 4, "3 + 4 should equal 7");
	}
}
