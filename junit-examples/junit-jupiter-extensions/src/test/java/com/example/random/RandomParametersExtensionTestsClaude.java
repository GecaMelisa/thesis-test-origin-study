package com.example.random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

@Tag("claude")
class RandomParametersExtensionTestsClaude {

    private final RandomParametersExtension extension = new RandomParametersExtension();

    // --- supportsParameter ---------------------------------------------------

    @Test
    void supportsParameter_returnsTrueWhenAnnotatedWithRandom() {
        ParameterContext parameterContext = mock(ParameterContext.class);
        when(parameterContext.isAnnotated(RandomParametersExtension.Random.class))
                .thenReturn(true);

        boolean result = extension.supportsParameter(parameterContext, mock(ExtensionContext.class));

        assertTrue(result, "Should support parameter annotated with @Random");
    }

    @Test
    void supportsParameter_returnsFalseWhenNotAnnotatedWithRandom() {
        ParameterContext parameterContext = mock(ParameterContext.class);
        when(parameterContext.isAnnotated(RandomParametersExtension.Random.class))
                .thenReturn(false);

        boolean result = extension.supportsParameter(parameterContext, mock(ExtensionContext.class));

        assertFalse(result, "Should not support parameter without @Random");
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

        when(parameterContext.getParameter()).thenReturn(parameter);
        doReturn(int.class).when(parameter).getType();
        when(extensionContext.getRoot()).thenReturn(root);
        when(root.getStore(Namespace.GLOBAL)).thenReturn(store);
        when(store.getOrComputeIfAbsent(java.util.Random.class)).thenReturn(random);
        when(random.nextInt()).thenReturn(42);

        Object value = extension.resolveParameter(parameterContext, extensionContext);

        assertTrue(value instanceof Integer);
        assertEquals(42, value);
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
        when(random.nextDouble()).thenReturn(0.75);

        Object value = extension.resolveParameter(parameterContext, extensionContext);

        assertTrue(value instanceof Double);
        assertEquals(0.75, (Double) value, 0.0001);
    }

    // --- resolveParameter: unsupported type ----------------------------------

    @Test
    void resolveParameter_throwsExceptionForUnsupportedType_String() {
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ExtensionContext root = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        Parameter parameter = mock(Parameter.class);

        when(parameterContext.getParameter()).thenReturn(parameter);
        doReturn(String.class).when(parameter).getType();
        when(extensionContext.getRoot()).thenReturn(root);
        when(root.getStore(Namespace.GLOBAL)).thenReturn(store);
        when(store.getOrComputeIfAbsent(java.util.Random.class)).thenReturn(new java.util.Random());

        assertThrows(ParameterResolutionException.class,
                () -> extension.resolveParameter(parameterContext, extensionContext));
    }

    @Test
    void resolveParameter_throwsExceptionForUnsupportedType_Long() {
        ParameterContext parameterContext = mock(ParameterContext.class);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ExtensionContext root = mock(ExtensionContext.class);
        ExtensionContext.Store store = mock(ExtensionContext.Store.class);
        Parameter parameter = mock(Parameter.class);

        when(parameterContext.getParameter()).thenReturn(parameter);
        doReturn(long.class).when(parameter).getType();
        when(extensionContext.getRoot()).thenReturn(root);
        when(root.getStore(Namespace.GLOBAL)).thenReturn(store);
        when(store.getOrComputeIfAbsent(java.util.Random.class)).thenReturn(new java.util.Random());

        assertThrows(ParameterResolutionException.class,
                () -> extension.resolveParameter(parameterContext, extensionContext),
                "long type is not supported and should throw ParameterResolutionException");
    }
}
