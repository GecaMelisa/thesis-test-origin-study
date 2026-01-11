package com.example.cartesian;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a test method whose arguments are generated as a cartesian product
 * of parameter sets provided by {@link CartesianProductProvider}.
 */
@TestTemplate
@ExtendWith(CartesianProductProvider.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CartesianProductTest {

    /**
     * Optional names of factory methods that provide the parameter sets.
     * If left empty, default factories are used (see CartesianProductProvider).
     */
    String[] value() default {};

    /**
     * Implementations provide the lists of parameter sets for the cartesian product.
     * CartesianProductProvider expects {@link #getSets()} to return a list of
     * parameter lists (one list per dimension of the product).
     */
    interface Sets {
        java.util.List<java.util.List<?>> getSets();
    }
}

