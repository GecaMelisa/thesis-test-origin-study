HUMAN

REM HUMAN - UNIT/INTEGRATION (JUnit)
.\gradlew.bat -PincludeTags=human clean test --no-daemon --console=plain

REM HUMAN - JaCoCo coverage
.\gradlew.bat jacocoTestReport --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\jacoco') { Copy-Item -Recurse -Force 'build\\reports\\jacoco' 'build\\reports\\jacocoHuman' }"
REM Human JaCoCo report: build\reports\jacocoHuman\index.html
start "" "build\reports\jacocoHuman\index.html"


REM HUMAN - PIT mutation
.\gradlew.bat pitest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pitest') { Copy-Item -Recurse -Force 'build\\reports\\pitest' 'build\\reports\\pitestHuman' }"
REM Human PIT report: build\reports\pitestHuman\index.html
start "" "build\reports\pitestHuman\index.html"


REM HUMAN - PMD static analysis
.\gradlew.bat pmdTest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pmd') { Copy-Item -Recurse -Force 'build\\reports\\pmd' 'build\\reports\\pmdHuman' }"
REM Human PMD report folder: build\reports\pmdHuman\
start "" "build\reports\pmdHuman"




#JaCoCo
.\gradlew.bat clean test jacocoTestReport --no-daemon --console=plain

#PIT
.\gradlew.bat clean pitest --no-daemon --console=plain

#ASSERTION-DENSITY
.\scripts\calc-assertion-density.ps1

#PMD
.\gradlew.bat pmdTest --no-daemon --console=plain

#GPT COMMANDS (explicitly run only GPT tests)
#GPT - UNIT/INTEGRATION (JUnit)
.\gradlew.bat -PincludeTags=gpt clean test --no-daemon --console=plain

#CODEX COMMANDS (explicitly run only Codex tests and copy reports)
#CODEX - UNIT/INTEGRATION (JUnit)
.\gradlew.bat -PincludeTags=codex clean test --no-daemon --console=plain

#CODEX - JaCoCo coverage
.\gradlew.bat jacocoTestReport --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\jacoco') { Copy-Item -Recurse -Force 'build\\reports\\jacoco' 'build\\reports\\jacocoCodex' }"
REM Codex JaCoCo report: build\reports\jacocoCodex\index.html

#CODEX - PIT mutation
.\gradlew.bat pitest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pitest') { Copy-Item -Recurse -Force 'build\\reports\\pitest' 'build\\reports\\pitestCodex' }"
REM Codex PIT report: build\reports\pitestCodex\index.html

#CODEX - PMD static analysisd
.\gradlew.bat pmdTest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pmd') { Copy-Item -Recurse -Force 'build\\reports\\pmd' 'build\\reports\\pmdCodex' }"
REM Codex PMD report folder: build\reports\pmdCodex\


CODEX - ALL IN ONE
.\gradlew.bat -PincludeTags=codex clean test jacocoTestReport pitest pmdTest --no-daemon --console=plain


ASSERTION DENSITY
powershell -NoProfile -File .\scripts\junit-jupiter-extensions\scripts\calc-assertion-density.ps1


#CLAUDE COMMANDS (explicitly run only Claude tests and copy reports)
#CLAUDE - UNIT/INTEGRATION (JUnit)
.\gradlew.bat -PincludeTags=claude clean test --no-daemon --console=plain

#CLAUDE - JaCoCo coverage
.\gradlew.bat jacocoTestReport --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\jacoco') { Copy-Item -Recurse -Force 'build\\reports\\jacoco' 'build\\reports\\jacocoClaude' }"
REM Claude JaCoCo report: build\reports\jacocoClaude\index.html

#CLAUDE - PIT mutation
.\gradlew.bat pitest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pitest') { Copy-Item -Recurse -Force 'build\\reports\\pitest' 'build\\reports\\pitestClaude' }"
REM Claude PIT report: build\reports\pitestClaude\index.html

#CLAUDE - PMD static analysis
.\gradlew.bat pmdTest --no-daemon --console=plain
powershell -NoProfile -Command "if (Test-Path 'build\\reports\\pmd') { Copy-Item -Recurse -Force 'build\\reports\\pmd' 'build\\reports\\pmdClaude' }"
REM Claude PMD report folder: build\reports\pmdClaude\

CLAUDE - ALL IN ONE
.\gradlew.bat -PincludeTags=claude clean test jacocoTestReport pitest pmdTest --no-daemon --console=plain
