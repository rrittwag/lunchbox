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



Distribution
------------

* `tar.gz`-File im Verzeichnis `target/universal/` erzeugen: `sbt universal:package-zip-tarball`



Wissen
------

* [Akka Dokumentation](http://akka.io/docs/)
* [Akka Logging](http://doc.akka.io/docs/akka/2.3.9/scala/logging.html)
* [Akka HTTP: kompaktes Beispiel inkl. JSON-Marshalling](https://typesafe.com/activator/template/akka-http-microservice)
* [Akka HTTP: Beispiel für XML-Marshalling + Authentication](https://github.com/akka/akka/blob/release-2.3-dev/akka-http-tests/src/test/scala/akka/http/server/TestServer.scala)
* [Akka HTTP: Beispiel für Streams + Server-Side-Events](https://github.com/hseeberger/reactive-flows)
* [Feed: Beschreibung des Atom-Formats](http://atomenabled.org/developers/syndication)
* [Feed: Validator](http://validator.w3.org/feed/)
* [Packaging/Distribution: Tutorial für einfache Applikationen](http://www.scala-sbt.org/sbt-native-packager/archetypes/java_app/my-first-project.html)
* [Packaging/Distribution: viele Beispiele für sbt-native-packager](https://github.com/muuki88/sbt-native-packager-examples)
* [Testing: ScalaTest User Guide](http://www.scalatest.org/user_guide)



TODOs
-----

* LunchOfferResolver für verbleibende Provider implementieren
* LunchOfferResolver: Wochenangebote gelten nicht für Feiertage
* DI für LunchProviderService => vergleiche https://github.com/ehalpern/sandbox/tree/master/src/main/scala/twine
* Unit-Tests (siehe Fowler)
* Akka-Supervisioning beschreiben
* Caching des Feeds (spray-caching?)
* in Docker deployen => http://www.scala-sbt.org/sbt-native-packager/archetypes/java_server/my-first-project.html & https://github.com/pussinboots/sbt-rpm/blob/master/project/packaging.scala
* in Vagrant deployen=> https://github.com/pussinboots/sbt-rpm
* Publish: sbt-release einsetzen + privates Maven-Repo?
* systemd-Skript basteln
* TODOs in Code abarbeiten
* schnelles Re-Deployment mit sbt-revolver ??
* Stoppen per Maintenance ermöglichen
* one actor per http connection => http://mogproject.blogspot.de/2014/07/scala-getting-started-with-akka-http.html



Urheberrecht
------------

Dieses Projekt beinhaltet beispielhafte Mittagspläne im HTML- & PDF-Format. Sie dienen dem automatisierten Test der Lunchbox-Server-Funktionalität. Urheber und Eigentümer der Dateien ist der jeweilige Anbieter. Die Dateien waren im betroffenen Zeitraum frei zugänglich über die Internetseite des Anbieters abrufbar.
