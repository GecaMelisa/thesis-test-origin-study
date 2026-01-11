package example.junit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(HumanTag.class)
public class HumanJUnit4ExampleTests {

    @Test
    public void humanStringTrimKeepsContent() {
        String value = "  human  ";
        assertEquals("Trim should remove surrounding spaces", "human", value.trim());
    }

    @Test
    public void humanListContainsExpectedValue() {
        List<Integer> values = Arrays.asList(3, 6, 9);
        assertTrue("List should contain 6", values.contains(6));
    }
}
