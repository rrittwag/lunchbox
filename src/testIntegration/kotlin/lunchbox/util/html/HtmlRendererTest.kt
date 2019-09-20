package lunchbox.util.html /* ktlint-disable max-line-length no-wildcard-imports */

import lunchbox.util.ocr.KtDockerComposeContainer
import org.amshove.kluent.shouldHaveSize
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.net.URL

@Testcontainers
class HtmlRendererTest {

  companion object {
    // start rendertron via Docker Compose (via TestContainers)
    @Container
    private val rendertronContainer =
      KtDockerComposeContainer(File("src/testIntegration/resources/docker-compose.rendertron.yml"))
        .withExposedService("rendertron_1", RENDERTRON_PORT)

    private fun rendertronUrl(): String {
      val host = rendertronContainer.getServiceHost("rendertron_1", RENDERTRON_PORT)
      val port = rendertronContainer.getServicePort("rendertron_1", RENDERTRON_PORT)
      return "http://$host:$port"
    }
  }

  @Test
  fun `render webapp`() {
    val pureHtml =
      HtmlRendererImpl(rendertronUrl())
        .render(URL("http://miniwebapp/"))

    Jsoup.parse(pureHtml).select("li") shouldHaveSize 3 // dynamically populated by JavaScript in browser
  }
}
