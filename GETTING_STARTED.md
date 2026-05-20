# Getting Started - Step by Step Guide

## What You Have Now

A **complete Scala project with sbt locally installed** - ready to compile and run your code.

**Location:** `C:\Users\PAVAN\Local_pro\Scala1`

---

## Step 1️⃣: Run Your First Program (2 minutes)

### Open PowerShell in your project folder

```powershell
cd C:\Users\PAVAN\Local_pro\Scala1
```

### Run the program

```powershell
.\scripts\fix_sbt_and_run.ps1 run
```

### Expected Output
```
[info] welcome to sbt 1.9.5 (Eclipse Adoptium Java 11.0.29)
[info] loading project definition from ...
[info] running Main 
Hello from Scala! This is a minimal Scala entrypoint.
[success] Total time: 3 s
```

**Congratulations!** Your Scala program ran successfully! 🎉

---

## Step 2️⃣: Edit Your Code

### Open the file in your editor

File: `C:\Users\PAVAN\Local_pro\Scala1\src\main\scala\Main.scala`

**Current Code:**
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

### Modify it (Example)

```scala
object Main extends App {
  val name: String = "pavan"
  val age: Int = 25
  val isActive: Boolean = true

  println(s"Name: $name")
  println(s"Age: $age")
  println(s"Active: $isActive")
  println("---")
  println("Scala is awesome!")
}
```

### Save the file

(Ctrl+S in most editors)

### Run again

```powershell
.\scripts\fix_sbt_and_run.ps1 run
```

### Your custom output will appear! ✨

---

## Step 3️⃣: Try Different sbt Commands

sbt can do more than just `run`. Try these:

### Compile only (no execution)
```powershell
.\scripts\fix_sbt_and_run.ps1 compile
```
Use when you want to check for errors without running.

### Clean build artifacts
```powershell
.\scripts\fix_sbt_and_run.ps1 clean
```
Use when you want a fresh start (deletes target/ folder).

### Run tests
```powershell
.\scripts\fix_sbt_and_run.ps1 test
```
Use when you have test files in `src/test/scala/`.

### Interactive Scala shell
```powershell
.\scripts\fix_sbt_and_run.ps1 console
```
Use to experiment with Scala interactively. (Type `:quit` to exit)

### Check sbt version
```powershell
.\scripts\fix_sbt_and_run.ps1 help
```

---

## Step 4️⃣: Add External Libraries (Optional)

### Example: Add JSON library

Edit: `C:\Users\PAVAN\Local_pro\Scala1\build.sbt`

**Current:**
```scala
name := "Scala1"
version := "0.1.0"
scalaVersion := "2.13.12"
```

**Add this line:**
```scala
name := "Scala1"
version := "0.1.0"
scalaVersion := "2.13.12"

libraryDependencies += "org.json4s" %% "json4s-native" % "4.0.6"
```

### Compile (downloads the library)
```powershell
.\scripts\fix_sbt_and_run.ps1 compile
```

### Use in your code
```scala
import org.json4s._
import org.json4s.native.JsonMethods._

object Main extends App {
  val json = parse("""{"name":"pavan","age":25}""")
  println(json)
}
```

### Run
```powershell
.\scripts\fix_sbt_and_run.ps1 run
```

---

## Step 5️⃣: Verify Your Setup (Optional)

### Check what's installed
```powershell
.\scripts\check_env.ps1
```

### Expected output
```
JAVA_HOME = C:\Program Files\Eclipse Adoptium\jdk-11.0.29.7-hotspot
java executable found: openjdk version "11.0.29"
SBT_HOME = C:\Users\PAVAN\Local_pro\Scala1\tools\sbt
...
```

---

## Step 6️⃣: Make Environment Permanent (Optional)

Currently, `fix_sbt_and_run.ps1` sets up everything for you automatically.

If you want sbt to **always be available** in PowerShell, run once:

```powershell
.\scripts\setenv_persist_user.ps1
```

Then **open a NEW PowerShell window** and you can use:

```powershell
sbt run
```

Directly without the script prefix!

---

## Common Tasks & Solutions

### "I get an error when I run the script"

**Check 1:** Make sure you're in the project folder
```powershell
cd C:\Users\PAVAN\Local_pro\Scala1
```

