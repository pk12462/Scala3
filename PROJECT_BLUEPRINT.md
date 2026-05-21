# 📋 SCALA SPARK EMPLOYEE ANALYTICS - PROJECT BLUEPRINT

---

## 📐 PROJECT ARCHITECTURE OVERVIEW

```
┌─────────────────────────────────────────────────────────────────────┐
│                    SCALA SPARK EMPLOYEE ANALYTICS                    │
│                        (Scala 2.13 | Spark 3.5)                      │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                    ┌───────────────┴───────────────┐
                    ▼                               ▼
        ┌─────────────────────┐      ┌──────────────────────┐
        │   DATA LAYER        │      │   PROCESSING LAYER   │
        │  (CSV Input/Output) │      │   (Spark Operations) │
        └─────────────────────┘      └──────────────────────┘
                    │                               │
        ┌───────────┴───────────┐      ┌───────────┴──────────────┐
        │                       │      │                          │
        ▼                       ▼      ▼                          ▼
   Input Data           Output Data   Filters              Aggregations
   employees.csv        employees_csv  Mappings             Joins
                        departments_csv Expressions         Statistics
```

---

## 🗂️ FILE STRUCTURE & REQUIREMENTS

### **Directory Layout**
```
Scala1/
├── build.sbt                          # BUILD CONFIGURATION (Scala version + Dependencies)
├── src/
│   ├── Main.java                      # LEGACY (not used - kept for reference)
│   └── main/scala/
│       ├── Main.scala                 # ENTRY POINT - Delegates to EmployeeAnalytics
│       ├── Employee.scala             # DATA MODEL - Case class for Employee records
│       └── EmployeeAnalytics.scala    # CORE LOGIC - All analytics functions (282 lines)
├── data/
│   └── employees.csv                  # INPUT DATA - Source CSV with 100 employee records
├── project/
│   └── build.properties               # SBT PROJECT METADATA
├── target/                            # BUILD ARTIFACTS (auto-generated)
│   └── scala-2.13/
└── scripts/                           # SHELL SCRIPTS FOR EXECUTION
```

---

## 📄 FILES EXPLANATION & REQUIREMENTS

### **1. `build.sbt` - Build Configuration**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\build.sbt`

**Purpose:** Defines project metadata, Scala version, and library dependencies.

**Requirements:**
```scala
name := "Scala1"
version := "0.1.0"
scalaVersion := "2.13.12"          # ✅ Scala 2.13.12 (must match)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "3.5.0"   # ✅ Apache Spark SQL 3.5.0
)

Compile / run / mainClass := Some("Main")  # ✅ Main class entry point
```

**What You Need:**
- ✅ Java 11+ (JDK must be installed and JAVA_HOME set)
- ✅ SBT 1.9.5+ (Scala Build Tool)
- ✅ Internet connection (for Maven downloads of Spark dependencies)

---

### **2. `src/main/scala/Employee.scala` - Data Model**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\src\main\scala\Employee.scala` (11 lines)

**Purpose:** Defines the Employee data structure (case class).

**Structure:**
```scala
case class Employee(
  empId: Int,        # Employee ID (numeric)
  name: String,      # Employee name
  department: String,# Department (Engineering, Sales, HR, Marketing)
  salary: Double,    # Annual salary
  joinDate: String,  # Join date (format: yyyy-MM-dd)
  city: String       # City location
)
```

**Used By:** EmployeeAnalytics reads CSV and casts columns to these types.

**CSV Format Required:**
```
empId,name,department,salary,joinDate,city
1,John Doe,Engineering,82000.0,2020-01-15,Bangalore
2,Jane Smith,Sales,75000.0,2019-03-22,Mumbai
...
```

---

### **3. `src/main/scala/Main.scala` - Entry Point**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\src\main\scala\Main.scala` (12 lines)

**Purpose:** Simple entry point that delegates to EmployeeAnalytics.

```scala
object Main {
  def main(args: Array[String]): Unit = {
    EmployeeAnalytics.main(args)
  }
}
```

**Used By:** SBT calls `Main.main()` when running `sbt run`

---

### **4. `src/main/scala/EmployeeAnalytics.scala` - CORE LOGIC (282 lines)**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\src\main\scala\EmployeeAnalytics.scala`

**Purpose:** Contains ALL analytics functionalities: filters, mappings, aggregations, joins, and statistics.

