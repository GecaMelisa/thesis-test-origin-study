# -------- settings --------
$MODULE_DIR = "C:\Users\melis\junit-examples\junit-jupiter-starter-maven"
$REPORTS    = Join-Path $MODULE_DIR "reports"
$AI_PATTERN = "*AITests"
$HUM_PATTERN= "*HumanTests"
# --------------------------

$ErrorActionPreference = "Stop"
Set-Location $MODULE_DIR
New-Item -Force -ItemType Directory -Path $REPORTS | Out-Null

function Save-Reports($label) {
  $dest = Join-Path $REPORTS $label
  New-Item -Force -ItemType Directory -Path $dest | Out-Null

  # JaCoCo
  if (Test-Path ".\target\site\jacoco") { Copy-Item -Recurse -Force .\target\site\jacoco $dest\jacoco }
  if (Test-Path ".\target\jacoco.exec") { Copy-Item -Force .\target\jacoco.exec $dest\jacoco.exec }

  # PIT (copy entire folder regardless of structure)
  $pitRoot = ".\target\pit-reports"
  if (Test-Path $pitRoot) {
    Copy-Item -Recurse -Force $pitRoot (Join-Path $dest "pit")
  }

  # Surefire (for test counts & run time)
  if (Test-Path ".\target\surefire-reports") { Copy-Item -Recurse -Force .\target\surefire-reports $dest\surefire }
}

# --- AI suite ---
mvn -U -q "-Dtest=$AI_PATTERN" clean verify
mvn -q "-DtargetTests=com.example.project.CalculatorAITests" org.pitest:pitest-maven:mutationCoverage
Save-Reports "ai"

# --- Human suite ---
mvn -U -q "-Dtest=$HUM_PATTERN" clean verify
mvn -q "-DtargetTests=com.example.project.CalculatorHumanTests" org.pitest:pitest-maven:mutationCoverage
Save-Reports "human"




Write-Host "`nReports saved under: $REPORTS"
Write-Host " - $REPORTS\ai\jacoco (coverage), $REPORTS\ai\pit (mutation)"
Write-Host " - $REPORTS\human\jacoco (coverage), $REPORTS\human\pit (mutation)"
