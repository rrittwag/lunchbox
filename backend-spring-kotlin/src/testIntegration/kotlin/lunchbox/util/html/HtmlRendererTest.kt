package lunchbox.util.html

import lunchbox.util.ocr.KtDockerComposeContainer
import lunchbox.util.url.UrlUtil.url
import org.amshove.kluent.shouldHaveSize
import org.jsoup.Jsoup
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File

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
    val renderedHtml =
      HtmlRendererImpl(rendertronUrl())
        .render(url("http://miniwebapp/"))

    Jsoup.parse(renderedHtml).select("li") shouldHaveSize 3 // dynamically populated by JavaScript in browser
  }

  @Test
  @Disabled
  fun `render and save webapp`() {
    val renderedHtml =
      HtmlRendererImpl(rendertronUrl())
        .render(url("https://de-de.facebook.com/pages/Kantine-BQuakatz-Allee-der-Kosmonauten/181190361991823"))

    File("src/test/resources/menus/gesundheitszentrum/html/2019-09-23_posts.html").apply {
      createNewFile()
      writeText(renderedHtml)
    }
  }
}
