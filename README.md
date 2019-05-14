# lunchbox-backend-spring-kotlin

Backend für Lunchbox.


## TODOs

- MockK einführen -> https://medium.com/@darych90/kotlin-spring-boot-mockk-6d1c1a6463ac
- MockMVC in Kotlin-Syntax nutzen -> https://docs.spring.io/spring/docs/5.2.0.M1/spring-framework-reference/languages.html#mockmvc-dsl
- LunchResolver: Scala-Implementierung übernehmen/"transponieren"
- LunchOfferUpdate: LunchResolver als Spring-Component umschreiben ?
  - https://stackoverflow.com/questions/28069086/spring-dynamic-qualifier/28082689#28082689
  - https://stackoverflow.com/questions/42909283/how-to-dynamically-inject-a-service-using-a-runtime-qualifier-variable/42985496#42985496
- LunchOfferUpdate: Unit-Tests schreiben
- Atom-Feed implementieren
  - https://spring.io/guides/tutorials/spring-boot-kotlin/
  - https://github.com/wonwoo/spring-boot-kotlin-example
- API: bei 4xx-/5xx-Exceptions eine message mitliefern
- API test: validate against JSON schema
- wenn Lunchbox-Backend fertig, testweise umschreiben mit Webflux, Kotlin DSL & Coroutines:
  - Gradle-Skripte auf Kotlin umstellen
    -> https://github.com/jnizet/gradle-kotlin-dsl-migration-guide
    -> https://github.com/mixitconf/mixit/blob/master/build.gradle.kts
  - MongoDB nutzen
    -> https://github.com/mixitconf/mixit/blob/master/src/main/kotlin/mixit/model/Event.kt
    -> https://github.com/mixitconf/mixit/blob/master/src/main/kotlin/mixit/repository/EventRepository.kt
    -> https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-nosql.html#boot-features-mongodb
  - Webflux & Coroutines verwenden
    -> https://www.baeldung.com/kotlin-coroutines
    -> https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow#spring-mvc-or-webflux
    -> https://docs.spring.io/spring/docs/5.2.0.M1/spring-framework-reference/languages.html#coroutines


## Wissen

- [Vortrag zu Kotlin-Support in Spring](https://www.infoq.com/presentations/spring-kotlin-boot)
- [Beispiele für Spring+Kotlin](https://github.com/sdeleuze/spring-kotlin-deepdive/tree/step3-coroutine)
- [simples Beispiel](https://github.com/sdeleuze/spring-boot-kotlin-demo)
