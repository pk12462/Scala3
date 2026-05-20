# Quick Reference Card - Scala/sbt Local Setup

## 🚀 Run Your Scala Program

```powershell
.\scripts\fix_sbt_and_run.ps1 run
```

**Output:**
```
Hello from Scala! This is a minimal Scala entrypoint.
```

---

## 📋 Available Commands

| Command | Purpose |
|---------|---------|
| `.\scripts\fix_sbt_and_run.ps1 run` | Compile & run program |
| `.\scripts\fix_sbt_and_run.ps1 compile` | Compile only |
| `.\scripts\fix_sbt_and_run.ps1 test` | Run tests |
| `.\scripts\fix_sbt_and_run.ps1 clean` | Clean build artifacts |
| `.\scripts\fix_sbt_and_run.ps1 console` | Interactive Scala shell |
| `.\scripts\check_env.ps1` | Verify setup |

---

## 📁 Project Structure

```
Scala1/
├── scripts/
│   ├── fix_sbt_and_run.ps1    ← USE THIS FOR sbt COMMANDS
│   ├── install_and_run_sbt.ps1
│   ├── check_env.ps1
│   ├── setenv_session.ps1
│   └── setenv_persist_user.ps1
├── src/main/scala/
│   └── Main.scala             ← YOUR SCALA CODE
├── build.sbt                  ← BUILD CONFIGURATION
└── tools/sbt/                 ← LOCAL sbt (1.9.5)
```

---

## ⚙️ Environment Setup

### Option 1: Temporary (Current Session Only)
```powershell
. .\scripts\setenv_session.ps1
sbt run
```

### Option 2: Persistent (All Future Sessions)
```powershell
.\scripts\setenv_persist_user.ps1
# Open a NEW PowerShell window, then:
sbt run
```

### Option 3: Recommended (Automatic)
```powershell
# Just use fix_sbt_and_run.ps1 - it handles everything
.\scripts\fix_sbt_and_run.ps1 run
```

---

## 🔧 System Info

| Component | Version | Location |
|-----------|---------|----------|
| Java | 11.0.29 (Temurin) | C:\Program Files\Eclipse Adoptium\jdk-11... |
| Scala | 2.13.12 | (Managed by sbt) |
| sbt | 1.9.5 | C:\Users\PAVAN\Local_pro\Scala1\tools\sbt |

---

## ❌ Troubleshooting

### Error: "sbt not found"
**Solution:**
```powershell
.\scripts\install_and_run_sbt.ps1
```

### Error: "ServerAlreadyBootingException"
**Solution:**
```powershell
# It's already fixed! Just use:
.\scripts\fix_sbt_and_run.ps1 run
```

### Error: "Script execution disabled"
**Solution:**
```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\scripts\fix_sbt_and_run.ps1 run
```

### "java not found"
**Check:** `java -version`
**Fix:** Install JDK 11 or update JAVA_HOME

---

## 📝 Edit Your Code

Open in your IDE or text editor:
```
C:\Users\PAVAN\Local_pro\Scala1\src\main\scala\Main.scala
```

**Example:**
```scala
object Main extends App {
  val name: String = "pavan"
  val age: Int = 25
  val isActive: Boolean = true

  println(age)
  println(name)
  println(isActive)
}
```

Then run:
```powershell
.\scripts\fix_sbt_and_run.ps1 run
```

---

## 📚 Documentation Files

- **README.md** - Full user guide with examples
- **SBT_FIX_SUMMARY.md** - Technical details on IPC lock fix
- **SBT_LOCAL_SETUP_SUMMARY.md** - Complete setup summary

---

## ✨ Key Features

✅ **No Admin Rights** - Everything local to project
✅ **No System Pollution** - Doesn't modify global Windows settings
✅ **Windows IPC Error Fixed** - Ready to use on Windows
✅ **Simple Commands** - One-liner to run your code
✅ **Easy Setup** - Auto-detects Java, handles environment

---

## 🎯 Typical Workflow

```powershell
# 1. Edit your code
code .\src\main\scala\Main.scala

# 2. Compile
.\scripts\fix_sbt_and_run.ps1 compile

# 3. Run
.\scripts\fix_sbt_and_run.ps1 run

# 4. Test (if you have tests)
.\scripts\fix_sbt_and_run.ps1 test
```

---

## 💾 Add Dependencies (Example)

Edit `build.sbt`:
```scala
name := "Scala1"
version := "0.1.0"
scalaVersion := "2.13.12"

libraryDependencies += "org.json4s" %% "json4s-native" % "4.0.6"
```

Then:
```powershell
.\scripts\fix_sbt_and_run.ps1 compile  # Downloads dependency
```

---

## 🔍 Check Setup

```powershell
.\scripts\check_env.ps1
```

**Expected output:**
```
JAVA_HOME = C:\Program Files\Eclipse Adoptium\jdk-11...
java executable found: openjdk version "11.0.29"
...
```

---

**Status:** ✅ **READY TO USE**

**Last Updated:** May 20, 2026

