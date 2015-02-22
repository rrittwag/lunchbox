import sbt.Keys._

organization := "info.rori.lunchbox.server.akka.scala"

name := "server_akka_scala"

version := "0.1"

scalaVersion := "2.11.5"

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  val akkaHttpVersion = "1.0-M3"
  Seq(
    // Akka
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "ch.qos.logback"    %  "logback-classic" % "1.1.2",
    // service dependencies
    "com.typesafe.akka"      %% "akka-http-experimental"            % akkaHttpVersion,
    "com.typesafe.akka"      %% "akka-http-spray-json-experimental" % akkaHttpVersion,
    "io.spray"               %% "spray-json"                        % "1.3.1",
    "com.typesafe.akka"      %% "akka-http-xml-experimental"        % akkaHttpVersion,
    "org.scala-lang.modules" %% "scala-xml"                         % "1.0.3",
    // domain model
    "com.github.nscala-time" %% "nscala-time" % "1.8.0",
    "org.joda"               %  "joda-money"  % "0.10.0",
    // domain logic
    "net.sourceforge.htmlcleaner" % "htmlcleaner"   % "2.10",
    "org.apache.commons"          % "commons-lang3" % "3.3.2",
    "org.apache.pdfbox"           % "pdfbox"        % "1.8.8"
    // test dependencies
    //    "com.typesafe.akka" %% "akka-testkit" % akkaV   % "test",
    //    "org.specs2"        %% "specs2-core"  % "2.3.7" % "test"
  )
}

