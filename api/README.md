Lunchbox API
============

Dieses Sub-Projekt beschreibt die REST API des Servers, von der die Clients die Mittagsangebote abrufen. Das Projekt enthält:

* Die Dokumentation der REST API, realisiert per Open API.
* Eine Mock-Implementierung mit Beispieldaten. Die technische Basis bilden JAX RS & Jersey.



Build
-----

* Projektdateien für IntelliJ IDEA generieren: `gradle idea`
* Projektdateien für Eclipse generieren: `gradle eclipse`

* Projekt übersetzen: `gradle build`



Benutzung
---------

* Projekt starten: `gradle run`.
* [Abruf der API-Dokumentation](http://localhost:8080)
* [Root der Mock-Implementierung](http://localhost:8080/api/v1/)



Wissen
------

* [Jersey Docs](https://jersey.github.io/documentation/latest/getting-started.html)
* [Swagger Docs](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Getting-started)
