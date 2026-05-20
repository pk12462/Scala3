<#
Usage: dot-source this file in PowerShell to set session-only environment variables:
  . .\scripts\setenv_session.ps1

Edit the paths below to match your local installations before dot-sourcing.
#>

# --- EDIT THESE PATHS TO MATCH YOUR INSTALL LOCATIONS ---
# If JAVA_HOME is already set in the environment, use it as a sensible default.
$javaHome = if ($env:JAVA_HOME) { $env:JAVA_HOME } else { 'C:\Program Files\Java\jdk-17' }
$scalaHome = 'C:\Program Files\scala-2.13.12'
$sbtHome = 'C:\Program Files\sbt'
# -----------------------------------------------------

if (-not (Test-Path $javaHome)) {
    Write-Warning "JAVA_HOME path '$javaHome' does not exist. Edit this script or install a JDK."
}

$env:JAVA_HOME = $javaHome
$env:SCALA_HOME = $scalaHome
$env:SBT_HOME = $sbtHome

# Prepend bin folders so current session uses these first
$env:Path = "${env:JAVA_HOME}\bin;${env:SCALA_HOME}\bin;${env:SBT_HOME}\bin;" + $env:Path

Write-Host "Session environment variables set (temporary)." -ForegroundColor Green
Write-Host "JAVA_HOME=`"$env:JAVA_HOME`""
Write-Host "SCALA_HOME=`"$env:SCALA_HOME`""
Write-Host "SBT_HOME=`"$env:SBT_HOME`""

