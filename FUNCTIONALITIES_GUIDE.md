# 📊 FUNCTIONALITIES QUICK REFERENCE & VISUAL GUIDE

---

## 🎨 CODE STRUCTURE AT A GLANCE

### **EmployeeAnalytics.scala Structure (282 Lines)**

```
┌────────────────────────────────────────────────────────────────────┐
│ EmployeeAnalytics.scala (282 lines)                                │
├────────────────────────────────────────────────────────────────────┤
│                                                                    │
│ ▌ IMPORTS (Lines 1-3)                                             │
│  └─ org.apache.spark.sql.*, functions.*                            │
│                                                                    │
│ ▌ DATA MODEL (Line 7)                                              │
│  └─ case class Department(department, manager)                     │
│                                                                    │
│ ▌ MAIN ORCHESTRATOR (Lines 11-145)                                 │
│  ├─ Initialize Spark (Lines 13-16)                                 │
│  ├─ Load Data (Lines 27-41)                                        │
│  ├─ Display Base Data (Lines 43-50)                                │
│  ├─ Expressions & Mappings (Lines 48-54)                           │
│  ├─ FILTERS Section (Lines 56-99)                                  │
│  │  ├─ By Department (Line 59)                                     │
│  │  ├─ By Salary Range (Line 64)                                   │
│  │  └─ By Join Date (Line 99)                                      │
│  ├─ AGGREGATIONS Section (Lines 66-109)                            │
│  │  ├─ Avg by Dept (Line 69)                                       │
│  │  ├─ Count by Dept (Line 74)                                     │
│  │  ├─ Top 3 Paid (Line 79)                                        │
│  │  ├─ By City (Line 84)                                           │
│  │  ├─ Avg by City (Line 89)                                       │
│  │  ├─ Highest Dept (Line 94)                                      │
│  │  ├─ Total Salary (Line 104)                                     │
│  │  ├─ Statistics (Line 109)                                       │
│  │  └─ Dept+City (Line 138)                                        │
│  ├─ Department Data Creation (Lines 112-118)                       │
│  ├─ CSV Write Operations (Lines 125-126)                           │
│  ├─ JOIN (Lines 129-133)                                           │
│  └─ Cleanup & Exit (Lines 141-144)                                 │
│                                                                    │
│ ▌ FILTER FUNCTIONS (Lines 150-228)                                 │
│  ├─ filterByDepartment (150-153)                                   │
│  ├─ filterBySalaryRange (158-162)                                  │
│  └─ employeesJoinedAfter (224-228)                                 │
│                                                                    │
│ ▌ AGGREGATOR FUNCTIONS (Lines 167-259)                             │
│  ├─ averageSalaryByDepartment (167-171)       [groupBy + agg]     │
│  ├─ employeeCountByDepartment (176-181)       [groupBy + count]   │
│  ├─ topPaidEmployees (186-190)                [limit + order]     │
│  ├─ employeesByCity (195-200)                 [groupBy + count]   │
│  ├─ averageSalaryByCity (205-209)             [groupBy + agg]     │
│  ├─ departmentWithHighestAvgSalary (214-219) [groupBy + limit]    │
│  ├─ totalSalaryByDepartment (233-237)        [groupBy + sum]      │
│  ├─ salaryStatistics (242-250)                [agg min/max/avg]   │
│  └─ aggregateByDeptCity (255-259)             [multi-groupBy]     │
│                                                                    │
│ ▌ EXPRESSION/MAPPING FUNCTIONS (Lines 271-278)                    │
│  ├─ addBonusColumn (271-274)       [withColumn + when/otherwise]  │
│  └─ nameUppercaseMapping (276-278) [withColumn + upper()]         │
│                                                                    │
│ ▌ JOIN FUNCTION (Lines 264-266)                                    │
│  └─ joinWithDepartmentInfo (264-266)          [left join]          │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
```

---

## 🔢 FUNCTION MATRIX

### **Quick Function Lookup**

