name := "Scala1"

version := "0.1.0"

scalaVersion := "2.13.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "3.5.0"
)

mainClass in (Compile, run) := Some("EmployeeAnalytics")


