# ============================================================
# run-psf-requests-claude.ps1
# Claude tests -- psf/requests
# ============================================================

$ErrorActionPreference = 'Continue'

$PYTHON  = "C:\Program Files\Python314\python.exe"
$SCRIPTS = "C:\Users\mgeca\AppData\Roaming\Python\Python314\Scripts"
$ROOT    = "C:\Users\mgeca\requests"
$REPORTS = "$ROOT\reports"

$CLAUDE_TESTS = @("tests/test_utils_claude.py", "tests/test_structures_claude.py")

New-Item -ItemType Directory -Force -Path $REPORTS          | Out-Null
New-Item -ItemType Directory -Force -Path "$ROOT\reportsClaude" | Out-Null

# -----------------------------------------------------------------------
# CLAUDE -- Plain tests
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================"
Write-Host " CLAUDE -- Plain tests"
Write-Host "============================================================"
& $PYTHON -m pytest @CLAUDE_TESTS -v `
    --html="$REPORTS\pytest-claude.html" --self-contained-html `
    --tb=short

# -----------------------------------------------------------------------
# CLAUDE -- Coverage
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================"
Write-Host " CLAUDE -- Coverage"
Write-Host "============================================================"
& $PYTHON -m coverage run -m pytest @CLAUDE_TESTS
& $PYTHON -m coverage html -d "$REPORTS\coverage-claude"
& $PYTHON -m coverage report

# -----------------------------------------------------------------------
# CLAUDE -- Static analysis (flake8 HTML)
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================"
Write-Host " CLAUDE -- Static analysis (flake8)"
Write-Host "============================================================"
& $PYTHON -m flake8 @CLAUDE_TESTS --max-line-length=100 `
    --format=html --htmldir="$REPORTS\flake8-claude"

# -----------------------------------------------------------------------
# CLAUDE -- Mutation testing (cosmic-ray)
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================"
Write-Host " CLAUDE -- Mutation testing (cosmic-ray)"
Write-Host "============================================================"
$SQLITE = "$REPORTS\cr-claude.sqlite"
if (Test-Path $SQLITE) { Remove-Item -Force $SQLITE }
& "$SCRIPTS\cosmic-ray.exe" init "$ROOT\cosmic-ray-claude.toml" $SQLITE
& "$SCRIPTS\cosmic-ray.exe" exec "$ROOT\cosmic-ray-claude.toml" $SQLITE
& "$SCRIPTS\cr-html.exe" $SQLITE | Out-File -FilePath "$REPORTS\mutmut-claude.html" -Encoding utf8
& "$SCRIPTS\cr-rate.exe" $SQLITE

# -----------------------------------------------------------------------
# Copy to reportsClaude
# -----------------------------------------------------------------------
if (Test-Path "$REPORTS\pytest-claude.html")         { Copy-Item -Force "$REPORTS\pytest-claude.html" "$ROOT\reportsClaude\pytest-claude.html" }
if (Test-Path "$REPORTS\coverage-claude")            { Copy-Item -Recurse -Force "$REPORTS\coverage-claude" "$ROOT\reportsClaude\coverage" }
if (Test-Path "$REPORTS\flake8-claude")              { Copy-Item -Recurse -Force "$REPORTS\flake8-claude" "$ROOT\reportsClaude\flake8" }
if (Test-Path "$REPORTS\mutmut-claude.html")         { Copy-Item -Force "$REPORTS\mutmut-claude.html" "$ROOT\reportsClaude\mutmut-claude.html" }

# -----------------------------------------------------------------------
# Open reports
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================"
Write-Host " Opening reports..."
Write-Host "============================================================"
if (Test-Path "$REPORTS\pytest-claude.html")         { Start-Process -FilePath "explorer.exe" -ArgumentList "$REPORTS\pytest-claude.html" }
if (Test-Path "$REPORTS\coverage-claude\index.html") { Start-Process -FilePath "explorer.exe" -ArgumentList "$REPORTS\coverage-claude\index.html" }
if (Test-Path "$REPORTS\flake8-claude\index.html")   { Start-Process -FilePath "explorer.exe" -ArgumentList "$REPORTS\flake8-claude\index.html" }
if (Test-Path "$REPORTS\mutmut-claude.html")         { Start-Process -FilePath "explorer.exe" -ArgumentList "$REPORTS\mutmut-claude.html" }

Write-Host ""
Write-Host "============================================================"
Write-Host " DONE -- Claude reports"
Write-Host "   $REPORTS\pytest-claude.html"
Write-Host "   $REPORTS\coverage-claude\index.html"
Write-Host "   $REPORTS\flake8-claude\index.html"
Write-Host "   $REPORTS\mutmut-claude.html"
Write-Host "============================================================"
