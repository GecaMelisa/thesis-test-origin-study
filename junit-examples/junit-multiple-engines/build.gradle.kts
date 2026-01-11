plugins {
    // Standard JVM project
    java

    // Coverage
    jacoco

    // Static analysis
    pmd

    // Mutation testing
    id("info.solidsoft.pitest") version "1.15.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val junit4Version = "4.13.2"
    val junitBomVersion = "5.13.4"
    val jqwikVersion = "1.9.3"

    // Align all JUnit 5 artifacts
    testImplementation(platform("org.junit:junit-bom:$junitBomVersion"))

    // --- JUnit Jupiter (JUnit 5) ---
    testImplementation("org.junit.jupiter:junit-jupiter")       // API + params
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")   // engine

    // --- JUnit 4 + Vintage engine ---
    testImplementation("junit:junit:$junit4Version")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")

    // --- jqwik property-based testing (engine + api) ---
    testImplementation("net.jqwik:jqwik:$jqwikVersion")
}

// Use JUnit Platform so all engines (Jupiter, Vintage, jqwik) are active
tasks.test {
    useJUnitPlatform()

    val tagFilter = System.getProperty("junit.jupiter.tags")?.trim()?.ifBlank { null }
    if (tagFilter != null) {
        useJUnitPlatform {
            includeTags(tagFilter)
            when (tagFilter) {
                "human" -> includeTags("example.junit4.HumanTag")
                "gpt" -> includeTags("example.junit4.GptTag")
                "codex" -> includeTags("example.junit4.CodexTag")
            }
        }
    }

    testLogging {
        events("passed", "failed", "skipped")
    }
}

jacoco {
    toolVersion = "0.8.12"
}

// JaCoCo HTML + XML report
tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

// --- Assertion density (assert calls per test method) ---
tasks.register("assertionDensity") {
    group = "verification"
    description = "Calculates assertion density for test sources, optionally filtered by -Djunit.jupiter.tags."

    doLast {
        val tagFilter = System.getProperty("junit.jupiter.tags")?.trim()?.ifBlank { null }
        val tagToCategory = mapOf(
            "human" to "HumanTag",
            "gpt" to "GptTag",
            "codex" to "CodexTag"
        )

        val testRoot = file("src/test/java")
        if (!testRoot.exists()) {
            println("No test sources found in src/test/java")
            return@doLast
        }

        val testAnnotationRegex = Regex("@Test\\b|@ParameterizedTest\\b|@Property\\b")
        val assertionRegex = Regex(
            "\\bassert(All|Equals|NotEquals|Null|NotNull|True|False|Throws|DoesNotThrow|IterableEquals)\\b"
        )

        var testCount = 0
        var assertionCount = 0

        testRoot.walkTopDown()
            .filter { it.isFile && it.extension == "java" }
            .forEach { file ->
                val text = file.readText()
                if (tagFilter != null) {
                    val tagToken = "@Tag(\"$tagFilter\")"
                    val categoryToken = tagToCategory[tagFilter]?.let { "@Category($it.class)" }
                    val matchesTag = text.contains(tagToken) || (categoryToken != null && text.contains(categoryToken))
                    if (!matchesTag) {
                        return@forEach
                    }
                }

                testCount += testAnnotationRegex.findAll(text).count()
                assertionCount += assertionRegex.findAll(text).count()
            }

        if (testCount == 0) {
            println("No tests (@Test/@ParameterizedTest/@Property) found.")
        } else {
            val density = assertionCount.toDouble() / testCount.toDouble()
            val tagLabel = tagFilter?.let { " (tag=$it)" } ?: ""
            println("Assertion density$tagLabel: $assertionCount assertions / $testCount tests = $density")
        }
    }
}

// --- PMD with custom ruleset (assertion density, messages) ---
pmd {
    isConsoleOutput = true
    toolVersion = "6.55.0"

    // use ONLY our custom ruleset file
    ruleSets = emptyList()                       // ⬅ no set(), just assign
    ruleSetFiles = files("config/pmd/ruleset.xml")  // ⬅ no setFrom()
}



// Make "check" also run coverage, PIT and PMD
tasks.check {
    dependsOn(tasks.jacocoTestReport)
    dependsOn("pitest")
    dependsOn("pmdMain", "pmdTest")
    dependsOn("assertionDensity")
}

// --- PIT configuration ---
pitest {
        pitestVersion.set("1.15.0")
        junit5PluginVersion.set("1.2.0")
        testPlugin.set("junit5")

        // we "pretend" to target example classes since the sample is test-heavy
        targetClasses.set(listOf("example.*"))
        targetTests.set(listOf("example.*"))

        threads.set(4)
        mutationThreshold.set(0)

        // do not fail build when there are no mutations
        failWhenNoMutations.set(false)

        val tagFilter = System.getProperty("junit.jupiter.tags")?.trim()?.ifBlank { null }
        if (tagFilter != null) {
            jvmArgs.set(listOf("-Djunit.jupiter.tags=$tagFilter"))
            when (tagFilter) {
                "human" -> targetTests.set(
                    listOf(
                        "example.jupiter.HumanJupiterExampleTests",
                        "example.junit4.HumanJUnit4ExampleTests",
                        "example.jqwik.HumanJqwikExampleProperties"
                    )
                )
                "gpt" -> targetTests.set(
                    listOf(
                        "example.jupiter.JupiterExampleTests",
                        "example.junit4.JUnit4ExampleTests",
                        "example.jqwik.JqwikExampleProperties"
                    )
                )
                "codex" -> targetTests.set(
                    listOf(
                        "example.jupiter.JupiterExampleTestsCodex",
                        "example.junit4.JUnit4ExampleTestsCodex",
                        "example.jqwik.JqwikExamplePropertiesCodex"
                    )
                )
            }
        }
}


