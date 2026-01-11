# HUMAN tests only
.\gradlew.bat clean humanTest --no-daemon --console=plain

# JaCoCo (HUMAN)
.\gradlew.bat jacocoHumanTestReport --no-daemon --console=plain
Start-Process "build\reports\jacoco\human\index.html"

# PIT (HUMAN)
.\gradlew.bat pitestHuman --no-daemon --console=plain
Start-Process "build\reports\pitest\human\index.html"

# Assertion density + PMD (same for all suites; runs on src/test/java)
.\gradlew.bat assertionDensity --no-daemon --console=plain
.\gradlew.bat pmdTest --no-daemon --console=plain


# GPT tests only
.\gradlew.bat clean gptTest --no-daemon --console=plain

# JaCoCo (GPT)
.\gradlew.bat jacocoGptTestReport --no-daemon --console=plain
Start-Process "build\reports\jacoco\gpt\index.html"

# PIT (GPT)
.\gradlew.bat pitestGpt --no-daemon --console=plain
Start-Process "build\reports\pitest\gpt\index.html"

# Assertion density + PMD
.\gradlew.bat assertionDensity --no-daemon --console=plain
.\gradlew.bat pmdTest --no-daemon --console=plain
Start-Process .\build\reports\pmd\test.xml


# CODEX tests only
.\gradlew.bat clean codexTest --no-daemon --console=plain

# JaCoCo (CODEX)
.\gradlew.bat jacocoCodexTestReport --no-daemon --console=plain
Start-Process "build\reports\jacoco\codex\index.html"

# PIT (CODEX)
.\gradlew.bat pitestCodex --no-daemon --console=plain
Start-Process "build\reports\pitest\codex\index.html"

# Assertion density + PMD
.\gradlew.bat assertionDensity --no-daemon --console=plain
.\gradlew.bat pmdTest --no-daemon --console=plain
