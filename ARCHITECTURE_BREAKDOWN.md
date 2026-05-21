# 🏗️ PROJECT ARCHITECTURE & FILE-BY-FILE BREAKDOWN

---

## 📐 COMPLETE PROJECT ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     SPARK EMPLOYEE ANALYTICS PROJECT                         │
│                            Architecture Diagram                              │
└─────────────────────────────────────────────────────────────────────────────┘

                             ┌──────────────────┐
                             │   build.sbt      │
                             │  (Dependencies)  │
                             │  ├─ Scala 2.13   │
                             │  └─ Spark 3.5    │
                             └────────┬─────────┘
                                      │
                    ┌─────────────────┼─────────────────┐
                    │                 │                 │
                    ▼                 ▼                 ▼
           ┌─────────────────┐ ┌──────────────┐ ┌────────────────────┐
           │  Employee.scala │ │  Main.scala  │ │EmployeeAnalytics   │
           │                 │ │              │ │   .scala (CORE)    │
           │ DATA MODEL      │ │ ENTRY POINT  │ │                    │
           │                 │ │              │ │ Complete Analytics │
           │ case class      │ │ delegates to │ │ - 3 Filters        │
           │ Employee        │ │ Analytics    │ │ - 2 Mappings       │
           │                 │ │              │ │ - 9 Aggregators    │
           │ 6 fields:       │ │ (12 lines)   │ │ - 1 Join           │
           │ empId: Int      │ │              │ │ (282 lines)        │
           │ name: String    │ │              │ │                    │
           │ department      │ │              │ │ ├─ Filters         │
           │ salary: Double  │ │              │ │ ├─ Expressions     │
           │ joinDate        │ │              │ │ ├─ Mappings        │
           │ city: String    │ │              │ │ ├─ Aggregators     │
           │                 │ │              │ │ ├─ Joins           │
           │ (11 lines)      │ │              │ │ └─ Outputs         │
           └────────┬────────┘ └──────┬───────┘ └──────────┬─────────┘
                    │                 │                    │
                    └─────────────────┼────────────────────┘
                                      │
                    ┌─────────────────┼─────────────────┐
                    │                 │                 │
                    ▼                 ▼                 ▼
        ┌─────────────────────┐  ┌──────────────┐  ┌──────────────┐
        │ data/employees.csv  │  │ CONSOLE      │  │ CSV OUTPUTS  │
        │ (INPUT - 100 rows)  │  │ (15 sections)│  │              │
        │                     │  │              │  │ employees_   │
        │ empId,name,dept,    │  │ Filters:     │  │   csv/       │
        │ salary,joinDate,    │  │ - By Dept    │  │              │
        │ city                │  │ - By Salary  │  │ departments_ │
        │                     │  │ - By Date    │  │   csv/       │
        │ 101,John Doe,       │  │              │  │              │
        │ Engineering,92500   │  │ Expressions: │  │ (Coalesced   │
        │ ...                 │  │ - Bonus      │  │  to single   │
        │                     │  │ - Salary Band│  │  file)       │
        │ (header + 100 rows) │  │              │  │              │
        │                     │  │ Aggregations:│  │              │
        │                     │  │ - 9 types    │  │              │
        │                     │  │ - Statistics │  │              │
        │                     │  │              │  │              │
        │                     │  │ Joins:       │  │              │
        │                     │  │ - Left Join  │  │              │
        │                     │  │              │  │              │
        └─────────────────────┘  └──────────────┘  └──────────────┘

        █ INPUT               █ COMPUTATION               █ OUTPUT
```

---

## 📁 FILE-BY-FILE BREAKDOWN

### **1. build.sbt**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\build.sbt` (14 lines)

**PURPOSE:** Build configuration & dependency management

**CONTENT:**
```scala
name := "Scala1"                                    # Project name
version := "0.1.0"                                 # Version
scalaVersion := "2.13.12"                          # Scala compiler

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "3.5.0"    # Spark SQL dependency
)

Compile / run / mainClass := Some("Main")         # Entry point for sbt run
```

