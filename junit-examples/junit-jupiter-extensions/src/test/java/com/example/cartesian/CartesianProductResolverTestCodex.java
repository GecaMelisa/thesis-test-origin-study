package com.example.cartesian;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

@Tag("codex")
class CartesianProductResolverTestCodex {

	@Test
	void supportsParameterTrueWhenIndexWithinBounds() {
		List<Integer> parameters = Arrays.asList(10, 20, 30);
		CartesianProductResolver resolver = new CartesianProductResolver(parameters);
		ParameterContext parameterContext = mock(ParameterContext.class);
		ExtensionContext extensionContext = mock(ExtensionContext.class);

		when(parameterContext.getIndex()).thenReturn(0, 2);

		assertTrue(resolver.supportsParameter(parameterContext, extensionContext));
		assertTrue(resolver.supportsParameter(parameterContext, extensionContext));
	}

	@Test
	void supportsParameterFalseForEmptyListOrIndexAtSize() {
		CartesianProductResolver emptyResolver = new CartesianProductResolver(Collections.emptyList());
		ParameterContext parameterContext = mock(ParameterContext.class);
		ExtensionContext extensionContext = mock(ExtensionContext.class);

		when(parameterContext.getIndex()).thenReturn(0);
		assertFalse(emptyResolver.supportsParameter(parameterContext, extensionContext));

		List<String> parameters = Arrays.asList("a", "b");
		CartesianProductResolver resolver = new CartesianProductResolver(parameters);
		when(parameterContext.getIndex()).thenReturn(parameters.size());
		assertFalse(resolver.supportsParameter(parameterContext, extensionContext));
	}

	@Test
	void resolveParameterReturnsCorrectElement() {
		List<String> parameters = Arrays.asList("first", "second", "third");
		CartesianProductResolver resolver = new CartesianProductResolver(parameters);
		ExtensionContext extensionContext = mock(ExtensionContext.class);

		ParameterContext firstIndex = mock(ParameterContext.class);
		when(firstIndex.getIndex()).thenReturn(0);
		ParameterContext lastIndex = mock(ParameterContext.class);
		when(lastIndex.getIndex()).thenReturn(2);

		assertEquals("first", resolver.resolveParameter(firstIndex, extensionContext));
		assertEquals("third", resolver.resolveParameter(lastIndex, extensionContext));
	}

	@Test
	void resolveParameterThrowsForNegativeIndex() {
		List<String> parameters = Collections.singletonList("only");
		CartesianProductResolver resolver = new CartesianProductResolver(parameters);
		ParameterContext parameterContext = mock(ParameterContext.class);
		ExtensionContext extensionContext = mock(ExtensionContext.class);

		when(parameterContext.getIndex()).thenReturn(-1);

		assertThrows(IndexOutOfBoundsException.class,
				() -> resolver.resolveParameter(parameterContext, extensionContext));
	}
}
