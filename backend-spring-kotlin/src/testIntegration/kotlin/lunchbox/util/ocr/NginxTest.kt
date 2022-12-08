package lunchbox.util.ocr /* ktlint-disable max-line-length */

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.testcontainers.containers.BindMode
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

  companion object {
    // provide test resources in Docker-ed nginx (via TestContainers)
    @Container
    private val resourcesContainer =
      KtNginxContainer()
        .withFileSystemBind(Paths.get("src/test/resources").toString(), "/usr/share/nginx/html", BindMode.READ_ONLY)
        .waitingFor(HttpWaitStrategy())

    private fun resourcesHost() = resourcesContainer.getBaseUrl("http", 80)
  }

  @Test
  fun `serve test resource files`() {
    val resourceFile = "menus/feldkueche/ocr/2016-10-10.jpg.txt"

    val httpResult =
      WebClient.create("${resourcesHost()}/$resourceFile")
        .get()
        .retrieve()
        .bodyToMono<String>()
        .block(Duration.ofSeconds(5)) ?: ""

    httpResult shouldBeEqualTo File("src/test/resources/$resourceFile").readText()
  }
}

// BUGFIX: Kotlin does not support SELF types
// -> https://github.com/testcontainers/testcontainers-java/issues/1010
class KtNginxContainer : NginxContainer<KtNginxContainer>("nginx:alpine")
