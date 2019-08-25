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

- Anzeige für keine Angebote
- Lade-Status als [Spinner](https://scotch.io/tutorials/add-loading-indicators-to-your-vuejs-application) darstellen
- Meldung bei fehlerhaftem Laden
- Mittagsangebote im LocalStorage halten und bei Neustart die Ladezeit überbrücken
    - https://github.com/robinvdvleuten/vuex-persistedstate
- About-View basteln
- Settings-View basteln
- Design: Umstellen auf TailwindCSS
- Responsive Design: Ausdehnung auf ca. 1800px Breite beschränken
- Design: Abstände der Provider-Boxen optimieren
- build: Bootstrap-Importe minimieren (Unterschied?)
- Unit-Tests schreiben (siehe angular-Frontend)
- Info: [Alexa Skill von Falko P.](https://www.amazon.de/s/ref=nb_sb_noss_2?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&url=search-alias%3Dalexa-skills&field-keywords=lunchbox) in Infoseite aufnehmen
- Direkte Anwahl einer Sub-Adresse (z.B. 'http://localhost/about') ermöglichen.
- als PWA realisieren -> Offline-Anzeige der Mittagsangebote
- Design: neues Lunchbox-Logo designen
- Model: Daten neu laden, wenn über 1 Tag alt
- Projekt mit HTML5 Boilerplate abgleichen
- durch Tage "swipen": auf Smartphone nach links und nach rechts ziehen
- Tags/Badges für "vegetarisch" & "vegan"
- Nutzerbezogenes Sortieren & Filtern der Mittagsanbieter
- System-Tests bereitstellen, via Selenium?
- i18n & l10n
- [App-Links in Meta-Info aufnehmen](http://ricostacruz.com/cheatsheets/applinks.html)
- [Komponenten mit Storybook dokumentieren](https://github.com/vuesion/vuesion/tree/master/src/app/shared/components/VueButton)
- [Store JavaScript-ish implementieren?](https://github.com/vuesion/vuesion/tree/master/src/app/app)



## Wissen

- Initiales Projekt erstellt mit [Vue CLI](https://cli.vuejs.org/).
- Die zentrale Datenverwaltung übernimmt [Vuex](https://vuex.vuejs.org/guide/).
- UI-Komponenten und Design implementiert [bootstrap-vue](https://bootstrap-vue.js.org/).
- Die Definition von Components und deren Eigenschaften (z.B. Props) erleichtern [Decorators/Annotationen](https://github.com/kaorun343/vue-property-decorator). Ebenso [im Vuex-Store](https://github.com/championswimmer/vuex-module-decorators).
