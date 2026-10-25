package lunchbox.util.ocr

import lunchbox.util.container.KtGenericContainer
import lunchbox.util.url.UrlUtil.url
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.testcontainers.containers.Network
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.MountableFile
import java.io.File

@Testcontainers
class OcrClientTest {
  companion object {
    private val network = Network.newNetwork()

    @Container
    private val resourcesContainer =
      KtGenericContainer("nginxinc/nginx-unprivileged:alpine")
        .withNetwork(network)
        .withNetworkAliases("test-resources")
        .withCopyFileToContainer(MountableFile.forHostPath("src/test/resources"), "/usr/share/nginx/html")

    @Container
    private val ocrContainer =
      KtGenericContainer("container.rori.dev/lunchbox/ocr")
        .withNetwork(network)
        .withExposedPorts(OCR_PORT)

    private fun ocrServerUrl(): String = "http://${ocrContainer.host}:${ocrContainer.getMappedPort(OCR_PORT)}"
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "menus/feldkueche/ocr/2016-10-10.jpg",
      "menus/feldkueche/ocr/2019-09-02.jpg",
    ],
  )
  fun `compare jpg OCR to saved OCR text`(file: String) {
    val ocrText =
      OcrClient(ocrServerUrl())
        .doOCR(url("http://test-resources:8080/$file"))

    ocrText shouldBeEqualTo File("src/test/resources/$file.txt").readText()
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "menus/torney/ocr/Tagesgericht-13.KW-2025.png",
    ],
  )
  @Disabled
  fun `generate and save OCR text`(file: String) {
    val ocrText =
      OcrClient(ocrServerUrl())
        .doOCR(url("http://test-resources:8080/$file"))

    File("src/test/resources/$file.txt").apply {
      createNewFile()
      writeText(ocrText)
    }
  }
}
