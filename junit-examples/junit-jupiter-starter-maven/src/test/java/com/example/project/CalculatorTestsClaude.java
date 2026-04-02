package com.example.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator tests (Claude)")
@Tag("claude")
class CalculatorTestsClaude {

    private final Calculator calculator = new Calculator();

    @Nested
    @DisplayName("add()")
    class AddTests {

        @ParameterizedTest(name = "{0} + {1} = {2}")
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

        @Test
        void addIsCommutative() {
            assertEquals(calculator.add(3, 7), calculator.add(7, 3),
                    "Addition should be commutative");
        }

        @Test
        void addMaxAndMinValue() {
            assertEquals(-1, calculator.add(Integer.MAX_VALUE, Integer.MIN_VALUE),
                    "MAX_VALUE + MIN_VALUE should equal -1");
        }

        @Test
        void addHandlesNegativeResult() {
            assertEquals(-1, calculator.add(2, -3), "2 + (-3) should equal -1");
        }

        @Test
        void addLargeNumbers() {
            assertEquals(1_200_000_000, calculator.add(1_000_000_000, 200_000_000),
                    "1,000,000,000 + 200,000,000 should equal 1,200,000,000");
        }
    }
}
