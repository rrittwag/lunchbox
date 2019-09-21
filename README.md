# lunchbox-backend-spring-kotlin

Backend für Lunchbox.


## Features

Version 1 ist vollständig implementiert. Es werden folgende Mittagsanbieter ausgewertet:

* [Neubrandenburg: AOK Cafeteria](http://www.hotel-am-ring.de/aok-cafeteria.html)
* [Neubrandenburg: Das Krauthof](https://www.daskrauthof.de/karte/)
* [Neubrandenburg: Suppenkulttour](http://www.suppenkult.com/wochenplan.html)
* [Neubrandenburg: Schweinestall](http://www.schweinestall-nb.de/)
* [Berlin: Feldküche Karow](https://www.feldkuechebkarow.de/speiseplan)
* [Berlin: Kantine B. Quakatz (Gesundheitszentrum)](https://de-de.facebook.com/pages/Kantine-BQuakatz-Allee-der-Kosmonauten/181190361991823)
* [Berlin: Salt 'n' Pepper](http://www.partyservice-rohde.de/bistro-angebot-der-woche/)
 

## Lokaler Start

Voraussetzungen: Es müssen Java 8, Docker und Docker Compose installiert sein.

Das Auswertung der Mittagsangebote bedarf der externen Dienste OpenOCR & Robotron. Sie werden via Docker Compose bereitgestellt:
                     
    docker-compose up -d

Backend starten mit

    ./gradlew bootRun

Der Debug-Endpunkt ist über Port 5005 erreichbar.

Bei Änderungen bitte Unit-Tests schreiben/anpassen. Der folgende Befehl führt die Test aus:

    ./gradlew test


## TODOs

- Feature: an Feiertagen keine Mittagsangebote
- API: bei 4xx-/5xx-Exceptions eine message mitliefern
- Integrations-Test schreiben
- Monitoring: wenn Mittagsangebote fehlen, Mail an Admin
- wenn Lunchbox-Backend fertig, testweise neue Technologien ausprobieren:
  - Webflux & Coroutines ([mit Spring 5.2) [1](https://www.baeldung.com/kotlin-coroutines) [2](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow#spring-mvc-or-webflux) [3](https://docs.spring.io/spring/docs/5.2.0.M1/spring-framework-reference/languages.html#coroutines)
  - MockMVC in Kotlin-Syntax ([mit Spring 5.2](https://docs.spring.io/spring/docs/5.2.0.M1/spring-framework-reference/languages.html#mockmvc-dsl))
  - Gradle-Skripte in Kotlin DSL [1](https://github.com/jnizet/gradle-kotlin-dsl-migration-guide) [2](https://github.com/mixitconf/mixit/blob/master/build.gradle.kts)


## Urheberrecht

Dieses Projekt beinhaltet beispielhafte Mittagspläne im HTML- & PDF-Format. Sie dienen dem automatisierten Test der Lunchbox-Server-Funktionalität. Urheber und Eigentümer der Dateien ist der jeweilige Anbieter. Die Dateien waren im betroffenen Zeitraum frei zugänglich über die Internetseite des Anbieters abrufbar.
