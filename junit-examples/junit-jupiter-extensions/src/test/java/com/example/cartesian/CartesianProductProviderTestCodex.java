package com.example.cartesian;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

@Tag("codex")
class CartesianProductProviderTestCodex {

	private final CartesianProductProvider provider = new CartesianProductProvider();

	@Test
	void supportsTemplateWhenAnnotationPresent() throws Exception {
		Method annotated = AnnotationSample.class.getDeclaredMethod("annotatedTest", String.class);
		ExtensionContext context = mock(ExtensionContext.class);
		when(context.getTestMethod()).thenReturn(java.util.Optional.of(annotated));

		assertTrue(provider.supportsTestTemplate(context));
	}

	@Test
	void supportsTemplateWhenAnnotationMissing() throws Exception {
		Method plain = AnnotationSample.class.getDeclaredMethod("plainMethod");
		ExtensionContext context = mock(ExtensionContext.class);
		when(context.getTestMethod()).thenReturn(java.util.Optional.of(plain));

		assertFalse(provider.supportsTestTemplate(context));
	}

	@Test
	void providesCartesianContextsUsingAnnotationValues() throws Exception {
		Method method = AnnotationSample.class.getDeclaredMethod("valueBased", String.class, String.class);
		ExtensionContext context = mock(ExtensionContext.class);
		when(context.getRequiredTestMethod()).thenReturn(method);

		List<CartesianProductContext> contexts = provider.provideTestTemplateInvocationContexts(context)
				.map(CartesianProductContext.class::cast)
				.collect(Collectors.toList());

		assertEquals(4, contexts.size(), "Two parameters with two values each should create four invocations");
		assertEquals("0: [x, x]", contexts.get(0).getDisplayName(0));
		assertEquals("3: [y, y]", contexts.get(3).getDisplayName(3));

		ExtensionContext dummyExtensionContext = mock(ExtensionContext.class);
		ParameterContext index0 = mock(ParameterContext.class);
		ParameterContext index1 = mock(ParameterContext.class);
		when(index0.getIndex()).thenReturn(0);
		when(index1.getIndex()).thenReturn(1);

		List<Object> secondInvocationParameters = contexts.get(1)
				.getAdditionalExtensions()
				.stream()
				.map(Extension.class::cast)
				.map(CartesianProductResolver.class::cast)
				.map(resolver -> Arrays.asList(resolver.resolveParameter(index0, dummyExtensionContext),
						resolver.resolveParameter(index1, dummyExtensionContext)))
				.findFirst()
				.orElseThrow(() -> new AssertionError("Resolver extension not found"));

		assertEquals(Arrays.asList("x", "y"), secondInvocationParameters);
	}

	@Test
	void usesFactoryMethodWhenAnnotationValueIsEmpty() throws Exception {
		Method method = FactorySamples.class.getDeclaredMethod("factoryDriven", String.class, Integer.class);
		ExtensionContext context = mock(ExtensionContext.class);
		when(context.getRequiredTestMethod()).thenReturn(method);

		List<CartesianProductContext> contexts = provider.provideTestTemplateInvocationContexts(context)
				.map(CartesianProductContext.class::cast)
				.collect(Collectors.toList());

		assertEquals(2, contexts.size(), "Factory produces one integer and two strings -> two invocations expected");
		assertEquals("0: [1, left]", contexts.get(0).getDisplayName(0));
		assertEquals("1: [1, right]", contexts.get(1).getDisplayName(1));
	}

	@Test
	void throwsAssertionWhenFactoryIsNotStatic() throws Exception {
		Method method = MisconfiguredFactories.class.getDeclaredMethod("nonStaticFactory", String.class);
		ExtensionContext context = mock(ExtensionContext.class);
		when(context.getRequiredTestMethod()).thenReturn(method);

		AssertionError error = assertThrows(AssertionError.class,
				() -> provider.provideTestTemplateInvocationContexts(context));
		assertTrue(error.getMessage().contains("must be static"));
	}

	@Test
	void throwsAssertionWhenFactoryReturnsWrongType() throws Exception {
		Method method = MisconfiguredFactories.class.getDeclaredMethod("wrongReturnType", Integer.class);
		ExtensionContext context = mock(ExtensionContext.class);
		when(context.getRequiredTestMethod()).thenReturn(method);

		AssertionError error = assertThrows(AssertionError.class,
				() -> provider.provideTestTemplateInvocationContexts(context));
		assertTrue(error.getMessage().contains("must return `CartesianProductTest.Sets`"));
	}

	@Test
	void throwsAssertionWhenFactoryHasParameters() throws Exception {
		Method method = MisconfiguredFactories.class.getDeclaredMethod("factoryWithParameters", String.class);
		ExtensionContext context = mock(ExtensionContext.class);
		when(context.getRequiredTestMethod()).thenReturn(method);

		AssertionError error = assertThrows(AssertionError.class,
				() -> provider.provideTestTemplateInvocationContexts(context));
		assertTrue(error.getMessage().contains("factoryWithParameters"),
				"Should mention the missing zero-arg factory when only parameterized overload exists");
	}

	@Test
	void throwsAssertionWhenFactoryIsMissing() throws Exception {
		Method method = MisconfiguredFactories.class.getDeclaredMethod("noFactoryAvailable", String.class);
		ExtensionContext context = mock(ExtensionContext.class);
		when(context.getRequiredTestMethod()).thenReturn(method);

		AssertionError error = assertThrows(AssertionError.class,
				() -> provider.provideTestTemplateInvocationContexts(context));
		assertTrue(error.getMessage().contains("not found"));
	}

	private static class AnnotationSample {

		@CartesianProductTest
		void annotatedTest(String value) {
		}

		void plainMethod() {
		}

		@CartesianProductTest({"x", "y"})
		void valueBased(String first, String second) {
		}
	}

	private static class FactorySamples {

		@CartesianProductTest
		void factoryDriven(String text, Integer number) {
		}

		static CartesianProductTest.Sets factoryDriven() {
			return () -> Arrays.asList(
					java.util.Collections.singletonList(1),
					Arrays.asList("left", "right"));
		}
	}

	private static class MisconfiguredFactories {

		@CartesianProductTest
		void nonStaticFactory(String value) {
		}

		CartesianProductTest.Sets nonStaticFactory() {
			return () -> java.util.Collections.singletonList(java.util.Collections.singletonList("x"));
		}

		@CartesianProductTest
		void wrongReturnType(Integer value) {
		}

		static java.util.List<java.util.List<?>> wrongReturnType() {
			return java.util.Collections.singletonList(java.util.Collections.singletonList(1));
		}

		@CartesianProductTest
		void factoryWithParameters(String value) {
		}

		static CartesianProductTest.Sets factoryWithParameters(int ignored) {
			return () -> java.util.Collections.singletonList(java.util.Collections.singletonList("x"));
		}

		@CartesianProductTest
		void noFactoryAvailable(String value) {
		}
	}
}
