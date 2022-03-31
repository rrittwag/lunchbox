# Lunchbox Web-Frontend mit Vue.js & TypeScript

Dieses Sub-Projekt beschreibt die Implementierung eines Lunchbox Web-Clients. Die Umsetzung erfolgt mit [Vue.js](https://vuejs.org/), [TypeScript](https://www.typescriptlang.org/) und [Tailwind CSS](https://tailwindcss.com/).



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

- a11y Navigation
  - [ ] Farbkontrastregeln für non-active NavLinks einhalten
  - [ ] am Anfang Link "Zum Inhalt springen" + onVRoute Fokus auf Seitenanfang setzen
  - [ ] https://marcus.io/blog/improved-accessible-routing-vuejs
  - [ ] https://codesandbox.io/s/improved-accessible-routing-vue-correct-recommendation-better-aria-current-cxw8w
  - [ ] https://github.com/tailwindcss/tailwindcss/releases/tag/v1.1.0#added-utilities-for-screenreader-visibility
  - [ ] https://v3.vuejs.org/guide/a11y-basics.html#
- a11y Mittagsangebote
  - [ ] Tag-/Angebotswechsel via aria-live anouncen
  - [ ] Versteckte Offer-Details mit Screen Reader wiedergeben
  - [ ] Versteckte Offer-Details tastaturfreundlich gestalten, z.B. Button zum Aufklappen
- Mobile
  - [ ] [Mittagsangebote swipen](https://github.com/vueuse/vueuse/blob/main/packages/core/useMouse/index.ts) / [Link2](https://vuejsexamples.com/tag/swipe)
- Design
  - [ ] neues Logo designen
  - [ ] Farb-Themes basteln (rot/grün/blau/navy/rosa/neon) + Dark Themes
  - [x] Fonts lokal laden (aufgrund von Performance und aus [Datenschutzgründen](https://www.golem.de/news/landgericht-muenchen-einbindung-von-google-fonts-ist-rechtswidrig-2202-162826.html)
  - [ ] OfferBoxes bei "Überlauf" stapeln, z.B. 6 Anbieter auf 4 oder weniger Spalten verteilen
    - https://github.com/shershen08/vue-masonry
    - https://tobiasahlin.com/blog/common-flexbox-patterns/#masonry-or-mosaic
    - https://w3bits.com/css-grid-masonry/
    - https://css-tricks.com/piecing-together-approaches-for-a-css-masonry-layout/
- Beschreibung für Mittagsanbieter
  - [ ] in OfferBox anzeigen, optional: auch wenn keine Mittagsangebote vorhanden
  - [ ] kann Links wiedergeben
  - [ ] Gilt sie als "Detail"? Verstecken in Mobile-Variante?
- Empty States gestalten
  - [ ] Anzeige für keine Angebote -> schöne Grafik "Heute zu Hause kochen"
  - [ ] ContentLoading: Lade-Status als Skeleton oder [Spinner](https://scotch.io/tutorials/add-loading-indicators-to-your-vuejs-application) darstellen
  - [ ] ContentError: schöne Grafik bei Fehler, z.B.
    - https://wall.alphacoders.com/big.php?i=507607&lang=German
    - https://www.vectorstock.com/royalty-free-vector/crying-little-girl-flat-cartoon-portrait-emoji-vector-13712121
- PWA
  - [ ] Mittagsangebote im LocalStorage halten und bei Neustart die Ladezeit überbrücken
    - https://github.com/robinvdvleuten/vuex-persistedstate
  - [ ] Offline-Anzeige der Mittagsangebote
- About-View
  - [ ] About-View basteln
    - Info: [Alexa Skill von Falko P.](https://www.amazon.de/s/ref=nb_sb_noss_2?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&url=search-alias%3Dalexa-skills&field-keywords=lunchbox) in Infoseite aufnehmen
- Sonstiges
  - [ ] System-Tests: Selenium-Tests schreiben
  - [ ] i18n & l10n
  - [ ] [HTML Boilerplate übernehmen](https://www.matuzo.at/blog/html-boilerplate/)
  - [ ] [App-Links in Meta-Info aufnehmen](http://ricostacruz.com/cheatsheets/applinks.html)
  - [ ] Dokumentation & Test von Komponenten: [Storybook](https://github.com/vuesion/vuesion/tree/master/src/app/shared/components/VueButton)
  - [ ] [Lighthouse-Performance-Index testen](https://blog.checklyhq.com/how-we-got-a-100-lighthouse-performance-score-for-our-vue-js-app/)



## Wissen

- Initiales Projekt erstellt mit [Vue CLI](https://cli.vuejs.org/).
- Die zentrale Datenverwaltung übernimmt [Vuex](https://vuex.vuejs.org/guide/).
- Das UI-Design unterstützt [Tailwind CSS](https://tailwindcss.com/).
- Die Definition von Components und deren Eigenschaften (z.B. Props) erleichtern [Decorators/Annotationen](https://github.com/kaorun343/vue-property-decorator). Ebenso [im Vuex-Store](https://github.com/championswimmer/vuex-module-decorators).
