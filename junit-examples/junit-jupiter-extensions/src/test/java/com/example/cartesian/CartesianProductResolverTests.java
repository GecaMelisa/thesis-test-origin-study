package com.example.cartesian;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("gpt")
class CartesianProductResolverTests {

    private CartesianProductResolver newResolver(List<?> parameters) {
        return new CartesianProductResolver(parameters);
    }

    @Test
    void supportsParameter_returnsTrue_whenIndexWithinBounds() {
        // given
        List<Integer> parameters = Arrays.asList(1, 2, 3);
        CartesianProductResolver resolver = newResolver(parameters);
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.getIndex()).thenReturn(0, 2);

        // when / then
        assertTrue(resolver.supportsParameter(parameterContext, extensionContext),
                "Index 0 should be supported");
        assertTrue(resolver.supportsParameter(parameterContext, extensionContext),
                "Index 2 should be supported");
    }

    @Test
    void supportsParameter_returnsFalse_whenIndexEqualsOrExceedsSize() {
        // given
        List<String> parameters = Arrays.asList("a", "b");
        CartesianProductResolver resolver = newResolver(parameters);
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.getIndex()).thenReturn(2, 3);

        // when / then
        assertFalse(resolver.supportsParameter(parameterContext, extensionContext),
                "Index equal to size should not be supported");
        assertFalse(resolver.supportsParameter(parameterContext, extensionContext),
                "Index greater than size should not be supported");
    }

    @Test
    void resolveParameter_returnsElementAtGivenIndex_forValidIndex() {
        // given
        List<String> parameters = Arrays.asList("first", "second");
        CartesianProductResolver resolver = newResolver(parameters);
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.getIndex()).thenReturn(1);

        // when
        Object resolved = resolver.resolveParameter(parameterContext, extensionContext);

        // then
        assertEquals("second", resolved);
    }

    @Test
    void resolveParameter_throwsIndexOutOfBounds_whenIndexTooHigh() {
        // given
        List<String> parameters = List.of("only");
        CartesianProductResolver resolver = newResolver(parameters);
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);

        when(parameterContext.getIndex()).thenReturn(1);

        // when / then
        assertThrows(IndexOutOfBoundsException.class,
                () -> resolver.resolveParameter(parameterContext, extensionContext),
                "Accessing index beyond list size should throw IndexOutOfBoundsException");
    }
}
