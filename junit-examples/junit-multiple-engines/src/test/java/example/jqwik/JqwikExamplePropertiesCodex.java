package example.jqwik;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tag;

@Tag("codex")
class JqwikExamplePropertiesCodex {

	@Property
	void reverseIsInvolution(@ForAll("latinStrings") String text) {
		String reversed = new StringBuilder(text).reverse().toString();
		String doubleReversed = new StringBuilder(reversed).reverse().toString();

		assertTrue(text.equals(doubleReversed) && reversed.equals(new StringBuilder(doubleReversed).reverse().toString()),
				"Double reverse should return original, and reversing again should yield first reverse");
	}

	@Property
	void concatenationLengthMatchesSum(@ForAll("latinStrings") String a, @ForAll("latinStrings") String b) {
		String concatenated = a + b;
		boolean lengthMatches = concatenated.length() == a.length() + b.length();
		assertTrue(lengthMatches, "Length of concatenation must equal sum of operands");
	}

	@Property
	void upperCaseIsIdempotent(@ForAll("latinStrings") String text) {
		String upperOnce = text.toUpperCase(Locale.ROOT);
		String upperTwice = upperOnce.toUpperCase(Locale.ROOT);
		boolean noLowerCase = upperOnce.chars().noneMatch(Character::isLowerCase);

		assertTrue(upperOnce.equals(upperTwice) && noLowerCase,
				"Uppercasing should be idempotent and contain no lowercase letters");
	}

	@Provide
	Arbitrary<String> latinStrings() {
		return Arbitraries.strings()
				.withChars("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
				.ofMinLength(0)
				.ofMaxLength(50);
	}
}