**WHAT IT MANAGES:**
- ✅ Scala version (2.13.12)
- ✅ Spark version (3.5.0)
- ✅ Maven dependency resolution
- ✅ Main class routing
- ✅ Compilation output

**DEPENDENCIES RESOLVED:**
```
├─ org.apache.spark:spark-sql_2.13:3.5.0
│  ├─ spark-core
│  ├─ spark-catalyst
│  ├─ hadoop-client
│  ├─ parquet-format
│  └─ arrow (for vectorized ops)
└─ scala-library:2.13.12
```

**KEY ROLES:**
1. Defines how `sbt compile` works
2. Defines how `sbt run` works
3. Downloads all required JAR files from Maven Central
4. Sets the main class to execute

---

### **2. src/main/scala/Employee.scala**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\src\main\scala\Employee.scala` (11 lines)

**PURPOSE:** Data model definition for employee records

**CONTENT:**
```scala
case class Employee(
  empId: Int,           # 1. Employee unique ID (101, 102, 103, ...)
  name: String,         # 2. Full name ("John Doe", "Jane Smith", ...)
  department: String,   # 3. Dept ("Engineering", "Sales", "HR", "Marketing")
  salary: Double,       # 4. Annual salary (82000.0, 75000.0, ...)
  joinDate: String,     # 5. Join date as string ("2020-01-15", ...)
  city: String          # 6. City location ("Bangalore", "Mumbai", "Pune")
)
```

**WHAT IT DEFINES:**
- ✅ Schema for employee records
- ✅ Data types for each column
- ✅ Field names used throughout project
- ✅ Immutable case class (Scala best practice)

**USED BY:**
- EmployeeAnalytics.scala (data structure reference)
- build.sbt (compilation target)
- CSV reader (implicit schema reference)

**WHAT MAPS TO CSV:**
```
CSV Column      │ Scala Field    │ Type    │ Used In Functions
────────────────┼────────────────┼─────────┼──────────────────
empId           │ empId          │ Int     │ Filters, Aggregations
name            │ name           │ String  │ Mappings (uppercase)
department      │ department     │ String  │ All filters & aggs
salary          │ salary         │ Double  │ All financial aggs
joinDate        │ joinDate       │ String  │ Date filters, aggs
city            │ city           │ String  │ City aggregations
```

**CONSTRAINTS:**
- ✅ Must have 6 fields (matches CSV columns)
- ✅ Names must match CSV header exactly (case-sensitive)
- ✅ Data types must be compatible with CSV values
- ✅ salary must be numeric (Double)
- ✅ empId must be numeric (Int)

---

### **3. src/main/scala/Main.scala**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\src\main\scala\Main.scala` (12 lines)

**PURPOSE:** Entry point for the application

**CONTENT:**
```scala
// Main entry point - delegates to EmployeeAnalytics
object Main {
  def main(args: Array[String]): Unit = {
    EmployeeAnalytics.main(args)
  }
}
```

**WHAT IT DOES:**
1. Receives command-line arguments (optional CSV path)
2. Forwards them to EmployeeAnalytics.main()
3. Allows SBT to find a `main` method to execute

**EXECUTION FLOW:**
```
User runs: sbt run
    ↓
SBT looks for Main.scala → Finds Main object
    ↓
SBT calls: Main.main(Array())
    ↓
Main.main delegates to: EmployeeAnalytics.main(args)
    ↓
EmployeeAnalytics.main() executes all analytics
```

**ARGUMENT PASSING:**
```powershell
# Default (no args):
sbt -no-server run
    → args = []
    → EmployeeAnalytics receives empty array
    → Uses default: "data/employees.csv"

# With custom path:
sbt -no-server 'run "C:/custom/path/employees.csv"'
    → args = ["C:/custom/path/employees.csv"]
    → EmployeeAnalytics receives array with path
    → Uses supplied path
```

