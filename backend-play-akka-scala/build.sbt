// project definition
// ~~~~~~~~~~~~~~~~~~
organization := "info.rori.lunchbox.server.play_akka_scala"
name := "lunchbox-server"
version := "1.0"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)



// dependencies
// ~~~~~~~~~~~~
scalaVersion := "2.12.16"
libraryDependencies ++= Seq(
//  jdbc,
//  cache,
  ws,
  filters,
  guice,
  // domain model
  "org.joda"               %  "joda-money"  % "1.0.1",
  // domain logic
  "org.scala-lang.modules"      %% "scala-xml"     % "1.1.1",
  "org.ccil.cowan.tagsoup"      %  "tagsoup"       % "1.2.1",
  "org.apache.pdfbox"           %  "pdfbox"        % "1.8.16",
  "org.scalactic"               %% "scalactic"     % "3.0.5",
  // external
  "net.databinder.dispatch"     %% "dispatch-core" % "0.13.4",
  // test
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
)



// settings
// ~~~~~~~~
scalacOptions += "-target:jvm-1.8"

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
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(SpacesAroundMultiImports, false)
  .setPreference(PreserveSpaceBeforeArguments, true)
  .setPreference(DoubleIndentConstructorArguments, true)



// Docker settings
// ~~~~~~~~~~~~~~~
import com.typesafe.sbt.packager.docker._

maintainer := "rori"
dockerBaseImage := "openjdk:8-jre-alpine"
dockerCommands := dockerCommands.value.flatMap{
  case cmd@Cmd("FROM",_) => List(cmd, Cmd("RUN", "apk update && apk --no-cache add bash"))
  case other => List(other)
}
dockerExposedPorts in Docker := Seq(9000)

dockerRepository := Some("rori")
dockerUpdateLatest := true
