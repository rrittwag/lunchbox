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

- browserslist aktualisieren
    - https://dev.to/amplifr/outdated-browser-detection-with-browserslist-10co
    - https://github.com/browserslist/browserslist/blob/master/README.md#debug
- a11y Navigation
    - Farbkontrastregeln für non-active NavLinks einhalten
    - am Anfang Link "Zum Inhalt springen" + onVRoute Fokus auf Seitenanfang setzen
    - https://marcus.io/blog/improved-accessible-routing-vuejs
    - https://codesandbox.io/s/improved-accessible-routing-vue-correct-recommendation-better-aria-current-cxw8w
    - https://github.com/tailwindcss/tailwindcss/releases/tag/v1.1.0#added-utilities-for-screenreader-visibility
- a11y Mittagsangebote
    - Tag-/Angebotswechsel via aria-live anouncen
    - Versteckte Offer-Details mit Screen Reader wiedergeben
    - Versteckte Offer-Details tastaturfreundlich gestalten, z.B. Button zum Aufklappen
- Design
    - Dark Themes für warm+grün+kalt realisieren
    - [semantische Farben?](https://twitter.com/adamwathan/status/1256657059770257410)
    - neues Logo designen
    - weitere Themes? navy/rosa/neon Theme
- Settings-View basteln
    - GUI-Inspiration [1](https://tailwindui.com/components) + [2](https://tailwindui.com/components/marketing/sections/faq-sections)
    - LunchLocation wechseln
    - Details immer einblenden
    - Mittagsanbieter sortieren & ausblenden
    - Theme auswählen
    - Dark Mode: ja/nein/System
    - Transitions on/off
- Komponenten basteln
    - [Tailwind UI](https://tailwindui.com/components)
    - https://github.com/bootstrap-vue/bootstrap-vue
    - https://github.com/vuejs/ui/blob/master/src/components/VueButton.vue
    - https://github.com/coreui/coreui-vue
    - https://quatrochan.github.io/Equal/
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
- Empty States gestalten
    - Anzeige für keine Angebote
    - ContentLoading: Lade-Status als [Spinner](https://scotch.io/tutorials/add-loading-indicators-to-your-vuejs-application) darstellen
    - ContentError: Meldung bei fehlerhaftem Laden
- PWA
    - Mittagsangebote im LocalStorage halten und bei Neustart die Ladezeit überbrücken
        - https://github.com/robinvdvleuten/vuex-persistedstate
    - Offline-Anzeige der Mittagsangebote
- About-View basteln
    - Info: [Alexa Skill von Falko P.](https://www.amazon.de/s/ref=nb_sb_noss_2?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&url=search-alias%3Dalexa-skills&field-keywords=lunchbox) in Infoseite aufnehmen
- System-Tests: Selenium-Tests schreiben
- i18n & l10n
- [App-Links in Meta-Info aufnehmen](http://ricostacruz.com/cheatsheets/applinks.html)
- Dokumentation & Test von Komponenten: [Storybook](https://github.com/vuesion/vuesion/tree/master/src/app/shared/components/VueButton)
- VueX-Store: entfernen oder [JavaScript-ish implementieren?](https://github.com/vuesion/vuesion/tree/master/src/app/app)



## Wissen

- Initiales Projekt erstellt mit [Vue CLI](https://cli.vuejs.org/).
- Die zentrale Datenverwaltung übernimmt [Vuex](https://vuex.vuejs.org/guide/).
- Das UI-Design unterstützt [Tailwind CSS](https://tailwindcss.com/).
- Die Definition von Components und deren Eigenschaften (z.B. Props) erleichtern [Decorators/Annotationen](https://github.com/kaorun343/vue-property-decorator). Ebenso [im Vuex-Store](https://github.com/championswimmer/vuex-module-decorators).
