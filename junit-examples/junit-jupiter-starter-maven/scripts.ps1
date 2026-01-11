<#
================================================================================
  Thesis Toolkit — scripts.ps1
  Location: project root (same folder as pom.xml)

  WHAT THIS FILE DOES
  - compare : Run AI-only and Human-only test suites, collect coverage (JaCoCo)
              and mutation (PIT) reports under reports\ai and reports\human
  - parse   : Parse JaCoCo + PIT + Surefire XML into reports\comparison.csv
  - suite   : Run just one suite (ai|human) for quick iterations
  - open    : Open the four HTML dashboards (AI/Human — JaCoCo/PIT)
  - clean   : Clean Maven target/ and the reports/ folder
  - help    : Show usage

  QUICK START
    cd C:\Users\melis\junit-examples\junit-jupiter-starter-maven
    Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
    .\scripts.ps1 compare
    .\scripts.ps1 parse
    .\scripts.ps1 open

  REQUIREMENTS (already set by your pom.xml)
  - JDK (17 recommended) + Maven installed
  - JUnit 5, JaCoCo, PIT plugin + PIT JUnit5 adapter in pom.xml
  - Test classes:
      com.example.project.CalculatorAITests    (AI suite)
      com.example.project.CalculatorHumanTests (Human suite)
================================================================================
#>

param(
  # command: compare | parse | suite | open | clean | help
  [ValidateSet("compare","parse","suite","open","clean","help")]
  [string]$cmd = "help",

  # used only when $cmd = "suite" → ai | human
  [ValidateSet("ai","human")]
  [string]$suite = "ai"
)

# ----------------------- SETTINGS -----------------------
$MODULE_DIR = "C:\Users\melis\junit-examples\junit-jupiter-starter-maven"
$REPORTS    = Join-Path $MODULE_DIR "reports"

# Surefire selectors for unit tests (by class name)
$AI_PATTERN = "*AITests"
$HUM_PATTERN= "*HumanTests"

# Fully-qualified class names used by PIT for JUnit5 test discovery
$AI_FQCN  = "com.example.project.CalculatorAITests"
$HUM_FQCN = "com.example.project.CalculatorHumanTests"

$ErrorActionPreference = "Stop"
# -------------------------------------------------------

Set-Location $MODULE_DIR
New-Item -Force -ItemType Directory -Path $REPORTS | Out-Null

function Save-Reports([string]$label) {
  # Copies outputs from target/ to reports/<label>/
  $dest = Join-Path $REPORTS $label
  New-Item -Force -ItemType Directory -Path $dest | Out-Null

  # JaCoCo (HTML + .exec)
  if (Test-Path ".\target\site\jacoco") { Copy-Item -Recurse -Force .\target\site\jacoco $dest\jacoco }
  if (Test-Path ".\target\jacoco.exec") { Copy-Item -Force .\target\jacoco.exec $dest\jacoco.exec }

  # PIT (copy everything from target\pit-reports into reports\<label>\pit)
  $pitRoot = ".\target\pit-reports"
  $pitDest = Join-Path $dest "pit"
  New-Item -Force -ItemType Directory -Path $pitDest | Out-Null
  if (Test-Path $pitRoot) { Copy-Item -Recurse -Force "$pitRoot\*" $pitDest }

  # Surefire (for test counts & total runtime)
  if (Test-Path ".\target\surefire-reports") {
    Copy-Item -Recurse -Force .\target\surefire-reports $dest\surefire
  }
}

function Run-Comparison {
  Write-Host "`n=== Running AI suite (coverage + mutation) ===" -ForegroundColor Cyan
  mvn -U -q "-Dtest=$AI_PATTERN" clean verify
  mvn -q "-DtargetTests=$AI_FQCN" org.pitest:pitest-maven:mutationCoverage
  Save-Reports "ai"

  Write-Host "`n=== Running Human suite (coverage + mutation) ===" -ForegroundColor Cyan
  mvn -U -q "-Dtest=$HUM_PATTERN" clean verify
  mvn -q "-DtargetTests=$HUM_FQCN" org.pitest:pitest-maven:mutationCoverage
  Save-Reports "human"

  Write-Host "`nReports saved under: $REPORTS" -ForegroundColor Green
  Write-Host " - $REPORTS\ai\jacoco (coverage), $REPORTS\ai\pit (mutation)"
  Write-Host " - $REPORTS\human\jacoco (coverage), $REPORTS\human\pit (mutation)"
}

