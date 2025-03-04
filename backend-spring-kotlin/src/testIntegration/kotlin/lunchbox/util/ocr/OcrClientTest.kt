package lunchbox.util.ocr

import lunchbox.util.url.UrlUtil.url
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File

@Testcontainers
class OcrClientTest {
  companion object {
    // start open-ocr-2 via Docker Compose (via TestContainers)
    @Container
    private val ocrContainer =
      KtDockerComposeContainer(File("src/testIntegration/resources/docker-compose.ocr.yml"))
        .withExposedService("ocr_1", OCR_PORT)

    private fun ocrServerUrl(): String {
      val host = ocrContainer.getServiceHost("ocr_1", OCR_PORT)
      val port = ocrContainer.getServicePort("ocr_1", OCR_PORT)
      return "http://$host:$port"
    }
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
        .doOCR(url("http://test-resources/$file"))

    ocrText shouldBeEqualTo File("src/test/resources/$file.txt").readText()
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "menus/torney/ocr/Tagesgericht-07.KW-2025.png",
    ],
  )
  fun `generate and save OCR text`(file: String) {
    val ocrText =
      OcrClient(ocrServerUrl())
        .doOCR(url("http://test-resources/$file"))

    File("src/test/resources/$file.txt").apply {
      createNewFile()
      writeText(ocrText)
    }
  }
}

// BUGFIX: Kotlin does not support SELF types
// -> https://github.com/testcontainers/testcontainers-java/issues/1010
class KtDockerComposeContainer(
  file: File,
) : DockerComposeContainer<KtDockerComposeContainer>(file)
