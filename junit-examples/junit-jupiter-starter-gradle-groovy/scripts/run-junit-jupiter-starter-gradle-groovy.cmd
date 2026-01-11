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
# report: printed to console (custom task)

# 5) PMD on tests
.\gradlew.bat pmdTest --no-daemon --console=plain
# report: build/reports/pmd/test

# 6) CODEX - only Codex tests and copied reports with Codex labels
.\gradlew.bat clean test --tests "*TestCodex" --no-daemon --console=plain

.\gradlew.bat jacocoTestReport --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\jacoco') { Copy-Item -Recurse -Force 'build\\reports\\jacoco' 'build\\reports\\jacocoCodex' }"
REM Codex JaCoCo report: build\reports\jacocoCodex\index.html

.\gradlew.bat pitest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pitest') { Copy-Item -Recurse -Force 'build\\reports\\pitest' 'build\\reports\\pitestCodex' }"
REM Codex PIT report: build\reports\pitestCodex\index.html

.\gradlew.bat pmdTest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pmd') { Copy-Item -Recurse -Force 'build\\reports\\pmd' 'build\\reports\\pmdCodex' }"
REM Codex PMD report folder: build\reports\pmdCodex\

# 7) Everything in one go
.\gradlew.bat clean fullTestMetrics --no-daemon --console=plain


.\gradlew.bat -PincludeTags=gpt clean test jacocoTestReport pitest pmdTest assertionDensity --no-daemon --console=plain
.\gradlew.bat test -PincludeTags=gpt --no-daemon --console=plain
.\gradlew.bat test -PincludeTags=codex --no-daemon --console=plain
.\gradlew.bat -PincludeTags=gpt clean test jacocoTestReport pitest pmdTest assertionDensity --no-daemon --console=plain