---

### **4. src/main/scala/EmployeeAnalytics.scala (★ CORE ★)**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\src\main\scala\EmployeeAnalytics.scala` (282 lines)

**PURPOSE:** Complete analytics logic, all transformations, aggregations, joins

#### **FILE STRUCTURE:**

**Lines 1-8: Imports & Setup**
```scala
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrame

case class Department(department: String, manager: String)
```

**Lines 11-145: main() function**
- Orchestrates entire pipeline
- Reads data
- Calls all filter/expression/aggregation functions
- Writes outputs
- 15 different output sections

**Lines 150-281: Private helper functions**
- All analytics functions separated for reusability
- Clear documentation

#### **SECTION BREAKDOWN:**

| Section | Lines | Description | Functions |
|---------|-------|-------------|-----------|
| **Imports** | 1-8 | Spark, SQL, DataFrame imports | - |
| **Data Model** | 7 | Department case class | Department |
| **main()** | 11-145 | Orchestrator function | (single main) |
| ├─ Spark Init | 13-16 | Create Spark session | SparkSession.builder() |
| ├─ Load Data | 27-41 | Read CSV, cast types | spark.read.csv() |
| ├─ Display | 43-54 | Show data & expressions | .show() calls |
| ├─ FILTERS | 59, 64, 99 | Call filter functions | 3 functions |
| ├─ AGGREGATORS | 69,74,79,84,89,94,104,109,138 | Call agg functions | 9 functions |
| ├─ JOINS | 112-133 | Department data & join | joinWithDepartmentInfo() |
| └─ Cleanup | 141-144 | Stop session, print summary | spark.stop() |
| **FILTERS** | 150-228 | 3 filter functions | |
| Filters | 150-153 | Filter by department | filterByDepartment() |
| Filters | 158-162 | Filter by salary range | filterBySalaryRange() |
| Filters | 224-228 | Filter by join date | employeesJoinedAfter() |
| **AGGREGATIONS** | 167-259 | 9 aggregation functions | |
| Agg | 167-171 | Avg by department | averageSalaryByDepartment() |
| Agg | 176-181 | Count by department | employeeCountByDepartment() |
| Agg | 186-190 | Top N by salary | topPaidEmployees() |
| Agg | 195-200 | Count by city | employeesByCity() |
| Agg | 205-209 | Avg + count by city | averageSalaryByCity() |
| Agg | 214-219 | Highest avg dept | departmentWithHighestAvgSalary() |
| Agg | 233-237 | Total salary by dept | totalSalaryByDepartment() |
| Agg | 242-250 | Global statistics | salaryStatistics() |
| Agg | 255-259 | Dept + city aggregation | aggregateByDeptCity() |
| **EXPRESSIONS** | 271-278 | 2 expression functions | |
| Expr | 271-274 | Add bonus & salary band | addBonusColumn() |
| Expr | 276-278 | Uppercase names | nameUppercaseMapping() |
| **JOINS** | 264-266 | 1 join function | joinWithDepartmentInfo() |

---

#### **DETAILED FUNCTIONALITY LIST:**

