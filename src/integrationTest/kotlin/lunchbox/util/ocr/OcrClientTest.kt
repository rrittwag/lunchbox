package lunchbox.util.ocr /* ktlint-disable max-line-length no-wildcard-imports */

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.net.URL

@Testcontainers
class OcrClientTest {

  companion object {
    // start open-ocr-2 via Docker Compose (via TestContainers)
    @Container
    private val ocrContainer =
      KtDockerComposeContainer(File("src/integrationTest/resources/docker-compose.openocr.yml"))
        .withExposedService("openocr_1", OCR_SERVER_PORT)

    private fun ocrServerUrl(): String {
      val serverHost = ocrContainer.getServiceHost("openocr_1", OCR_SERVER_PORT)
      val serverPort = ocrContainer.getServicePort("openocr_1", OCR_SERVER_PORT)
      return "http://$serverHost:$serverPort"
    }
  }

  @ParameterizedTest
  @ValueSource(strings = [
    "menus/feldkueche/ocr/feldkueche_2016-10-10.jpg",
    "menus/feldkueche/ocr/feldkueche_2019-09-02.jpg"
  ])
  fun `compare jpg OCR to saved OCR text`(file: String) {
    val ocrText = OcrClient().doOCR(
      URL("http://test-resources/$file"),
      ocrServerUrl()
    )
    ocrText shouldEqual File("src/test/resources/$file.txt").readText()
  }

  @Test
  @Disabled
  fun `generate and save OCR text`() {
    val file = "menus/feldkueche/ocr/feldkueche_2016-10-10.jpg"
    val ocrText = OcrClient().doOCR(
      URL("http://test-resources/$file"),
      ocrServerUrl()
    )
    File("src/test/resources/$file.txt").apply {
      createNewFile()
      writeText(ocrText)
    }
  }
}

// BUGFIX: Kotlin does not support SELF types
// -> https://github.com/testcontainers/testcontainers-java/issues/1010
class KtDockerComposeContainer(file: File) :
  DockerComposeContainer<KtDockerComposeContainer>(file)
