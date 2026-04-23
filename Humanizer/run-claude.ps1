Set-StrictMode -Version 3.0
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Web

$TESTS_PROJ   = "$PSScriptRoot\tests\Humanizer.Tests\Humanizer.Tests.csproj"
$TFM          = "net11.0"
$OUT          = "$PSScriptRoot\reports\claude"
$COVERAGE_XML = "$OUT\coverage.cobertura.xml"
$COVERAGE_HTML= "$OUT\coverage-html"
$TEST_HTML    = "$OUT\test-results.html"
$SA_HTML      = "$OUT\static-analysis.html"
$AD_HTML      = "$OUT\assertion-density.html"

$TEST_FILES   = @(
    "$PSScriptRoot\tests\Humanizer.Tests\StringHumanizeClaudeTests.cs",
    "$PSScriptRoot\tests\Humanizer.Tests\NumberToWordsClaudeTests.cs"
)

Write-Host "============================================================="
Write-Host " CLAUDE tests: StringHumanizeClaudeTests, NumberToWordsClaudeTests"
Write-Host "============================================================="

if (-not (Test-Path $OUT)) { New-Item -ItemType Directory -Force -Path $OUT | Out-Null }

# -----------------------------------------------------------------------
# [1] BUILD
# -----------------------------------------------------------------------
Write-Host "`n[1/4] Build..."
dotnet build "$PSScriptRoot\Humanizer.slnx" -c Release

# -----------------------------------------------------------------------
# [2] TESTS + COVERAGE
# -----------------------------------------------------------------------
Write-Host "`n[2/4] Tests + coverage..."
dotnet test $TESTS_PROJ --framework $TFM --no-build -c Release `
    --filter-class "Humanizer.Tests.StringHumanizeClaudeTests" `
    --filter-class "Humanizer.Tests.NumberToWordsClaudeTests" `
    --coverage `
    --coverage-output $COVERAGE_XML `
    --coverage-output-format cobertura `
    --report-xunit-html `
    --report-xunit-html-filename "test-results.html" `
    --results-directory $OUT

dotnet tool restore | Out-Null
dotnet reportgenerator `
    "-reports:$COVERAGE_XML" `
    "-targetdir:$COVERAGE_HTML" `
    "-reporttypes:Html" `
    "-verbosity:Warning" | Out-Null

# -----------------------------------------------------------------------
# [3] STATIC ANALYSIS
# -----------------------------------------------------------------------
Write-Host "`n[3/4] Static analysis..."
$saLines = dotnet format "$PSScriptRoot\Humanizer.slnx" --verify-no-changes --verbosity diagnostic 2>&1
$saStatus = if ($LASTEXITCODE -eq 0) { "PASSED" } else { "ISSUES FOUND" }
$saColor  = if ($LASTEXITCODE -eq 0) { "green" } else { "red" }
$saRows   = ($saLines | ForEach-Object { "<tr><td>$([System.Web.HttpUtility]::HtmlEncode($_))</td></tr>" }) -join "`n"
@"
<!DOCTYPE html><html><head><meta charset='utf-8'>
<title>Static Analysis - Claude</title>
<style>body{font-family:monospace;padding:20px} h1{font-size:1.4em}
.status{font-weight:bold;color:$saColor;font-size:1.2em}
table{border-collapse:collapse;width:100%} td{padding:3px 8px;border-bottom:1px solid #eee;white-space:pre-wrap}
</style></head><body>
<h1>Static Analysis (dotnet format) - Claude Tests</h1>
<p class='status'>Status: $saStatus</p>
<table>$saRows</table>
</body></html>
"@ | Set-Content -LiteralPath $SA_HTML -Encoding UTF8
Write-Host "Static analysis: $saStatus"

# -----------------------------------------------------------------------
# [4] ASSERTION DENSITY
# -----------------------------------------------------------------------
Write-Host "`n[4/4] Assertion density..."
$totalMethods = 0
$totalAssertions = 0
$fileRows = ""
foreach ($f in $TEST_FILES) {
    $src        = Get-Content $f -Raw
    $methods    = ([regex]::Matches($src, '\[(?:Fact|Theory)\]')).Count
    $assertions = ([regex]::Matches($src, '\bAssert\.')).Count
    $d          = if ($methods -gt 0) { [Math]::Round($assertions / $methods, 2) } else { 0 }
    $fileRows  += "<tr><td>$(Split-Path $f -Leaf)</td><td>$methods</td><td>$assertions</td><td>$d</td></tr>`n"
    Write-Host "  $(Split-Path $f -Leaf): $methods tests, $assertions assertions"
    $totalMethods    += $methods
    $totalAssertions += $assertions
}
$density = if ($totalMethods -gt 0) { [Math]::Round($totalAssertions / $totalMethods, 2) } else { 0 }
@"
<!DOCTYPE html><html><head><meta charset='utf-8'>
<title>Assertion Density - Claude</title>
<style>body{font-family:sans-serif;padding:20px} h1{font-size:1.4em}
table{border-collapse:collapse;width:100%} th{background:#333;color:#fff;padding:8px 12px;text-align:left}
td{padding:7px 12px;border-bottom:1px solid #ddd} .total{font-weight:bold;background:#f5f5f5}
</style></head><body>
<h1>Assertion Density - Claude Tests</h1>
<table>
<tr><th>File</th><th>Test Methods</th><th>Assertions</th><th>Density (assert/test)</th></tr>
$fileRows
<tr class='total'><td>TOTAL</td><td>$totalMethods</td><td>$totalAssertions</td><td>$density</td></tr>
</table>
</body></html>
"@ | Set-Content -LiteralPath $AD_HTML -Encoding UTF8

# -----------------------------------------------------------------------
# OPEN REPORTS
# -----------------------------------------------------------------------
foreach ($f in @("$COVERAGE_HTML\index.html", $TEST_HTML, $SA_HTML, $AD_HTML)) {
    if (Test-Path $f) { Start-Process $f }
}

Write-Host "`n============================================================="
Write-Host " Done."
Write-Host "  Test results    : $TEST_HTML"
Write-Host "  Coverage HTML   : $COVERAGE_HTML\index.html"
Write-Host "  Static analysis : $SA_HTML"
Write-Host "  Assertion density: $AD_HTML"
Write-Host "============================================================="
