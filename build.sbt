name := "scalajs-stripe"
organization := "com.outr"
version := "1.0.0-SNAPSHOT"
scalaVersion := "2.12.1"
crossScalaVersions := List("2.12.1", "2.11.8")
sbtVersion := "0.13.13"
scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies += "com.outr" %%% "scribe" % "1.2.6"
libraryDependencies += "org.scalactic" %%% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1" % "test"

enablePlugins(ScalaJSPlugin)

jsDependencies += RuntimeDOM
//jsDependencies += ProvidedJS / "stripe-v2.js" % Test