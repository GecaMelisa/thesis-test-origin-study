package example.jqwik;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Tag;

@Tag("gpt")
public class JqwikExampleProperties {

	@Property
	void lengthMatchesGeneratedString(@ForAll String value) {
		assertEquals(value.length(), value.toCharArray().length, "Length should match char array size");
	}
}
