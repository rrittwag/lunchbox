package info.rori.lunchbox.server.akka.scala

import akka.actor.ActorSystem
import akka.http.Http
import akka.stream.FlowMaterializer

object Server extends App {
  val host = "localhost"
  val port = 8080

  implicit val system = ActorSystem("lunchbox_server")
  implicit val fm = FlowMaterializer()

  println("Starting akka-http server at " + host + ":" + port)

  val serverBinding = Http(system).bind(interface = host, port = port)

  serverBinding.connections.foreach { connection =>
    println("Accepted connection from: " + connection.remoteAddress)
    connection handleWithSyncHandler RequestHandler().handler
  }
}