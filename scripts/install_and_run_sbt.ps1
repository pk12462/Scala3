<#
Downloads an sbt distribution into the project's tools folder (no admin required),
adds it to the current session PATH, then invokes `sbt sbtVersion` and `sbt run`.

Edit the sbtVersion variable below if you want a different sbt release.
#>

$sbtVersion = '1.9.5'
$root = 'C:\Users\PAVAN\Local_pro\Scala1'
$tools = Join-Path $root 'tools'
$sbtZip = Join-Path $tools "sbt-$sbtVersion.zip"
$sbtDir = Join-Path $tools 'sbt'

New-Item -ItemType Directory -Force -Path $tools | Out-Null

if (-not (Test-Path $sbtDir)) {
    Write-Host "Downloading sbt $sbtVersion..."
    $url = "https://github.com/sbt/sbt/releases/download/v$sbtVersion/sbt-$sbtVersion.zip"
    try {
        Invoke-WebRequest -Uri $url -OutFile $sbtZip -UseBasicParsing -ErrorAction Stop
    } catch {
        Write-Error "Failed to download sbt from $url : $_"
        exit 1
    }

    try {
        Expand-Archive -Path $sbtZip -DestinationPath $tools -Force -ErrorAction Stop
    } catch {
        Write-Error "Failed to extract $sbtZip : $_"
        exit 1
    }

    # Normalize the extracted folder name to 'sbt'
    $extracted = Get-ChildItem -Path $tools -Directory | Where-Object { $_.Name -match '^sbt' } | Select-Object -First 1
    if ($null -ne $extracted) {
        Rename-Item -Path $extracted.FullName -NewName 'sbt' -Force
    }
} else {
    Write-Host 'sbt already present in tools folder.'
}

# Set SBT_HOME for this session and prepend its bin to PATH
$env:SBT_HOME = Join-Path $tools 'sbt'
$env:Path = "$env:SBT_HOME\bin;" + $env:Path

Write-Host "sbt home: $env:SBT_HOME"

# Disable sbt server to avoid Windows IPC lock conflicts
$env:SBT_OPTS = "-Dsbt.server.autostart=false"

cd $root

Write-Host "Running sbt to show sbtVersion (may download launcher and dependencies)..."
try {
    & sbt -no-server -batch sbtVersion
} catch {
    Write-Error "sbt invocation failed: $_"
}

Write-Host 'Running sbt run to compile and execute the Scala app...'
try {
    & sbt -no-server -batch run
} catch {
    Write-Error "sbt run failed: $_"
}

Write-Host 'Done.'

