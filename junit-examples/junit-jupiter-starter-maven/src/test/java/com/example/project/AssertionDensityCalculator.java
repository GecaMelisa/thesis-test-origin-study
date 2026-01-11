package com.example.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * Helper executed by exec-maven-plugin to compute assertion density
 * (number of assert* calls per @Test / @ParameterizedTest).
 */
public final class AssertionDensityCalculator {

    private static final Pattern TEST_ANNOTATION =
            Pattern.compile("@Test\\b|@ParameterizedTest\\b");

    private static final Pattern ASSERTION =
            Pattern.compile("\\bassert(All|Equals|NotEquals|Null|NotNull|True|False|Throws|DoesNotThrow|IterableEquals)\\b");

    public static void main(String[] args) throws IOException {
        String tagFilter = System.getProperty("junit.jupiter.tags");
        Path testRoot = Paths.get("src", "test", "java");
        if (!Files.exists(testRoot)) {
            System.out.println("No test sources found at " + testRoot.toAbsolutePath());
            return;
        }

        final int[] tests = {0};
        final int[] assertions = {0};

        Files.walk(testRoot)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> {
                    try {
                        String text = Files.readString(p);
                        if (tagFilter != null && !tagFilter.isBlank()) {
                            String tagToken = "@Tag(\"" + tagFilter.trim() + "\")";
                            if (!text.contains(tagToken)) {
                                return;
                            }
                        }
                        tests[0] += countMatches(TEST_ANNOTATION, text);
                        assertions[0] += countMatches(ASSERTION, text);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        if (tests[0] == 0) {
            System.out.println("No @Test/@ParameterizedTest methods found.");
        } else {
            double density = assertions[0] / (double) tests[0];
            System.out.printf(
                    "Assertion density: %d assertions / %d tests = %.3f%n",
                    assertions[0], tests[0], density
            );
        }
    }

    private static int countMatches(Pattern pattern, String text) {
        int count = 0;
        var m = pattern.matcher(text);
        while (m.find()) {
            count++;
        }
        return count;
    }

    private AssertionDensityCalculator() {
        // utility class
    }
}
