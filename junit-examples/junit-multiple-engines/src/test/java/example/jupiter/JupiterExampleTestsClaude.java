package example.jupiter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import example.SampleMath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("claude")
class JupiterExampleTestsClaude {

    private final SampleMath math = new SampleMath();

    @Test
    @DisplayName("adds two positive integers")
    void addsTwoPositiveIntegers() {
        assertEquals(7, math.add(3, 4), "3 + 4 should equal 7");
    }

    @Test
    @DisplayName("adds negative integers")
    void addsNegativeIntegers() {
        assertEquals(-5, math.add(-2, -3), "-2 + -3 should equal -5");
    }

    @Test
    @DisplayName("adds zero keeps value unchanged")
    void addsZero() {
        assertEquals(5, math.add(5, 0), "5 + 0 should equal 5");
        assertEquals(5, math.add(0, 5), "0 + 5 should equal 5");
    }

    @Test
    @DisplayName("addition is commutative")
    void addIsCommutative() {
        assertEquals(math.add(3, 7), math.add(7, 3),
                "Addition should be commutative");
    }

    @Test
    @DisplayName("clamp returns value when within range")
    void clampReturnsValueWhenInRange() {
        assertEquals(5, math.clamp(5, 0, 10),
                "Value within range should be returned as-is");
    }

    @Test
    @DisplayName("clamp returns min when value is below range")
    void clampReturnsMinWhenBelowRange() {
        assertEquals(0, math.clamp(-5, 0, 10),
                "Value below min should be clamped to min");
    }

    @Test
    @DisplayName("clamp returns max when value is above range")
    void clampReturnsMaxWhenAboveRange() {
        assertEquals(10, math.clamp(15, 0, 10),
                "Value above max should be clamped to max");
    }

    @Test
    @DisplayName("clamp returns min when value equals min")
    void clampReturnsMinWhenValueEqualsMin() {
        assertEquals(0, math.clamp(0, 0, 10),
                "Value equal to min should be returned");
    }

    @Test
    @DisplayName("clamp returns max when value equals max")
    void clampReturnsMaxWhenValueEqualsMax() {
        assertEquals(10, math.clamp(10, 0, 10),
                "Value equal to max should be returned");
    }

    @Test
    @DisplayName("clamp works with negative range")
    void clampWorksWithNegativeRange() {
        assertEquals(-5, math.clamp(-10, -5, -1),
                "Value below negative min should be clamped to min");
        assertEquals(-3, math.clamp(-3, -5, -1),
                "Value within negative range should be returned as-is");
        assertEquals(-1, math.clamp(0, -5, -1),
                "Value above negative max should be clamped to max");
    }
}
