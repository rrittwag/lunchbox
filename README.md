Lunchbox
========

Mittagspause! Aber wo gehen wir essen? Hier hilft Lunchbox!

Lunchbox stellt die Mittagsangebote im Umkreis zusammen und ermöglicht ein übersichtliches und bequemes Abrufen per Smartphone-App oder Webseite.

Lunchbox bietet in Version 1 folgende Funktionalitäten:

* Für 6 Mittagsanbieter (s.u.) werden automatisiert die Mittagsangebote des aktuellen Tages ermittelt (auf Server)
* Der Nutzer erhält eine Übersicht über die Mittagsangebote, gefiltert nacht ausgewähltem Umkreis (auf Client)

Für Version 1 sind folgende Mittagsanbieter vorgesehen:

* [Neubrandenburg: Schweinestall](http://www.schweinestall-nb.de/)
* [Neubrandenburg: Hotel am Ring](http://www.hotel-am-ring.de/restaurant-rethra.html)
* [Neubrandenburg: AOK Cafeteria](http://www.hotel-am-ring.de/aok-cafeteria.html)
* [Neubrandenburg: Suppenkulttour](http://www.suppenkult.com/wochenplan.html)
* [Berlin: Salt 'n' Pepper](http://www.partyservice-rohde.de/bistro-angebot-der-woche/)
* [Berlin: Katine B. Quakatz (Gesundheitszentrum)](https://de-de.facebook.com/pages/Kantine-BQuakatz-Allee-der-Kosmonauten/181190361991823)

Das Projekt ist zugleich Spielwiese zum Ausprobieren technischer Frameworks und Spezifikationen. Server und Client dürfen also gern in verschiedensten Sprachen und Varianten implementiert werden.

Ideen für weitere Versionen:

* Nutzer wählt favorisiertes Mittagsangebot aus
* Nutzer können sich zu einer Essengruppe zusammenschließen
* Essengruppe koordiniert Zeitpunkt und Mittagsanbieter
* "kurzentschlossener" Nutzer findet spontan Essengruppe in seiner Nähe
* ...



Projektstruktur
---------------

* `api` - Dokumentation der REST API für Client-Aufrufe. Das Projekt hält eine Mock-Implementierung mit Beispieldaten.
* `server_*` - Sub-Projekte mit Server-Implementierung (z.B. `server_akka_scala` für Implementierung mit Scala & Akka).
* `client_*` - Sub-Projekte mit Client-Implementierung (z.B. `client_web_angular`, `client_android`, `client_ios`, ...).



Status
------

**Stable**

DONE

* api - Beschreibt REST API Version 1, samt Mock-Implementierung.
* server_akka_scala - Der Server erfüllt Version 1 vollständig.
* server_play_akka_scala - Der Server erfüllt Version 1 vollständig.
* client_web_angular - Der Client erfüllt Version 1 vollständig.
* client_android - Der Client erfüllt Version 1 vollständig.

TODO

* clients: ios, ...



Build
-----

Siehe Sub-Projekt.



Benutzung
---------

Siehe Sub-Projekt.
