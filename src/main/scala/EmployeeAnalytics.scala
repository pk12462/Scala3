import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.Window

// Updated case class to match departments.csv structure (6 columns)
case class Department(deptId: Int, department: String, manager: String, location: String, budget: Long, employees: Int)

object EmployeeAnalytics {

  def main(args: Array[String]): Unit = {
    // Initialize Spark Session
    val spark = SparkSession.builder()
      .appName("Employee Analytics")
      .master("local[*]")
      .getOrCreate()

    println("=" * 80)
    println("SPARK EMPLOYEE ANALYTICS APPLICATION")
    println("=" * 80)

    // Import spark implicits for automatic conversion
    import spark.implicits._

     // Load employee data from CSV
     // This project includes `data/employees.csv` with 100 employees
     val employeePath: String = if (args.nonEmpty) args(0) else "data/employees.csv"

     // Read employees CSV and normalize schema/columns. We cast important fields to the correct types
     val employeeDF: DataFrame = {
       val df = spark.read
         .option("header", "true")
         .option("inferSchema", "true")
         .csv(employeePath)

       // Ensure correct types and a proper date column
       df.withColumn("empId", col("empId").cast("int"))
         .withColumn("salary", col("salary").cast("double"))
         .withColumn("joinDate", to_date(col("joinDate"), "yyyy-MM-dd"))
         .select("empId", "name", "department", "salary", "joinDate", "city")
     }

     // Load department data from CSV
     // This project includes `data/departments.csv` with 100 department entries
     val departmentPath: String = "data/departments.csv"
     val departmentDF: DataFrame = {
       val df = spark.read
         .option("header", "true")
         .option("inferSchema", "true")
         .csv(departmentPath)

       // Ensure correct types for department CSV
       df.withColumn("deptId", col("deptId").cast("int"))
         .withColumn("budget", col("budget").cast("long"))
         .withColumn("employees", col("employees").cast("int"))
         .select("deptId", "department", "manager", "location", "budget", "employees")
     }

     println("\n1. ALL EMPLOYEE DATA (First 20 of 100 employees)")
     println("-" * 80)
     employeeDF.show(20, false)

     // Demonstrate expressions, mappings and derived columns
     println("\n1a. DERIVED COLUMNS: BONUS (10%) AND SALARY_BAND (First 15)")
     println("-" * 80)
     addBonusColumn(employeeDF).show(15, false)

     println("\n1b. NAME MAPPING: UPPERCASE NAMES (First 15)")
     println("-" * 80)
     nameUppercaseMapping(employeeDF).show(15, false)

     // Functionality 1: Filter employees by department
     println("\n2. EMPLOYEES IN ENGINEERING DEPARTMENT")
     println("-" * 80)
     filterByDepartment(employeeDF, "Engineering").show(15, false)

     // Functionality 2: Filter employees by salary range
     println("\n3. EMPLOYEES WITH SALARY BETWEEN 68000 AND 80000")
     println("-" * 80)
     filterBySalaryRange(employeeDF, 68000.0, 80000.0).show(15, false)

     // Functionality 3: Average salary by department
     println("\n4. AVERAGE SALARY BY DEPARTMENT")
     println("-" * 80)
     averageSalaryByDepartment(employeeDF).show(false)

     // Functionality 4: Count employees by department
     println("\n5. EMPLOYEE COUNT BY DEPARTMENT")
     println("-" * 80)
     employeeCountByDepartment(employeeDF).show(false)

     // Functionality 5: Highest paid employees
     println("\n6. TOP 10 HIGHEST PAID EMPLOYEES")
     println("-" * 80)
     topPaidEmployees(employeeDF, 10).show(false)

     // Functionality 6: Employees by city
     println("\n7. EMPLOYEES BY CITY")
     println("-" * 80)
     employeesByCity(employeeDF).show(false)

     // Functionality 7: Average salary by city
     println("\n8. AVERAGE SALARY BY CITY")
     println("-" * 80)
     averageSalaryByCity(employeeDF).show(false)

     // Functionality 8: Department with highest average salary
     println("\n9. DEPARTMENT WITH HIGHEST AVERAGE SALARY")
     println("-" * 80)
     departmentWithHighestAvgSalary(employeeDF).show(false)

     // Functionality 9: Count employees joined after a date
     println("\n10. EMPLOYEES JOINED AFTER 2020-01-01")
     println("-" * 80)
     employeesJoinedAfter(employeeDF, "2020-01-01").show(15, false)

     // Functionality 10: Total salary expense by department
     println("\n11. TOTAL SALARY EXPENSE BY DEPARTMENT")
     println("-" * 80)
     totalSalaryByDepartment(employeeDF).show(false)

     // Functionality 11: Statistics
     println("\n12. SALARY STATISTICS")
     println("-" * 80)
     salaryStatistics(employeeDF).show(false)

     println("\n13. DEPARTMENT DATA (First 10 from 100 departments)")
     println("-" * 80)
     departmentDF.show(10, false)

     // Write CSVs (will create directories employees_csv/ and departments_csv/)
     try {
       employeeDF.coalesce(1).write.mode("overwrite").option("header","true").csv("employees_csv")
       departmentDF.coalesce(1).write.mode("overwrite").option("header","true").csv("departments_csv")
       println("✅ Wrote employees_csv/ and departments_csv/ directories with CSV files")
     } catch {
       case e: Exception => println("⚠️ CSV write failed (requires HADOOP_HOME): " + e.getMessage)
     }

     // Join example: left join employees with department info (matching on department column)
     println("\n14. JOIN EMPLOYEES WITH DEPARTMENTS (LEFT JOIN on 'department')")
     println("-" * 80)
     val joinedDF = joinWithDepartmentInfo(employeeDF, departmentDF)
     joinedDF.show(10, false)

     // Aggregator example
     println("\n15. AGGREGATORS: AVG/MAX SALARY BY DEPARTMENT AND CITY (First 15)")
     println("-" * 80)
     aggregateByDeptCity(employeeDF).show(15, false)

    // Stop Spark Session
    spark.stop()
    println("\n" + "=" * 80)
    println("Analysis Complete!")
    println("=" * 80)
  }

