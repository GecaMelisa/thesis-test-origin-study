package com.example.project

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Computes a simple assertion density metric: number of assert* calls per @Test/@ParameterizedTest.
 * Invoked via exec-maven-plugin.
 */
fun main() {
    val tagFilter = System.getProperty("junit.jupiter.tags")?.trim()?.takeIf { it.isNotEmpty() }
    val roots = listOf(
        Paths.get("src", "test", "kotlin"),
        Paths.get("src", "test", "java")
    )

    val existingRoots = roots.filter { Files.exists(it) }
    if (existingRoots.isEmpty()) {
        println("No test sources found in src/test/kotlin or src/test/java")
        return
    }

    val testPattern = Regex("@Test\\b|@ParameterizedTest\\b")
    val assertPattern = Regex("\\bassert(All|Equals|NotEquals|Null|NotNull|True|False|Throws|DoesNotThrow|IterableEquals)\\b")

    var testCount = 0
    var assertionCount = 0

    existingRoots.forEach { root ->
        Files.walk(root).use { paths ->
            paths.filter { Files.isRegularFile(it) && (it.toString().endsWith(".kt") || it.toString().endsWith(".java")) }
                .forEach { path ->
                    val text = Files.readString(path)
                    if (tagFilter != null && !text.contains("@Tag(\"$tagFilter\")")) {
                        return@forEach
                    }
                    testCount += testPattern.findAll(text).count()
                    assertionCount += assertPattern.findAll(text).count()
                }
        }
    }

    if (testCount == 0) {
        println("No @Test/@ParameterizedTest methods found.")
    } else {
        val density = assertionCount.toDouble() / testCount.toDouble()
        val tagLabel = tagFilter?.let { " (tag=$it)" } ?: ""
        println("Assertion density$tagLabel: $assertionCount assertions / $testCount tests = %.3f".format(density))
    }
}