**Structure Overview:**
```
EmployeeAnalytics (object)
├── main(args) - Orchestrates the entire pipeline
├── FILTERS (3 functions)
├── EXPRESSIONS/MAPPINGS (2 functions)
├── AGGREGATORS (6 functions)
├── JOINS (1 function)
└── DATA MODEL (Department case class)
```

---

### **5. `data/employees.csv` - Input Data**
**Location:** `C:\Users\PAVAN\Local_pro\Scala1\data\employees.csv`

**Purpose:** Source data for analytics (100 employee records).

**Required Columns:**
```
empId     | int    | Unique employee ID
name      | string | Employee full name
department| string | Department name (Engineering, Sales, HR, Marketing)
salary    | double | Annual salary (numeric)
joinDate  | string | Date joined (yyyy-MM-dd format)
city      | string | City/location
```

**Sample Row:**
```
empId,name,department,salary,joinDate,city
101,Ramesh Kumar,Engineering,92500.0,2018-05-10,Bangalore
```

**Generated Output CSVs:**
- `employees_csv/` - Coalesced CSV output of all employees
- `departments_csv/` - Coalesced CSV output of department data

---

## 🔧 ALL FUNCTIONALITIES BREAKDOWN

### **A. FILTERS (3 Functions)**

| Function | Location | Input | Output | Example |
|----------|----------|-------|--------|---------|
| `filterByDepartment(df, dept)` | Line 150-153 | DataFrame, String | Filtered DF | Engineering dept only |
| `filterBySalaryRange(df, min, max)` | Line 158-162 | DF, Double, Double | Filtered DF | Salary between 75K-85K |
| `employeesJoinedAfter(df, date)` | Line 224-228 | DF, String | Filtered DF | Joined after 2021-01-01 |

**Code Locations:**
```scala
// FILTER 1: By Department (Line 150-153)
private def filterByDepartment(df: DataFrame, dept: String): DataFrame = {
  df.filter(col("department") === dept)
    .select("empId", "name", "department", "salary", "city")
}

// FILTER 2: By Salary Range (Line 158-162)
private def filterBySalaryRange(df: DataFrame, minSalary: Double, maxSalary: Double): DataFrame = {
  df.filter(col("salary").between(minSalary, maxSalary))
    .select("empId", "name", "department", "salary")
    .orderBy(col("salary").desc)
}

// FILTER 3: By Join Date (Line 224-228)
private def employeesJoinedAfter(df: DataFrame, date: String): DataFrame = {
  df.filter(col("joinDate") > date)
    .select("empId", "name", "department", "joinDate", "city")
    .orderBy(col("joinDate").desc)
}
```

**Called In main() At:**
- Line 59: Engineering filter
- Line 64: Salary range 75K-85K
- Line 99: Joined after 2021-01-01

---

### **B. EXPRESSIONS & MAPPINGS (2 Functions)**

| Function | Location | Purpose | Output Columns |
|----------|----------|---------|-----------------|
| `addBonusColumn(df)` | Line 271-274 | Derive bonus (10% of salary) + salary band | `bonus`, `salary_band` |
| `nameUppercaseMapping(df)` | Line 276-278 | Map names to uppercase | `name_upper` |

**Code Locations:**
```scala
// EXPRESSION 1: Add Bonus & Salary Band (Line 271-274)
private def addBonusColumn(df: DataFrame): DataFrame = {
  df.withColumn("bonus", round(col("salary") * 0.10, 2))
    .withColumn("salary_band", 
      when(col("salary") >= 90000, "A")
        .when(col("salary") >= 80000, "B")
        .otherwise("C")
    )
}

// MAPPING: Name to Uppercase (Line 276-278)
private def nameUppercaseMapping(df: DataFrame): DataFrame = {
  df.withColumn("name_upper", upper(col("name")))
}
```

**Called In main() At:**
- Line 50: Bonus + salary band
- Line 54: Name uppercase mapping

---

### **C. AGGREGATORS (6 Functions)**

| Function | Location | Group By | Output | Example |
|----------|----------|----------|--------|---------|
| `averageSalaryByDepartment(df)` | Line 167-171 | Department | Avg salary | Eng: $80K, Sales: $75K |
| `employeeCountByDepartment(df)` | Line 176-181 | Department | Count | Eng: 25, Sales: 20 |
| `averageSalaryByCity(df)` | Line 205-209 | City | Avg salary + count | Bangalore: 15 employees |
| `departmentWithHighestAvgSalary(df)` | Line 214-219 | - (single) | Top 1 dept | Highest paid dept |
| `totalSalaryByDepartment(df)` | Line 233-237 | Department | Sum salary | Total salary expense |
| `salaryStatistics(df)` | Line 242-250 | - (single) | Min/Max/Avg/StdDev/Count | Overall stats |
| `aggregateByDeptCity(df)` | Line 255-259 | Dept + City | Avg/Max/Count | Per combo analysis |

