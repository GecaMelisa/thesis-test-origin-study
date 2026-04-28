# ============================================================
# run-psf-requests-human.ps1
# Human tests -- psf/requests
# ============================================================

$ErrorActionPreference = 'Continue'

$PYTHON  = "C:\Program Files\Python314\python.exe"
$SCRIPTS = "C:\Users\mgeca\AppData\Roaming\Python\Python314\Scripts"
$ROOT    = "C:\Users\mgeca\requests"
$REPORTS = "$ROOT\reports"

$HUMAN_TESTS = @("tests/test_utils.py", "tests/test_structures.py")

New-Item -ItemType Directory -Force -Path $REPORTS         | Out-Null
New-Item -ItemType Directory -Force -Path "$ROOT\reportsHuman" | Out-Null

# -----------------------------------------------------------------------
# HUMAN -- Plain tests
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================"
Write-Host " HUMAN -- Plain tests"
Write-Host "============================================================"
& $PYTHON -m pytest @HUMAN_TESTS -v `
    --html="$REPORTS\pytest-human.html" --self-contained-html `
    --tb=short

# -----------------------------------------------------------------------
# HUMAN -- Coverage
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================"
Write-Host " HUMAN -- Coverage"
Write-Host "============================================================"
& $PYTHON -m coverage run -m pytest @HUMAN_TESTS
& $PYTHON -m coverage html -d "$REPORTS\coverage-human"
& $PYTHON -m coverage report

# -----------------------------------------------------------------------
# HUMAN -- Static analysis (flake8 HTML)
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================"
Write-Host " HUMAN -- Static analysis (flake8)"
Write-Host "============================================================"
& $PYTHON -m flake8 @HUMAN_TESTS --max-line-length=100 `
    --format=html --htmldir="$REPORTS\flake8-human"

# -----------------------------------------------------------------------
# HUMAN -- Mutation testing (cosmic-ray)
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================"
Write-Host " HUMAN -- Mutation testing (cosmic-ray)"
Write-Host "============================================================"
$SQLITE = "$REPORTS\cr-human.sqlite"
if (Test-Path $SQLITE) { Remove-Item -Force $SQLITE }
& "$SCRIPTS\cosmic-ray.exe" init "$ROOT\cosmic-ray-human.toml" $SQLITE
& "$SCRIPTS\cosmic-ray.exe" exec "$ROOT\cosmic-ray-human.toml" $SQLITE
& "$SCRIPTS\cr-html.exe" $SQLITE | Out-File -FilePath "$REPORTS\mutmut-human.html" -Encoding utf8
& "$SCRIPTS\cr-rate.exe" $SQLITE

# -----------------------------------------------------------------------
# Copy to reportsHuman
# -----------------------------------------------------------------------
if (Test-Path "$REPORTS\pytest-human.html")         { Copy-Item -Force "$REPORTS\pytest-human.html" "$ROOT\reportsHuman\pytest-human.html" }
if (Test-Path "$REPORTS\coverage-human")            { Copy-Item -Recurse -Force "$REPORTS\coverage-human" "$ROOT\reportsHuman\coverage" }
if (Test-Path "$REPORTS\flake8-human")              { Copy-Item -Recurse -Force "$REPORTS\flake8-human" "$ROOT\reportsHuman\flake8" }
if (Test-Path "$REPORTS\mutmut-human.html")         { Copy-Item -Force "$REPORTS\mutmut-human.html" "$ROOT\reportsHuman\mutmut-human.html" }

# -----------------------------------------------------------------------
# Open reports
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================"
Write-Host " Opening reports..."
Write-Host "============================================================"
if (Test-Path "$REPORTS\pytest-human.html")         { Start-Process -FilePath "explorer.exe" -ArgumentList "$REPORTS\pytest-human.html" }
if (Test-Path "$REPORTS\coverage-human\index.html") { Start-Process -FilePath "explorer.exe" -ArgumentList "$REPORTS\coverage-human\index.html" }
if (Test-Path "$REPORTS\flake8-human\index.html")   { Start-Process -FilePath "explorer.exe" -ArgumentList "$REPORTS\flake8-human\index.html" }
if (Test-Path "$REPORTS\mutmut-human.html")         { Start-Process -FilePath "explorer.exe" -ArgumentList "$REPORTS\mutmut-human.html" }

Write-Host ""
Write-Host "============================================================"
Write-Host " DONE -- Human reports"
Write-Host "   $REPORTS\pytest-human.html"
Write-Host "   $REPORTS\coverage-human\index.html"
Write-Host "   $REPORTS\flake8-human\index.html"
Write-Host "   $REPORTS\mutmut-human.html"
Write-Host "============================================================"
