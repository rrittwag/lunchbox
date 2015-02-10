import sbt.Keys._

organization := "info.rori.lunchbox.server.akka.scala"

name := "server_akka_scala"

version := "0.1"

scalaVersion := "2.11.5"

libraryDependencies ++= {
  val akkaV = "2.3.9"
  Seq(
    "com.typesafe.akka" %% "akka-actor"   % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental"            % "1.0-M3",
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "1.0-M3",
    "io.spray"          %% "spray-json" % "1.3.1",
    "com.typesafe.akka" %% "akka-slf4j"   % akkaV,
    "ch.qos.logback"    %  "logback-classic" % "1.1.2"
    //    "com.typesafe.akka" %% "akka-testkit" % akkaV   % "test",
    //    "org.specs2"        %% "specs2-core"  % "2.3.7" % "test"
  )
}

