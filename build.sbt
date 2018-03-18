import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.ethanmcdonough",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "PBot",
  )
  
mainClass in (Compile, packageBin) := Some("com.ethanmcdonough.PBot.Main")
mainClass in (Compile, run) := Some("com.ethanmcdonough.PBot.Main")

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.5"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.9.4"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.4"
