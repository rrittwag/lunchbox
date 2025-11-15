# lunchbox-backend-spring-kotlin

Backend für Lunchbox.


## Features

Das Backend implementiert Version 1 vollständig. Es wertet folgende Mittagsanbieter aus:

* [Neubrandenburg: AOK Cafeteria](http://www.hotel-am-ring.de/aok-cafeteria.html)
* [Neubrandenburg: Das Krauthof](https://www.daskrauthof.de/karte/)
* [Neubrandenburg: Suppenkulttour](http://www.suppenkult.com/wochenplan.html)
* [Neubrandenburg: Schweinestall](http://www.schweinestall-nb.de/)
* [Berlin: Feldküche Karow](https://www.feldkuechebkarow.de/speiseplan)
* [Berlin: Kantine B. Quakatz (Gesundheitszentrum)](https://de-de.facebook.com/pages/Kantine-BQuakatz-Allee-der-Kosmonauten/181190361991823)
* [Berlin: Salt 'n' Pepper](http://www.partyservice-rohde.de/bistro-angebot-der-woche/)
 

## Lokaler Start

Voraussetzungen: Es müssen Java (mind. Version 8), Docker und Docker Compose installiert sein.

Das Auswertung der Mittagsangebote bedarf der externen Dienste OpenOCR & Robotron. Sie werden via Docker Compose bereitgestellt:
                     
    docker compose up -d

Das Backend startet mit dem Befehl

    ./gradlew bootRun

Der Debug-Endpunkt ist über Port 5005 erreichbar.

Bei Änderungen bitte Unit-Tests schreiben/anpassen. Der folgende Befehl führt die Test aus:

    ./gradlew test


## TODOs

- API: bei 4xx-/5xx-Exceptions eine message mitliefern
- Integrations-Test schreiben
- Monitoring: wenn Mittagsangebote fehlen, Mail an Admin
- Feiertage filtern anhand [API](https://github.com/bundesAPI/feiertage-api)
- Technologien ausprobieren:
  - OCR in eigenes Go-Backend vefrachten (via gRPC ansprechen)
  - [Kotlin Assertion Library ersetzen](https://www.novatec-gmbh.de/en/blog/kotlin-assertion-libraries-conclusions)
  - Webflux & Coroutines [1](https://www.baeldung.com/kotlin-coroutines) [2](https://docs.spring.io/spring/docs/current/spring-framework-reference/languages.html#coroutines)
  - Gradle-Skripte in Kotlin DSL [1](https://github.com/jnizet/gradle-kotlin-dsl-migration-guide) [2](https://github.com/mixitconf/mixit/blob/master/build.gradle.kts)


## Urheberrecht

Dieses Projekt beinhaltet beispielhafte Mittagspläne im HTML- & PDF-Format. Sie dienen dem automatisierten Test der Lunchbox-Server-Funktionalität. Urheber und Eigentümer der Dateien ist der jeweilige Anbieter. Die Dateien waren im betroffenen Zeitraum frei zugänglich über die Internetseite des Anbieters abrufbar.
