package com.example.cartesian;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("claude")
class CartesianProductProviderTestsClaude {

    @SuppressWarnings("unchecked")
    private List<List<?>> invokeCartesianProduct(List<List<?>> lists) throws Exception {
        Method method = CartesianProductProvider.class.getDeclaredMethod("cartesianProduct", List.class);
        method.setAccessible(true);
        try {
            return (List<List<?>>) method.invoke(null, lists);
        }
        catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw ex;
        }
    }

    @Test
    void cartesianProduct_returnsSingleEmptyList_whenInputIsEmpty() throws Exception {
        List<List<?>> input = Collections.emptyList();

        List<List<?>> result = invokeCartesianProduct(input);

        assertEquals(1, result.size(), "Result must contain exactly one list");
        assertEquals(Collections.emptyList(), result.get(0), "That list must be empty");
    }

    @Test
    void cartesianProduct_returnsSingleElementLists_whenSingleListProvided() throws Exception {
        List<List<?>> input = Collections.singletonList(Arrays.asList("a", "b"));

        List<List<?>> result = invokeCartesianProduct(input);

        assertEquals(2, result.size());
        assertEquals(Collections.singletonList("a"), result.get(0));
        assertEquals(Collections.singletonList("b"), result.get(1));
    }

    @Test
    void cartesianProduct_returnsCartesianProduct_forTwoLists() throws Exception {
        List<?> first = Arrays.asList(1, 2);
        List<?> second = Arrays.asList("x", "y");
        List<List<?>> input = Arrays.asList(first, second);

        List<List<?>> result = invokeCartesianProduct(input);

        assertEquals(4, result.size(), "2 x 2 input must create 4 combinations");
        assertTrue(result.contains(Arrays.asList(1, "x")));
        assertTrue(result.contains(Arrays.asList(1, "y")));
        assertTrue(result.contains(Arrays.asList(2, "x")));
        assertTrue(result.contains(Arrays.asList(2, "y")));
    }

    @Test
    void cartesianProduct_handlesEmptyInnerList_byReturningEmptyResult() throws Exception {
        List<?> first = Arrays.asList(1, 2);
        List<?> empty = Collections.emptyList();
        List<List<?>> input = Arrays.asList(first, empty);

        List<List<?>> result = invokeCartesianProduct(input);

        assertTrue(result.isEmpty(), "Any empty inner list should make the product empty");
    }

    @Test
    void cartesianProduct_returnsCartesianProduct_forThreeLists() throws Exception {
        List<?> a = Arrays.asList("a", "b");
        List<?> b = Arrays.asList(1, 2);
        List<?> c = Arrays.asList(true, false);
        List<List<?>> input = Arrays.asList(a, b, c);

        List<List<?>> result = invokeCartesianProduct(input);

        assertEquals(8, result.size(), "2 x 2 x 2 combinations expected");
        assertTrue(result.contains(Arrays.asList("a", 1, true)));
        assertTrue(result.contains(Arrays.asList("a", 2, false)));
        assertTrue(result.contains(Arrays.asList("b", 1, true)));
        assertTrue(result.contains(Arrays.asList("b", 2, false)));
    }

    @Test
    void cartesianProduct_singleElementLists_returnsSingleCombination() throws Exception {
        List<?> a = Collections.singletonList("only");
        List<?> b = Collections.singletonList(42);
        List<List<?>> input = Arrays.asList(a, b);

        List<List<?>> result = invokeCartesianProduct(input);

        assertEquals(1, result.size(), "1 x 1 should produce 1 combination");
        assertEquals(Arrays.asList("only", 42), result.get(0));
    }

    @Test
    void cartesianProduct_throwsNullPointerException_whenInputIsNull() {
        assertThrows(NullPointerException.class,
                () -> invokeCartesianProduct(null),
                "Null input for lists should ultimately result in NullPointerException");
    }
}
