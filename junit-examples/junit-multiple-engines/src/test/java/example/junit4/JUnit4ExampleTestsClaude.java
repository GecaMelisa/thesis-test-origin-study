package example.junit4;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import example.SampleMath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JUnit 4 style tests to be executed via the Vintage engine (Claude-generated).
 */
@Category(ClaudeTag.class)
public class JUnit4ExampleTestsClaude {

    @Test
    public void addTwoPositiveNumbers() {
        SampleMath math = new SampleMath();
        assertEquals("3 + 4 should be 7", 7, math.add(3, 4));
    }

    @Test
    public void addNegativeNumbers() {
        SampleMath math = new SampleMath();
        assertEquals("-2 + -3 should be -5", -5, math.add(-2, -3));
    }

    @Test
    public void addWithZero() {
        SampleMath math = new SampleMath();
        assertEquals("5 + 0 should be 5", 5, math.add(5, 0));
    }

    @Test
    public void clampReturnsValueInRange() {
        SampleMath math = new SampleMath();
        assertEquals("5 clamped to [0,10] should be 5", 5, math.clamp(5, 0, 10));
    }

    @Test
    public void clampReturnsBoundaryMin() {
        SampleMath math = new SampleMath();
        assertEquals("-5 clamped to [0,10] should be 0", 0, math.clamp(-5, 0, 10));
    }

    @Test
    public void clampReturnsBoundaryMax() {
        SampleMath math = new SampleMath();
        assertEquals("15 clamped to [0,10] should be 10", 10, math.clamp(15, 0, 10));
    }

    @Test
    public void stringLengthIsCorrect() {
        String value = "claude";
        int length = value.length();
        assertEquals("Length of 'claude' should be 6", 6, length);
    }

    @Test
    public void listIsNotEmpty() {
        java.util.List<Integer> numbers = java.util.Arrays.asList(1, 2, 3);
        assertFalse("List should not be empty", numbers.isEmpty());
    }
}