```
┌─────────────────────────────────────────────────────────────────────┐
│                        ALL 17 FUNCTIONS                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│ ▌ CATEGORY: FILTERS (3)                                             │
│  1. filterByDepartment(df, dept)          [L150-153]                │
│     Returns: Employees from specified department                    │
│     Called in main(): Line 59                                       │
│                                                                     │
│  2. filterBySalaryRange(df, min, max)     [L158-162]                │
│     Returns: Employees with salary in range                         │
│     Called in main(): Line 64                                       │
│                                                                     │
│  3. employeesJoinedAfter(df, date)        [L224-228]                │
│     Returns: Employees who joined after date                        │
│     Called in main(): Line 99                                       │
│                                                                     │
│ ▌ CATEGORY: EXPRESSIONS & MAPPINGS (2)                              │
│  4. addBonusColumn(df)                    [L271-274]                │
│     Returns: DF with bonus (10%) and salary_band (A/B/C)           │
│     Called in main(): Line 50                                       │
│                                                                     │
│  5. nameUppercaseMapping(df)              [L276-278]                │
│     Returns: DF with name_upper column                              │
│     Called in main(): Line 54                                       │
│                                                                     │
│ ▌ CATEGORY: AGGREGATORS (9)                                         │
│  6. averageSalaryByDepartment(df)         [L167-171]                │
│     Returns: Dept | Avg_Salary (ordered desc)                       │
│     Called in main(): Line 69                                       │
│                                                                     │
│  7. employeeCountByDepartment(df)         [L176-181]                │
│     Returns: Dept | Employee_Count (ordered desc)                   │
│     Called in main(): Line 74                                       │
│                                                                     │
│  8. topPaidEmployees(df, n)               [L186-190]                │
│     Returns: Top n employees by salary                              │
│     Called in main(): Line 79                                       │
│                                                                     │
│  9. employeesByCity(df)                   [L195-200]                │
│     Returns: City | Employee_Count (ordered desc)                   │
│     Called in main(): Line 84                                       │
│                                                                     │
│  10. averageSalaryByCity(df)              [L205-209]                │
│     Returns: City | Avg_Salary | Emp_Count                          │
│     Called in main(): Line 89                                       │
│                                                                     │
│  11. departmentWithHighestAvgSalary(df)   [L214-219]                │
│     Returns: Top 1 department by avg salary                         │
│     Called in main(): Line 94                                       │
│                                                                     │
│  12. totalSalaryByDepartment(df)          [L233-237]                │
│     Returns: Dept | Total_Salary_Expense (ordered desc)             │
│     Called in main(): Line 104                                      │
│                                                                     │
│  13. salaryStatistics(df)                 [L242-250]                │
│     Returns: Min/Max/Avg/StdDev/Count of all salaries               │
│     Called in main(): Line 109                                      │
│                                                                     │
│  14. aggregateByDeptCity(df)              [L255-259]                │
│     Returns: (Dept, City) | Avg_Salary | Max_Salary | Count        │
│     Called in main(): Line 138                                      │
│                                                                     │
│ ▌ CATEGORY: JOINS (1)                                               │
│  15. joinWithDepartmentInfo(df, deptDF)   [L264-266]                │
│     Returns: Employees LEFT JOINED with departments (manager)       │
│     Called in main(): Line 132                                      │
│                                                                     │
│ ▌ CATEGORY: HELPER FUNCTIONS IN main() (2)                          │
│  16. Load CSV                             [L30-41]                  │
│     spark.read.csv() + type casting                                 │
│                                                                     │
│  17. Write CSV                            [L125-126]                │
│     coalesce(1).write.csv() for employees_csv/ and depts_csv/      │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

### **5. data/employees.csv**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\data\employees.csv`

**PURPOSE:** Source data for all analytics

**FORMAT:**
```
empId,name,department,salary,joinDate,city
101,John Doe,Engineering,92500.0,2020-01-15,Bangalore
102,Jane Smith,Sales,75000.0,2019-03-22,Mumbai
...
200,Xyz Person,HR,68000.0,2021-06-10,Pune
```

**STRUCTURE:**
- **Header Row:** 1 row (column names)
- **Data Rows:** 100 employee records
- **Total Lines:** 101

**COLUMN DETAILS:**
| Column | Type | Sample | Used In Filters/Aggs |
|--------|------|--------|----------|
| empId | Integer | 101 | Selection, join key |
| name | String | John Doe | Mapping (uppercase) |
| department | String | Engineering | Filters, groupBy |
| salary | Double | 92500.0 | All financial aggs |
| joinDate | String (→ Date) | 2020-01-15 | Date filters |
| city | String | Bangalore | City groupBy |

