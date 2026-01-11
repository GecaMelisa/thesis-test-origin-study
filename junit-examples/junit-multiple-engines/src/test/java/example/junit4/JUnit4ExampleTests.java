package example.junit4;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Simple JUnit 4 style test to be executed via the Vintage engine.
 */
public class JUnit4ExampleTests {

    @Test
    @Category(GptTag.class)
    public void stringLengthIsCorrect() {
        String value = "junit4";

        int length = value.length();

        assertEquals("Length of 'junit4' should be 6", 6, length);
    }

    @Test
    @Category(GptTag.class)
    public void listIsNotEmpty() {
        java.util.List<Integer> numbers = java.util.Arrays.asList(1, 2, 3);

        assertFalse("List should not be empty", numbers.isEmpty());
    }
}