**Code Locations:**
```scala
// AGGREGATOR 1: Average by Department (Line 167-171)
private def averageSalaryByDepartment(df: DataFrame): DataFrame = {
  df.groupBy("department")
    .agg(avg("salary").as("Average_Salary"))
    .orderBy(col("Average_Salary").desc)
}

// AGGREGATOR 2: Count by Department (Line 176-181)
private def employeeCountByDepartment(df: DataFrame): DataFrame = {
  df.groupBy("department")
    .count()
    .withColumnRenamed("count", "Employee_Count")
    .orderBy(col("Employee_Count").desc)
}

// AGGREGATOR 3: Average Salary by City (Line 205-209)
private def averageSalaryByCity(df: DataFrame): DataFrame = {
  df.groupBy("city")
    .agg(avg("salary").as("Average_Salary"), count("*").as("Employee_Count"))
    .orderBy(col("Average_Salary").desc)
}

// AGGREGATOR 4: Highest Avg Salary Department (Line 214-219)
private def departmentWithHighestAvgSalary(df: DataFrame): DataFrame = {
  df.groupBy("department")
    .agg(avg("salary").as("Average_Salary"))
    .orderBy(col("Average_Salary").desc)
    .limit(1)
}

// AGGREGATOR 5: Total Salary by Department (Line 233-237)
private def totalSalaryByDepartment(df: DataFrame): DataFrame = {
  df.groupBy("department")
    .agg(sum("salary").as("Total_Salary_Expense"))
    .orderBy(col("Total_Salary_Expense").desc)
}

// AGGREGATOR 6: Overall Statistics (Line 242-250)
private def salaryStatistics(df: DataFrame): DataFrame = {
  df.agg(
    min("salary").as("Minimum_Salary"),
    max("salary").as("Maximum_Salary"),
    avg("salary").as("Average_Salary"),
    stddev("salary").as("Std_Dev_Salary"),
    count("*").as("Total_Employees")
  )
}

// AGGREGATOR 7: By Department + City (Line 255-259)
private def aggregateByDeptCity(df: DataFrame): DataFrame = {
  df.groupBy("department", "city")
    .agg(avg("salary").as("Avg_Salary"), max("salary").as("Max_Salary"), count("*").as("Employee_Count"))
    .orderBy(col("department"), col("city"))
}
```

**Called In main() At:**
- Line 69: Avg salary by dept
- Line 74: Count by dept
- Line 79: Top 3 highest paid
- Line 84: Employees by city
- Line 89: Avg salary by city
- Line 94: Dept with highest avg salary
- Line 104: Total salary by dept
- Line 109: Global salary statistics
- Line 138: Agg by dept+city

---

### **D. JOINS (1 Function)**

| Function | Location | Join Type | Tables | On |
|----------|----------|-----------|--------|-----|
| `joinWithDepartmentInfo(df, deptDF)` | Line 264-266 | LEFT JOIN | Employees + Departments | department column |

**Code Location:**
```scala
// JOIN: Left join employees with department managers (Line 264-266)
private def joinWithDepartmentInfo(df: DataFrame, deptDF: DataFrame): DataFrame = {
  df.join(deptDF, Seq("department"), "left")
}
```

**Department Data (Created in main() at Line 112-118):**
```scala
val departmentData = Seq(
  Department("Engineering", "Ramesh Kumar"),
  Department("Sales", "Sunita Rao"),
  Department("HR", "Kavita Gupta"),
  Department("Marketing", "Suresh Pillai")
)
val departmentDF = departmentData.toDF()
```

**Output:** Full employee data + manager names

**Called In main() At:**
- Line 132: Join with department info

---

### **E. OTHER HELPER FUNCTIONS (2 Functions)**

| Function | Location | Purpose |
|----------|----------|---------|
| `topPaidEmployees(df, n)` | Line 186-190 | Get top N highest paid employees |
| `employeesByCity(df)` | Line 195-200 | Count employees per city |

