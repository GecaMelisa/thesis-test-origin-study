package com.example.cartesian;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.extension.Extension;

import static org.junit.jupiter.api.Assertions.*;

@Tag("claude")
class CartesianProductContextTestsClaude {

    @Test
    void getDisplayName_includesInvocationIndexAndParameters_forNonEmptyParameters() {
        List<Object> parameters = Arrays.asList("foo", 42);
        CartesianProductContext context = new CartesianProductContext(parameters);

        String displayName = context.getDisplayName(1);

        assertEquals("1: [foo, 42]", displayName);
    }

    @Test
    void getDisplayName_includesInvocationIndexAndEmptyList_forEmptyParameters() {
        List<Object> parameters = Collections.emptyList();
        CartesianProductContext context = new CartesianProductContext(parameters);

        String displayName = context.getDisplayName(0);

        assertEquals("0: []", displayName);
    }

    @Test
    void getDisplayName_withSingleParameter_displaysCorrectly() {
        List<Object> parameters = Collections.singletonList("only");
        CartesianProductContext context = new CartesianProductContext(parameters);

        String displayName = context.getDisplayName(3);

        assertEquals("3: [only]", displayName);
    }

    @Test
    void getDisplayName_withNullElement_displaysNull() {
        List<Object> parameters = Arrays.asList("a", null, "b");
        CartesianProductContext context = new CartesianProductContext(parameters);

        String displayName = context.getDisplayName(2);

        assertEquals("2: [a, null, b]", displayName);
    }

    @Test
    void getAdditionalExtensions_returnsSingleResolverExtension() {
        List<Object> parameters = Arrays.asList("a", "b");
        CartesianProductContext context = new CartesianProductContext(parameters);

        List<Extension> extensions = context.getAdditionalExtensions();

        assertEquals(1, extensions.size(), "Exactly one extension should be registered");
        assertTrue(extensions.get(0) instanceof CartesianProductResolver,
                "Extension must be a CartesianProductResolver");
    }

    @Test
    void getAdditionalExtensions_withEmptyParameters_returnsResolver() {
        List<Object> parameters = Collections.emptyList();
        CartesianProductContext context = new CartesianProductContext(parameters);

        List<Extension> extensions = context.getAdditionalExtensions();

        assertEquals(1, extensions.size());
        assertTrue(extensions.get(0) instanceof CartesianProductResolver);
    }
}
