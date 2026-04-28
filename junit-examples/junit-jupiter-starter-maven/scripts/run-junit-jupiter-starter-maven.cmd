# 1) Plain tests
.\mvnw.cmd clean test
# runs unit tests (Surefire + JUnit Jupiter)

# 2) JaCoCo coverage
.\mvnw.cmd clean test jacoco:report
# report: target/site/jacoco/index.html

# 3) PIT mutation testing
.\mvnw.cmd clean test org.pitest:pitest-maven:mutationCoverage
# report: target/pit-reports/*/index.html

# 4) Assertion density
.\mvnw.cmd test-compile exec:java@assertion-density
# metric printed in console

# 5) PMD on main & test sources
.\mvnw.cmd pmd:check
# report: target/site/pmd.html

# 6) CODEX - only Codex tests and copied reports with Codex labels
.\mvnw.cmd clean test -Dtest=*TestCodex

.\mvnw.cmd jacoco:report
powershell -NoProfile -Command "if (Test-Path 'target\\site\\jacoco') { Copy-Item -Recurse -Force 'target\\site\\jacoco' 'target\\site\\jacocoCodex' }"
REM Codex JaCoCo report: target\site\jacocoCodex\index.html

.\mvnw.cmd org.pitest:pitest-maven:mutationCoverage -DtargetTests="com.example.project.*Codex" -DtargetClasses="com.example.project.*" -DmutationThreshold=0
powershell -NoProfile -Command "if (Test-Path 'target\\pit-reports') { Copy-Item -Recurse -Force 'target\\pit-reports' 'target\\pit-reports-Codex' }"
REM Codex PIT report: target\pit-reports-Codex\index.html

.\mvnw.cmd pmd:check
powershell -NoProfile -Command "if (Test-Path 'target\\site\\pmd.html') { Copy-Item -Force 'target\\site\\pmd.html' 'target\\site\\pmd-Codex.html' }"
REM Codex PMD report: target\site\pmd-Codex.html

# 7) Everything in one go
.\mvnw.cmd clean verify
# all reports generated in target/site and target/pit-reports


.\mvnw.cmd -Dtest=* -Djunit.jupiter.tags=human test
.\mvnw.cmd -Dtest=* -Djunit.jupiter.tags=gpt test
.\mvnw.cmd -Dtest=* -Djunit.jupiter.tags=codex test

.\mvnw.cmd -Pcodex verify

Start-Process "target\site\jacoco\index.html"
Start-Process "target\site\pmd.html"
Start-Process "target\pit-reports\index.html"


# CLAUDE - only Claude tests and copied reports with Claude labels
.\mvnw.cmd clean test -Dtest=*TestsClaude -DfailIfNoTests=false

.\mvnw.cmd jacoco:report
powershell -NoProfile -Command "if (Test-Path 'target\\site\\jacoco') { Copy-Item -Recurse -Force 'target\\site\\jacoco' 'target\\site\\jacocoClaude' }"
REM Claude JaCoCo report: target\site\jacocoClaude\index.html

.\mvnw.cmd org.pitest:pitest-maven:mutationCoverage -DtargetTests="com.example.project.*Claude" -DtargetClasses="com.example.project.*" -DmutationThreshold=0
powershell -NoProfile -Command "if (Test-Path 'target\\pit-reports') { Copy-Item -Recurse -Force 'target\\pit-reports' 'target\\pit-reports-Claude' }"
REM Claude PIT report: target\pit-reports-Claude\index.html

.\mvnw.cmd pmd:check
powershell -NoProfile -Command "if (Test-Path 'target\\site\\pmd.html') { Copy-Item -Force 'target\\site\\pmd.html' 'target\\site\\pmd-Claude.html' }"
REM Claude PMD report: target\site\pmd-Claude.html

# CLAUDE - via tag filter
.\mvnw.cmd -Dtest=* -Djunit.jupiter.tags=claude test

# CLAUDE - ALL IN ONE (verify)
.\mvnw.cmd clean test jacoco:report org.pitest:pitest-maven:mutationCoverage pmd:check -Dtest=*TestsClaude -DfailIfNoTests=false -DtargetTests="com.example.project.*Claude" -DtargetClasses="com.example.project.*"
powershell -NoProfile -Command "if (Test-Path 'target\\site\\jacoco') { Copy-Item -Recurse -Force 'target\\site\\jacoco' 'target\\site\\jacocoClaude' }"
powershell -NoProfile -Command "if (Test-Path 'target\\pit-reports') { Copy-Item -Recurse -Force 'target\\pit-reports' 'target\\pit-reports-Claude' }"
powershell -NoProfile -Command "if (Test-Path 'target\\site\\pmd.html') { Copy-Item -Force 'target\\site\\pmd.html' 'target\\site\\pmd-Claude.html' }"
REM Claude JaCoCo report:  target\site\jacocoClaude\index.html
REM Claude PIT report:     target\pit-reports-Claude\<timestamp>\index.html
REM Claude PMD report:     target\site\pmd-Claude.html
Start-Process "target\site\jacocoClaude\index.html"
powershell -NoProfile -Command "Start-Process (Get-ChildItem 'target\\pit-reports-Claude' | Sort-Object LastWriteTime -Descending | Select-Object -First 1 | ForEach-Object { Join-Path $_.FullName 'index.html' })"
Start-Process "target\site\pmd-Claude.html"
