import play.Project._

name := """fractal"""

scalaVersion := "2.11.7"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.2.2", 
  "org.webjars" % "bootstrap" % "3.3.6",
  "com.google.inject" % "guice" % "3.0"
)

playScalaSettings
