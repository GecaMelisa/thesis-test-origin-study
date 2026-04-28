# ============================================================
# run-psf-requests.ps1
# Automated test experiment runner -- psf/requests
# Modules: requests.utils, requests.structures
# ============================================================

$PYTHON  = "C:\Program Files\Python314\python.exe"
$SCRIPTS = "C:\Users\mgeca\AppData\Roaming\Python\Python314\Scripts"

$HUMAN_TESTS  = @("tests/test_utils.py", "tests/test_structures.py")
$CLAUDE_TESTS = @("tests/test_utils_claude.py", "tests/test_structures_claude.py")
$MUTMUT_SRC   = "src/requests/utils.py,src/requests/structures.py"

New-Item -ItemType Directory -Force -Path "reports" | Out-Null
New-Item -ItemType Directory -Force -Path "reportsHuman" | Out-Null
New-Item -ItemType Directory -Force -Path "reportsClaude" | Out-Null

# -----------------------------------------------------------------------
# HUMAN - ALL IN ONE
# -----------------------------------------------------------------------

Write-Host ""
Write-Host "============================================================"
Write-Host " HUMAN -- Plain tests"
Write-Host "============================================================"
& $PYTHON -m pytest @HUMAN_TESTS -v `
    --html="reports/pytest-human.html" --self-contained-html `
    --tb=short

Write-Host ""
Write-Host "============================================================"
Write-Host " HUMAN -- Coverage"
Write-Host "============================================================"
& $PYTHON -m coverage run -m pytest @HUMAN_TESTS
& $PYTHON -m coverage html -d "reports/coverage-human"
& $PYTHON -m coverage report

Write-Host ""
Write-Host "============================================================"
Write-Host " HUMAN -- Static analysis (flake8)"
Write-Host "============================================================"
& "$SCRIPTS\flake8.exe" @HUMAN_TESTS --max-line-length=100 `
    --format="%(path)s:%(row)d:%(col)d: %(code)s %(text)s" `
    | Tee-Object -FilePath "reports/flake8-human.txt"

Write-Host ""
Write-Host "============================================================"
Write-Host " HUMAN -- Mutation testing (mutmut -- requires WSL on Windows)"
Write-Host "============================================================"
if (Get-Command wsl -ErrorAction SilentlyContinue) {
    wsl python3 -m mutmut run `
        --paths-to-mutate $MUTMUT_SRC `
        --runner "python3 -m pytest $($HUMAN_TESTS -join ' ') -x -q"
    wsl python3 -m mutmut html
    if (Test-Path "html") {
        Copy-Item -Recurse -Force "html" "reports/mutmut-human"
        Remove-Item -Recurse -Force "html"
    }
} else {
    Write-Host "WSL not available -- skipping mutmut (see https://github.com/boxed/mutmut/issues/397)"
}

# Copy human reports
Copy-Item -Force  "reports/pytest-human.html"          "reportsHuman/pytest-human.html"
if (Test-Path "reports/coverage-human") {
    Copy-Item -Recurse -Force "reports/coverage-human" "reportsHuman/coverage"
}
if (Test-Path "reports/mutmut-human") {
    Copy-Item -Recurse -Force "reports/mutmut-human"   "reportsHuman/mutmut"
}
if (Test-Path "reports/flake8-human.txt") {
    Copy-Item -Force "reports/flake8-human.txt"        "reportsHuman/flake8-human.txt"
}

# Open human reports
Start-Process "reports/pytest-human.html"
Start-Process "reports/coverage-human/index.html"
if (Test-Path "reports/mutmut-human/index.html") { Start-Process "reports/mutmut-human/index.html" }

# -----------------------------------------------------------------------
# CLAUDE - ALL IN ONE
# -----------------------------------------------------------------------

Write-Host ""
Write-Host "============================================================"
Write-Host " CLAUDE -- Plain tests"
Write-Host "============================================================"
& $PYTHON -m pytest @CLAUDE_TESTS -v `
    --html="reports/pytest-claude.html" --self-contained-html `
    --tb=short

Write-Host ""
Write-Host "============================================================"
Write-Host " CLAUDE -- Coverage"
Write-Host "============================================================"
& $PYTHON -m coverage run -m pytest @CLAUDE_TESTS
& $PYTHON -m coverage html -d "reports/coverage-claude"
& $PYTHON -m coverage report

Write-Host ""
Write-Host "============================================================"
Write-Host " CLAUDE -- Static analysis (flake8)"
Write-Host "============================================================"
& "$SCRIPTS\flake8.exe" @CLAUDE_TESTS --max-line-length=100 `
    --format="%(path)s:%(row)d:%(col)d: %(code)s %(text)s" `
    | Tee-Object -FilePath "reports/flake8-claude.txt"

Write-Host ""
Write-Host "============================================================"
Write-Host " CLAUDE -- Mutation testing (mutmut -- requires WSL on Windows)"
Write-Host "============================================================"
if (Get-Command wsl -ErrorAction SilentlyContinue) {
    wsl python3 -m mutmut run `
        --paths-to-mutate $MUTMUT_SRC `
        --runner "python3 -m pytest $($CLAUDE_TESTS -join ' ') -x -q"
    wsl python3 -m mutmut html
    if (Test-Path "html") {
        Copy-Item -Recurse -Force "html" "reports/mutmut-claude"
        Remove-Item -Recurse -Force "html"
    }
} else {
    Write-Host "WSL not available -- skipping mutmut (see https://github.com/boxed/mutmut/issues/397)"
}

# Copy claude reports
Copy-Item -Force  "reports/pytest-claude.html"          "reportsClaude/pytest-claude.html"
if (Test-Path "reports/coverage-claude") {
    Copy-Item -Recurse -Force "reports/coverage-claude" "reportsClaude/coverage"
}
if (Test-Path "reports/mutmut-claude") {
    Copy-Item -Recurse -Force "reports/mutmut-claude"   "reportsClaude/mutmut"
}
if (Test-Path "reports/flake8-claude.txt") {
    Copy-Item -Force "reports/flake8-claude.txt"        "reportsClaude/flake8-claude.txt"
}

# Open claude reports
Start-Process "reports/pytest-claude.html"
Start-Process "reports/coverage-claude/index.html"
if (Test-Path "reports/mutmut-claude/index.html") { Start-Process "reports/mutmut-claude/index.html" }

Write-Host ""
Write-Host "============================================================"
Write-Host " DONE"
Write-Host "   reports/pytest-human.html      reports/pytest-claude.html"
Write-Host "   reports/coverage-human/        reports/coverage-claude/"
Write-Host "   reports/mutmut-human/          reports/mutmut-claude/"
Write-Host "   reports/flake8-human.txt       reports/flake8-claude.txt"
Write-Host "   reportsHuman/                  reportsClaude/"
Write-Host "============================================================"