**DATA LOADING (EmployeeAnalytics L30-41):**
```scala
val employeeDF = {
  val df = spark.read
    .option("header", "true")      // First row = column names
    .option("inferSchema", "true") // Guess data types
    .csv("data/employees.csv")     // Read file
  
  // Explicit type casting for correctness
  df.withColumn("empId", col("empId").cast("int"))
    .withColumn("salary", col("salary").cast("double"))
    .withColumn("joinDate", to_date(col("joinDate"), "yyyy-MM-dd"))
    .select("empId", "name", "department", "salary", "joinDate", "city")
}
```

**KEY POINTS:**
- ✅ Must have exactly 6 columns
- ✅ Column names must match (case-sensitive: empId, not empid)
- ✅ salary must be numeric (not text like "$92500")
- ✅ empId must be numeric
- ✅ joinDate must be in yyyy-MM-dd format
- ✅ 100+ rows for meaningful analytics

---

### **6. project/build.properties**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\project\build.properties` (1 line)

**PURPOSE:** SBT version specification

**CONTENT:**
```
sbt.version=1.9.5
```

**WHAT IT DOES:**
- ✅ Tells SBT which version to use
- ✅ Auto-downloads SBT 1.9.5 if not installed
- ✅ Ensures consistency across environments

---

## 🔄 FUNCTION DEPENDENCY GRAPH

```
                    ┌─────────────────────┐
                    │  data/employees.csv │ (100 rows)
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │    spark.read       │
                    │   (Load & Cast)     │
                    │   employeeDF        │
                    └──────────┬──────────┘
                               │
        ┌──────────────────────┴──────────────────────┐
        │                                              │
        ▼                                              ▼
   ┌─────────────┐                          ┌─────────────────┐
   │   FILTERS   │                          │ EXPRESSIONS     │
   │             │                          │   & MAPPINGS    │
   │ 3 functions │                          │                 │
   │             │                          │ 2 functions     │
   └─────────────┘                          └─────────────────┘
        │                                              │
        │                                              │
        └──────────────────┬─────────────────────────┘
                          │
                          ▼
                   ┌─────────────┐
                   │ AGGREGATIONS│
                   │             │
                   │ 9 functions │
                   └──────┬──────┘
                          │
        ┌─────────────────┼─────────────────┐
        │                 │                 │
        ▼                 ▼                 ▼
   ┌────────────┐  ┌────────────┐  ┌────────────┐
   │ aggregateBy│  │ Console    │  │ Write CSV  │
   │ DeptCity() │  │ Output     │  │ functions  │
   └────────────┘  │ (15 secs)  │  └────────────┘
                   └────────────┘
                         │
                         ▼
                   ┌────────────┐
                   │ Department │
                   │ DataFrame  │
                   │ (4 rows)   │
                   └──────┬─────┘
                          │
                          ▼
                   ┌────────────┐
                   │   JOIN     │
                   │ (Left Join)│
                   └────────────┘
                         │
                         ▼
                   ┌────────────┐
                   │ Joined DF  │
                   │ (100 x 7)  │
                   └────────────┘
```

---

## 📊 FILE INTERACTION MATRIX

