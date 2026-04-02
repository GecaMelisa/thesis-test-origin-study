# 1) Plain tests
.\gradlew.bat test --no-daemon --console=plain

# 2) JaCoCo coverage
.\gradlew.bat clean test jacocoTestReport --no-daemon --console=plain
# report: build/reports/jacoco/test/html/index.html

# 3) PIT mutation testing
.\gradlew.bat clean pitest --no-daemon --console=plain
# report: build/reports/pitest/index.html

# 4) Assertion density
.\gradlew.bat assertionDensity --no-daemon --console=plain
# report: build/reports/assertion-density/assertion-density.txt

# 5) PMD on tests
.\gradlew.bat pmdTest --no-daemon --console=plain
# report: build/reports/pmd/test

# 6) Tagged test runs
.\gradlew.bat clean testHuman --no-daemon --console=plain
.\gradlew.bat clean testGpt --no-daemon --console=plain
.\gradlew.bat clean testCodex --no-daemon --console=plain

# 7) CODEX - only Codex tests and copied reports with Codex labels
.\gradlew.bat clean testCodex --no-daemon --console=plain

.\gradlew.bat jacocoTestReport --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\jacoco') { Copy-Item -Recurse -Force 'build\\reports\\jacoco' 'build\\reports\\jacocoCodex' }"
REM Codex JaCoCo report: build\reports\jacocoCodex\index.html

.\gradlew.bat pitest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pitest') { Copy-Item -Recurse -Force 'build\\reports\\pitest' 'build\\reports\\pitestCodex' }"
REM Codex PIT report: build\reports\pitestCodex\index.html

.\gradlew.bat pmdTest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pmd') { Copy-Item -Recurse -Force 'build\\reports\\pmd' 'build\\reports\\pmdCodex' }"
REM Codex PMD report folder: build\reports\pmdCodex\

# 8) Everything in one go
.\gradlew.bat clean fullTestMetrics --no-daemon --console=plain

.\gradlew.bat -PincludeTags=codex clean test jacocoTestReport pitest pmdTest assertionDensity --no-daemon --console=plain
Start-Process .\build\reports\jacoco\test\html\index.html
Start-Process .\build\reports\pitest\index.html
Start-Process .\build\reports\pmd\test.html


# CLAUDE - only Claude tests and copied reports with Claude labels
.\gradlew.bat clean testClaude --no-daemon --console=plain

.\gradlew.bat jacocoTestReport --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\jacoco') { Copy-Item -Recurse -Force 'build\\reports\\jacoco' 'build\\reports\\jacocoClaude' }"
REM Claude JaCoCo report: build\reports\jacocoClaude\index.html

.\gradlew.bat pitest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pitest') { Copy-Item -Recurse -Force 'build\\reports\\pitest' 'build\\reports\\pitestClaude' }"
REM Claude PIT report: build\reports\pitestClaude\index.html

.\gradlew.bat pmdTest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pmd') { Copy-Item -Recurse -Force 'build\\reports\\pmd' 'build\\reports\\pmdClaude' }"
REM Claude PMD report folder: build\reports\pmdClaude\

# CLAUDE - ALL IN ONE
.\gradlew.bat -PincludeTags=claude clean test jacocoTestReport pitest pmdTest assertionDensity --no-daemon --console=plain