```
╔════════════════════════════════════════════════════════════════════════╗
║ FILTERS (df → filtered df)                                             ║
╠════════════════════════════════════════════════════════════════════════╣
║ filterByDepartment(df, dept)         │ Line 150  │ dept="Engineering" ║
║ filterBySalaryRange(df, min, max)    │ Line 158  │ 75K-85K range      ║
║ employeesJoinedAfter(df, date)       │ Line 224  │ 2021-01-01+        ║
╚════════════════════════════════════════════════════════════════════════╝

╔════════════════════════════════════════════════════════════════════════╗
║ EXPRESSIONS & MAPPINGS (df → df + new columns)                         ║
╠════════════════════════════════════════════════════════════════════════╣
║ addBonusColumn(df)                   │ Line 271  │ +bonus, +salary_band║
║ nameUppercaseMapping(df)             │ Line 276  │ +name_upper         ║
╚════════════════════════════════════════════════════════════════════════╝

╔════════════════════════════════════════════════════════════════════════╗
║ AGGREGATORS (df → grouped/stats df)                                    ║
╠════════════════════════════════════════════════════════════════════════╣
║ averageSalaryByDepartment(df)        │ Line 167  │ dept → avg_salary   ║
║ employeeCountByDepartment(df)        │ Line 176  │ dept → count        ║
║ topPaidEmployees(df, n)              │ Line 186  │ top n employees     ║
║ employeesByCity(df)                  │ Line 195  │ city → count        ║
║ averageSalaryByCity(df)              │ Line 205  │ city → avg, count   ║
║ departmentWithHighestAvgSalary(df)   │ Line 214  │ top 1 dept by avg   ║
║ totalSalaryByDepartment(df)          │ Line 233  │ dept → sum_salary   ║
║ salaryStatistics(df)                 │ Line 242  │ min/max/avg/std     ║
║ aggregateByDeptCity(df)              │ Line 255  │ (dept,city) → stats ║
╚════════════════════════════════════════════════════════════════════════╝

╔════════════════════════════════════════════════════════════════════════╗
║ JOINS (empDF + deptDF → joined df)                                     ║
╠════════════════════════════════════════════════════════════════════════╣
║ joinWithDepartmentInfo(empDF, deptDF)│ Line 264 │ LEFT JOIN on dept    ║
╚════════════════════════════════════════════════════════════════════════╝
```

---

## 📍 WHERE EACH FUNCTIONALITY IS CALLED

```
main() EXECUTION ORDER
│
├─► Line 45  │ Show all employee data
│
├─► Line 50  │ Expression: Add bonus + salary_band
│
├─► Line 54  │ Mapping: Names to uppercase
│
├─► Line 59  │ FILTER 1: filterByDepartment("Engineering")
│
├─► Line 64  │ FILTER 2: filterBySalaryRange(75000, 85000)
│
├─► Line 69  │ AGGREGATOR 1: averageSalaryByDepartment()
│
├─► Line 74  │ AGGREGATOR 2: employeeCountByDepartment()
│
├─► Line 79  │ AGGREGATOR 3: topPaidEmployees(3)
│
├─► Line 84  │ AGGREGATOR 4: employeesByCity()
│
├─► Line 89  │ AGGREGATOR 5: averageSalaryByCity()
│
├─► Line 94  │ AGGREGATOR 6: departmentWithHighestAvgSalary()
│
├─► Line 104 │ AGGREGATOR 7: totalSalaryByDepartment()
│
├─► Line 109 │ AGGREGATOR 8: salaryStatistics()
│
├─► Line 125-126 │ WRITE: CSV output files
│
├─► Line 132 │ JOIN: joinWithDepartmentInfo()
│
└─► Line 138 │ AGGREGATOR 9: aggregateByDeptCity()
```

---

## 📥 INPUT FILES & DATA MODEL

### **Employee.scala (11 lines)**
```scala
case class Employee(
  empId: Int,          ← Unique ID
  name: String,        ← Full name
  department: String,  ← Dept (must match department CSV)
  salary: Double,      ← Annual salary (used in all aggregations)
  joinDate: String,    ← Date format yyyy-MM-dd
  city: String         ← City/location
)
```

### **EmployeeAnalytics.scala - Department Case Class (Line 7)**
```scala
case class Department(
  department: String,  ← Must match employee.department
  manager: String      ← Manager name
)
```

### **data/employees.csv Format**
```
empId,name,department,salary,joinDate,city
101,John Doe,Engineering,92500.0,2020-01-15,Bangalore
102,Jane Smith,Sales,75000.0,2019-03-22,Mumbai
...
200,Xyz Person,HR,68000.0,2021-06-10,Pune
```

---

## 📤 OUTPUT GENERATION

