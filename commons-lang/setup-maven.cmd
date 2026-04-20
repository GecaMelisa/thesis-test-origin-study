@echo off
REM Checks if mvn is on PATH; if not, uses local .mvn-bin\ cache or downloads Maven 3.9.9.
REM Call this with:  call setup-maven.cmd
REM No setlocal — PATH change propagates to the calling script.

where mvn >nul 2>&1
if %ERRORLEVEL% == 0 (
    echo [Maven] Found on PATH.
    goto :eof
)

set MAVEN_VERSION=3.9.9
set MAVEN_DIR=%~dp0.mvn-bin
set MAVEN_HOME=%MAVEN_DIR%\apache-maven-%MAVEN_VERSION%

if exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo [Maven] Cached at %MAVEN_HOME%
    set PATH=%MAVEN_HOME%\bin;%PATH%
    goto :eof
)

echo [Maven] Not found. Downloading Apache Maven %MAVEN_VERSION%...
if not exist "%MAVEN_DIR%" mkdir "%MAVEN_DIR%"

set MAVEN_ZIP=%MAVEN_DIR%\mvn-download.zip

powershell -NoProfile -Command ^
    "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile '%MAVEN_ZIP%' -UseBasicParsing"
if %ERRORLEVEL% neq 0 ( echo [Maven] ERROR: Download failed. & exit /b 1 )

echo [Maven] Extracting...
powershell -NoProfile -Command ^
    "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%MAVEN_DIR%' -Force"
if %ERRORLEVEL% neq 0 ( echo [Maven] ERROR: Extraction failed. & exit /b 1 )

del /q "%MAVEN_ZIP%"

set PATH=%MAVEN_HOME%\bin;%PATH%
echo [Maven] Apache Maven %MAVEN_VERSION% installed at %MAVEN_HOME%
echo [Maven] Ready.
