<#
Calculates a simple assertion density metric for tests:

- Counts number of lines containing "assert" in src/test/java
- Counts number of @Test occurrences
- Prints: assertion count, test count, and assertions per test
#>

$ErrorActionPreference = "Stop"

# Locate repo root (where src/test/java lives)
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$current = $scriptDir
$found = $false
for ($i = 0; $i -lt 5; $i++) {
    $candidate = Join-Path $current "src/test/java"
    if (Test-Path $candidate) {
        Set-Location $current
        $found = $true
        break
    }
    $current = Split-Path -Parent $current
}

if (-not $found) {
    Write-Host "Test directory 'src/test/java' not found." -ForegroundColor Red
    exit 1
}

$testDir = "src/test/java"

# Count assertion usages (primitive heuristic)
$assertPatterns = @(
    "assertEquals", "assertTrue", "assertFalse",
    "assertNull", "assertNotNull", "assertThrows",
    "assertAll", "assertIterableEquals", "assertArrayEquals"
)

$assertCount = 0
foreach ($pattern in $assertPatterns) {
    $count = (Get-ChildItem $testDir -Recurse -Filter *.java |
        Select-String -Pattern $pattern).Count
    $assertCount += $count
}

# Count @Test occurrences
$testCount = (Get-ChildItem $testDir -Recurse -Filter *.java |
    Select-String -Pattern "@Test").Count

Write-Host "=== Assertion Density ===" -ForegroundColor Cyan
Write-Host "Assertions (approx.): $assertCount"
Write-Host "Test methods (@Test):  $testCount"

if ($testCount -gt 0) {
    $density = [double]$assertCount / [double]$testCount
    Write-Host ("Assertions per test: {0:N2}" -f $density)
} else {
    Write-Host "No @Test methods found."
}