**Check 2:** If you see "execution policy" error, run this once:
```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
```

Then try again:
```powershell
.\scripts\fix_sbt_and_run.ps1 run
```

### "I want to use a different Java version"

Check which Java you have:
```powershell
java -version
```

If you have multiple JDKs, edit `scripts/setenv_persist_user.ps1` and change the `$javaHome` variable.

### "My program has a compile error"

sbt will show you the error. For example:
```
[error] Main.scala:5:3: not found: value printlnn
[error]     printlnn("Hello")
[error]     ^
```

Fix it in your editor (typo: `printlnn` → `println`) and run again.

### "I want to organize my code into multiple files"

Create new Scala files in `src/main/scala/`:

**Example: `src/main/scala/Helper.scala`**
```scala
object Helper {
  def greet(name: String): String = s"Hello, $name!"
}
```

**Use in Main.scala:**
```scala
object Main extends App {
  println(Helper.greet("pavan"))
}
```

Run:
```powershell
.\scripts\fix_sbt_and_run.ps1 run
```

---

## Project Structure Explained

```
C:\Users\PAVAN\Local_pro\Scala1/           ← Your project folder
│
├── src/                                    ← Your source code
│   └── main/scala/
│       └── Main.scala                      ← Your main program (edit this!)
│
├── build.sbt                               ← Project configuration (edit to add libraries)
│
├── scripts/                                ← Helper scripts
│   └── fix_sbt_and_run.ps1                ← Use this to run sbt commands!
│
├── tools/sbt/                              ← sbt installation (don't edit)
│
└── target/                                 ← Compiled output (auto-created)
```

---

## Tips & Tricks

### Make it faster: Use sbt's interactive mode

Instead of:
```powershell
.\scripts\fix_sbt_and_run.ps1 compile
.\scripts\fix_sbt_and_run.ps1 run
```

Do this once:
```powershell
.\scripts\fix_sbt_and_run.ps1 console
```

Then in the Scala shell:
```scala
scala> :reload
scala> :quit
```

### Save time: Skip the lock file delays

If you only run sbt once, you can just use:
```powershell
.\scripts\install_and_run_sbt.ps1
```

It's slightly simpler but doesn't clean lock files as aggressively.

### Debug your code: Add print statements

```scala
object Main extends App {
  val x = 10
  println(s"Debug: x = $x")  // This shows you the value
  println(x + 5)
}
```

Run: `.\scripts\fix_sbt_and_run.ps1 run`

### Use your favorite editor

- **Visual Studio Code:** Open folder, install Scala extension
- **IntelliJ IDEA Community:** File → Open → select folder
- **Sublime Text:** Open folder
- **Notepad++:** Just edit the file directly

Then run with sbt to compile/execute.

---

## What's Next?

### Learning Path

1. ✅ **Now:** Run your first program (you did this!)
2. 📝 **Next:** Modify Main.scala with your own code
3. 📚 **Then:** Learn Scala basics (see resources below)
4. 📦 **Later:** Add external libraries as needed

### Learning Resources

- **30-minute Scala intro:** https://docs.scala-lang.org/tour/tour-of-scala.html
- **Interactive Scala playground:** https://scastie.scala-lang.org/
- **Scala official docs:** https://docs.scala-lang.org/
- **sbt tutorial:** https://www.scala-sbt.org/1.x/docs/getting-started

---

## Troubleshooting Quick Links

| Problem | Solution |
|---------|----------|
| "sbt: The term is not recognized" | Use `.\scripts\fix_sbt_and_run.ps1` (it sets up PATH) |
| "Could not find Main.scala" | Make sure you're in `Scala1` folder and file is in `src/main/scala/` |
| "java not found" | Install Java 11+ or check JAVA_HOME |
| "Compile error" | Read sbt's error message, fix the Scala code |
| "Script won't execute" | Run: `Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process` |

---

## You're Ready! 🚀

Everything is set up. Start coding:

```powershell
# 1. Edit your code
# 2. Run:
.\scripts\fix_sbt_and_run.ps1 run
# 3. See output
# 4. Repeat!
```

**Happy coding!** 📝

---

**Next command to try:**
```powershell
.\scripts\fix_sbt_and_run.ps1 run
```

Let me know if you need help!

