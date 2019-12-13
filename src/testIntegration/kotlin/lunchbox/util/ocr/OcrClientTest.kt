package lunchbox.util.ocr /* ktlint-disable max-line-length */

import java.io.File
import java.net.URL
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class OcrClientTest {

  companion object {
    // start open-ocr-2 via Docker Compose (via TestContainers)
    @Container
    private val ocrContainer =
      KtDockerComposeContainer(File("src/testIntegration/resources/docker-compose.openocr.yml"))
        .withExposedService("openocr_1", OCR_PORT)

    private fun ocrServerUrl(): String {
      val host = ocrContainer.getServiceHost("openocr_1", OCR_PORT)
      val port = ocrContainer.getServicePort("openocr_1", OCR_PORT)
      return "http://$host:$port"
    }
  }

  @ParameterizedTest
  @ValueSource(strings = [
    "menus/feldkueche/ocr/2016-10-10.jpg",
    "menus/feldkueche/ocr/2019-09-02.jpg"
  ])
  fun `compare jpg OCR to saved OCR text`(file: String) {
    val ocrText =
      OcrClient(ocrServerUrl())
        .doOCR(URL("http://test-resources/$file"))

    ocrText shouldEqual File("src/test/resources/$file.txt").readText()
  }

  @ParameterizedTest
  @ValueSource(strings = [
    "menus/gesundheitszentrum/ocr/2019-08-05.jpg",
    "menus/gesundheitszentrum/ocr/2019-09-09.jpg",
    "menus/gesundheitszentrum/ocr/2019-09-16.jpg"
  ])
  @Disabled
  fun `generate and save OCR text`(file: String) {
    val ocrText =
      OcrClient(ocrServerUrl())
        .doOCR(URL("http://test-resources/$file"))

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
