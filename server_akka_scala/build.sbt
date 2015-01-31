organization := "info.rori.lunchbox.server.akka.scala"

name := "server_akka_scala"

version := "0.1"

scalaVersion := "2.11.5"

libraryDependencies ++= {
  val akkaV = "2.3.9"
  Seq(
    "com.typesafe.akka" %% "akka-actor"   % akkaV,
//    "com.typesafe.akka" %% "akka-testkit" % akkaV   % "test",
    "com.typesafe.akka" %% "akka-http-experimental"      % "1.0-M2" //,
//  "org.specs2"        %% "specs2-core"  % "2.3.7" % "test"
  )
}

