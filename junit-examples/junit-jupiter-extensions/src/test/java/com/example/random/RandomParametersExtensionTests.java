package com.example.random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Parameter;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

@Tag("gpt")
class RandomParametersExtensionTests {

    private final RandomParametersExtension extension = new RandomParametersExtension();

    // --- supportsParameter ---------------------------------------------------

    @Test
    void supportsParameter_returnsTrueWhenParameterIsAnnotatedWithRandom() {
        ParameterContext parameterContext = mock(ParameterContext.class);

        when(parameterContext.isAnnotated(RandomParametersExtension.Random.class))
                .thenReturn(true);

        boolean result = extension.supportsParameter(parameterContext, mock(ExtensionContext.class));

        assertTrue(result);
    }

    @Test
    void supportsParameter_returnsFalseWhenParameterIsNotAnnotatedWithRandom() {
        ParameterContext parameterContext = mock(ParameterContext.class);

        when(parameterContext.isAnnotated(RandomParametersExtension.Random.class))
                .thenReturn(false);

        boolean result = extension.supportsParameter(parameterContext, mock(ExtensionContext.class));

        assertTrue(!result);
    }

    // --- resolveParameter: int -----------------------------------------------

    @Test
    void resolveParameter_returnsRandomInt_forPrimitiveIntParameter() {
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ExtensionContext root = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        Parameter parameter = mock(Parameter.class);
        java.util.Random random = mock(java.util.Random.class);

        // Common wiring
        when(parameterContext.getParameter()).thenReturn(parameter);
        doReturn(int.class).when(parameter).getType();

        when(extensionContext.getRoot()).thenReturn(root);
        when(root.getStore(Namespace.GLOBAL)).thenReturn(store);
        when(store.getOrComputeIfAbsent(java.util.Random.class)).thenReturn(random);

        when(random.nextInt()).thenReturn(123);

        Object value = extension.resolveParameter(parameterContext, extensionContext);

        assertTrue(value instanceof Integer);
        assertEquals(123, value);
    }

    // --- resolveParameter: double --------------------------------------------

    @Test
    void resolveParameter_returnsRandomDouble_forPrimitiveDoubleParameter() {
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ExtensionContext root = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        Parameter parameter = mock(Parameter.class);
        java.util.Random random = mock(java.util.Random.class);

        when(parameterContext.getParameter()).thenReturn(parameter);
        doReturn(double.class).when(parameter).getType();

        when(extensionContext.getRoot()).thenReturn(root);
        when(root.getStore(Namespace.GLOBAL)).thenReturn(store);
        when(store.getOrComputeIfAbsent(java.util.Random.class)).thenReturn(random);

        when(random.nextDouble()).thenReturn(0.5);

        Object value = extension.resolveParameter(parameterContext, extensionContext);

        assertTrue(value instanceof Double);
        assertEquals(0.5, (Double) value);
    }

    // --- resolveParameter: unsupported type ----------------------------------

    @Test
    void resolveParameter_throwsExceptionForUnsupportedType() {
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ExtensionContext root = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        Parameter parameter = mock(Parameter.class);
        java.util.Random random = new java.util.Random(); // real instance is fine here

        when(parameterContext.getParameter()).thenReturn(parameter);
        // Unsupported -> String.class
        doReturn(String.class).when(parameter).getType();

        when(extensionContext.getRoot()).thenReturn(root);
        when(root.getStore(Namespace.GLOBAL)).thenReturn(store);
        when(store.getOrComputeIfAbsent(java.util.Random.class)).thenReturn(random);

        assertThrows(ParameterResolutionException.class,
                () -> extension.resolveParameter(parameterContext, extensionContext));
    }
}
