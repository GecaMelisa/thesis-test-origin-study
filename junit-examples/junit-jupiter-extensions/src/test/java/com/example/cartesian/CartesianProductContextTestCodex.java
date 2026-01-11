package com.example.cartesian;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.Extension;

@Tag("codex")
class CartesianProductContextTestCodex {

	@Test
	void formatsDisplayNameWithInvocationIndexAndParameters() {
		List<String> parameters = Arrays.asList("left", "right");
		CartesianProductContext context = new CartesianProductContext(parameters);

		String displayName = context.getDisplayName(5);

		assertEquals("5: [left, right]", displayName);
		assertTrue(displayName.startsWith("5: "), "Display name should prefix invocation index");
	}

	@Test
	void formatsDisplayNameForNegativeIndexAndEmptyParameters() {
		CartesianProductContext context = new CartesianProductContext(Collections.emptyList());

		String displayName = context.getDisplayName(-1);

		assertEquals("-1: []", displayName);
	}

	@Test
	void providesSingleResolverExtensionWithSameParameterListInstance() throws Exception {
		List<Object> parameters = Arrays.asList("x", 99);
		CartesianProductContext context = new CartesianProductContext(parameters);

		List<Extension> extensions = context.getAdditionalExtensions();

		assertEquals(1, extensions.size(), "Exactly one extension expected");
		assertTrue(extensions.get(0) instanceof CartesianProductResolver);
		assertThrows(UnsupportedOperationException.class, () -> extensions.add(null),
				"Returned list should be unmodifiable");

		CartesianProductResolver resolver = (CartesianProductResolver) extensions.get(0);
		Field field = CartesianProductResolver.class.getDeclaredField("parameters");
		field.setAccessible(true);
		assertSame(parameters, field.get(resolver), "Resolver must hold original parameters instance");
	}
}
