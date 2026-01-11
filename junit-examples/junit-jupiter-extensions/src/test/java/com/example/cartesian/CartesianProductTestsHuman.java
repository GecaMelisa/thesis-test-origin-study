/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.example.cartesian;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;

@Tag("human")
class CartesianProductTestsHuman {

	@CartesianProductTest({"0", "1"})
	void threeBits(String a, String b, String c) {
		int value = Integer.parseUnsignedInt(a + b + c, 2);
		assertTrue((0b000 <= value) && (value <= 0b111));
	}

	@CartesianProductTest
	@DisplayName("S ? T ? U")
	void nFold(String string, Class<?> type, TimeUnit unit, TestInfo info) {
		assertTrue(string.endsWith("a"));
		assertTrue(type.isInterface());
		assertTrue(unit.name().endsWith("S"));
		assertTrue(info.getTags().isEmpty() || info.getTags().contains("human"));
	}

	static CartesianProductTest.Sets nFold() {
		return () -> Arrays.asList(
				Arrays.asList("Alpha", "Omega"),
				Arrays.asList(Runnable.class, Comparable.class, TestInfo.class),
				Arrays.asList(TimeUnit.DAYS, TimeUnit.HOURS));
	}

}
