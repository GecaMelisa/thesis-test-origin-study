package com.example.cartesian;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("claude")
class CartesianProductResolverTestsClaude {

    private CartesianProductResolver newResolver(List<?> parameters) {
        return new CartesianProductResolver(parameters);
    }

    @Test
    void supportsParameter_returnsTrue_whenIndexWithinBounds() {
        List<Integer> parameters = Arrays.asList(1, 2, 3);
        CartesianProductResolver resolver = newResolver(parameters);
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.getIndex()).thenReturn(0);
        assertTrue(resolver.supportsParameter(parameterContext, extensionContext),
                "Index 0 should be supported");

        when(parameterContext.getIndex()).thenReturn(2);
        assertTrue(resolver.supportsParameter(parameterContext, extensionContext),
                "Index 2 (last valid) should be supported");
    }

    @Test
    void supportsParameter_returnsFalse_whenIndexEqualsOrExceedsSize() {
        List<String> parameters = Arrays.asList("a", "b");
        CartesianProductResolver resolver = newResolver(parameters);
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.getIndex()).thenReturn(2);
        assertFalse(resolver.supportsParameter(parameterContext, extensionContext),
                "Index equal to size should not be supported");

        when(parameterContext.getIndex()).thenReturn(5);
        assertFalse(resolver.supportsParameter(parameterContext, extensionContext),
                "Index greater than size should not be supported");
    }

    @Test
    void supportsParameter_returnsFalse_forEmptyParameterList() {
        CartesianProductResolver resolver = newResolver(Collections.emptyList());
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.getIndex()).thenReturn(0);
        assertFalse(resolver.supportsParameter(parameterContext, extensionContext),
                "Empty parameter list should not support any index");
    }

    @Test
    void resolveParameter_returnsElementAtGivenIndex() {
        List<String> parameters = Arrays.asList("first", "second", "third");
        CartesianProductResolver resolver = newResolver(parameters);
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.getIndex()).thenReturn(0);
        assertEquals("first", resolver.resolveParameter(parameterContext, extensionContext));

        when(parameterContext.getIndex()).thenReturn(1);
        assertEquals("second", resolver.resolveParameter(parameterContext, extensionContext));

        when(parameterContext.getIndex()).thenReturn(2);
        assertEquals("third", resolver.resolveParameter(parameterContext, extensionContext));
    }

    @Test
    void resolveParameter_throwsIndexOutOfBounds_whenIndexTooHigh() {
        List<String> parameters = List.of("only");
        CartesianProductResolver resolver = newResolver(parameters);
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.getIndex()).thenReturn(1);

        assertThrows(IndexOutOfBoundsException.class,
                () -> resolver.resolveParameter(parameterContext, extensionContext),
                "Accessing index beyond list size should throw IndexOutOfBoundsException");
    }

    @Test
    void resolveParameter_returnsNullElement_whenListContainsNull() {
        List<String> parameters = Arrays.asList("a", null, "c");
        CartesianProductResolver resolver = newResolver(parameters);
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.getIndex()).thenReturn(1);
        assertNull(resolver.resolveParameter(parameterContext, extensionContext),
                "Should return null when the element at the index is null");
    }
}
