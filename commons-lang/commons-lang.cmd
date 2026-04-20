@echo off
call "%~dp0setup-maven.cmd"

set HUMAN_TESTS=org.apache.commons.lang3.StringUtilsTest,org.apache.commons.lang3.StringUtilsEmptyBlankTest,org.apache.commons.lang3.RandomStringUtilsTest,org.apache.commons.lang3.math.NumberUtilsTest
set CLAUDE_TESTS=org.apache.commons.lang3.StringUtilsClaudeTest,org.apache.commons.lang3.RandomStringUtilsClaudeTest,org.apache.commons.lang3.math.NumberUtilsClaudeTest
set PIT_TARGET=org.apache.commons.lang3.StringUtils,org.apache.commons.lang3.RandomStringUtils,org.apache.commons.lang3.math.NumberUtils
set SKIP=-Drat.skip=true -Dcommons.jacoco.haltOnFailure=false

REM -----------------------------------------------------------------------
REM HUMAN - ALL IN ONE
mvn clean test "-Dtest=$CLAUDE_TESTS" jacoco:report pmd:pmd @SKIP
mvn surefire-report:report-only
if (Test-Path "target\site\surefire-report.html") { Copy-Item -Force "target\site\surefire-report.html" "target\site\surefire-report-claude.html" }
if (Test-Path "target\site\jacoco") { Copy-Item -Recurse -Force "target\site\jacoco" "target\site\jacocoClaude" }
mvn org.pitest:pitest-maven:mutationCoverage "-DtargetClasses=$PIT_TARGET" "-DtargetTests=$CLAUDE_TESTS" "-DoutputFormats=HTML,XML" @SKIP
Get-ChildItem "target\pit-reports" | Sort-Object LastWriteTime -Descending | Select-Object -First 1 | ForEach-Object { Copy-Item -Recurse -Force $_.FullName "target\pit-reports\pitClaude" }
Start-Process "target\site\surefire-report-claude.html"
Start-Process "target\site\jacocoClaude\index.html"
Start-Process "target\pit-reports\pitClaude\index.html"
Start-Process "target\site\pmdClaude.html"


REM -----------------------------------------------------------------------
REM CLAUDE - ALL IN ONE
REM -----------------------------------------------------------------------
mvn clean test -Dtest="%CLAUDE_TESTS%" jacoco:report pmd:pmd %SKIP%
mvn surefire-report:report-only
powershell -NoProfile -Command "if (Test-Path 'target\\site\\surefire-report.html') { Copy-Item -Force 'target\\site\\surefire-report.html' 'target\\site\\surefire-report-claude.html' }"
powershell -NoProfile -Command "if (Test-Path 'target\\site\\jacoco') { Copy-Item -Recurse -Force 'target\\site\\jacoco' 'target\\site\\jacocoClaude' }"
mvn org.pitest:pitest-maven:mutationCoverage -DtargetClasses="%PIT_TARGET%" -DtargetTests="%CLAUDE_TESTS%" -DoutputFormats=HTML,XML %SKIP%
powershell -NoProfile -Command "Get-ChildItem 'target\\pit-reports' | Sort-Object LastWriteTime -Descending | Select-Object -First 1 | ForEach-Object { Copy-Item -Recurse -Force $_.FullName 'target\\pit-reports\\pitClaude' }"
start target\site\surefire-report-claude.html
start target\site\jacocoClaude\index.html
start target\pit-reports\pitClaude\index.html
start target\site\pmdClaude.html
