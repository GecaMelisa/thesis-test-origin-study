@echo off
setlocal

REM ============================================================
REM  requests.cmd  --  Automated test experiment runner
REM  Modules: requests.utils, requests.structures
REM  Tools:   pytest, coverage, mutmut, flake8
REM ============================================================

set PYTHON="C:\Program Files\Python314\python.exe"
set SCRIPTS=C:\Users\mgeca\AppData\Roaming\Python\Python314\Scripts

REM ============================================================
REM  STEP 1 -- Plain tests (all tests)
REM ============================================================
echo.
echo ============================================================
echo STEP 1: Plain pytest (all tests)
echo ============================================================
%PYTHON% -m pytest tests/ -v
echo.

REM ============================================================
REM  STEP 2 -- Coverage (all tests)
REM ============================================================
echo ============================================================
echo STEP 2: Coverage -- all tests
echo ============================================================
%PYTHON% -m coverage run -m pytest tests/
%PYTHON% -m coverage html -d reports\coverage_all
%PYTHON% -m coverage report
echo.

REM ============================================================
REM  STEP 3 -- Mutation testing (Claude tests only)
REM ============================================================
echo ============================================================
echo STEP 3: Mutation testing with mutmut
echo ============================================================
%PYTHON% -m mutmut run --paths-to-mutate src\requests\utils.py,src\requests\structures.py --tests-dir tests
%PYTHON% -m mutmut html
echo.

REM ============================================================
REM  STEP 4 -- Assertion density
REM ============================================================
echo ============================================================
echo STEP 4: Assertion density (not implemented)
echo ============================================================
echo Assertion density not implemented
echo.

REM ============================================================
REM  STEP 5 -- Static analysis (flake8)
REM ============================================================
echo ============================================================
echo STEP 5: Static analysis with flake8
echo ============================================================
%SCRIPTS%\flake8.exe tests\test_utils_claude.py tests\test_structures_claude.py --max-line-length=100
echo.

REM ============================================================
REM  STEP 6 -- Codex tests (placeholder -- no Codex files yet)
REM ============================================================
echo ============================================================
echo STEP 6: Codex tests + Coverage + Reports
echo ============================================================
echo No Codex test files present. Skipping.
echo.
if not exist reportsCodex mkdir reportsCodex
echo Codex coverage report placeholder > reportsCodex\README.txt

REM ============================================================
REM  STEP 7 -- Claude tests: coverage + reports
REM ============================================================
echo ============================================================
echo STEP 7: Claude tests + Coverage + Reports
echo ============================================================
%PYTHON% -m coverage run -m pytest tests\test_utils_claude.py tests\test_structures_claude.py -v
%PYTHON% -m coverage html -d reports\coverage_claude
%PYTHON% -m coverage report
echo.

REM -- Copy Claude coverage report to reportsClaude
echo Copying Claude reports to reportsClaude ...
if not exist reportsClaude mkdir reportsClaude
powershell -Command "Copy-Item -Path 'reports\coverage_claude\*' -Destination 'reportsClaude\' -Recurse -Force"
echo.

REM -- Copy mutmut HTML report to reportsClaude
if exist html (
    powershell -Command "Copy-Item -Path 'html\*' -Destination 'reportsClaude\mutmut\' -Recurse -Force"
)

echo ============================================================
echo DONE. Reports written to:
echo   reports\coverage_all     -- full suite coverage
echo   reports\coverage_claude  -- Claude tests coverage
echo   reportsClaude\           -- Claude reports copy
echo   reportsCodex\            -- Codex reports (placeholder)
echo ============================================================
endlocal
