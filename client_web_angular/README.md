Lunchbox Web-Client mit AngularJS und Bootstrap
===============================================

Dieses Sub-Projekt beschreibt die Implementierung eines Lunchbox Web-Clients. Die Umsetzung erfolgt mit [AngularJS](https://angularjs.org) und [Bootstrap](http://getbootstrap.com).



Status
------

Alpha-Version



Build & Benutzung
-----------------

* Node.js, Gulp & Bower müssen installiert sein - siehe [Homepage zu generator-gulp-angular](https://www.npmjs.com/package/generator-gulp-angular)
* `gulp serve` deployt das Web-App auf einem internen Web-Server.
* Der Chrome-Browser startet automatisch und lädt das Web-App unter der Adresse [http://localhost:3000/](http://localhost:3000/).



Distribution
------------

* `gulp build` generiert distributierbare Dateien im Verzeichnis `dist`.



TODOs
-----

* Mittagsangebote visualisieren (responsive!)
* Mittagsangebote des aktuellen Tages via REST abrufen
* NavBar mit About versehen: Infos zu Projekt, Entwickler & Mitarbeit
* Lunchbox-Icon bereitstellen für NavBar & Favicon
* Feed verlinken + in Meta-Infos aufnehmen
* Projekt mit HTML5 Boilerplate abgleichen
* durch Tage klicken
* Filter für Location in NavBar einbinden
* durch Tage "touchen"
* schöneres Bootstrap-Theme einsetzen ?



Wissen
------

* [AngularJS-Vorlage: Yeoman generator for AngularJS with GulpJS](https://www.npmjs.com/package/generator-gulp-angular)
* [HTML-Vorlage: HTML5 Boilerplate](https://github.com/h5bp/html5-boilerplate)
* [AngularJS: gutes einfaches Beispiel](https://github.com/tastejs/todomvc/tree/master/examples/angularjs)
