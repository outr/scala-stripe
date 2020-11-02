import sbtcrossproject.CrossPlugin.autoImport.crossProject

name := "scala-stripe"
organization in ThisBuild := "com.outr"
version in ThisBuild := "1.1.12-SNAPSHOT"
scalaVersion in ThisBuild := "2.13.3"
crossScalaVersions in ThisBuild := List("2.13.3", "2.12.12")
scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")
resolvers in ThisBuild += Resolver.sonatypeRepo("releases")

publishTo in ThisBuild := sonatypePublishTo.value
sonatypeProfileName in ThisBuild := "com.outr"
publishMavenStyle in ThisBuild := true
licenses in ThisBuild := Seq("MIT" -> url("https://github.com/outr/scala-stripe/blob/master/LICENSE"))
sonatypeProjectHosting in ThisBuild := Some(xerial.sbt.Sonatype.GitHubHosting("outr", "scala-stripe", "matt@outr.com"))
homepage in ThisBuild := Some(url("https://github.com/outr/scala-stripe"))
scmInfo in ThisBuild := Some(
  ScmInfo(
    url("https://github.com/outr/scala-stripe"),
    "scm:git@github.com:outr/scala-stripe.git"
  )
)
developers in ThisBuild := List(
  Developer(id="darkfrog", name="Matt Hicks", email="matt@matthicks.com", url=url("http://matthicks.com"))
)

val youiVersion = "0.13.17"

lazy val root = project.in(file("."))
  .aggregate(coreJS, coreJVM)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val core = crossProject(JVMPlatform, JSPlatform).in(file("core"))
  .settings(
    name := "scala-stripe",
    libraryDependencies ++= Seq(
      "org.scalactic" %%% "scalactic" % "3.2.2",
      "org.scalatest" %%% "scalatest" % "3.2.2" % "test",
      "org.scalatest" %% "scalatest-wordspec" % "3.2.2" % "test",
      "org.scalatest" %%% "scalatest-matchers-core" % "3.2.2" % "test",
      "com.outr" %%% "profig" % "3.0.4" % "test"
    )
  )
  .jvmSettings(
    fork := true,
    libraryDependencies ++= Seq(
      "io.youi" %% "youi-client" % youiVersion
    )
  )
  .jsSettings(
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.1.0"
  )

lazy val coreJS = core.js
lazy val coreJVM = core.jvm
