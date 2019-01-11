import sbtcrossproject.CrossPlugin.autoImport.crossProject

name := "scala-stripe"
organization in ThisBuild := "com.outr"
version in ThisBuild := "1.1.3"
scalaVersion in ThisBuild := "2.12.7"
crossScalaVersions in ThisBuild := List("2.12.7", "2.11.11")
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

val youiVersion = "0.9.10"

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