# ---------------- XML PARSERS → CSV ----------------
function Get-JaCoCo([string]$suite) {
  # Reads reports\<suite>\jacoco\jacoco.xml and returns coverage stats
  $xmlPath = Join-Path $REPORTS "$suite\jacoco\jacoco.xml"
  if (!(Test-Path $xmlPath)) { throw "JaCoCo XML not found for $suite at $xmlPath" }
  [xml]$xml = Get-Content $xmlPath

  $line   = $xml.report.counter  | Where-Object { $_.type -eq "LINE" }   | Select-Object -First 1
  $branch = $xml.report.counter  | Where-Object { $_.type -eq "BRANCH" } | Select-Object -First 1

  if (-not $line) {
    $lineMissed  = 0; $lineCovered  = 0
    $xml.report.package.class.counter | Where-Object { $_.type -eq "LINE" } | ForEach-Object {
      $lineMissed  += [int]$_.missed; $lineCovered += [int]$_.covered
    }
  } else { $lineMissed = [int]$line.missed; $lineCovered = [int]$line.covered }

  if (-not $branch) {
    $branchMissed = 0; $branchCovered = 0
    $xml.report.package.class.counter | Where-Object { $_.type -eq "BRANCH" } | ForEach-Object {
      $branchMissed += [int]$_.missed; $branchCovered += [int]$_.covered
    }
  } else { $branchMissed = [int]$branch.missed; $branchCovered = [int]$branch.covered }

  $linePct   = if (($lineMissed + $lineCovered) -gt 0)   { [math]::Round(100 * $lineCovered   / ($lineMissed + $lineCovered), 2) } else { 0 }
  $branchPct = if (($branchMissed + $branchCovered) -gt 0){ [math]::Round(100 * $branchCovered / ($branchMissed + $branchCovered), 2) } else { 0 }

  [pscustomobject]@{
    LineCovered=$lineCovered; LineMissed=$lineMissed; LineCoveragePct=$linePct
    BranchCovered=$branchCovered; BranchMissed=$branchMissed; BranchCoveragePct=$branchPct
  }
}

function Get-PIT([string]$suite) {
  # Finds mutations.xml under reports\<suite>\pit (or target fallback)
  $pitDir = Join-Path $REPORTS "$suite\pit"
  if (!(Test-Path $pitDir)) { $pitDir = Join-Path $MODULE_DIR "target\pit-reports" }
  if (!(Test-Path $pitDir)) { throw "PIT folder not found for $suite at $pitDir" }

  $mutXml = Get-ChildItem -Recurse -Filter "mutations.xml" -Path $pitDir | Select-Object -First 1
  if (-not $mutXml) { throw "mutations.xml not found under $pitDir" }
  [xml]$xml = Get-Content $mutXml.FullName

  $mutants = @($xml.mutations.mutation)
  if ($mutants.Count -eq 0) {
    return [pscustomobject]@{
      MutantsTotal=0; MutantsDetected=0; MutationScorePct=0
      Killed=0; Survived=0; NoCoverage=0; TimedOut=0; MemoryError=0; RunError=0; NonViable=0
    }
  }

  $statusCounts = @{ KILLED=0; SURVIVED=0; NO_COVERAGE=0; TIMED_OUT=0; MEMORY_ERROR=0; RUN_ERROR=0; NON_VIABLE=0 }
  foreach ($m in $mutants) {
    $s = $m.status.ToString()
    if ($statusCounts.ContainsKey($s)) { $statusCounts[$s]++ }
  }

  $total    = $statusCounts.KILLED + $statusCounts.SURVIVED + $statusCounts.NO_COVERAGE + $statusCounts.TIMED_OUT + $statusCounts.MEMORY_ERROR + $statusCounts.RUN_ERROR
  $detected = $statusCounts.KILLED + $statusCounts.TIMED_OUT + $statusCounts.MEMORY_ERROR + $statusCounts.RUN_ERROR
  $scorePct = if ($total -gt 0) { [math]::Round(100 * $detected / $total, 2) } else { 0 }

  [pscustomobject]@{
    MutantsTotal=$total; MutantsDetected=$detected; MutationScorePct=$scorePct
    Killed=$statusCounts.KILLED; Survived=$statusCounts.SURVIVED; NoCoverage=$statusCounts.NO_COVERAGE
    TimedOut=$statusCounts.TIMED_OUT; MemoryError=$statusCounts.MEMORY_ERROR; RunError=$statusCounts.RUN_ERROR; NonViable=$statusCounts.NON_VIABLE
  }
}

function Get-Surefire([string]$suite) {
  # Aggregates test counts & time from reports\<suite>\surefire\TEST-*.xml
  $sfDir = Join-Path $REPORTS "$suite\surefire"
  if (!(Test-Path $sfDir)) { return [pscustomobject]@{ Tests=$null; Failures=$null; Errors=$null; Skipped=$null; TimeSeconds=$null } }

  $tests=0; $fail=0; $err=0; $skip=0; $time=0.0
  Get-ChildItem -Path $sfDir -Filter "TEST-*.xml" | ForEach-Object {
    [xml]$x = Get-Content $_.FullName
    foreach ($ts in $x.testsuite) {
      $tests += [int]$ts.tests
      $fail  += [int]$ts.failures
      $err   += [int]$ts.errors
      $skip  += [int]$ts.skipped
      $time  += [double]$ts.time
    }
  }
  [pscustomobject]@{ Tests=$tests; Failures=$fail; Errors=$err; Skipped=$skip; TimeSeconds=[math]::Round($time,3) }
}

