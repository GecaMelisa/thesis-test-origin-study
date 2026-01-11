package example.jqwik;

import static org.junit.jupiter.api.Assertions.assertTrue;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Tag;

@Tag("human")
class HumanJqwikExampleProperties {

	@Property
	void concatenationStartsWithLeft(@ForAll String left, @ForAll String right) {
		String combined = left + right;
		assertTrue(combined.startsWith(left), "Concatenation should start with left operand");
	}
}