  /**
   * Functionality 1: Filter employees by department
   */
  private def filterByDepartment(df: DataFrame, dept: String): DataFrame = {
    df.filter(col("department") === dept)
      .select("empId", "name", "department", "salary", "city")
  }

  /**
   * Functionality 2: Filter employees by salary range
   */
  private def filterBySalaryRange(df: DataFrame, minSalary: Double, maxSalary: Double): DataFrame = {
    df.filter(col("salary").between(minSalary, maxSalary))
      .select("empId", "name", "department", "salary")
      .orderBy(col("salary").desc)
  }

  /**
   * Functionality 3: Average salary by department
   */
  private def averageSalaryByDepartment(df: DataFrame): DataFrame = {
    df.groupBy("department")
      .agg(avg("salary").as("Average_Salary"))
      .orderBy(col("Average_Salary").desc)
  }

  /**
   * Functionality 4: Count employees by department
   */
  private def employeeCountByDepartment(df: DataFrame): DataFrame = {
    df.groupBy("department")
      .count()
      .withColumnRenamed("count", "Employee_Count")
      .orderBy(col("Employee_Count").desc)
  }

  /**
   * Functionality 5: Top N highest paid employees
   */
  private def topPaidEmployees(df: DataFrame, n: Int): DataFrame = {
    df.orderBy(col("salary").desc)
      .limit(n)
      .select("empId", "name", "department", "salary", "city")
  }

  /**
   * Functionality 6: Employees by city
   */
  private def employeesByCity(df: DataFrame): DataFrame = {
    df.groupBy("city")
      .count()
      .withColumnRenamed("count", "Employee_Count")
      .orderBy(col("Employee_Count").desc)
  }

  /**
   * Functionality 7: Average salary by city
   */
  private def averageSalaryByCity(df: DataFrame): DataFrame = {
    df.groupBy("city")
      .agg(avg("salary").as("Average_Salary"), count("*").as("Employee_Count"))
      .orderBy(col("Average_Salary").desc)
  }

  /**
   * Functionality 8: Department with highest average salary
   */
  private def departmentWithHighestAvgSalary(df: DataFrame): DataFrame = {
    df.groupBy("department")
      .agg(avg("salary").as("Average_Salary"))
      .orderBy(col("Average_Salary").desc)
      .limit(1)
  }

  /**
   * Functionality 9: Employees joined after a specific date
   */
  private def employeesJoinedAfter(df: DataFrame, date: String): DataFrame = {
    df.filter(col("joinDate") > date)
      .select("empId", "name", "department", "joinDate", "city")
      .orderBy(col("joinDate").desc)
  }

  /**
   * Functionality 10: Total salary expense by department
   */
  private def totalSalaryByDepartment(df: DataFrame): DataFrame = {
    df.groupBy("department")
      .agg(sum("salary").as("Total_Salary_Expense"))
      .orderBy(col("Total_Salary_Expense").desc)
  }

  /**
   * Functionality 11: Salary statistics
   */
  private def salaryStatistics(df: DataFrame): DataFrame = {
    df.agg(
      min("salary").as("Minimum_Salary"),
      max("salary").as("Maximum_Salary"),
      avg("salary").as("Average_Salary"),
      stddev("salary").as("Std_Dev_Salary"),
      count("*").as("Total_Employees")
    )
  }

  /**
   * Aggregator: Avg/Max salary by department and city
   */
  private def aggregateByDeptCity(df: DataFrame): DataFrame = {
    df.groupBy("department", "city")
      .agg(avg("salary").as("Avg_Salary"), max("salary").as("Max_Salary"), count("*").as("Employee_Count"))
      .orderBy(col("department"), col("city"))
  }

   /**
    * Join employees with department info
    * Get only the first/primary department manager for each department (using row_number window function)
    */
   private def joinWithDepartmentInfo(df: DataFrame, deptDF: DataFrame): DataFrame = {
     // Get only the first department entry per department name (primary manager)
     val windowSpec = Window.partitionBy("department").orderBy("deptId")
     val primaryDeptDF = deptDF.withColumn("rn", row_number().over(windowSpec))
       .filter(col("rn") === 1)
       .drop("rn", "deptId", "budget", "employees", "location")  // Keep only department and manager

     df.join(primaryDeptDF, Seq("department"), "left")
   }

  /**
   * Expressions & Mapping examples
   */
  private def addBonusColumn(df: DataFrame): DataFrame = {
    df.withColumn("bonus", round(col("salary") * 0.10, 2))
      .withColumn("salary_band", when(col("salary") >= 90000, "A").when(col("salary") >= 80000, "B").otherwise("C"))
  }

  private def nameUppercaseMapping(df: DataFrame): DataFrame = {
    df.withColumn("name_upper", upper(col("name")))
  }

}

