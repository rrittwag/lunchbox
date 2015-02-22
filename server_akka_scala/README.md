Lunchbox Server mit Akka & Scala
================================

Dieses Sub-Projekt beschreibt eine Implementierung des Lunchbox-Servers. Die Umsetzung erfolgt mit Akka und Scala.



Build
-----

* Projektdateien für IntelliJ IDEA generieren: `sbt gen-idea`
* Projektdateien für Eclipse generieren: `sbt eclipse`

* Projekt übersetzen: `sbt compile`



Benutzung
---------

* Server starten: `sbt run`
* Die REST API ist unter [http://localhost:8080/api/v1/](http://localhost:8080/api/v1/) erreichbar



Wissen
------

* [Akka Dokumentation](http://akka.io/docs/)
* [Akka Logging](http://doc.akka.io/docs/akka/2.3.9/scala/logging.html)
* [Akka HTTP: kompaktes Beispiel inkl. JSON-Marshalling](https://typesafe.com/activator/template/akka-http-microservice)
* [Akka HTTP: Beispiel für XML-Marshalling + Authentication](https://github.com/akka/akka/blob/release-2.3-dev/akka-http-tests/src/test/scala/akka/http/server/TestServer.scala)
* [Akka HTTP: Beispiel für Streams + Server-Side-Events](https://github.com/hseeberger/reactive-flows)
