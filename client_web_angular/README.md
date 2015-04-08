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
* `gulp serve` deployt das Web-App auf einem internen Web-Server.
* Der Chrome-Browser startet automatisch und lädt das Web-App unter der Adresse [http://localhost:3000/](http://localhost:3000/).



Distribution
------------

* `gulp build` generiert distributierbare Dateien im Verzeichnis `dist`.



TODOs
-----

* Mittagsangebote des aktuellen Tages via REST abrufen
* Unit-Tests für Controller, Filter & Service schreiben
* NavBar mit About versehen: Infos zu Projekt, Entwickler & Mitarbeit
* Lunchbox-Icon bereitstellen für NavBar & Favicon
* Feed in Website-Meta-Infos aufnehmen
* Projekt mit HTML5 Boilerplate abgleichen
* durch Tage klicken: Pfeil links + Pfeil rechts + Datum in NavBar
* Tag auswählen per Query-Parameter "day"
* in Feed Links zur Website setzen
* Filter für Location in NavBar einbinden
* System-Tests via [Selenium und Protractor](https://github.com/angular/protractor) bereitstellen
* durch Tage "swipen": auf Smartphone nach links und nach rechts ziehen
* schöneres Bootstrap-Theme einsetzen ? z.B. [Bootstrap Material Design](http://fezvrasta.github.io/bootstrap-material-design/) oder [Bootswatch Paper](https://bootswatch.com/paper/) ?
* i18n & l10n
* Umstellen auf ECMAScript 6, CoffeScript, Dart oder Scala.js?


Wissen
------

* [AngularJS-Vorlage: Yeoman generator for AngularJS with GulpJS](https://www.npmjs.com/package/generator-gulp-angular)
* [HTML-Vorlage: HTML5 Boilerplate](https://github.com/h5bp/html5-boilerplate)
* [AngularJS: Getting Started](https://docs.angularjs.org/misc/started)
* [AngularJS: gutes einfaches Beispiel](https://github.com/tastejs/todomvc/tree/master/examples/angularjs)
