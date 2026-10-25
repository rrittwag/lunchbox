package lunchbox.util.html

import lunchbox.util.container.KtGenericContainer
import lunchbox.util.url.UrlUtil.url
import org.amshove.kluent.shouldHaveSize
import org.jsoup.Jsoup
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testcontainers.containers.Network
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.MountableFile
import java.io.File

@Testcontainers
class HtmlRendererTest {
  companion object {
    private val network = Network.newNetwork()

    @Container
    private val miniwebappContainer =
      KtGenericContainer("nginxinc/nginx-unprivileged:alpine")
        .withNetwork(network)
        .withNetworkAliases("miniwebapp")
        .withCopyFileToContainer(
          MountableFile.forHostPath("src/testIntegration/resources/miniwebapp"),
          "/usr/share/nginx/html",
        )

    @Container
    private val rendertronContainer =
      KtGenericContainer("ammobindotca/rendertron")
        .withNetwork(network)
        .withEnv("PORT", RENDERTRON_PORT.toString())
        .withExposedPorts(RENDERTRON_PORT)

    private fun rendertronUrl(): String =
      "http://${rendertronContainer.host}:${rendertronContainer.getMappedPort(RENDERTRON_PORT)}"
  }

  @Test
  fun `render webapp`() {
    val renderedHtml =
      HtmlRendererImpl(rendertronUrl())
        .render(url("http://miniwebapp:8080/"))

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
