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
* [Beispiel für Akka HTTP-Server](https://github.com/hseeberger/reactive-flows)
