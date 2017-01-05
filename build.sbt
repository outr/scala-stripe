name := "scala-stripe"
organization in ThisBuild := "com.outr"
version in ThisBuild := "1.0.0-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.1"
crossScalaVersions in ThisBuild := List("2.12.1", "2.11.8")
sbtVersion in ThisBuild := "0.13.13"
scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")
resolvers in ThisBuild += Resolver.sonatypeRepo("releases")

val circeVersion = "0.6.1"

lazy val root = project.in(file("."))
  .aggregate(coreJS, coreJVM)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val core = crossProject.in(file("core"))
  .settings(
    name := "scala-stripe",
    libraryDependencies += "com.outr" %%% "scribe" % "1.3.2",
    libraryDependencies += "org.scalactic" %%% "scalactic" % "3.0.1",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.1" % "test"
  )
  .jvmSettings(
    libraryDependencies += "com.eed3si9n" %% "gigahorse-core" % "0.2-SNAPSHOT",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )
  .jsSettings(
    jsDependencies += RuntimeDOM,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1"
  )

lazy val coreJS = core.js
lazy val coreJVM = core.jvm