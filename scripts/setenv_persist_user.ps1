<#
Usage: Run this script in PowerShell to persistently set USER environment variables.
It sets JAVA_HOME and appends selected bin folders to the current user's PATH.

Edit the paths below to match your local installations before running.
Note: You must open a new terminal window to see persistent changes.
#>

# --- EDIT THESE PATHS TO MATCH YOUR INSTALL LOCATIONS ---
# Prefer existing JAVA_HOME if present
$javaHome = if ($env:JAVA_HOME) { $env:JAVA_HOME } else { 'C:\Program Files\Java\jdk-17' }
$scalaHome = 'C:\Program Files\scala-2.13.12'
# If sbt has been downloaded into the project tools folder, prefer that as a sensible default
$projectSbt = 'C:\Users\PAVAN\Local_pro\Scala1\tools\sbt'
$sbtHome = if ($env:SBT_HOME) { $env:SBT_HOME } elseif (Test-Path $projectSbt) { $projectSbt } else { 'C:\Program Files\sbt' }
# -----------------------------------------------------

if (-not (Test-Path $javaHome)) {
    Write-Warning "JAVA_HOME path '$javaHome' does not exist. Edit this script or install a JDK."
}

[Environment]::SetEnvironmentVariable('JAVA_HOME', $javaHome, 'User')
[Environment]::SetEnvironmentVariable('SCALA_HOME', $scalaHome, 'User')
[Environment]::SetEnvironmentVariable('SBT_HOME', $sbtHome, 'User')

# Append bins to existing user PATH in a safe way
$old = [Environment]::GetEnvironmentVariable('Path','User')
$add = "${javaHome}\bin;${scalaHome}\bin;${sbtHome}\bin"

if ($old -notlike "*${javaHome}\bin*") { $new = "$old;$add" } else { $new = $old }
[Environment]::SetEnvironmentVariable('Path', $new, 'User')

Write-Host "Persisted USER environment variables. Open a new terminal to see changes." -ForegroundColor Green