---

## 📊 DATA FLOW DIAGRAM

```
                    ┌─────────────────────┐
                    │  data/employees.csv │
                    │   (100 rows)        │
                    └──────────┬──────────┘
                               │
                               │ spark.read.csv()
                               │
                    ┌──────────▼──────────┐
                    │  employeeDF         │
                    │ (100 employees,     │
                    │  6 columns)         │
                    └──────────┬──────────┘
                               │
          ┌────────────────────┼────────────────────┐
          │                    │                    │
          ▼                    ▼                    ▼
    ┌──────────┐         ┌──────────┐         ┌──────────┐
    │ FILTERS  │         │EXPRESSIONS│        │AGGREGAT. │
    │          │         │ MAPPINGS  │        │ & JOINS  │
    └────┬─────┘         └────┬─────┘        └────┬─────┘
         │                    │                    │
         │ Filter results     │ New columns       │ Statistics
         │ Sorted/selected    │ Bonus, band       │ GroupBy results
         │                    │ name_upper        │ Joined data
         │
         └────────────────────┼────────────────────┘
                              │
                    ┌─────────▼──────────┐
                    │  Console Output    │ (15 sections)
                    │  (Printed Results) │
                    └────────┬───────────┘
                              │
                    ┌─────────▼──────────┐
                    │  CSV Output        │
                    │ employees_csv/     │
                    │ departments_csv/   │
                    └────────────────────┘
```

---

## 🚀 EXECUTION FLOW

### **Program Sequence (in main() function)**

```
1. Initialize Spark Session (local[*])
   ↓
2. Read data/employees.csv → employeeDF
   ├─ Cast columns to correct types
   ├─ Convert joinDate to date type
   └─ Select 6 columns: empId, name, department, salary, joinDate, city
   ↓
3. Display Data & Derived Columns
   ├─ Show all employee data (1a)
   ├─ Add bonus + salary_band (1a)
   └─ Map names to uppercase (1b)
   ↓
4. Execute FILTERS
   ├─ Filter by department (2)
   ├─ Filter by salary range (3)
   └─ Filter by join date (10)
   ↓
5. Execute AGGREGATIONS
   ├─ Average salary by department (4)
   ├─ Count by department (5)
   ├─ Top 3 highest paid (6)
   ├─ Employees by city (7)
   ├─ Average salary by city (8)
   ├─ Highest avg salary department (9)
   ├─ Total salary by department (11)
   ├─ Salary statistics (12)
   └─ Aggregate by department+city (15)
   ↓
6. Create Department DataFrame
   └─ Manual Seq with 4 departments + managers
   ↓
7. Write OUTPUT CSVs
   ├─ employees_csv/ (coalesced from employeeDF)
   └─ departments_csv/ (coalesced from departmentDF)
   ↓
8. Execute JOIN
   ├─ LEFT JOIN employees with departments on "department" column
   └─ Show joined result
   ↓
9. Stop Spark Session & Print Summary
```

---

## 🔍 SPECIFIC FUNCTIONALITY LOCATIONS

| Functionality | Function Name | File | Lines | Input | Output |
|--------------|--------------|------|-------|-------|--------|
| Read CSV Data | main() | EmployeeAnalytics.scala | 30-41 | data/employees.csv | employeeDF |
| Filter Dept | filterByDepartment | EmployeeAnalytics.scala | 150-153 | DF, String | Filtered DF |
| Filter Salary | filterBySalaryRange | EmployeeAnalytics.scala | 158-162 | DF, Range | Filtered DF |
| Filter Date | employeesJoinedAfter | EmployeeAnalytics.scala | 224-228 | DF, String | Filtered DF |
| Add Bonus | addBonusColumn | EmployeeAnalytics.scala | 271-274 | DF | DF with 2 new cols |
| Uppercase Names | nameUppercaseMapping | EmployeeAnalytics.scala | 276-278 | DF | DF with name_upper |
| Avg by Dept | averageSalaryByDepartment | EmployeeAnalytics.scala | 167-171 | DF | Grouped DF |
| Count by Dept | employeeCountByDepartment | EmployeeAnalytics.scala | 176-181 | DF | Grouped DF |
| Top N Paid | topPaidEmployees | EmployeeAnalytics.scala | 186-190 | DF, Int | Top N DF |
| By City | employeesByCity | EmployeeAnalytics.scala | 195-200 | DF | Grouped DF |
| Avg by City | averageSalaryByCity | EmployeeAnalytics.scala | 205-209 | DF | Grouped DF |
| Highest Dept | departmentWithHighestAvgSalary | EmployeeAnalytics.scala | 214-219 | DF | Top 1 DF |
| Total Salary | totalSalaryByDepartment | EmployeeAnalytics.scala | 233-237 | DF | Grouped DF |
| Statistics | salaryStatistics | EmployeeAnalytics.scala | 242-250 | DF | 5-column result |
| Dept+City Agg | aggregateByDeptCity | EmployeeAnalytics.scala | 255-259 | DF | Multi-group DF |
| Left Join | joinWithDepartmentInfo | EmployeeAnalytics.scala | 264-266 | DF, DF | Joined DF |
| Write CSV | main() | EmployeeAnalytics.scala | 125-126 | employeeDF, deptDF | employees_csv/, departments_csv/ |

