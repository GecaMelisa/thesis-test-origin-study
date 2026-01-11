package example.junit4;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(CodexTag.class)
public class JUnit4ExampleTestsCodex {

	@Test
	public void stringReverseIsInvolution() {
		String original = "Vintage";
		String reversed = new StringBuilder(original).reverse().toString();
		String doubleReversed = new StringBuilder(reversed).reverse().toString();

		assertTrue("Double reverse should recover original", original.equals(doubleReversed) && reversed.length() == original.length());
	}

	@Test
	public void listSizeMatchesElements() {
		java.util.List<Integer> numbers = java.util.Arrays.asList(2, 4, 6, 8);
		assertTrue("List size should match element count", numbers.size() == 4 && numbers.contains(8));
	}
}
