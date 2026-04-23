@echo off
setlocal enabledelayedexpansion

echo =============================================================
echo  Humanizer Experiment Script
echo  Scope: StringHumanizeExtensions, NumberToWordsExtension,
echo         DateTimeHumanizeAlgorithms
echo =============================================================

set TESTS_PROJ=tests\Humanizer.Tests\Humanizer.Tests.csproj
set TFM=net11.0
set COVERAGE_DIR=coverage
set STRYKER_DIR=StrykerOutput
set REPORTS_CODEX=reportsCodex
set REPORTS_CLAUDE=reportsClaude

echo.
echo [1/7] Restoring and building solution...
dotnet restore Humanizer.slnx
if %errorlevel% neq 0 (
    echo ERROR: restore failed
    exit /b %errorlevel%
)
dotnet build Humanizer.slnx -c Release
if %errorlevel% neq 0 (
    echo ERROR: build failed
    exit /b %errorlevel%
)

echo.
echo [2/7] Running plain tests (all tests, framework: %TFM%)...
dotnet test %TESTS_PROJ% --framework %TFM% --no-build -c Release
if %errorlevel% neq 0 (
    echo ERROR: plain tests failed
    exit /b %errorlevel%
)

echo.
echo [3/7] Running tests with Coverlet coverage (opencover format)...
if not exist %COVERAGE_DIR% mkdir %COVERAGE_DIR%
dotnet test %TESTS_PROJ% --framework %TFM% --no-build -c Release ^
    /p:CollectCoverage=true ^
    /p:CoverletOutput=%COVERAGE_DIR%\ ^
    /p:CoverletOutputFormat=opencover ^
    /p:Include="[Humanizer]*StringHumanizeExtensions,[Humanizer]*NumberToWordsExtension,[Humanizer]*DateTimeHumanizeAlgorithms"
if %errorlevel% neq 0 (
    echo WARNING: coverage run reported failures or partial coverage
)

echo.
echo [4/7] Running mutation testing with Stryker.NET...
dotnet tool restore
dotnet stryker ^
    --project Humanizer.csproj ^
    --test-project %TESTS_PROJ% ^
    --target-framework %TFM% ^
    --mutation-level Standard ^
    --reporter html ^
    --reporter progress
if %errorlevel% neq 0 (
    echo WARNING: Stryker exited with non-zero code (check report for details)
)

echo.
echo [5/7] Assertion density — not implemented (requires external tool)
echo       Metric: assertions per test method counted manually or via script.

echo.
echo [6/7] Static analysis (dotnet format — verify no changes)...
dotnet format Humanizer.slnx --verify-no-changes --verbosity diagnostic
if %errorlevel% neq 0 (
    echo NOTE: formatting issues detected. Run 'dotnet format Humanizer.slnx' to auto-fix.
)

echo.
echo [7/7] Copying reports...

echo Copying coverage reports for Codex...
if not exist %REPORTS_CODEX% mkdir %REPORTS_CODEX%
if exist %COVERAGE_DIR% (
    powershell -Command "Copy-Item -Path '%COVERAGE_DIR%\*' -Destination '%REPORTS_CODEX%\' -Recurse -Force"
)
if exist %STRYKER_DIR% (
    powershell -Command "Copy-Item -Path '%STRYKER_DIR%\*' -Destination '%REPORTS_CODEX%\StrykerOutput\' -Recurse -Force"
)

echo Copying coverage reports for Claude...
if not exist %REPORTS_CLAUDE% mkdir %REPORTS_CLAUDE%
if exist %COVERAGE_DIR% (
    powershell -Command "Copy-Item -Path '%COVERAGE_DIR%\*' -Destination '%REPORTS_CLAUDE%\' -Recurse -Force"
)
if exist %STRYKER_DIR% (
    powershell -Command "Copy-Item -Path '%STRYKER_DIR%\*' -Destination '%REPORTS_CLAUDE%\StrykerOutput\' -Recurse -Force"
)

echo.
echo =============================================================
echo  All steps completed.
echo  Coverage XML : %COVERAGE_DIR%\
echo  Stryker HTML : %STRYKER_DIR%\
echo  Codex reports: %REPORTS_CODEX%\
echo  Claude reports: %REPORTS_CLAUDE%\
echo =============================================================
endlocal