### **Console Output (15 Sections)**
```
Section  │ Function Called              │ Output Type
─────────┼──────────────────────────────┼──────────────────
1   │ employeeDF.show()              │ 100 employees
1a  │ addBonusColumn(employeeDF)     │ +2 new columns
1b  │ nameUppercaseMapping(emp.DF)   │ +1 new column
2   │ filterByDepartment(...)        │ Filtered rows
3   │ filterBySalaryRange(...)       │ Filtered rows
4   │ averageSalaryByDepartment(...) │ Grouped by dept
5   │ employeeCountByDepartment(...) │ Grouped by dept
6   │ topPaidEmployees(3)            │ Top 3 rows
7   │ employeesByCity(...)           │ Grouped by city
8   │ averageSalaryByCity(...)       │ Grouped by city
9   │ departmentWithHighestAvgSalary │ 1 row
11  │ totalSalaryByDepartment(...)   │ Grouped by dept
12  │ salaryStatistics(...)          │ 1 row (5 columns)
13  │ departmentDF.show()            │ 4 departments
14  │ joinWithDepartmentInfo(...)    │ Joined DF
15  │ aggregateByDeptCity(...)       │ Multi-grouped
```

### **CSV File Output (Lines 125-126)**
```
employees_csv/
    └─ part-00000.csv (coalesced from 100 employees)
       └─ Columns: empId, name, department, salary, joinDate, city

departments_csv/
    └─ part-00000.csv (coalesced from department data)
       └─ Columns: department, manager
```

**Note:** CSV write requires HADOOP_HOME/winutils.exe on Windows!

---

## 🔧 BUILD & DEPENDENCY TREE

### **build.sbt Configuration**
```
Scala1 (Project Name)
├── Version: 0.1.0
├── Scala: 2.13.12
│
└── Dependencies:
    └─ org.apache.spark:spark-sql_2.13:3.5.0
        ├─ Scala: 2.13.12
        ├─ Spark Core
        ├─ Spark Catalyst
        └─ Hadoop/Parquet/Arrow (distributed computing libs)
```

### **Build Process**
```
sbt compile
    ↓
Resolves dependencies from Maven
    ↓
Compiles:
    ├─ Employee.scala   → Employee.class
    ├─ Main.scala       → Main.class
    └─ EmployeeAnalytics.scala → EmployeeAnalytics.class
                                 + EmployeeAnalytics$.class
    ↓
Artifacts → target/scala-2.13/
```

---

## 🎯 TYPICAL DATA FLOW EXAMPLES

### **Example 1: Filter + Show**
```
Input: employeeDF (100 rows, 6 columns)
       ↓
filterByDepartment(employeeDF, "Engineering")
       ├─ filter(col("department") === "Engineering")
       └─ select(empId, name, department, salary, city)
       ↓
Output: DataFrame (25 engineers only, 5 columns)
        .show() prints to console
```

### **Example 2: Expression + Derived Column**
```
Input: employeeDF (100 rows, 6 columns)
       ↓
addBonusColumn(employeeDF)
       ├─ withColumn("bonus", round(salary * 0.10, 2))
       ├─ withColumn("salary_band",
       │     when(salary >= 90K, "A")
       │       .when(salary >= 80K, "B")
       │       .otherwise("C"))
       ↓
Output: DataFrame (100 rows, 8 columns)
        [empId, name, dept, salary, joinDate, city, bonus, salary_band]
```

### **Example 3: Aggregation**
```
Input: employeeDF (100 rows)
       ↓
averageSalaryByDepartment(employeeDF)
       ├─ groupBy("department")
       ├─ agg(avg("salary").as("Average_Salary"))
       ├─ orderBy(col("Average_Salary").desc)
       ↓
Output: DataFrame (4 rows, 2 columns)
        [department, Average_Salary]
        
Sample:
┌─────────────────────────────────────┐
│department    │ Average_Salary      │
├─────────────────────────────────────┤
│Engineering   │ 85000.5             │
│Sales         │ 78000.25            │
│HR            │ 72000.0             │
│Marketing     │ 76500.75            │
└─────────────────────────────────────┘
```

### **Example 4: Join**
```
Input: employeeDF (100 rows, 6 cols)
       departmentDF (4 rows, 2 cols)
       ↓
joinWithDepartmentInfo(employeeDF, departmentDF)
       ├─ .join(deptDF, Seq("department"), "left")
       ├─ Matches on: employee.department = dept.department
       ↓
Output: DataFrame (100 rows, 7 columns)
        [empId, name, dept, salary, joinDate, city, manager]
        
All 100 employees now have their manager names!
```

---

## 🔌 EXTENSION POINTS

### **To Add a New Filter:**
1. Create function in EmployeeAnalytics.scala:
   ```scala
   private def filterByBonusEligible(df: DataFrame): DataFrame = {
     df.filter(col("salary") >= 80000)
   }
   ```
