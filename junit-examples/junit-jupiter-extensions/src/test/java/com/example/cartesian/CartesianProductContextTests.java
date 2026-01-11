package com.example.cartesian;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.extension.Extension;

import static org.junit.jupiter.api.Assertions.*;

@Tag("gpt")
class CartesianProductContextTests {

    @Test
    void getDisplayName_includesInvocationIndexAndParameters_forNonEmptyParameters() {
        // given
        List<Object> parameters = Arrays.asList("foo", 42);
        CartesianProductContext context = new CartesianProductContext(parameters);

        // when
        String displayName = context.getDisplayName(1);

        // then
        assertEquals("1: [foo, 42]", displayName);
    }

    @Test
    void getDisplayName_includesInvocationIndexAndEmptyList_forEmptyParameters() {
        // given
        List<Object> parameters = Collections.emptyList();
        CartesianProductContext context = new CartesianProductContext(parameters);

        // when
        String displayName = context.getDisplayName(0);

        // then
        assertEquals("0: []", displayName);
    }

    @Test
    void getAdditionalExtensions_returnsSingleResolverWithSameParameterList() throws Exception {
        // given
        List<Object> parameters = Arrays.asList("a", "b");
        CartesianProductContext context = new CartesianProductContext(parameters);

        // when
        List<Extension> extensions = context.getAdditionalExtensions();

        // then
        assertEquals(1, extensions.size(), "Exactly one extension should be registered");
        assertTrue(extensions.get(0) instanceof CartesianProductResolver,
                "Extension must be a CartesianProductResolver");

        CartesianProductResolver resolver = (CartesianProductResolver) extensions.get(0);

        // reflectively verify that the resolver holds the same parameter list instance
        Field field = CartesianProductResolver.class.getDeclaredField("parameters");
        field.setAccessible(true);
        Object storedParameters = field.get(resolver);

        assertSame(parameters, storedParameters, "Resolver must use the same parameters list instance");
    }
}
