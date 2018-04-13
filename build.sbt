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

libraryDependencies ++= Seq(
	"com.typesafe.play" %% "play-json" % "2.6.9",
	"org.scalaj" %% "scalaj-http" % "2.3.0",
    "com.typesafe" % "config" % "1.3.3"
)
