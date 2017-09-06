// project definition
// ~~~~~~~~~~~~~~~~~~
organization := "info.rori.lunchbox.server.play_akka_scala"
name := "lunchbox-server"
version := "1.0"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)



// dependencies
// ~~~~~~~~~~~~
scalaVersion := "2.12.2"
libraryDependencies ++= Seq(
//  jdbc,
//  cache,
  ws,
  filters,
  guice,         
  // domain model
  "org.joda"               %  "joda-money"  % "0.12",
  // domain logic
  "net.sourceforge.htmlcleaner" %  "htmlcleaner"   % "2.21",
  "org.apache.commons"          %  "commons-text"  % "1.1",
  "org.apache.pdfbox"           %  "pdfbox"        % "1.8.11",
  "org.scalactic"               %% "scalactic"     % "3.0.1",
  // external
  "net.databinder.dispatch"     %% "dispatch-core" % "0.13.1",
  // test
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
)



// settings
// ~~~~~~~~
// enhances available types in routes file
routesImport += "util.PlayDateTimeHelper._"

// changing port to 8080 (in dev mode)
PlayKeys.devSettings := Seq("play.server.http.port" -> "8080")

// no Javadoc generation
sources in (Compile,doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false



// code formatter settings
// ~~~~~~~~~~~~~~~~~~~~~~~
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(SpacesAroundMultiImports, false)
  .setPreference(PreserveSpaceBeforeArguments, true)
  .setPreference(DoubleIndentConstructorArguments, true)



// Docker settings
// ~~~~~~~~~~~~~~~
import com.typesafe.sbt.packager.docker._

maintainer := "rori"
dockerBaseImage := "frolvlad/alpine-oraclejdk8"
dockerCommands := dockerCommands.value.flatMap{
  case cmd@Cmd("FROM",_) => List(cmd, Cmd("RUN", "apk update && apk add bash"))
  case other => List(other)
}
dockerExposedPorts in Docker := Seq(9000)

dockerRepository := Some("rori")
dockerUpdateLatest := true