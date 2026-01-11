import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.gradle.api.plugins.quality.Pmd
import java.util.Locale

plugins {
	// Kotlin JVM – compatible with JUnit 6 Kotlin metadata
	kotlin("jvm") version "2.2.20"

	// Metrics / analysis
	jacoco
	id("info.solidsoft.pitest") version "1.15.0"
	pmd
}

group = "com.example.project"
version = "1.0.0-SNAPSHOT"

repositories {
	mavenCentral()
}

// Use Java 17 everywhere (Java + Kotlin) to avoid target mismatch warnings
java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

kotlin {
	jvmToolchain(17)
}

fun findTaggedTestClasses(tag: String): Set<String> {
	val testSrcDirs = listOf(file("src/test/kotlin"), file("src/test/java")).filter { it.exists() }
	if (testSrcDirs.isEmpty()) {
		return emptySet()
	}

	val tagRegex = Regex("@Tag\\(\"([^\"]+)\"\\)")
	val packageRegex = Regex("^\\s*package\\s+([\\w.]+)")
	val classRegex = Regex("\\bclass\\s+(\\w+)")

	val matches = mutableSetOf<String>()

	testSrcDirs.forEach { dir ->
		dir.walkTopDown()
				.filter { it.isFile && (it.extension == "kt" || it.extension == "java") }
				.forEach { file ->
					var pkg = ""
					var pendingTags = mutableSetOf<String>()

					file.forEachLine { line ->
						packageRegex.find(line)?.let { match ->
							pkg = match.groupValues[1]
						}

						tagRegex.findAll(line).forEach { match ->
							pendingTags.add(match.groupValues[1])
						}

						classRegex.find(line)?.let { match ->
							val className = match.groupValues[1]
							if (pendingTags.contains(tag)) {
								val fqcn = if (pkg.isBlank()) className else "$pkg.$className"
								matches.add(fqcn)
							}
							pendingTags.clear()
						}
					}
				}
	}

	return matches
}

fun findTaggedTestFiles(tag: String): Set<File> {
	val testSrcDirs = listOf(file("src/test/kotlin"), file("src/test/java")).filter { it.exists() }
	if (testSrcDirs.isEmpty()) {
		return emptySet()
	}

	val tagRegex = Regex("@Tag\\(\"([^\"]+)\"\\)")
	val matches = mutableSetOf<File>()

	testSrcDirs.forEach { dir ->
		dir.walkTopDown()
				.filter { it.isFile && (it.extension == "kt" || it.extension == "java") }
				.forEach { file ->
					val text = file.readText()
					val tagsInFile = tagRegex.findAll(text).map { it.groupValues[1] }.toSet()
					if (tagsInFile.contains(tag)) {
						matches.add(file)
					}
				}
	}

	return matches
}

// ----------------------
// JUnit Jupiter
// ----------------------
dependencies {
	// keep all JUnit pieces on the same version
	testImplementation(platform("org.junit:junit-bom:6.0.1"))

	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.junit.jupiter:junit-jupiter-params")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

// ----------------------
// JaCoCo – coverage
// ----------------------
jacoco {
	toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
		csv.required.set(false)
	}
}

// ----------------------
// PIT – mutation testing
// ----------------------
configure<PitestPluginExtension> {
	pitestVersion.set("1.16.1")
	junit5PluginVersion.set("1.2.1")

	// your code and tests are in this package
	targetClasses.set(setOf("com.example.project.*"))
	targetTests.set(setOf("com.example.project.*"))

	threads.set(Runtime.getRuntime().availableProcessors())
	outputFormats.set(setOf("XML", "HTML"))
	timestampedReports.set(false)
	failWhenNoMutations.set(false)

	val tagFilter = System.getProperty("junit.jupiter.tags")?.trim()?.ifBlank { null }
	if (tagFilter != null) {
		val taggedTests = findTaggedTestClasses(tagFilter)
		if (taggedTests.isNotEmpty()) {
			targetTests.set(taggedTests)
		} else {
			println("No tests found with @Tag(\"$tagFilter\") for PIT; using default targetTests.")
		}
		jvmArgs.set(listOf("-Djunit.jupiter.tags=$tagFilter"))
	}
}

// ----------------------
// PMD ƒ?" tag-isolated tasks
// ----------------------
val basePmdTest = tasks.named<Pmd>("pmdTest")

fun registerTaggedPmdTask(tag: String) {
	val suffix = tag.substring(0, 1).toUpperCase(Locale.US) + tag.substring(1)
	tasks.register<Pmd>("pmdTest$suffix") {
		group = "verification"
		description = "Runs PMD on @Tag(\"$tag\") test sources only."
		classpath = files()
		source = files(findTaggedTestFiles(tag)).asFileTree
		ruleSets = basePmdTest.get().ruleSets
		ruleSetFiles = basePmdTest.get().ruleSetFiles
		ignoreFailures = basePmdTest.get().ignoreFailures

		reports {
			xml.required.set(true)
			html.required.set(true)
			xml.outputLocation.set(file("build/reports/pmd/test-$tag.xml"))
			html.outputLocation.set(file("build/reports/pmd/test-$tag.html"))
		}

		onlyIf {
			val hasSources = source.files.any { it.exists() }
			if (!hasSources) {
				println("No @Tag(\"$tag\") test sources found for PMD.")
			}
			hasSources
		}
	}
}

registerTaggedPmdTask("human")
registerTaggedPmdTask("gpt")
registerTaggedPmdTask("codex")

// ----------------------
// Assertion density metric
// ----------------------
tasks.register("assertionDensity") {
	group = "verification"
	description = "Calculates assertion density (assert calls per @Test), optionally filtered by -Djunit.jupiter.tags."

	doLast {
		val tagFilterRaw = System.getProperty("junit.jupiter.tags")?.trim()?.ifBlank { null }
		val tagFilter = tagFilterRaw
				?.split(',', '&', '|')
				?.map { it.trim() }
				?.firstOrNull { it.isNotEmpty() }

		val testSrcDirs = listOf(file("src/test/kotlin"), file("src/test/java")).filter { it.exists() }
		if (testSrcDirs.isEmpty()) {
			println("No test sources found in src/test/kotlin or src/test/java")
			return@doLast
		}

		var testCount = 0
		var assertionCount = 0

		val testAnnotationRegex = Regex("@Test\\b|@ParameterizedTest\\b")
		val assertionRegex = Regex(
				"\\bassert(All|Equals|NotEquals|Null|NotNull|True|False|Throws|DoesNotThrow|IterableEquals)\\b"
		)
		testSrcDirs.forEach { dir ->
			dir.walkTopDown()
					.filter { it.isFile && (it.extension == "kt" || it.extension == "java") }
					.forEach { file ->
						val text = file.readText()
						if (tagFilter != null && !text.contains("@Tag(\"$tagFilter\")")) {
							return@forEach
						}

						testCount += testAnnotationRegex.findAll(text).count()
						assertionCount += assertionRegex.findAll(text).count()
					}
		}

		if (testCount == 0) {
			println("No tests (@Test/@ParameterizedTest) found in Kotlin test sources.")
		} else {
			val density = assertionCount.toDouble() / testCount.toDouble()
			val tagLabel = tagFilter?.let { " (tag=$it)" } ?: ""
			println("Assertion density$tagLabel: $assertionCount assertions / $testCount tests = $density")
		}
	}
}

// ----------------------
// Check task – run all metrics
// ----------------------
tasks.check {
	dependsOn(tasks.jacocoTestReport)
	dependsOn("pitest")
	dependsOn("assertionDensity")
}