```
┌─────────────────────────────────────────────────────────────────────────┐
│ FILE INTERACTIONS & DEPENDENCIES                                        │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│ build.sbt                                                               │
│  └─► Manages: Employee.scala, Main.scala, EmployeeAnalytics.scala     │
│      Downloads: Spark SQL 3.5.0 + dependencies                         │
│      Defines: Main class entry point                                    │
│                                                                         │
│ Main.scala                                                              │
│  └─► Calls: EmployeeAnalytics.main()                                   │
│      Receives: args (optional CSV path)                                 │
│      Forwards: args to EmployeeAnalytics                               │
│                                                                         │
│ Employee.scala                                                          │
│  └─► Used by: EmployeeAnalytics.scala (schema reference)               │
│      Defines: 6-field case class                                        │
│      Matches: data/employees.csv columns                                │
│                                                                         │
│ EmployeeAnalytics.scala (CORE)                                          │
│  ├─► Reads: data/employees.csv (via spark.read.csv)                    │
│  ├─► References: Employee.scala (implicit schema)                       │
│  ├─► Creates: Department case class (internal)                          │
│  ├─► Writes: employees_csv/ and departments_csv/                        │
│  └─► Outputs: 15 sections to console                                    │
│                                                                         │
│ data/employees.csv                                                      │
│  └─► Loaded by: EmployeeAnalytics.scala (line 34)                      │
│      Format: 6 columns, 100 data rows                                   │
│      Default path: "data/employees.csv"                                 │
│                                                                         │
│ project/build.properties                                                │
│  └─► Used by: SBT build system                                          │
│      Specifies: sbt.version = 1.9.5                                     │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 🚦 EXECUTION SEQUENCE

```
STEP 1: BUILD
  build.sbt
    ├─ sbt compile
    ├─ Resolve: org.apache.spark:spark-sql:3.5.0
    ├─ Compile: Employee.scala → Employee.class
    ├─ Compile: Main.scala → Main.class
    ├─ Compile: EmployeeAnalytics.scala → EmployeeAnalytics.class
    └─ Output: target/scala-2.13/classes/

STEP 2: MAIN ENTRY
  Main.scala
    ├─ sbt run (or sbt run "custom/path.csv")
    ├─ Calls: Main.main(args)
    └─ Delegates: EmployeeAnalytics.main(args)

STEP 3: INITIALIZATION
  EmployeeAnalytics.main() Line 13-16
    ├─ Create SparkSession("Employee Analytics")
    ├─ Master: local[*]
    └─ Import spark.implicits

STEP 4: DATA LOADING
  EmployeeAnalytics.main() Line 27-41
    ├─ Determine path: args(0) or "data/employees.csv"
    ├─ spark.read.csv(path)
    ├─ Cast types (Int, Double, Date)
    ├─ Select 6 columns
    └─ Result: employeeDF (100 rows, 6 cols)

STEP 5: DISPLAY DATA
  EmployeeAnalytics.main() Line 43-54
    ├─ employeeDF.show()
    ├─ addBonusColumn(employeeDF).show()
    └─ nameUppercaseMapping(employeeDF).show()

STEP 6: FILTERS
  EmployeeAnalytics.main() Line 59, 64, 99
    ├─ filterByDepartment("Engineering")
    ├─ filterBySalaryRange(75000, 85000)
    └─ employeesJoinedAfter("2021-01-01")

STEP 7: AGGREGATIONS
  EmployeeAnalytics.main() Line 69-138
    ├─ 9 aggregation functions
    ├─ groupBy, agg, orderBy, limit
    └─ Each outputs to console

STEP 8: CSV WRITE
  EmployeeAnalytics.main() Line 125-126
    ├─ employeeDF.coalesce(1).write.csv("employees_csv/")
    ├─ departmentDF.coalesce(1).write.csv("departments_csv/")
    └─ Creates: .../part-00000.csv files

STEP 9: JOIN
  EmployeeAnalytics.main() Line 132
    ├─ joinWithDepartmentInfo(employeeDF, departmentDF)
    ├─ LEFT JOIN on "department" column
    └─ Result: 100 employees + their manager names

STEP 10: FINAL AGGREGATION
  EmployeeAnalytics.main() Line 138
    ├─ aggregateByDeptCity(employeeDF)
    └─ Multi-level groupBy: (dept, city)

STEP 11: CLEANUP
  EmployeeAnalytics.main() Line 141-144
    ├─ spark.stop()
    ├─ Print summary
    └─ Graceful exit
