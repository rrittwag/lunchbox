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
  ws,
  filters,
  // domain model
  "com.github.nscala-time" %% "nscala-time" % "2.12.0",
  "org.joda"               %  "joda-money"  % "0.11",
  // domain logic
  "net.sourceforge.htmlcleaner" %  "htmlcleaner"   % "2.16",
  "org.apache.commons"          %  "commons-lang3" % "3.4",
  "org.apache.pdfbox"           %  "pdfbox"        % "1.8.11",
  "org.scalactic"               %% "scalactic"     % "2.2.6",
  // external
  "net.databinder.dispatch"     %% "dispatch-core" % "0.11.3",
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
