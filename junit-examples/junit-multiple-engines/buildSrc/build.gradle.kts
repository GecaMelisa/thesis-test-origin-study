plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    val junit4Version = "4.13.2"
    val junitBomVersion = "5.13.4"

    // Align all JUnit 5 artifacts
    testImplementation(platform("org.junit:junit-bom:$junitBomVersion"))

    // JUnit Jupiter (JUnit 5)
    testImplementation("org.junit.jupiter:junit-jupiter")

    // JUnit Vintage (for running JUnit 3/4 tests)
    testImplementation("org.junit.vintage:junit-vintage-engine")

    // JUnit 4 itself (so Vintage has something to run)
    testImplementation("junit:junit:$junit4Version")

    // jqwik for property-based tests
    testImplementation("net.jqwik:jqwik:1.9.3")
}

tasks.test {
    // Make sure JUnit Platform is used to discover all engines
    useJUnitPlatform()
}