2. Call in main():
   ```scala
   println("\n16. BONUS ELIGIBLE EMPLOYEES")
   filterByBonusEligible(employeeDF).show()
   ```

### **To Add a New Aggregation:**
1. Create function:
   ```scala
   private def salaryRangeByDept(df: DataFrame): DataFrame = {
     df.groupBy("department")
       .agg(
         min("salary").as("Min"),
         max("salary").as("Max"),
         (max("salary") - min("salary")).as("Range")
       )
   }
   ```
2. Call in main()

### **To Add a New Expression:**
1. Create function:
   ```scala
   private def addPercentageOfMax(df: DataFrame): DataFrame = {
     val maxSalary = df.agg(max("salary")).collect()(0)(0).asInstanceOf[Double]
     df.withColumn("% of Max Salary", 
       round((col("salary") / maxSalary) * 100, 2))
   }
   ```

---

## 📋 FILES CHECKLIST

```
✅ Project Root: C:\Users\PAVAN\Local_pro\Scala1\
   │
   ├─ ✅ build.sbt                    [14 lines]   - Build config
   │
   ├─ ✅ src/main/scala/
   │  ├─ Employee.scala               [11 lines]   - Data model
   │  ├─ Main.scala                   [12 lines]   - Entry point
   │  └─ EmployeeAnalytics.scala      [282 lines]  - CORE LOGIC ⭐
   │
   ├─ ✅ data/
   │  └─ employees.csv                [101 lines]  - Input data (header + 100 rows)
   │
   ├─ ✅ project/
   │  └─ build.properties             [1 line]     - sbt metadata
   │
   ├─ ✅ target/                      [auto]       - Build output
   │
   └─ ✅ scripts/                     [*.ps1]      - Execution scripts
```

---

## 🚀 QUICK START COMMAND REFERENCE

### **Compile & Run**
```powershell
cd C:\Users\PAVAN\Local_pro\Scala1
sbt -no-server compile
sbt -no-server run
```

### **Run with Custom CSV Path**
```powershell
sbt -no-server 'run "C:/path/to/custom/employees.csv"'
```

### **Clean Build**
```powershell
sbt -no-server clean compile
```

### **Check for Errors**
```powershell
sbt -no-server check
```

---

## 💡 KEY CONCEPTS MAPPING

```
CONCEPT              │ FILE(S)                    │ IMPLEMENTATION
──────────────────────────────────────────────────────────────────
Data Model           │ Employee.scala             │ case class
Spark Setup          │ EmployeeAnalytics.scala   │ main() L13-16
Data Loading         │ EmployeeAnalytics.scala   │ main() L30-41
Type Casting         │ EmployeeAnalytics.scala   │ main() L37-40
Filtering            │ EmployeeAnalytics.scala   │ L150-228 (3 funcs)
Expression/Mapping   │ EmployeeAnalytics.scala   │ L271-278 (2 funcs)
Aggregation          │ EmployeeAnalytics.scala   │ L167-259 (9 funcs)
Grouping             │ EmployeeAnalytics.scala   │ .groupBy()
Sorting              │ EmployeeAnalytics.scala   │ .orderBy()
Joining              │ EmployeeAnalytics.scala   │ L264-266
Statistics           │ EmployeeAnalytics.scala   │ l242-250
CSV Write            │ EmployeeAnalytics.scala   │ L125-126
CSV Read             │ EmployeeAnalytics.scala   │ L31-34
Spark Session        │ EmployeeAnalytics.scala   │ L13-16
Console Output       │ EmployeeAnalytics.scala   │ .show() calls
```

---

## 🎓 LEARNING HIERARCHY

```
Level 1: Understanding
├─ Read Employee.scala (simple data model)
└─ Read Main.scala (entry point)

Level 2: Input/Output
├─ Understand CSV loading (L30-41 in EmployeeAnalytics)
└─ Understand CSV writing (L125-126)

Level 3: Filtering
├─ Study filterByDepartment (L150-153)
├─ Study filterBySalaryRange (L158-162)
└─ Study employeesJoinedAfter (L224-228)

Level 4: Transformations
├─ Study addBonusColumn (L271-274)
└─ Study nameUppercaseMapping (L276-278)

Level 5: Aggregations
├─ Study averageSalaryByDepartment (L167-171)
├─ Study employeeCountByDepartment (L176-181)
└─ Study aggregateByDeptCity (L255-259)

Level 6: Joins
└─ Study joinWithDepartmentInfo (L264-266)

Level 7: Advanced
├─ Combine filters with aggregations
├─ Chain multiple transformations
└─ Create custom aggregations
```


