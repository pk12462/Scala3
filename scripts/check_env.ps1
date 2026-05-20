<#
Quick environment checker for Java/Scala/sbt in the current shell.
Run: .\scripts\check_env.ps1
#>

Write-Host "--- Scala environment check ---" -ForegroundColor Cyan

Write-Host "JAVA_HOME = $env:JAVA_HOME"
if (Get-Command java -ErrorAction SilentlyContinue) {
    Write-Host "java executable found:" -NoNewline; java -version
} else { Write-Host "java not found in PATH" -ForegroundColor Yellow }

if ($env:SCALA_HOME) { Write-Host "SCALA_HOME = $env:SCALA_HOME" }
if (Get-Command scala -ErrorAction SilentlyContinue) {
    Write-Host "scala executable found:" -NoNewline; scala -version
} else { Write-Host "scala not found in PATH" -ForegroundColor Yellow }

if ($env:SBT_HOME) { Write-Host "SBT_HOME = $env:SBT_HOME" }
if (Get-Command sbt -ErrorAction SilentlyContinue) {
    Write-Host "sbt executable found:"; sbt --version
} else { Write-Host "sbt not found in PATH" -ForegroundColor Yellow }

Write-Host "--- check complete ---" -ForegroundColor Cyan

