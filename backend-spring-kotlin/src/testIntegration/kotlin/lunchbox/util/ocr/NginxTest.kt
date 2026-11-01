@file:Suppress("ktlint:standard:max-line-length")

package lunchbox.util.ocr

import lunchbox.util.container.KtGenericContainer
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import org.springframework.web.client.toEntity
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.MountableFile
import java.io.File

/**
 * Der Test ist unsinnig. Er demonstriert nur die Funktionsweise von TestContainers' nginx Modul.
 */
@Testcontainers
class NginxTest {
  companion object {
    private val network = Network.newNetwork()

    // provide test resources in Docker-ed nginx (via TestContainers)
    @Container
    private val resourcesContainer =
      KtGenericContainer("nginxinc/nginx-unprivileged:alpine")
        .withNetwork(network)
        .withCopyFileToContainer(MountableFile.forHostPath("src/test/resources"), "/usr/share/nginx/html")
        .withExposedPorts(8080)
        .waitingFor(Wait.forHttp("/").forPort(8080))

    private fun resourcesHost() = "http://${resourcesContainer.host}:${resourcesContainer.getMappedPort(8080)}"
  }

  @Test
  fun `serve test resource files`() {
    val resourceFile = "menus/feldkueche/ocr/2016-10-10.jpg.txt"

    val httpResult =
      RestClient
        .create("${resourcesHost()}/$resourceFile")
        .get()
        .retrieve()
        .body<ByteArray>() ?: byteArrayOf()

    String(httpResult) shouldBeEqualTo File("src/test/resources/$resourceFile").readText()
  }
}
