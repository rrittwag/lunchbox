Lunchbox API
============

Dieses Sub-Projekt beschreibt die REST API des Servers, von der die Clients die Mittagsangebote abrufen. Das Projekt enthält:

* Die Dokumentation der REST API, realisiert per Swagger.
* Eine Mock-Implementierung mit Beispieldaten. Die technische Basis bilden JAX RS & Jersey.



Build
-----

* Projektdateien für IntelliJ IDEA generieren: `gradle idea`
* Projektdateien für Eclipse generieren: `gradle eclipse`

* Projekt übersetzen und WAR-File erstellen: `gradle build`



Benutzung
---------

* WAR-File deployen: `gradle jettyRun`.
* [Abruf der Swagger-Dokumentation](http://localhost:8080/api-docs)
* [Abruf der Mock-Implementierung](http://localhost:8080/api/v1)



Wissen
------

* [Tutorial: Jersey & JAX-RS](http://www.vogella.com/tutorials/REST/article.html#restjersey)
* [Tutorial: Jersey & Servlet 2](https://jersey.java.net/documentation/latest/user-guide.html#deployment.servlet.2)
* Tutorial: Swagger-Konfiguration [Quelle 1](http://aredko.blogspot.de/2013/09/swagger-make-developers-to-love-working.html) + [Quelle 2](https://github.com/swagger-api/swagger-core/wiki/Swagger-Core-Jersey-2.X-Project-Setup)
