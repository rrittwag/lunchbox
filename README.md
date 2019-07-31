# lunchbox-backend-spring-kotlin

Backend für Lunchbox.


## TODOs

- LunchResolver: Scala-Implementierung übernehmen/"transponieren"
- LunchOfferUpdate: Unit-Tests schreiben
- Atom-Feed implementieren [1](https://spring.io/guides/tutorials/spring-boot-kotlin/) [2](https://github.com/wonwoo/spring-boot-kotlin-example)
- PDFBox aktualisieren
- Feature: an Feiertagen keine Mittagsangebote
- API: bei 4xx-/5xx-Exceptions eine message mitliefern
- Integration-Test schreiben
- MockMVC in Kotlin-Syntax nutzen ([mit Spring 5.2](https://docs.spring.io/spring/docs/5.2.0.M1/spring-framework-reference/languages.html#mockmvc-dsl))
- wenn Lunchbox-Backend fertig, testweise umschreiben mit Webflux, Kotlin DSL & Coroutines:
  - Gradle-Skripte auf Kotlin umstellen [1](https://github.com/jnizet/gradle-kotlin-dsl-migration-guide) [2](https://github.com/mixitconf/mixit/blob/master/build.gradle.kts)
  - MongoDB nutzen [1](https://github.com/mixitconf/mixit/blob/master/src/main/kotlin/mixit/model/Event.kt) [2](https://github.com/mixitconf/mixit/blob/master/src/main/kotlin/mixit/repository/EventRepository.kt) [3](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-nosql.html#boot-features-mongodb)
  - Webflux & Coroutines verwenden [1](https://www.baeldung.com/kotlin-coroutines) [2](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow#spring-mvc-or-webflux) [3](https://docs.spring.io/spring/docs/5.2.0.M1/spring-framework-reference/languages.html#coroutines)


## Wissen

- [Vortrag zu Kotlin-Support in Spring](https://www.infoq.com/presentations/spring-kotlin-boot)
- [Beispiele für Spring+Kotlin](https://github.com/sdeleuze/spring-kotlin-deepdive/tree/step3-coroutine)
- [simples Beispiel](https://github.com/sdeleuze/spring-boot-kotlin-demo)
