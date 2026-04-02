package example.jqwik;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import example.SampleMath;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Tag;
import net.jqwik.api.constraints.IntRange;

@Tag("claude")
public class JqwikExamplePropertiesClaude {

    @Property
    void lengthMatchesGeneratedString(@ForAll String value) {
        assertEquals(value.length(), value.toCharArray().length,
                "Length should match char array size");
    }

    @Property
    void concatenationStartsWithLeft(@ForAll String left, @ForAll String right) {
        String combined = left + right;
        assertTrue(combined.startsWith(left),
                "Concatenation should start with left operand");
    }

    @Property
    void addIsCommutative(@ForAll int a, @ForAll int b) {
        SampleMath math = new SampleMath();
        assertEquals(math.add(a, b), math.add(b, a),
                "Addition should be commutative");
    }

    @Property
    void clampNeverExceedsBounds(
            @ForAll int value,
            @ForAll @IntRange(min = -100, max = 0) int min,
            @ForAll @IntRange(min = 1, max = 100) int max) {
        SampleMath math = new SampleMath();
        int result = math.clamp(value, min, max);
        assertTrue(result >= min, "Clamped result should be >= min");
        assertTrue(result <= max, "Clamped result should be <= max");
    }
}
