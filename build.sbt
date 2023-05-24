ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "idleadventurer"
  )

libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.1"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.1"