```

---

## 💾 MEMORY & PERFORMANCE SNAPSHOT

```
RESOURCE USAGE (for 100 employee records)
│
├─ RAM (Spark Driver)
│  └─ Base Spark: ~500MB
│  └─ 100 employees DF: ~1-2MB
│  └─ Aggregations: ~500KB per result
│  └─ Total typical: ~600MB
│
├─ EXECUTION TIME
│  └─ Spark init: ~2-3 seconds
│  └─ CSV load: ~500ms
│  └─ All filters/aggs: ~200-500ms
│  └─ Total runtime: ~5-10 seconds
│
├─ I/O OPERATIONS
│  └─ Read: 1 CSV file (100 rows)
│  └─ Write: 2 CSV directories (coalesced)
│  └─ Console: 15 output sections
│
└─ NETWORK (if not local)
   └─ None (local[*] mode = no cluster)
```

---

## 🔍 TRACING A SINGLE FUNCTIONALITY EXAMPLE

**Example: "Filter employees by Engineering department"**

```
1. CODE LOCATION
   EmployeeAnalytics.scala, Line 59:
   ├─ filterByDepartment(employeeDF, "Engineering").show(false)
   │
   └─ IMPLEMENTATION: Lines 150-153
      private def filterByDepartment(df: DataFrame, dept: String): DataFrame = {
        df.filter(col("department") === dept)
          .select("empId", "name", "department", "salary", "city")
      }

2. DATA FLOW
   employeeDF (100 rows)
   ├─ filter(col("department") === "Engineering")
   ├─ Result: ~25 rows (estimated)
   ├─ select(5 columns)
   └─ Return: Filtered DataFrame

3. SPARK OPTIMIZATION
   ├─ Pushes down filter to CSV reader (if possible)
   ├─ Predicates: department == "Engineering"
   ├─ Projection: 5 columns selected
   └─ Execution: Single pass through data

4. OUTPUT
   Called from: main() Line 59
   Prints: "2. EMPLOYEES IN ENGINEERING DEPARTMENT"
   Shows: All engineering employees (name, dept, salary, city, empId)
```

---

## 📋 FINAL CHECKLIST: FILES CREATED & REQUIRED

```
✅ REQUIRED FILES (MUST EXIST)
├─ build.sbt                             [14 lines]
├─ src/main/scala/Employee.scala         [11 lines]
├─ src/main/scala/Main.scala             [12 lines]
├─ src/main/scala/EmployeeAnalytics.scala [282 lines]
├─ data/employees.csv                    [101 lines - header + 100 rows]
└─ project/build.properties              [1 line]

✅ AUTO-GENERATED (After sbt compile)
├─ target/
│  ├─ scala-2.13/classes/
│  │  ├─ Employee.class
│  │  ├─ Employee$.class
│  │  ├─ Main.class
│  │  ├─ Main$.class
│  │  ├─ EmployeeAnalytics.class
│  │  ├─ EmployeeAnalytics$.class
│  │  └─ Department.class
│  └─ ... (other build artifacts)
└─ .bsp/                                 [IDE integration]

✅ GENERATED AT RUNTIME (After sbt run)
├─ employees_csv/
│  └─ part-00000.csv
├─ departments_csv/
│  └─ part-00000.csv
└─ [Console output with 15 sections]

⚠️ OPTIONAL (But recommended)
├─ PROJECT_BLUEPRINT.md                  [Created by agent]
├─ FUNCTIONALITIES_GUIDE.md              [Created by agent]
└─ README.md                             [User documentation]
```

---

## 🎯 KEY TAKEAWAYS

| Aspect | Details |
|--------|---------|
| **Entry Point** | Main.scala → Main.main() → EmployeeAnalytics.main() |
| **Data Source** | data/employees.csv (100 rows, 6 columns) |
| **Core Logic** | EmployeeAnalytics.scala (282 lines, 17 functions) |
| **Data Model** | Employee.scala (6 fields) + Department (2 fields) |
| **Build Config** | build.sbt (Scala 2.13.12 + Spark 3.5.0) |
| **Outputs** | 15 console sections + 2 CSV directories |
| **Functionalities** | 3 Filters + 2 Expressions + 9 Aggregators + 1 Join |
| **Total Lines** | 412 lines of Scala code (excl. build/config) |


