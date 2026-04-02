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


# CLAUDE tests only
.\gradlew.bat -PincludeTags=claude clean test --no-daemon --console=plain

# JaCoCo (CLAUDE)
.\gradlew.bat jacocoTestReport --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\jacoco') { Copy-Item -Recurse -Force 'build\\reports\\jacoco' 'build\\reports\\jacocoClaude' }"
REM Claude JaCoCo report: build\reports\jacocoClaude\index.html

# PIT (CLAUDE)
.\gradlew.bat pitest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pitest') { Copy-Item -Recurse -Force 'build\\reports\\pitest' 'build\\reports\\pitestClaude' }"
REM Claude PIT report: build\reports\pitestClaude\index.html

# Assertion density + PMD (CLAUDE)
.\gradlew.bat assertionDensity --no-daemon --console=plain
.\gradlew.bat pmdTest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pmd') { Copy-Item -Recurse -Force 'build\\reports\\pmd' 'build\\reports\\pmdClaude' }"
REM Claude PMD report folder: build\reports\pmdClaude\

# CLAUDE - ALL IN ONE
.\gradlew.bat -PincludeTags=claude clean test jacocoTestReport pitest pmdTest assertionDensity --no-daemon --console=plain