function Row-ForSuite([string]$suite) {
  $cov = Get-JaCoCo $suite
  $mut = Get-PIT    $suite
  $sf  = Get-Surefire $suite
  [pscustomobject]@{
    Suite=$suite
    LineCovered=$cov.LineCovered; LineMissed=$cov.LineMissed; LineCoveragePct=$cov.LineCoveragePct
    BranchCovered=$cov.BranchCovered; BranchMissed=$cov.BranchMissed; BranchCoveragePct=$cov.BranchCoveragePct
    MutantsTotal=$mut.MutantsTotal; MutantsDetected=$mut.MutantsDetected; MutationScorePct=$mut.MutationScorePct
    Killed=$mut.Killed; Survived=$mut.Survived; NoCoverage=$mut.NoCoverage; TimedOut=$mut.TimedOut; MemoryError=$mut.MemoryError; RunError=$mut.RunError; NonViable=$mut.NonViable
    Tests=$sf.Tests; Failures=$sf.Failures; Errors=$sf.Errors; Skipped=$sf.Skipped; TimeSeconds=$sf.TimeSeconds
  }
}

function Parse-ToCsv {
  $outCsv = Join-Path $REPORTS "comparison.csv"
  $rows = @()
  $rows += Row-ForSuite "ai"
  $rows += Row-ForSuite "human"
  $rows | Export-Csv -Path $outCsv -NoTypeInformation -Encoding UTF8
  Write-Host "✅ CSV written to $outCsv" -ForegroundColor Green
}

function Run-Suite([string]$which) {
  if ($which -eq "ai") {
    Write-Host "`n=== Running AI suite only ===" -ForegroundColor Cyan
    mvn -U "-Dtest=$AI_PATTERN" clean verify
    mvn "-DtargetTests=$AI_FQCN" org.pitest:pitest-maven:mutationCoverage
  } else {
    Write-Host "`n=== Running HUMAN suite only ===" -ForegroundColor Cyan
    mvn -U "-Dtest=$HUM_PATTERN" clean verify
    mvn "-DtargetTests=$HUM_FQCN" org.pitest:pitest-maven:mutationCoverage
  }
  Write-Host "Done. Check target\site\jacoco (coverage) and target\pit-reports (mutation)." -ForegroundColor Green
}

function Open-Reports {
  start "$REPORTS\ai\jacoco\index.html"
  start "$REPORTS\ai\pit\index.html"
  start "$REPORTS\human\jacoco\index.html"
  start "$REPORTS\human\pit\index.html"
}

function Clean-All {
  mvn -q clean
  if (Test-Path ".\reports") { Remove-Item -Recurse -Force ".\reports" }
  Write-Host "Cleaned target/ and reports/." -ForegroundColor Yellow
}

function Show-Help {
  @"
USAGE:
  .\scripts.ps1 compare           # Run AI & Human suites, collect reports to reports\...
  .\scripts.ps1 parse             # Build reports\comparison.csv from JaCoCo + PIT + Surefire
  .\scripts.ps1 suite -suite ai   # Run only AI suite (coverage + mutation)
  .\scripts.ps1 suite -suite human# Run only Human suite (coverage + mutation)
  .\scripts.ps1 open              # Open the four HTML dashboards
  .\scripts.ps1 clean             # Delete target/ and reports/

NOTES:
- If execution is blocked:
    Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
- Reports live at:
    reports\ai\jacoco\index.html     (AI coverage)
    reports\ai\pit\index.html        (AI mutation)
    reports\human\jacoco\index.html  (Human coverage)
    reports\human\pit\index.html     (Human mutation)
- CSV output:
    reports\comparison.csv (AI vs Human metrics)
"@ | Write-Host
}

# ---------------- COMMAND DISPATCH ----------------
switch ($cmd) {
  "compare" { Run-Comparison; break }
  "parse"   { Parse-ToCsv; break }
  "suite"   { Run-Suite $suite; break }
  "open"    { Open-Reports; break }
  "clean"   { Clean-All; break }
  default   { Show-Help; break }
}


#HOW TO USE

cd C:\Users\melis\junit-examples\junit-jupiter-starter-maven
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

# end-to-end (AI + Human) and collect reports
.\scripts.ps1 compare

# export CSV with metrics
.\scripts.ps1 parse
start .\reports\comparison.csv

# open dashboards
.\scripts.ps1 open

# run only AI or only Human
.\scripts.ps1 suite -suite ai
.\scripts.ps1 suite -suite human

# clean everything
.\scripts.ps1 clean
