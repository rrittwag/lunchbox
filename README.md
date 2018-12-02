# Lunchbox Web-Frontend mit Vue.js & TypeScript

Dieses Sub-Projekt beschreibt die Implementierung eines Lunchbox Web-Clients. Die Umsetzung erfolgt mit [Vue.js](https://vuejs.org/), [TypeScript](https://www.typescriptlang.org/) und [Bootstrap](https://getbootstrap.com/).



## Status

- Erfüllt Lunchbox Version 1 vollständig.
- Das Web-App bietet zudem eine Infoseite für das Lunchbox-Projekt.



## Build & Benutzung

- [Node.js installieren](https://nodejs.org/en/download/package-manager/)
- [yarn installieren](https://yarnpkg.com/lang/en/docs/install)
- ins Projektverzeichnis stellen und Abhängigkeiten aktualisieren via `yarn install`
- `yarn start` deployt das Web-App auf einem internen Web-Server. Die Web-App ist unter der Adresse [http://localhost:3000/](http://localhost:3000/) erreichbar. Quellcode-Änderungen werden auto-deployt.



## Distribution

- `yarn build` generiert distributierbare Dateien im Verzeichnis `dist`.



## TODOs

- Funktionalitäten aus angular-Frontend übernehmen (z.B. Preisangaben in LunchOffers, DaySelector, LunchOffer-Filterung, Message-Box, About-View, Settings-View, Responsive Design)
- Unit-Tests schreiben (siehe angular-Frontend)
- Info: [Alexa Skill von Falko P.](https://www.amazon.de/s/ref=nb_sb_noss_2?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&url=search-alias%3Dalexa-skills&field-keywords=lunchbox) in Infoseite aufnehmen
- PWA-tauglich machen
- Design: Lunchbox-Logo für Jumbotron bereitstellen
- Model: Daten neu laden, wenn über 1 Tag alt
- Projekt mit HTML5 Boilerplate abgleichen
- Adress-Query-Parameter "day": gezielt die Angebote für einen Tag abfragen
- in Feed Links zur Website setzen
- System-Tests bereitstellen, via Selenium?
- durch Tage "swipen": auf Smartphone nach links und nach rechts ziehen
- i18n & l10n
- Direkte Anwahl einer Sub-Adresse (z.B. 'http://localhost/about') ermöglichen.
- [App-Links in Meta-Info aufnehmen](http://ricostacruz.com/cheatsheets/applinks.html)



## Wissen

- Initiales Projekt erstellt mit [Vue CLI](https://cli.vuejs.org/).
- Die zentrale Datenverwaltung übernimmt [Vuex](https://vuex.vuejs.org/guide/).
- UI-Komponenten und Design implementiert [bootstrap-vue](https://bootstrap-vue.js.org/).
