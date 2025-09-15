ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.5"

lazy val root = (project in file("."))
  .settings(
    name := "funsets"
  )

// 의존성 정의
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "junit" % "junit" % "4.10" % "test"
)