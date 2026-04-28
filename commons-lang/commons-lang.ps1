# Setup Maven
$mavenBin = "$PSScriptRoot\.mvn-bin\apache-maven-3.9.9\bin"
if (Test-Path $mavenBin) { $env:PATH = "$mavenBin;$env:PATH" }
$env:MAVEN_OPTS = "-Dmaven.resolver.transport=wagon -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true"

$HUMAN_TESTS = "org.apache.commons.lang3.StringUtilsTest,org.apache.commons.lang3.StringUtilsEmptyBlankTest,org.apache.commons.lang3.RandomStringUtilsTest,org.apache.commons.lang3.math.NumberUtilsTest"
$CLAUDE_TESTS = "org.apache.commons.lang3.StringUtilsClaudeTest,org.apache.commons.lang3.RandomStringUtilsClaudeTest,org.apache.commons.lang3.math.NumberUtilsClaudeTest"
$CHATGPT_TESTS = "org.apache.commons.lang3.StringUtilsGPTTest,org.apache.commons.lang3.RandomStringUtilsGPTTest,org.apache.commons.lang3.math.NumberUtilsGPTTest"
$PIT_TARGET  = "org.apache.commons.lang3.StringUtils,org.apache.commons.lang3.RandomStringUtils,org.apache.commons.lang3.math.NumberUtils"
$SKIP = @("-Drat.skip=true", "-Dcommons.jacoco.haltOnFailure=false")

# -----------------------------------------------------------------------
# HUMAN - ALL IN ONE
# -----------------------------------------------------------------------
mvn clean test "-Dtest=$HUMAN_TESTS" jacoco:report pmd:pmd @SKIP
mvn surefire-report:report-only
if (Test-Path "target\reports\surefire.html")    { Copy-Item -Force "target\reports\surefire.html" "target\reports\surefire-human.html" }
if (Test-Path "target\reports\pmd.html")         { Copy-Item -Force "target\reports\pmd.html" "target\reports\pmd-human.html" }
if (Test-Path "target\site\jacoco")              { Copy-Item -Recurse -Force "target\site\jacoco" "target\site\jacocoHuman" }
mvn org.pitest:pitest-maven:mutationCoverage "-DtargetClasses=$PIT_TARGET" "-DtargetTests=$HUMAN_TESTS" "-DoutputFormats=HTML,XML" @SKIP
if (Test-Path "target\pit-reports") { Copy-Item -Recurse -Force "target\pit-reports" "target\pitHuman" }
Start-Process "target\reports\surefire-human.html"
Start-Process "target\site\jacocoHuman\index.html"
Start-Process "target\pitHuman\index.html"
Start-Process "target\reports\pmd-human.html"

# -----------------------------------------------------------------------
# CLAUDE - ALL IN ONE
# -----------------------------------------------------------------------
mvn clean test "-Dtest=$CLAUDE_TESTS" jacoco:report pmd:pmd @SKIP
mvn surefire-report:report-only
if (Test-Path "target\reports\surefire.html")    { Copy-Item -Force "target\reports\surefire.html" "target\reports\surefire-claude.html" }
if (Test-Path "target\reports\pmd.html")         { Copy-Item -Force "target\reports\pmd.html" "target\reports\pmd-claude.html" }
if (Test-Path "target\site\jacoco")              { Copy-Item -Recurse -Force "target\site\jacoco" "target\site\jacocoClaude" }
mvn org.pitest:pitest-maven:mutationCoverage "-DtargetClasses=$PIT_TARGET" "-DtargetTests=$CLAUDE_TESTS" "-DoutputFormats=HTML,XML" @SKIP
if (Test-Path "target\pit-reports") { Copy-Item -Recurse -Force "target\pit-reports" "target\pitClaude" }
Start-Process "target\reports\surefire-claude.html"
Start-Process "target\site\jacocoClaude\index.html"
Start-Process "target\pitClaude\index.html"
Start-Process "target\reports\pmd-claude.html"

# -----------------------------------------------------------------------
# CHATGPT - ALL IN ONE
# -----------------------------------------------------------------------

mvn clean test "-Dtest=$CHATGPT_TESTS" jacoco:report pmd:pmd @SKIP
mvn surefire-report:report-only
if (Test-Path "target\reports\surefire.html")    { Copy-Item -Force "target\reports\surefire.html" "target\reports\surefire-chatgpt.html" }
if (Test-Path "target\reports\pmd.html")         { Copy-Item -Force "target\reports\pmd.html" "target\reports\pmd-chatgpt.html" }
if (Test-Path "target\site\jacoco")              { Copy-Item -Recurse -Force "target\site\jacoco" "target\site\jacocoChatGPT" }
mvn org.pitest:pitest-maven:mutationCoverage "-DtargetClasses=$PIT_TARGET" "-DtargetTests=$CHATGPT_TESTS" "-DoutputFormats=HTML,XML" @SKIP
if (Test-Path "target\pit-reports") { Copy-Item -Recurse -Force "target\pit-reports" "target\pitChatGPT" }
Start-Process "target\reports\surefire-chatgpt.html"
Start-Process "target\site\jacocoChatGPT\index.html"
Start-Process "target\pitChatGPT\index.html"
Start-Process "target\reports\pmd-chatgpt.html"
