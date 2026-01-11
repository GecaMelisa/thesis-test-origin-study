# Prereq: ensure sbt is on PATH in this session (e.g., $env:Path += ";C:\Program Files (x86)\sbt\bin")

# 1) Plain tests
sbt test

# 2) JaCoCo coverage (via sbt-jacoco plugin)
sbt jacoco
# report: target/scala-*/jacoco/report/html/index.html

# 3) Tag-isolated runs + copied JaCoCo reports
# Note: PIT and PMD are not configured for SBT in this starter.

# Human
sbt -DincludeTags=human test
sbt -DincludeTags=human jacoco
powershell -NoProfile -Command "if (Test-Path 'target\\scala-*\\jacoco\\report\\html') { Get-ChildItem -Path 'target\\scala-*\\jacoco\\report\\html' | ForEach-Object { Copy-Item -Recurse -Force $_.FullName 'target\\jacocoHuman' } }"
Start-Process "target\\jacocoHuman\\index.html"

# GPT
sbt -DincludeTags=gpt test
sbt -DincludeTags=gpt jacoco
powershell -NoProfile -Command "if (Test-Path 'target\\scala-*\\jacoco\\report\\html') { Get-ChildItem -Path 'target\\scala-*\\jacoco\\report\\html' | ForEach-Object { Copy-Item -Recurse -Force $_.FullName 'target\\jacocoGpt' } }"
Start-Process "target\\jacocoGpt\\index.html"

# Codex
sbt -DincludeTags=codex test
sbt -DincludeTags=codex jacoco
powershell -NoProfile -Command "if (Test-Path 'target\\scala-*\\jacoco\\report\\html') { Get-ChildItem -Path 'target\\scala-*\\jacoco\\report\\html' | ForEach-Object { Copy-Item -Recurse -Force $_.FullName 'target\\jacocoCodex' } }"
Start-Process "target\\jacocoCodex\\index.html"
