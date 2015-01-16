Lunchbox API
============

Dieses Sub-Projekt beschreibt die REST API des Servers, von der die Clients die Mittagsangebote abrufen. Es wird folgendes bereitgestellt:

* Die Dokumentation der REST API, realisiert per Swagger UI.
* Eine Mock-Implementierung mit Beispieldaten. Es kommen JAX RS & Jersey zum Einsatz.



Build
-----

* Projektdateien für IntelliJ IDEA generieren: `gradle idea`
* Projektdateien für Eclipse generieren: `gradle eclipse`

* Projekt übersetzen und WAR-File erstellen: `gradle build`



Benutzung
---------

* WAR-File deployen: `gradle appRun`.
* [Abruf der Swagger-Dokumentation](http://localhost:8080/api-docs)
* [Abruf der Mock-Implementierung](http://localhost:8080/api/v1)



Wissen
------

* [Tutorial: Jersey & JAX-RS](http://www.vogella.com/tutorials/REST/article.html#restjersey)
* [Tutorial: Jersey & Servlet 2](https://jersey.java.net/documentation/latest/user-guide.html#deployment.servlet.2)