---

## 📦 DEPENDENCIES & REQUIREMENTS

### **Compile-Time Requirements**
```
✅ Scala                    2.13.12
✅ Apache Spark SQL         3.5.0 (compiled for Scala 2.13)
✅ Java Development Kit     11+ (with JAVA_HOME set)
✅ SBT (Scala Build Tool)   1.9.5+
```

### **Runtime Requirements**
```
✅ Java 11+                 (for executing .jar files)
✅ Spark 3.5.0              (distributed computing; runs locally via local[*])
✅ HADOOP_HOME              (for CSV write operations on Windows)
   └─ winutils.exe         (Windows-specific requirement)
```

### **Input Data Requirements**
```
✅ data/employees.csv       (100+ rows, 6 columns: empId, name, department, salary, joinDate, city)
```

---

## 🛠️ HOW TO EXTEND WITH NEW FEATURES

### **Add a New Filter**
Add function to EmployeeAnalytics.scala:
```scala
private def filterByStatus(df: DataFrame, status: String): DataFrame = {
  df.filter(col("status") === status)
}
```
Then call in main():
```scala
filterByStatus(employeeDF, "Active").show()
```

### **Add a New Aggregation**
```scala
private def medianSalaryByDept(df: DataFrame): DataFrame = {
  df.groupBy("department").agg(percentile_approx(col("salary"), 0.5).as("Median_Salary"))
}
```

### **Add a New Expression**
```scala
private def yearsSinceJoin(df: DataFrame): DataFrame = {
  df.withColumn("years_employed", datediff(current_date(), col("joinDate")) / 365)
}
```

---

## 📋 SUMMARY TABLE

| Category | Count | Examples |
|----------|-------|----------|
| **Filters** | 3 | Department, Salary Range, Join Date |
| **Expressions/Mappings** | 2 | Bonus Calc, Name Uppercase |
| **Aggregators** | 7 | Avg by Dept, Count by Dept, Total Salary |
| **Joins** | 1 | Left Join Employees-Departments |
| **Helper Functions** | 2 | Top Paid, Employees by City |
| **Data Models** | 2 | Employee (case class), Department (case class) |
| **Input Files** | 1 | data/employees.csv (100 rows) |
| **Output Files** | 2 | employees_csv/, departments_csv/ |
| **Total Lines** | 282 | EmployeeAnalytics.scala |

---

## 🎯 KEY TAKEAWAYS

1. **Single Source of Truth:** All analytics logic lives in `EmployeeAnalytics.scala`
2. **Data Model:** `Employee.scala` defines the structure
3. **Entry Point:** `Main.scala` is the starting point
4. **Configuration:** `build.sbt` manages dependencies and build settings
5. **Input:** `data/employees.csv` provides the data
6. **15 Outputs:** The main() function prints 15 different analytics sections
7. **CSV Write:** Lines 125-126 write output CSVs (requires HADOOP_HOME on Windows)
8. **Scalable:** Add new functions by creating `private def functionName(...)` and calling in main()

---

## 🔗 QUICK NAVIGATION

```
Need to add a new filter?          → EmployeeAnalytics.scala, Lines 150-228
Need to add analytics?             → EmployeeAnalytics.scala, Lines 167-259
Need to change input CSV path?     → EmployeeAnalytics.scala, Line 27
Need to modify output location?    → EmployeeAnalytics.scala, Lines 125-126
Need to change employee schema?    → Employee.scala, Lines 2-9
Need to change Scala/Spark version? → build.sbt, Lines 5-8
```


