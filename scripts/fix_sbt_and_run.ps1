<#
Fixes the sbt IPC socket lock error (ServerAlreadyBootingException) on Windows by:
1. Killing any running sbt/java processes
2. Clearing sbt lock files from ~/.sbt
3. Running sbt with -no-server flag (disables sbt server to avoid IPC conflicts)

Usage: .\scripts\fix_sbt_and_run.ps1 [sbt_args]
Example: .\scripts\fix_sbt_and_run.ps1 run
#>

param(
    [string[]]$SbtArgs = @('run')
)

Write-Host "--- Fixing sbt IPC lock error ---" -ForegroundColor Cyan

# Set SBT_HOME to project-local sbt if available
$root = 'C:\Users\PAVAN\Local_pro\Scala1'
$projectSbt = Join-Path $root 'tools\sbt'
if (Test-Path $projectSbt) {
    $env:SBT_HOME = $projectSbt
    $env:Path = "$env:SBT_HOME\bin;" + $env:Path
    Write-Host "Using project-local sbt at: $env:SBT_HOME"
    # Disable sbt server to avoid Windows IPC lock conflicts
    $env:SBT_OPTS = "-Dsbt.server.autostart=false"
} elseif (-not (Get-Command sbt -ErrorAction SilentlyContinue)) {
    Write-Error "sbt not found in PATH and not in project tools folder. Run install_and_run_sbt.ps1 first."
    exit 1
}

# 1. Kill any running sbt/java processes
Write-Host "Killing any running sbt processes..."
Get-Process java -ErrorAction SilentlyContinue | Where-Object { $_.CommandLine -like '*sbt*' } | Stop-Process -Force -ErrorAction SilentlyContinue
Start-Sleep -Milliseconds 500

# 2. Clear sbt lock files
Write-Host "Clearing sbt lock files..."
$sbtHome = "$env:USERPROFILE\.sbt"
if (Test-Path $sbtHome) {
    Get-ChildItem -Path $sbtHome -Filter "*lock*" -Recurse -ErrorAction SilentlyContinue | Remove-Item -Force -Recurse -ErrorAction SilentlyContinue
    Write-Host "Cleared lock files from $sbtHome"
}

# 3. Run sbt with -no-server flag and other options to suppress warnings
Write-Host "Running sbt with -no-server flag..." -ForegroundColor Green
cd C:\Users\PAVAN\Local_pro\Scala1

$sbtCommand = @('-no-server', '-batch') + $SbtArgs
Write-Host "Command: sbt $($sbtCommand -join ' ')"

try {
    & sbt $sbtCommand
} catch {
    Write-Error "sbt command failed: $_"
    exit 1
}

Write-Host "Done." -ForegroundColor Cyan

