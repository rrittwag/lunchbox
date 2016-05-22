Lunchbox Server mit Play, Akka & Scala
======================================

Dieses Sub-Projekt beschreibt eine Implementierung des Lunchbox-Servers. Die Umsetzung erfolgt mit [Play](https://www.playframework.com/), [Akka](http://akka.io) und [Scala](http://www.scala-lang.org).



Status
------

Erfüllt die Anforderungen für Lunchbox v1 vollständig:

* automatische Ermittlung der Mittagsangebote (4x NB, 2x Berlin)
* Wiedergabe der Mittagsangebote per REST und Web-Feed



Build
-----

* Projekt übersetzen: `sbt compile`



Benutzung
---------

* Server im Dev-Mode starten via `sbt run`
* ... oder via `scripts/docker-run-from-source.sh` Docker-Image erzeugen und Server starten
* Die REST API ist unter [http://localhost:8080/api/v1/](http://localhost:8080/api/v1/) erreichbar
* Der Web-Feed ist unter [http://localhost:8080/feed](http://localhost:8080/feed) erreichbar



Distribution
------------

* via `sbt clean docker:stage docker:publish` Docker-Image erzeugen und in Remote-Repository stellen
* in Produktionssystem via `scripts/docker-run-from-repo.sh` Docker-Image laden und starten (vorher Skript-Variablen anpassen!)



TODOs
-----

* DI auch in Domain
* Dispatch durch WS ersetzen
* Testing: automatisierte Tests erweitern, siehe [Microservice Testing](http://martinfowler.com/articles/microservice-testing/)
  * Unit-Tests für Helper-Klassen schreiben
  * Unit-Tests für Controller schreiben (Akka mocken mit akka-testkit?)
  * Unit-Tests für Aktoren schreiben
  * Unit-Tests für external Clients schreiben
* aus PlayDateTimeHelper ein DateTimeJsonFormats-Trait herausziehen?
* Metrics & Tracing, um Antwortzeiten und Last zu überwachen, mit [ActorStacks](http://de.slideshare.net/EvanChan2/akka-inproductionpnw-scala2013) und Frameworks [Kamon, Graphite, Statsd, ...](http://mukis.de/pages/monitoring-akka-with-kamon/)
* auf PDF-Box v2.0 migrieren
* auf Facebook Graph API v2.6 migrieren
* Akka-Supervisioning beschreiben (siehe auch Buch "Reactive Web Applications")
* LunchResolver: globalen durch eigenen ExecutionContext ersetzen?
* LunchResolver automatisch auflösen (via DI-MultiBinder oder ServiceLoader oder custom-Reflection?)
* Caching der Requests
* Scalastyle einrichten
* TODOs in Code abarbeiten



Wissen
------

* [Akka Dokumentation](http://akka.io/docs/)
* [Akka Logging](http://doc.akka.io/docs/akka/2.3.9/scala/logging.html)
* [Feed: Beschreibung des Atom-Formats](http://atomenabled.org/developers/syndication)
* [Feed: Validator](http://validator.w3.org/feed/)
* [Testing: ScalaTest User Guide](http://www.scalatest.org/user_guide)
* [Docker: Build-Konfiguration](http://www.scala-sbt.org/sbt-native-packager/formats/docker.html) & [Deployment](https://www.packtpub.com/books/content/deploying-play-application-coreos-and-docker)



Urheberrecht
------------

Dieses Projekt beinhaltet beispielhafte Mittagspläne im HTML- & PDF-Format. Sie dienen dem automatisierten Test der Lunchbox-Server-Funktionalität. Urheber und Eigentümer der Dateien ist der jeweilige Anbieter. Die Dateien waren im betroffenen Zeitraum frei zugänglich über die Internetseite des Anbieters abrufbar.
