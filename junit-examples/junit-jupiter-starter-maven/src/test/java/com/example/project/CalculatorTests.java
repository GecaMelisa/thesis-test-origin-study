package com.example.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator tests (add only)")
@Tag("gpt")
class CalculatorTests {

    private final Calculator calculator = new Calculator();

    @Nested
    @DisplayName("add()")
    class AddTests {

        @ParameterizedTest
        @CsvSource({
                "1, 1, 2",
                "2, 3, 5",
                "-1, 1, 0",
                "-2, -3, -5",
                "0, 0, 0"
        })
        void addVarious(int a, int b, int expected) {
            int result = calculator.add(a, b);
            assertEquals(expected, result);
        }

        @Test
        void addZeroDoesNotChangeValue() {
            assertAll(
                    () -> assertEquals(5, calculator.add(5, 0)),
                    () -> assertEquals(5, calculator.add(0, 5)),
                    () -> assertEquals(-3, calculator.add(-3, 0))
            );
        }

        @Test
        void addWithLargePositiveAndNegative() {
            int a = Integer.MAX_VALUE;
            int b = -1;
            int result = calculator.add(a, b);
            assertEquals(Integer.MAX_VALUE - 1, result);
        }
    }
}
