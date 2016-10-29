// project definition
// ~~~~~~~~~~~~~~~~~~
organization := "info.rori.lunchbox.server.play_akka_scala"
name := "lunchbox-server"
version := "1.0"

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
// project uses Java 8 features
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

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
  .setPreference(DoubleIndentClassDeclaration, true)



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
dockerUpdateLatest := true // bug in sbt-native-packager stops publishing Docker image!
                           // Set false and tag the Docker image yourself => https://docs.docker.com/engine/getstarted/step_six/
