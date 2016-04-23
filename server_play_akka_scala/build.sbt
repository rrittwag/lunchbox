import com.typesafe.sbt.packager.docker._



// project definition
// ~~~~~~~~~~~~~~~~~~
organization := "info.rori.lunchbox.server.play_akka_scala"
name := "lunchbox_server"
version := "0.1"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)



// dependencies
// ~~~~~~~~~~~~
scalaVersion := "2.11.8"
libraryDependencies ++= Seq(
//  jdbc,
//  cache,
//  ws,
  filters,
  // domain model
  "com.github.nscala-time" %% "nscala-time" % "2.12.0",
  "org.joda"               %  "joda-money"  % "0.11",
  // test
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % Test
)



// settings
// ~~~~~~~~

// changing port to 8080 (in dev mode)
PlayKeys.devSettings := Seq("play.server.http.port" -> "8080")

// no Javadoc generation
sources in (Compile,doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false



// Docker settings
// ~~~~~~~~~~~~~~~
maintainer := "rori"
dockerBaseImage := "frolvlad/alpine-oraclejdk8"
dockerCommands := dockerCommands.value.flatMap{
  case cmd@Cmd("FROM",_) => List(cmd, Cmd("RUN", "apk update && apk add bash"))
  case other => List(other)
}
dockerExposedPorts in Docker := Seq(9000)
