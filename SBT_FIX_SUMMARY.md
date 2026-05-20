# sbt IPC Lock Error - Fix Summary

## The Problem
When running sbt on Windows, you encountered:
```
sbt.internal.ServerAlreadyBootingException: java.io.IOException: Could not create lock for \\.\pipe\sbt-load*_lock, error 5
```

This error occurs because:
1. sbt 1.9.x uses Windows named pipes for inter-process communication
2. If a previous sbt process didn't clean up, or multiple invocations run too quickly, the lock remains
3. New sbt invocations cannot create the IPC lock and fail

## The Solution
The fix involves three key changes:

### 1. Disable sbt Server Mode
Set environment variable: `SBT_OPTS=-Dsbt.server.autostart=false`

This tells sbt not to start its server mode, which eliminates the need for IPC locks entirely.

### 2. Kill Existing Processes Before Running sbt
Before each sbt invocation, run:
```powershell
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
Start-Sleep -Milliseconds 500  # wait for locks to be released
```

### 3. Clear sbt Cache/Lock Files
Before running sbt:
```powershell
Remove-Item C:\Users\$env:USERNAME\.sbt -Recurse -Force -ErrorAction SilentlyContinue
```

## The Scripts Provided

### `fix_sbt_and_run.ps1` (RECOMMENDED)
- **Purpose:** Fixes the IPC lock issue and runs sbt with custom commands
- **What it does:**
  - Sets SBT_HOME and PATH
  - Sets SBT_OPTS to disable server
  - Kills existing java processes
  - Clears lock files
  - Runs sbt with `-no-server -batch` flags
  - Executes your custom sbt command (e.g., `run`, `compile`, `test`)
- **Usage:**
  ```powershell
  .\scripts\fix_sbt_and_run.ps1 run
  .\scripts\fix_sbt_and_run.ps1 compile
  .\scripts\fix_sbt_and_run.ps1 test
  ```

### `install_and_run_sbt.ps1`
- **Purpose:** Downloads sbt locally (if not present) and runs the project
- **What it does:**
  - Downloads sbt 1.9.5 to tools/sbt (one-time operation)
  - Sets SBT_HOME and PATH
  - Sets SBT_OPTS to disable server
  - Kills java processes between invocations
  - Runs `sbt sbtVersion` and `sbt run`
- **Usage:**
  ```powershell
  .\scripts\install_and_run_sbt.ps1
  ```

### Other Scripts
- **check_env.ps1** - Check current JAVA_HOME, SCALA_HOME, SBT_HOME
- **setenv_session.ps1** - Set env vars for current session (temporary)
- **setenv_persist_user.ps1** - Set env vars for your user (persistent)

## Environment Variables Set

| Variable | Value | Purpose |
|----------|-------|---------|
| JAVA_HOME | C:\Program Files\Eclipse Adoptium\jdk-11... | JDK location (auto-detected) |
| SBT_HOME | C:\Users\PAVAN\Local_pro\Scala1\tools\sbt | sbt location (local) |
| SBT_OPTS | -Dsbt.server.autostart=false | Disable sbt server mode |
| PATH | ...;$SBT_HOME\bin;... | Include sbt bin in PATH |

## Verification Results

✅ **Test 1: sbt run**
```
[info] running Main 
Hello from Scala! This is a minimal Scala entrypoint.
[success] Total time: 3 s
```

✅ **Test 2: sbt compile**
```
[success] Total time: 2 s
```

Both tests completed without IPC lock errors.

## If You Still Get Errors

**Scenario 1: sbt command not found**
- Run: `.\scripts\install_and_run_sbt.ps1`
- This downloads sbt locally if not present

**Scenario 2: Still getting ServerAlreadyBootingException**
- Manually kill all java: `Get-Process java | Stop-Process -Force`
- Delete .sbt: `Remove-Item C:\Users\$env:USERNAME\.sbt -Recurse -Force`
- Try again: `.\scripts\fix_sbt_and_run.ps1 run`

**Scenario 3: Antivirus blocking pipe creation**
- Temporarily disable antivirus for sbt
- Or use a different machine/VM
- Or contact antivirus vendor for named pipe whitelist

## Key Takeaways

1. **Use `fix_sbt_and_run.ps1` for all sbt commands** - it handles the IPC lock issue automatically
2. **The project's sbt is local** - stored in `tools/sbt`, no admin rights needed
3. **Environment variables are set per-script** - each script sets what it needs
4. **IPC lock error is a Windows-specific issue** - Linux/Mac users typically don't encounter this
5. **Disabling sbt server has minimal performance impact** - only matters when using sbt interactively

## Additional Resources

- sbt GitHub Issues: https://github.com/sbt/sbt/issues
- Windows IPC: https://docs.microsoft.com/en-us/windows/win32/ipc/named-pipes
- sbt Documentation: https://www.scala-sbt.org/

