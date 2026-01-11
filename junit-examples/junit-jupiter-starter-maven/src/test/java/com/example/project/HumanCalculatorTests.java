package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("human")
class HumanCalculatorTests {

	@Test
	void humanOnePlusOneEqualsTwo() {
		Calculator calculator = new Calculator();
		assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
	}

	@ParameterizedTest(name = "human {0} + {1} = {2}")
	@CsvSource({
			"0, 1, 1",
			"1, 2, 3",
			"49, 51, 100",
			"1, 100, 101"
	})
	void humanAdd(int first, int second, int expectedResult) {
		Calculator calculator = new Calculator();
		assertEquals(expectedResult, calculator.add(first, second),
				first + " + " + second + " should equal " + expectedResult);
	}

	@Test
	void humanAddHandlesNegative() {
		Calculator calculator = new Calculator();
		assertEquals(-1, calculator.add(2, -3), "2 + (-3) should equal -1");
	}
}
