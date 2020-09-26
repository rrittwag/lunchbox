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
    - Farbkontrastregeln für non-active NavLinks einhalten
    - am Anfang Link "Zum Inhalt springen" + onVRoute Fokus auf Seitenanfang setzen
    - https://marcus.io/blog/improved-accessible-routing-vuejs
    - https://codesandbox.io/s/improved-accessible-routing-vue-correct-recommendation-better-aria-current-cxw8w
    - https://github.com/tailwindcss/tailwindcss/releases/tag/v1.1.0#added-utilities-for-screenreader-visibility
    - https://v3.vuejs.org/guide/a11y-basics.html#
- a11y Mittagsangebote
    - Tag-/Angebotswechsel via aria-live anouncen
    - Versteckte Offer-Details mit Screen Reader wiedergeben
    - Versteckte Offer-Details tastaturfreundlich gestalten, z.B. Button zum Aufklappen
- Design
    - Regeln für Dark Themes aufstellen [Link 1](https://css-tricks.com/a-complete-guide-to-dark-mode-on-the-web/), [Link 2](https://stackoverflow.blog/2020/03/31/building-dark-mode-on-stack-overflow/)
    - Dark Themes für warm+grün+kalt realisieren
    - [semantische Farben?](https://twitter.com/adamwathan/status/1256657059770257410)
    - neues Logo designen
    - weitere Themes? navy/rosa/neon Theme, [Inspirationen](https://colorsinspo.com/)
    - OfferBoxes bei "Überlauf" stapeln, z.B. 6 Anbieter auf 4 oder weniger Spalten verteilen
        - https://tobiasahlin.com/blog/common-flexbox-patterns/#masonry-or-mosaic
        - https://w3bits.com/css-grid-masonry/
        - https://css-tricks.com/piecing-together-approaches-for-a-css-masonry-layout/
- Komponenten basteln
    - [Tailwind UI](https://tailwindui.com/components)
    - https://mertjf.github.io/tailblocks/
    - https://github.com/bootstrap-vue/bootstrap-vue
    - https://github.com/vuejs/ui/blob/master/src/components/VueButton.vue
    - https://github.com/coreui/coreui-vue
    - https://quatrochan.github.io/Equal/
    - https://tailwindcomponents.com/
    - https://stegosource.com/vuetensils-0-6-simpler-forms-better-accessibility-useful-filters/
    - https://vue.chakra-ui.com/
    - https://github.com/DivanteLtd/vue-storefront/tree/master/src/themes/default/components
    - https://github.com/creativetimofficial/tailwind-starter-kit
    - https://vue-tailwind.com/components/table.html
    - https://proton.efelle.co/#/documentation/components/sortable
    - https://github.com/knipferrc/tails-ui/tree/master/src/components
    - https://egghead.io/instructors/adam-wathan
    - https://polished-sunset-0ex44e5cb9xt.tailwindcss.com/components/198272c2-e9b3-46e5-99da-d0911ad1e07c
    - https://tailwindui.com/page-examples/detail-view-01#
    - https://github.com/moesaid/cleopatra
    - https://shoelace.style/
    - https://ui.black-kro.dev/themes
- Beschreibung für Mittagsanbieter
    - in OfferBox anzeigen, optional: auch wenn keine Mittagsangebote vorhanden
    - kann Links wiedergeben
    - Gilt sie als "Detail"? Verstecken in Mobile-Variante?
- Empty States gestalten
    - Anzeige für keine Angebote -> schöne Grafik "Heute zu Hause kochen"
    - ContentLoading: Lade-Status als Skeleton oder [Spinner](https://scotch.io/tutorials/add-loading-indicators-to-your-vuejs-application) darstellen
    - ContentError: schöne Grafik bei Fehler, z.B.
        - https://wall.alphacoders.com/big.php?i=507607&lang=German
        - https://www.vectorstock.com/royalty-free-vector/crying-little-girl-flat-cartoon-portrait-emoji-vector-13712121
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
- VueX-Store
    - entfernen?
    - [ohne VueX realisieren?](https://vueschool.io/articles/vuejs-tutorials/state-management-with-composition-api/)
    - [JavaScript-ish implementieren?](https://github.com/vuesion/vuesion/tree/master/src/app/app)
- [Lighthouse-Performance-Index erhöhen](https://blog.checklyhq.com/how-we-got-a-100-lighthouse-performance-score-for-our-vue-js-app/)



## Wissen

- Initiales Projekt erstellt mit [Vue CLI](https://cli.vuejs.org/).
- Die zentrale Datenverwaltung übernimmt [Vuex](https://vuex.vuejs.org/guide/).
- Das UI-Design unterstützt [Tailwind CSS](https://tailwindcss.com/).
- Die Definition von Components und deren Eigenschaften (z.B. Props) erleichtern [Decorators/Annotationen](https://github.com/kaorun343/vue-property-decorator). Ebenso [im Vuex-Store](https://github.com/championswimmer/vuex-module-decorators).
