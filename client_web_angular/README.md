Lunchbox Web-Client mit AngularJS und Bootstrap
===============================================

Dieses Sub-Projekt beschreibt die Implementierung eines Lunchbox Web-Clients. Die Umsetzung erfolgt mit [AngularJS](https://angularjs.org) und [Bootstrap](http://getbootstrap.com).



Status
------

Alpha-Version



Build & Benutzung
-----------------

* Node.js, Gulp & Bower müssen installiert sein - siehe [Homepage zu generator-gulp-angular](https://www.npmjs.com/package/generator-gulp-angular)
* Abhängigkeiten aktualisieren mit `bower install`.
* `gulp serve` deployt das Web-App auf einem internen Web-Server. Der Chrome-Browser startet automatisch und lädt das Web-App unter der Adresse [http://localhost:3000/](http://localhost:3000/). Quellcode-Änderungen werden auto-deployt.
* `gulp test` führt die Unit-Tests aus.



Distribution
------------

* `gulp build` generiert distributierbare Dateien im Verzeichnis `dist`.



TODOs
-----

* Design: Lunchbox-Icon bereitstellen für NavBar & Favicon
* Design: Infoseite verschönern, evtl. vereinfachen?
* Filter für Location bereitstellen (in NavBar?)
* auf Smartphone & Tab testen (inkl. NavBar-Menü-Buttons & schickes Design)
* Location-Auswähler: Auswahl des Nutzers für nächsten Seitenaufruf merken
* Projekt mit HTML5 Boilerplate abgleichen
* Adress-Query-Parameter "day": gezielt die Angebote für einen Tag abfragen
* in Feed Links zur Website setzen
* System-Tests bereitstellen, via [Selenium und Protractor](https://github.com/angular/protractor) ?
* Design: [Bootstrap-Tooltips-Erweiterung](http://getbootstrap.com/javascript/#tooltips) für alle Links/Interaktionen einsetzen
* durch Tage "swipen": auf Smartphone nach links und nach rechts ziehen
* schöneres Bootstrap-Theme einsetzen ? z.B. [Bootstrap Material Design](http://fezvrasta.github.io/bootstrap-material-design/) oder [Bootswatch Paper](https://bootswatch.com/paper/) ?
* i18n & l10n
* assert-Funktion in main.filter verschieben in eigene Lib?
* Underscore per RequireJS einbinden?
* Direkte Anwahl einer Sub-Adresse (z.B. 'http://localhost/about') ermöglichen. Wie kann nginx die Subadresse an Client/AngularJS weiterreichen?
* Tag-Auswähler: auslagern in eigenen Controller? Oder gar in Direktive (Angular 2.0)?
* [App-Links in Meta-Info aufnehmen](http://ricostacruz.com/cheatsheets/applinks.html)
* Umstellen auf ECMAScript 6, CoffeScript, Dart oder Scala.js?


Wissen
------

* [Vorlage: Yeoman generator for AngularJS with GulpJS](https://www.npmjs.com/package/generator-gulp-angular)
* [Vorlage: HTML5 Boilerplate](https://github.com/h5bp/html5-boilerplate)
* [AngularJS: Getting Started](https://docs.angularjs.org/misc/started)
* [AngularJS: gutes einfaches Beispiel](https://github.com/tastejs/todomvc/tree/master/examples/angularjs)
* [AngularJS: REST-Aufruf](https://docs.angularjs.org/tutorial/step_11)
* [AngularJS: Bootstrap-Navigationsleiste](https://angularjs.de/artikel/navigation-menu-bootstrap)
* [Testing: Einführung in Unit-Testing mit AngularJS](https://docs.angularjs.org/guide/unit-testing)
* [Testing: Einführung in BDD-Framework Jasmine](http://jasmine.github.io/2.2/introduction.html)
