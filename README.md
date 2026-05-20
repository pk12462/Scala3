# Scala1 - Local Scala/sbt Environment Setup

A minimal Scala project with helper scripts to run sbt on Windows without IPC lock errors.

## Files Included
- **build.sbt** - sbt build definition (Scala 2.13.12)
- **project/build.properties** - sbt version pinning (1.9.5)
- **src/main/scala/Main.scala** - minimal Scala entrypoint
- **scripts/fix_sbt_and_run.ps1** - **RECOMMENDED: Fixes IPC lock error + sets proper env vars + runs sbt**
- **scripts/install_and_run_sbt.ps1** - Downloads sbt locally (if needed) and runs the project
- **scripts/setenv_session.ps1** - Set env vars for current session only (temporary)
- **scripts/setenv_persist_user.ps1** - Persist env vars for your user account
- **scripts/check_env.ps1** - Check what JAVA_HOME/SCALA_HOME/SBT_HOME are available

## Quick Start

**To compile and run the Scala program (fixes sbt server lock error):**

```powershell
.\scripts\fix_sbt_and_run.ps1 run
```

This script automatically:
- Kills any running sbt/java processes
- Clears sbt lock files from ~/.sbt
- Sets SBT_HOME and PATH for this session
- Disables sbt server mode (avoids Windows IPC conflicts)
- Compiles and executes your Scala program

**To run other sbt commands (e.g., compile, test, console):**

```powershell
.\scripts\fix_sbt_and_run.ps1 compile
.\scripts\fix_sbt_and_run.ps1 test
.\scripts\fix_sbt_and_run.ps1 console
```

## Why the IPC Lock Error Happens

sbt 1.9.x on Windows tries to create a named pipe for inter-process communication. If:
- A previous sbt process didn't clean up properly, or
- Multiple sbt invocations run too quickly, or
- Antivirus/file locking blocks the pipe creation

Then sbt fails with: `ServerAlreadyBootingException: Could not create lock for \\.\pipe\sbt-load*_lock, error 5`

**Solution:** The scripts disable sbt server mode (SBT_OPTS=-Dsbt.server.autostart=false) and kill existing processes between invocations.

## Environment Setup (Manual)

If you want to manually set up environment variables:

**For the current session only (temporary):**
```powershell
. .\scripts\setenv_session.ps1
.\scripts\check_env.ps1
```

**For your user account (persistent, all future shells):**
```powershell
.\scripts\setenv_persist_user.ps1
# Open a NEW PowerShell window after running the above
# Then verify:
.\scripts\check_env.ps1
sbt run
```

## File Structure

```powershell
Scala1/
├── src/
│   └── main/
│       └── scala/
│           └── Main.scala
├── project/
│   └── build.properties
├── scripts/
│   ├── fix_sbt_and_run.ps1       (recommended - fixes IPC lock error)
│   ├── install_and_run_sbt.ps1   (downloads sbt if needed)
│   ├── setenv_session.ps1        (temp session vars)
│   ├── setenv_persist_user.ps1   (persistent user vars)
│   └── check_env.ps1             (verify env vars)
├── target/                        (compiled output, created by sbt)
├── tools/                         (local sbt, created by install script)
├── build.sbt
└── README.md
```

## Troubleshooting

**sbt not found in PATH:**
- Run `.\scripts\install_and_run_sbt.ps1` to download sbt locally
- Or run `.\scripts\fix_sbt_and_run.ps1 run` which will use the local sbt

**Still getting ServerAlreadyBootingException:**
1. Kill all java processes:
   ```powershell
   Get-Process java | Stop-Process -Force
```
2. Delete the lock file cache:
   ```powershell
   Remove-Item C:\Users\$env:USERNAME\.sbt -Recurse -Force
   ```
3. Run again:
   ```powershell
   .\scripts\fix_sbt_and_run.ps1 run
   ```

**java.lang.UnsupportedClassVersionError:**
- Your JDK is too old. Install JDK 11 or newer.
- Check: `java -version`

## Environment Variables Used
- **JAVA_HOME** - Path to JDK (auto-detected if already set)
- **SBT_HOME** - Path to sbt (defaults to project tools/sbt)
- **SBT_OPTS** - JVM options for sbt (set to disable server: `-Dsbt.server.autostart=false`)
- **PATH** - Updated to include bin directories

