package com.example.random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Random;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.mockito.InOrder;

@Tag("codex")
class RandomParametersExtensionTestCodex {

	private final RandomParametersExtension extension = new RandomParametersExtension();

	@Test
	void supportsParameterDelegatesAnnotationPresence() {
		ParameterContext annotated = mock(ParameterContext.class);
		when(annotated.isAnnotated(RandomParametersExtension.Random.class)).thenReturn(true);

		ParameterContext unannotated = mock(ParameterContext.class);
		when(unannotated.isAnnotated(RandomParametersExtension.Random.class)).thenReturn(false);

		assertTrue(extension.supportsParameter(annotated, mock(ExtensionContext.class)));
		assertFalse(extension.supportsParameter(unannotated, mock(ExtensionContext.class)));
		verify(annotated, times(1)).isAnnotated(RandomParametersExtension.Random.class);
		verify(unannotated, times(1)).isAnnotated(RandomParametersExtension.Random.class);
	}

	@Test
	void resolvesValuesUsingSharedRandomFromGlobalStore() throws Exception {
		Method method = SampleTargets.class.getDeclaredMethod("annotated", int.class, double.class, String.class);
		Parameter intParameter = method.getParameters()[0];
		Parameter doubleParameter = method.getParameters()[1];

		Random expectedSequence = new Random(123L);
		int expectedInt = expectedSequence.nextInt();
		double expectedDouble = expectedSequence.nextDouble();

		Random storeRandom = new Random(123L);
		Random spyRandom = org.mockito.Mockito.spy(storeRandom);

		ExtensionContext.Store store = mock(ExtensionContext.Store.class);
		when(store.getOrComputeIfAbsent(Random.class)).thenReturn(spyRandom);

		ExtensionContext root = mock(ExtensionContext.class);
		when(root.getStore(Namespace.GLOBAL)).thenReturn(store);

		ExtensionContext extensionContext = mock(ExtensionContext.class);
		when(extensionContext.getRoot()).thenReturn(root);

		ParameterContext intContext = mock(ParameterContext.class);
		when(intContext.getParameter()).thenReturn(intParameter);
		ParameterContext doubleContext = mock(ParameterContext.class);
		when(doubleContext.getParameter()).thenReturn(doubleParameter);

		Object first = extension.resolveParameter(intContext, extensionContext);
		Object second = extension.resolveParameter(doubleContext, extensionContext);

		assertEquals(expectedInt, first);
		assertEquals(expectedDouble, (Double) second, 0.0);

		verify(store, times(2)).getOrComputeIfAbsent(Random.class);
		InOrder order = inOrder(spyRandom);
		order.verify(spyRandom).nextInt();
		order.verify(spyRandom).nextDouble();
	}

	@Test
	void unsupportedTypeThrowsParameterResolutionExceptionWithTypeName() throws Exception {
		Method method = SampleTargets.class.getDeclaredMethod("unsupported", String.class);
		Parameter unsupported = method.getParameters()[0];

		ExtensionContext.Store store = mock(ExtensionContext.Store.class);
		when(store.getOrComputeIfAbsent(Random.class)).thenReturn(new Random());

		ExtensionContext root = mock(ExtensionContext.class);
		when(root.getStore(Namespace.GLOBAL)).thenReturn(store);

		ExtensionContext extensionContext = mock(ExtensionContext.class);
		when(extensionContext.getRoot()).thenReturn(root);

		ParameterContext context = mock(ParameterContext.class);
		when(context.getParameter()).thenReturn(unsupported);

		ParameterResolutionException exception = assertThrows(ParameterResolutionException.class,
				() -> extension.resolveParameter(context, extensionContext));

		assertTrue(exception.getMessage().contains(unsupported.getType().getName()));
	}

	private static class SampleTargets {

		void annotated(@RandomParametersExtension.Random int number,
				@RandomParametersExtension.Random double decimal,
				@RandomParametersExtension.Random String unsupported) {
		}

		void unsupported(@RandomParametersExtension.Random String value) {
		}
	}
}
