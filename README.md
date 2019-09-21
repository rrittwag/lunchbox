Lunchbox
========

Mittagspause! Aber wo gehen wir essen? Hier hilft Lunchbox!

Lunchbox ermittelt die Mittagsangebote im Umkreis und ermöglicht ein übersichtliches und bequemes Abrufen per Smartphone-App oder Web-App.

Das Projekt ist zugleich Spielwiese zum Ausprobieren technischer Frameworks und Spezifikationen. Server und Client dürfen also gern in verschiedensten Sprachen und Varianten implementiert werden.


Version 1
---------

Lunchbox bietet in Version 1 folgende Funktionalitäten:

* Für 6 Mittagsanbieter (s.u.) werden automatisiert die Mittagsangebote des aktuellen Tages ermittelt (am Backend)
* Der Nutzer erhält eine Übersicht über die Mittagsangebote, gefiltert nacht ausgewähltem Umkreis (auf Client)

In Version 1 sind (mindestens) folgende 6 Mittagsanbieter zu berücksichtigen:

* [Neubrandenburg: AOK Cafeteria](http://www.hotel-am-ring.de/aok-cafeteria.html)
* [Neubrandenburg: Das Krauthof](https://www.daskrauthof.de/karte/)
* [Neubrandenburg: Suppenkulttour](http://www.suppenkult.com/wochenplan.html)
* [Neubrandenburg: Schweinestall](http://www.schweinestall-nb.de/)
* [Berlin: Kantine B. Quakatz (Gesundheitszentrum)](https://de-de.facebook.com/pages/Kantine-BQuakatz-Allee-der-Kosmonauten/181190361991823)
* [Berlin: Salt 'n' Pepper](http://www.partyservice-rohde.de/bistro-angebot-der-woche/)


Ideen für weitere Versionen
---------------------------

* Badges helfen vegetarische & vegane Mittagsangeboten zu identifizieren
* Nutzer können sich zu einer Essengruppe zusammenschließen
* Essengruppe koordiniert Zeitpunkt und Mittagsanbieter
* "kurzentschlossener" Nutzer findet spontan Essengruppe in seiner Nähe
* ...


Sub-Projekte
------------

* [Dokumentation der REST API für Server-Client-Kommunikation](https://github.com/rrittwag/lunchbox/tree/master/api). Das Projekt enthält eine Mock-Implementierung mit Beispieldaten.
* Server-Implementierung realisiert mit ...
  * [Spring Boot & Kotlin](https://github.com/rrittwag/lunchbox-backend-spring-kotlin)
  * [Play, Akka & Scala](https://github.com/rrittwag/lunchbox/tree/master/server_play_akka_scala) (wird nicht mehr gepflegt)
* [Web-App realisiert mit AngularJS & Bootstrap](https://github.com/rrittwag/lunchbox/tree/master/client_web_angular)
* [Android-App](https://github.com/data-experts/Lunchbox)
* [Alexa-Skill](https://www.amazon.de/Falko-V-Partzsch-DEG-Lunchbox/dp/B074XD9G5D/ref=sr_1_1?__mk_de_DE=ÅMÅŽÕÑ&keywords=lunchbox&qid=1569082911&s=digital-skills&sr=1-1)
