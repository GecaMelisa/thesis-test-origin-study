Set-StrictMode -Version 3.0
$ErrorActionPreference = 'Stop'

$TESTS_PROJ  = "$PSScriptRoot\tests\Humanizer.Tests\Humanizer.Tests.csproj"
$TFM         = "net11.0"
$COVERAGE    = "$PSScriptRoot\coverage"
$SKIP_PROPS  = @()

$HUMAN_FILTER  = "FullyQualifiedName~StringHumanize|FullyQualifiedName~NumberToWords"
$CLAUDE_FILTER = "FullyQualifiedName~StringHumanizeClaude|FullyQualifiedName~NumberToWordsClaude"

Write-Host "============================================================="
Write-Host " Humanizer Experiment Script"
Write-Host " Scope: StringHumanizeExtensions, NumberToWordsExtension"
Write-Host "============================================================="

# -----------------------------------------------------------------------
# BUILD
# -----------------------------------------------------------------------
Write-Host "`n[1/2] Restoring and building solution..."
dotnet restore "$PSScriptRoot\Humanizer.slnx"
dotnet build   "$PSScriptRoot\Humanizer.slnx" -c Release --no-restore

# -----------------------------------------------------------------------
# HUMAN - ALL IN ONE
# -----------------------------------------------------------------------
Write-Host "`n--- HUMAN: tests ---"
dotnet test $TESTS_PROJ --framework $TFM --no-build -c Release `
    --filter $HUMAN_FILTER `
    /p:CollectCoverage=true `
    /p:CoverletOutput="$COVERAGE\human\" `
    /p:CoverletOutputFormat=opencover

if (Test-Path "$COVERAGE\human") {
    Copy-Item -Recurse -Force "$COVERAGE\human" "$COVERAGE\human-snapshot"
}

Write-Host "`n--- HUMAN: mutation testing (Stryker) ---"
dotnet tool restore
dotnet stryker `
    --project Humanizer.csproj `
    --test-project $TESTS_PROJ `
    --target-framework $TFM `
    --mutation-level Standard `
    --reporter html `
    --reporter progress

$latest = Get-ChildItem "$PSScriptRoot\StrykerOutput" -Directory |
          Sort-Object LastWriteTime -Descending | Select-Object -First 1
if ($latest) { Copy-Item -Recurse -Force $latest.FullName "$PSScriptRoot\StrykerOutput\pitHuman" }

# -----------------------------------------------------------------------
# CLAUDE - ALL IN ONE
# -----------------------------------------------------------------------
Write-Host "`n--- CLAUDE: tests ---"
dotnet test $TESTS_PROJ --framework $TFM --no-build -c Release `
    --filter $CLAUDE_FILTER `
    /p:CollectCoverage=true `
    /p:CoverletOutput="$COVERAGE\claude\" `
    /p:CoverletOutputFormat=opencover

if (Test-Path "$COVERAGE\claude") {
    Copy-Item -Recurse -Force "$COVERAGE\claude" "$COVERAGE\claude-snapshot"
}

Write-Host "`n--- CLAUDE: mutation testing (Stryker) ---"
dotnet stryker `
    --project Humanizer.csproj `
    --test-project $TESTS_PROJ `
    --target-framework $TFM `
    --mutation-level Standard `
    --reporter html `
    --reporter progress

$latest = Get-ChildItem "$PSScriptRoot\StrykerOutput" -Directory |
          Sort-Object LastWriteTime -Descending | Select-Object -First 1
if ($latest) { Copy-Item -Recurse -Force $latest.FullName "$PSScriptRoot\StrykerOutput\pitClaude" }

# -----------------------------------------------------------------------
# STATIC ANALYSIS
# -----------------------------------------------------------------------
Write-Host "`n--- Static analysis (dotnet format) ---"
dotnet format "$PSScriptRoot\Humanizer.slnx" --verify-no-changes --verbosity diagnostic
if ($LASTEXITCODE -ne 0) {
    Write-Host "NOTE: formatting issues detected. Run 'dotnet format Humanizer.slnx' to auto-fix."
}

# -----------------------------------------------------------------------
# OPEN REPORTS
# -----------------------------------------------------------------------
$humanCoverage  = "$COVERAGE\human\index.htm"
$claudeCoverage = "$COVERAGE\claude\index.htm"
$humanPit       = "$PSScriptRoot\StrykerOutput\pitHuman\index.html"
$claudePit      = "$PSScriptRoot\StrykerOutput\pitClaude\index.html"

foreach ($report in @($humanCoverage, $claudeCoverage, $humanPit, $claudePit)) {
    if (Test-Path $report) { Start-Process $report }
}

Write-Host "`n============================================================="
Write-Host " All steps completed."
Write-Host " Human coverage  : $COVERAGE\human\"
Write-Host " Claude coverage : $COVERAGE\claude\"
Write-Host " Human Stryker   : $PSScriptRoot\StrykerOutput\pitHuman\"
Write-Host " Claude Stryker  : $PSScriptRoot\StrykerOutput\pitClaude\"
Write-Host "============================================================="
