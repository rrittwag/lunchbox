Lunchbox
========

Mittagspause! Aber wo gehen wir essen? Hier hilft Lunchbox!

Lunchbox stellt die Mittagsangebote im Umkreis zusammen und ermöglicht ein übersichtliches und bequemes Abrufen per Smartphone-App oder Webseite.

Lunchbox bietet in Version 1 folgende Funktionalitäten:
* Für 5 Mittagsanbieter (s.u.) werden automatisiert die Mittagsangebote des aktuellen Tages ermittelt (auf Server)
* Der Nutzer erhält eine Übersicht über die Mittagsangebote, gefiltert nacht ausgewähltem Umkreis (auf Client)

Für Version 1 sind folgende Mittagsanbieter vorgesehen:
* [Neubrandenburg: Schweinestall](http://www.schweinestall-nb.de/)
* [Neubrandenburg: Hotel am Ring](http://www.hotel-am-ring.de/restaurant-rethra.html)
* [Neubrandenburg: AOK Cafeteria](http://www.hotel-am-ring.de/aok-cafeteria.html)
* [Neubrandenburg: Suppenkulttour](http://www.suppenkult.com/wochenplan.html)
* [Berlin: Salt 'n' Pepper](http://www.partyservice-rohde.de/bistro-salt-n-pepper/bistro-angebot-der-woche.html)

Das Projekt ist zugleich Spielwiese zum Ausprobieren technischer Frameworks und Spezifikationen. Server und Client dürfen also gern in verschiedensten Sprachen und Varianten implementiert werden.

Ideen für weitere Versionen:
* Nutzer wählt favorisiertes Mittagsangebot aus
* Nutzer können sich zu einer Essengruppe zusammenschließen
* Essengruppe koordiniert Zeitpunkt und Mittagsanbieter
* "kurzentschlossener" Nutzer findet spontan Essengruppe in seiner Nähe
* ...



Projektstruktur
---------------

* `api` - Dokumentation der REST API für Client-Aufrufe. Das Projekt hält zudem eine Mock-Implementierung mit Beispieldaten.
* `server_*` - Sub-Projekt mit Server-Implementierung (z.B. `server_scala_spray` für Implementierung mit Scala & Spray).
* `client_*` - Sub-Projekt mit Client-Implementierung (z.B. `client_web_angular`, `client_android`, `client_ios`, ...).



Status
------

**early early alpha**

DONE
* api - REST API Version 1 beschrieben

TODO
* server(s)
* clients: web, android, ios, ...



Build
-----

Siehe Sub-Projekt.



Benutzung
---------

Siehe Sub-Projekt.
