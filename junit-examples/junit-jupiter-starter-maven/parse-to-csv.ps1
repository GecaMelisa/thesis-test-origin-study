$ErrorActionPreference = "Stop"

$ROOT     = "C:\Users\melis\junit-examples\junit-jupiter-starter-maven"
$REPORTS  = Join-Path $ROOT "reports"
$OUT_CSV  = Join-Path $REPORTS "comparison.csv"

function Get-JaCoCo {
  param([string]$suite) # "ai" or "human"
  $xmlPath = Join-Path $REPORTS "$suite\jacoco\jacoco.xml"
  if (!(Test-Path $xmlPath)) { throw "JaCoCo XML not found for $suite at $xmlPath" }
  [xml]$xml = Get-Content $xmlPath

  # Prefer top-level report counters
  $line = $xml.report.counter | Where-Object { $_.type -eq "LINE" } | Select-Object -First 1
  $branch = $xml.report.counter | Where-Object { $_.type -eq "BRANCH" } | Select-Object -First 1

  # Fallback: sum over all package/class counters if top-level missing
  if (-not $line) {
    $lineMissed  = 0; $lineCovered  = 0
    $xml.report.package.class.counter | Where-Object { $_.type -eq "LINE" } | ForEach-Object {
      $lineMissed  += [int]$_.missed
      $lineCovered += [int]$_.covered
    }
  } else {
    $lineMissed  = [int]$line.missed
    $lineCovered = [int]$line.covered
  }

  if (-not $branch) {
    $branchMissed = 0; $branchCovered = 0
    $xml.report.package.class.counter | Where-Object { $_.type -eq "BRANCH" } | ForEach-Object {
      $branchMissed  += [int]$_.missed
      $branchCovered += [int]$_.covered
    }
  } else {
    $branchMissed  = [int]$branch.missed
    $branchCovered = [int]$branch.covered
  }

  $lineTotal   = [double]($lineMissed + $lineCovered)
  $branchTotal = [double]($branchMissed + $branchCovered)
  $linePct     = if ($lineTotal   -gt 0) { [math]::Round(100 * $lineCovered   / $lineTotal,   2) } else { 0 }
  $branchPct   = if ($branchTotal -gt 0) { [math]::Round(100 * $branchCovered / $branchTotal, 2) } else { 0 }

  return [pscustomobject]@{
    LineCovered      = $lineCovered
    LineMissed       = $lineMissed
    LineCoveragePct  = $linePct
    BranchCovered    = $branchCovered
    BranchMissed     = $branchMissed
    BranchCoveragePct= $branchPct
  }
}

function Get-PIT {
  param([string]$suite) # "ai" or "human"
  $pitDir = Join-Path $REPORTS "$suite\pit"
  if (!(Test-Path $pitDir)) { throw "PIT folder not found for $suite at $pitDir" }
  # Find the (single) timestamped dir that was copied
  $mutXml = Get-ChildItem -Recurse -Filter "mutations.xml" -Path $pitDir | Select-Object -First 1
  if (-not $mutXml) { throw "mutations.xml not found under $pitDir" }
  [xml]$xml = Get-Content $mutXml.FullName

  $mutants = @($xml.mutations.mutation)
  if ($mutants.Count -eq 0) {
    return [pscustomobject]@{
      MutantsTotal      = 0
      MutantsDetected   = 0
      MutationScorePct  = 0
      Killed            = 0
      Survived          = 0
      NoCoverage        = 0
      TimedOut          = 0
      MemoryError       = 0
      RunError          = 0
      NonViable         = 0
    }
  }

  # PIT statuses to count:
  $statusCounts = @{
    KILLED=0; SURVIVED=0; NO_COVERAGE=0; TIMED_OUT=0; MEMORY_ERROR=0; RUN_ERROR=0; NON_VIABLE=0
  }
  foreach ($m in $mutants) {
    $s = $m.status.ToString()
    if ($statusCounts.ContainsKey($s)) { $statusCounts[$s]++ }
  }

  # Total = all except NON_VIABLE (commonly excluded from denominator)
  $total    = $statusCounts.KILLED + $statusCounts.SURVIVED + $statusCounts.NO_COVERAGE + $statusCounts.TIMED_OUT + $statusCounts.MEMORY_ERROR + $statusCounts.RUN_ERROR
  $detected = $statusCounts.KILLED + $statusCounts.TIMED_OUT + $statusCounts.MEMORY_ERROR + $statusCounts.RUN_ERROR
  $scorePct = if ($total -gt 0) { [math]::Round(100 * $detected / $total, 2) } else { 0 }

  return [pscustomobject]@{
    MutantsTotal      = $total
    MutantsDetected   = $detected
    MutationScorePct  = $scorePct
    Killed            = $statusCounts.KILLED
    Survived          = $statusCounts.SURVIVED
    NoCoverage        = $statusCounts.NO_COVERAGE
    TimedOut          = $statusCounts.TIMED_OUT
    MemoryError       = $statusCounts.MEMORY_ERROR
    RunError          = $statusCounts.RUN_ERROR
    NonViable         = $statusCounts.NON_VIABLE
  }
}

function Get-Surefire {
  param([string]$suite) # "ai" or "human"
  $sfDir = Join-Path $REPORTS "$suite\surefire"
  if (!(Test-Path $sfDir)) {
    return [pscustomobject]@{ Tests= $null; Failures= $null; Errors= $null; Skipped= $null; TimeSeconds= $null }
  }
  $tests = 0; $fail=0; $err=0; $skip=0; $time=0.0
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
  return [pscustomobject]@{
    Tests      = $tests
    Failures   = $fail
    Errors     = $err
    Skipped    = $skip
    TimeSeconds= [math]::Round($time, 3)
  }
}

function Row-ForSuite {
  param([string]$suite)
  $cov = Get-JaCoCo -suite $suite
  $mut = Get-PIT    -suite $suite
  $sf  = Get-Surefire -suite $suite
  return [pscustomobject]@{
    Suite               = $suite
    LineCovered         = $cov.LineCovered
    LineMissed          = $cov.LineMissed
    LineCoveragePct     = $cov.LineCoveragePct
    BranchCovered       = $cov.BranchCovered
    BranchMissed        = $cov.BranchMissed
    BranchCoveragePct   = $cov.BranchCoveragePct
    MutantsTotal        = $mut.MutantsTotal
    MutantsDetected     = $mut.MutantsDetected
    MutationScorePct    = $mut.MutationScorePct
    Killed              = $mut.Killed
    Survived            = $mut.Survived
    NoCoverage          = $mut.NoCoverage
    TimedOut            = $mut.TimedOut
    MemoryError         = $mut.MemoryError
    RunError            = $mut.RunError
    NonViable           = $mut.NonViable
    Tests               = $sf.Tests
    Failures            = $sf.Failures
    Errors              = $sf.Errors
    Skipped             = $sf.Skipped
    TimeSeconds         = $sf.TimeSeconds
  }
}

$rows = @()
$rows += Row-ForSuite -suite "ai"
$rows += Row-ForSuite -suite "human"

$rows | Export-Csv -Path $OUT_CSV -NoTypeInformation -Encoding UTF8
Write-Host " CSV written to $OUT_CSV"
