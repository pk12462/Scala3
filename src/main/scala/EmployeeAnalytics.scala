import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import scala.io.Source

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

    // Create sample employee data
    val employeeData = Seq(
      Employee(1, "Pavan Kumar", "Engineering", 85000.0, "2021-01-15", "Bangalore"),
      Employee(2, "Priya Singh", "Engineering", 90000.0, "2020-06-20", "Bangalore"),
      Employee(3, "Rajesh Sharma", "Sales", 75000.0, "2019-03-10", "Mumbai"),
      Employee(4, "Neha Verma", "HR", 70000.0, "2022-02-14", "Delhi"),
      Employee(5, "Amit Patel", "Engineering", 88000.0, "2021-05-22", "Bangalore"),
      Employee(6, "Anjali Desai", "Sales", 72000.0, "2020-11-30", "Mumbai"),
      Employee(7, "Vikram Singh", "Engineering", 92000.0, "2019-09-05", "Bangalore"),
      Employee(8, "Deepika Menon", "Marketing", 76000.0, "2021-07-18", "Delhi"),
      Employee(9, "Arjun Nair", "Sales", 78000.0, "2022-01-10", "Mumbai"),
      Employee(10, "Sana Khan", "HR", 71000.0, "2021-04-25", "Delhi")
    )

    // Convert to DataFrame
    val employeeDF = employeeData.toDF()

    println("\n1. ALL EMPLOYEE DATA")
    println("-" * 80)
    employeeDF.show(false)

    // Functionality 1: Filter employees by department
    println("\n2. EMPLOYEES IN ENGINEERING DEPARTMENT")
    println("-" * 80)
    filterByDepartment(employeeDF, "Engineering").show(false)

    // Functionality 2: Filter employees by salary range
    println("\n3. EMPLOYEES WITH SALARY BETWEEN 75000 AND 85000")
    println("-" * 80)
    filterBySalaryRange(employeeDF, 75000.0, 85000.0).show(false)

    // Functionality 3: Average salary by department
    println("\n4. AVERAGE SALARY BY DEPARTMENT")
    println("-" * 80)
    averageSalaryByDepartment(employeeDF).show(false)

    // Functionality 4: Count employees by department
    println("\n5. EMPLOYEE COUNT BY DEPARTMENT")
    println("-" * 80)
    employeeCountByDepartment(employeeDF).show(false)

    // Functionality 5: Highest paid employees
    println("\n6. TOP 3 HIGHEST PAID EMPLOYEES")
    println("-" * 80)
    topPaidEmployees(employeeDF, 3).show(false)

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
    println("\n10. EMPLOYEES JOINED AFTER 2021-01-01")
    println("-" * 80)
    employeesJoinedAfter(employeeDF, "2021-01-01").show(false)

    // Functionality 10: Total salary expense by department
    println("\n11. TOTAL SALARY EXPENSE BY DEPARTMENT")
    println("-" * 80)
    totalSalaryByDepartment(employeeDF).show(false)

    // Functionality 11: Statistics
    println("\n12. SALARY STATISTICS")
    println("-" * 80)
    salaryStatistics(employeeDF).show(false)

    // Stop Spark Session
    spark.stop()
    println("\n" + "=" * 80)
    println("Analysis Complete!")
    println("=" * 80)
  }

  /**
   * Functionality 1: Filter employees by department
   */
  def filterByDepartment(df: org.apache.spark.sql.DataFrame, dept: String) = {
    df.filter(col("department") === dept)
      .select("empId", "name", "department", "salary", "city")
  }

  /**
   * Functionality 2: Filter employees by salary range
   */
  def filterBySalaryRange(df: org.apache.spark.sql.DataFrame, minSalary: Double, maxSalary: Double) = {
    df.filter(col("salary").between(minSalary, maxSalary))
      .select("empId", "name", "department", "salary")
      .orderBy(col("salary").desc)
  }

  /**
   * Functionality 3: Average salary by department
   */
  def averageSalaryByDepartment(df: org.apache.spark.sql.DataFrame) = {
    df.groupBy("department")
      .agg(avg("salary").as("Average_Salary"))
      .orderBy(col("Average_Salary").desc)
  }

  /**
   * Functionality 4: Count employees by department
   */
  def employeeCountByDepartment(df: org.apache.spark.sql.DataFrame) = {
    df.groupBy("department")
      .count()
      .withColumnRenamed("count", "Employee_Count")
      .orderBy(col("Employee_Count").desc)
  }

  /**
   * Functionality 5: Top N highest paid employees
   */
  def topPaidEmployees(df: org.apache.spark.sql.DataFrame, n: Int) = {
    df.orderBy(col("salary").desc)
      .limit(n)
      .select("empId", "name", "department", "salary", "city")
  }

  /**
   * Functionality 6: Employees by city
   */
  def employeesByCity(df: org.apache.spark.sql.DataFrame) = {
    df.groupBy("city")
      .count()
      .withColumnRenamed("count", "Employee_Count")
      .orderBy(col("Employee_Count").desc)
  }

  /**
   * Functionality 7: Average salary by city
   */
  def averageSalaryByCity(df: org.apache.spark.sql.DataFrame) = {
    df.groupBy("city")
      .agg(avg("salary").as("Average_Salary"), count("*").as("Employee_Count"))
      .orderBy(col("Average_Salary").desc)
  }

  /**
   * Functionality 8: Department with highest average salary
   */
  def departmentWithHighestAvgSalary(df: org.apache.spark.sql.DataFrame) = {
    df.groupBy("department")
      .agg(avg("salary").as("Average_Salary"))
      .orderBy(col("Average_Salary").desc)
      .limit(1)
  }

  /**
   * Functionality 9: Employees joined after a specific date
   */
  def employeesJoinedAfter(df: org.apache.spark.sql.DataFrame, date: String) = {
    df.filter(col("joinDate") > date)
      .select("empId", "name", "department", "joinDate", "city")
      .orderBy(col("joinDate").desc)
  }

  /**
   * Functionality 10: Total salary expense by department
   */
  def totalSalaryByDepartment(df: org.apache.spark.sql.DataFrame) = {
    df.groupBy("department")
      .agg(sum("salary").as("Total_Salary_Expense"))
      .orderBy(col("Total_Salary_Expense").desc)
  }

  /**
   * Functionality 11: Salary statistics
   */
  def salaryStatistics(df: org.apache.spark.sql.DataFrame) = {
    df.agg(
      min("salary").as("Minimum_Salary"),
      max("salary").as("Maximum_Salary"),
      avg("salary").as("Average_Salary"),
      stddev("salary").as("Std_Dev_Salary"),
      count("*").as("Total_Employees")
    )
  }
}

