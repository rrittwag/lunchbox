@file:Suppress("ktlint:standard:max-line-length")

package lunchbox.util.ocr

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.nginx.NginxContainer
import java.io.File
import java.nio.file.Paths

/**
 * Der Test ist unsinnig. Er demonstriert nur die Funktionsweise von TestContainers' nginx Modul.
 */
@Testcontainers
class NginxTest {
  companion object {
    // provide test resources in Docker-ed nginx (via TestContainers)
    @Container
    private val resourcesContainer =
      NginxContainer("nginx:alpine")
        .withFileSystemBind(Paths.get("src/test/resources").toString(), "/usr/share/nginx/html", BindMode.READ_ONLY)
        .waitingFor(HttpWaitStrategy())

    private fun resourcesHost() = resourcesContainer.getBaseUrl("http", 80)
  }

  @Test
  fun `serve test resource files`() {
    val resourceFile = "menus/feldkueche/ocr/2016-10-10.jpg.txt"

    val httpResult =
      RestClient
        .create("${resourcesHost()}/$resourceFile")
        .get()
        .retrieve()
        .body<String>() ?: ""

    httpResult shouldBeEqualTo File("src/test/resources/$resourceFile").readText()
  }
}
