Lunchbox Web-Client mit AngularJS und Bootstrap
===============================================

Dieses Sub-Projekt beschreibt die Implementierung eines Lunchbox Web-Clients. Die Umsetzung erfolgt mit [AngularJS](https://angularjs.org) und [Bootstrap](http://getbootstrap.com).



Status
------

Alpha-Version



Build & Benutzung
-----------------

* Node.js, Gulp & Bower müssen installiert sein - siehe [Homepage zu generator-gulp-angular](https://www.npmjs.com/package/generator-gulp-angular)
* Abhängigkeiten aktualisieren mit `npm update` und `bower install`.
* `gulp serve` deployt das Web-App auf einem internen Web-Server. Der Chrome-Browser startet automatisch und lädt das Web-App unter der Adresse [http://localhost:3000/](http://localhost:3000/). Quellcode-Änderungen werden auto-deployt.
* `gulp test` führt die Unit-Tests aus.



Distribution
------------

* `gulp build` generiert distributierbare Dateien im Verzeichnis `dist`.



TODOs
-----

* Test: auf Smartphone & Tab testen (inkl. NavBar-Menü-Buttons & schickes Design)
* Test: auf MS Edge testen
* Design: Lunchbox-Logo für Jumbotron bereitstellen
* Model: Daten neu laden, wenn über 1 Tag alt
* Projekt mit HTML5 Boilerplate abgleichen
* Adress-Query-Parameter "day": gezielt die Angebote für einen Tag abfragen
* in Feed Links zur Website setzen
* Tag-Auswähler: als Komponente auslagern
* Meldungsdialog als Komponente auslagern
* System-Tests bereitstellen, via [Selenium und Protractor](https://github.com/angular/protractor) ?
* durch Tage "swipen": auf Smartphone nach links und nach rechts ziehen
* schöneres Bootstrap-Theme einsetzen ? z.B. [Bootstrap Material Design](http://fezvrasta.github.io/bootstrap-material-design/) oder [Bootswatch Paper](https://bootswatch.com/paper/) ?
* i18n & l10n
* assert-Funktion in offers.filter verschieben in eigene Lib?
* Direkte Anwahl einer Sub-Adresse (z.B. 'http://localhost/about') ermöglichen. Wie kann nginx die Subadresse an Client/AngularJS weiterreichen?
* [App-Links in Meta-Info aufnehmen](http://ricostacruz.com/cheatsheets/applinks.html)
* Umstellen auf ECMAScript 6, CoffeScript, Dart oder Scala.js?


Wissen
------

* [Vorlage: Yeoman generator for AngularJS with GulpJS](https://www.npmjs.com/package/generator-gulp-angular)
* [Vorlage: HTML5 Boilerplate](https://github.com/h5bp/html5-boilerplate)
* [AngularJS: Getting Started](https://docs.angularjs.org/misc/started)
* [AngularJS: gutes einfaches Beispiel](https://github.com/tastejs/todomvc/tree/master/examples/angularjs)
* [AngularJS: gute Beispielsammlung zu Basics](http://www.angularjshub.com/examples/)
* [AngularJS: REST-Aufruf](https://docs.angularjs.org/tutorial/step_11)
* [AngularJS: Bootstrap-Navigationsleiste](https://angularjs.de/artikel/navigation-menu-bootstrap)
* [Testing: Einführung in Unit-Testing mit AngularJS](https://docs.angularjs.org/guide/unit-testing)
* [Testing: Einführung in BDD-Framework Jasmine](http://jasmine.github.io/2.2/introduction.html)
* Design: Überblick zu [Bootstrap-Features](http://getbootstrap.com/css/), [Bootstrap-Komponenten](http://getbootstrap.com/components/) & [weitere AngularJS-Bootstrap-Komponenten](https://angular-ui.github.io/bootstrap/)
* Design: [App Store Guideline](https://developer.apple.com/app-store/marketing/guidelines/de/) & [Google Play Badge Generator](https://developer.android.com/distribute/tools/promote/badges.html)
