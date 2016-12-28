name := "scala-stripe"
organization in ThisBuild := "com.outr"
version in ThisBuild := "1.0.0-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.1"
crossScalaVersions in ThisBuild := List("2.12.1", "2.11.8")
sbtVersion in ThisBuild := "0.13.13"
scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

lazy val root = project.in(file("."))
  .aggregate(client, server)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val client = project.in(file("client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "scala-stripe-client",
    //jsDependencies += ProvidedJS / "stripe-v2.js" % Test
    jsDependencies += RuntimeDOM,
    libraryDependencies += "com.outr" %%% "scribe" % "1.2.6",
    libraryDependencies += "org.scalactic" %%% "scalactic" % "3.0.1",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.1" % "test",
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1" % "test"
  )

lazy val server = project.in(file("server"))
  .settings(
    name := "scala-stripe-server",
    fork := true,
    libraryDependencies += "com.outr" %% "scribe-slf4j" % "1.2.6",
    libraryDependencies += "com.eed3si9n" %% "gigahorse-core" % "0.2-SNAPSHOT",
    libraryDependencies += "com.lihaoyi" %% "upickle" % "0.4.4",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )