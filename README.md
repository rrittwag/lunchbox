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

- Design
    - in Figma übertragen
    - Display-Font auswählen
    - Font Display 1 bis 4 responsiv anwenden
    - dunklblau/navy Theme
    - Dark Theme (warm+kalt)
    - Farben für Badges & Card Background definieren
    - neues Logo designen
- Settings-View basteln
    - Location wechseln
    - Beschreibung immer einblenden
    - Badges immer einblenden
    - Theme auswählen
    - Mittagsanbieter sortierbar/ausblendbar
    - Nutzerbezogenes Sortieren & Filtern der Mittagsanbieter
- Komponenten basteln
    - [Tailwind UI](https://tailwindui.com/components)
    - https://github.com/bootstrap-vue/bootstrap-vue
    - https://github.com/vuejs/ui/blob/master/src/components/VueButton.vue
    - https://github.com/coreui/coreui-vue
    - https://tailwindcomponents.com/
    - https://github.com/DivanteLtd/vue-storefront/tree/master/src/themes/default/components
    - https://github.com/creativetimofficial/tailwind-starter-kit
    - https://vue-tailwind.com/components/table.html
    - https://proton.efelle.co/#/documentation/components/sortable
    - https://github.com/knipferrc/tails-ui/tree/master/src/components
    - https://egghead.io/instructors/adam-wathan
    - https://polished-sunset-0ex44e5cb9xt.tailwindcss.com/components/198272c2-e9b3-46e5-99da-d0911ad1e07c
    - https://tailwindui.com/page-examples/detail-view-01#
    - https://github.com/moesaid/cleopatra
  Empty States
    - Anzeige für keine Angebote
    - Lade-Status als [Spinner](https://scotch.io/tutorials/add-loading-indicators-to-your-vuejs-application) darstellen
    - Meldung bei fehlerhaftem Laden
- PWA
    - Mittagsangebote im LocalStorage halten und bei Neustart die Ladezeit überbrücken
        - https://github.com/robinvdvleuten/vuex-persistedstate
    - Offline-Anzeige der Mittagsangebote
- About-View basteln
    - Info: [Alexa Skill von Falko P.](https://www.amazon.de/s/ref=nb_sb_noss_2?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&url=search-alias%3Dalexa-skills&field-keywords=lunchbox) in Infoseite aufnehmen
- Unit-Tests schreiben (siehe angular-Frontend)
- Direkte Anwahl einer Sub-Adresse (z.B. 'http://localhost/about') ermöglichen.
- Projekt mit HTML5 Boilerplate abgleichen
- durch Tage "swipen": auf Smartphone nach links und nach rechts ziehen
- System-Tests bereitstellen, via Selenium?
- i18n & l10n
- [App-Links in Meta-Info aufnehmen](http://ricostacruz.com/cheatsheets/applinks.html)
- [Komponenten mit Storybook dokumentieren](https://github.com/vuesion/vuesion/tree/master/src/app/shared/components/VueButton)
- [Store JavaScript-ish implementieren?](https://github.com/vuesion/vuesion/tree/master/src/app/app)



## Wissen

- Initiales Projekt erstellt mit [Vue CLI](https://cli.vuejs.org/).
- Die zentrale Datenverwaltung übernimmt [Vuex](https://vuex.vuejs.org/guide/).
- Das UI-Design unterstützt [Tailwind CSS](https://tailwindcss.com/).
- Die Definition von Components und deren Eigenschaften (z.B. Props) erleichtern [Decorators/Annotationen](https://github.com/kaorun343/vue-property-decorator). Ebenso [im Vuex-Store](https://github.com/championswimmer/vuex-module-decorators).
