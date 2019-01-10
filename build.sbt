import sbtcrossproject.CrossPlugin.autoImport.crossProject

name := "scala-stripe"
organization in ThisBuild := "com.outr"
version in ThisBuild := "1.1.2-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.7"
crossScalaVersions in ThisBuild := List("2.12.7", "2.11.11")
scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")
resolvers in ThisBuild += Resolver.sonatypeRepo("releases")

val youiVersion = "0.9.9"

lazy val root = project.in(file("."))
  .aggregate(coreJS, coreJVM)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val core = crossProject.in(file("core"))
  .settings(
    name := "scala-stripe",
    libraryDependencies += "org.scalactic" %%% "scalactic" % "3.0.5",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.5" % "test"
  )
  .jvmSettings(
    fork := true,
    libraryDependencies ++= Seq(
      "io.youi" %% "youi-client" % youiVersion
    )
  )
  .jsSettings(
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv,
    skip in packageJSDependencies := false,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.6",
    scalacOptions += "-P:scalajs:sjsDefinedByDefault"
  )

lazy val coreJS = core.js
lazy val coreJVM = core.jvm