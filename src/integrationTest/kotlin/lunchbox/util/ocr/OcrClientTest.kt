package lunchbox.util.ocr /* ktlint-disable max-line-length no-wildcard-imports */

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.net.URL

@Testcontainers
class OcrClientTest {

  companion object {
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

  @Test
  fun `compare jpg OCR to saved OCR text`() {
    val file = "menus/feldkueche/ocr/feldkueche_2016-10-10"
    val ocrText = OcrClient().doOCR(
      URL("http://test-resources/$file.jpg"),
      ocrServerUrl()
    )
    ocrText shouldEqual File("src/test/resources/${file}_ocr.txt").readText()
  }

  @Test
  @Disabled
  fun `generate and save OCR text`() {
    val file = "menus/feldkueche/ocr/feldkueche_2016-10-10"
    val ocrText = OcrClient().doOCR(
      URL("http://test-resources/$file.jpg"),
      ocrServerUrl()
    )
    File("src/test/resources/${file}_ocr.txt").apply {
      createNewFile()
      writeText(ocrText)
    }
  }
}

// BUGFIX: Kotlin does not support SELF types
// -> https://github.com/testcontainers/testcontainers-java/issues/1010
class KtDockerComposeContainer(file: File) :
  DockerComposeContainer<KtDockerComposeContainer>(file)
