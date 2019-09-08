package lunchbox.util.ocr /* ktlint-disable max-line-length no-wildcard-imports */

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.testcontainers.containers.NginxContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.nio.file.Paths
import java.time.Duration

/**
 * Der Test ist unsinnig. Er demonstriert nur die Funktionsweise von TestContainers' nginx Modul.
 */
@Testcontainers
class NginxTest {

  @Container
  private val resourcesContainer =
    KtNginxContainer()
      .withCustomContent(Paths.get("src/test/resources").toString())
      .waitingFor(HttpWaitStrategy())

  @Test
  fun success() {
    val resourcesHost = resourcesContainer.getBaseUrl("http", 80)
    val resourceFile = "menus/feldkueche/ocr/feldkueche_2016-10-10_ocr.txt"

    val httpResult =
      WebClient.create("$resourcesHost/$resourceFile")
        .get()
        .retrieve()
        .bodyToMono<String>()
        .block(Duration.ofSeconds(5)) ?: ""

    httpResult shouldEqual File("src/test/resources/$resourceFile").readText()
  }
}

class KtNginxContainer : NginxContainer<KtNginxContainer>()
