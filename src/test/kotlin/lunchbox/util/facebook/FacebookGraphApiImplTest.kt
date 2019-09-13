package lunchbox.util.facebook /* ktlint-disable max-line-length no-wildcard-imports */

import lunchbox.util.json.createObjectMapper
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.net.URL

class FacebookGraphApiImplTest {

  @Test
  fun `convert posts from 2015-07-05`() {
    val contentAsString = readFileContent("/menus/gesundheitszentrum/graphapi/2015-07-05_posts.json")

    val result = graphApi().fromJson(contentAsString, Posts::class.java)
      ?: fail { "fail" }

    result.data shouldHaveSize 25
    result.data[0].id shouldEqual "181190361991823_723372204440300"
    result.data[0].message shouldContain "10.07.2015"

    result.data[0].attachments.data[0].media shouldNotBe null
    result.data[0].attachments.data[0].subattachments.data shouldHaveSize 0
    result.data[4].attachments.data[0].media shouldBe null
    result.data[4].attachments.data[0].subattachments.data shouldHaveSize 2
  }

  @Test
  fun `convert image from 2015-07-05`() {
    val contentAsString = readFileContent("/menus/gesundheitszentrum/graphapi/2015-07-05_image.json")

    val result = graphApi().fromJson(contentAsString, Image::class.java)
      ?: fail { "fail" }

    result.id shouldEqual "723372204440300"
    result.images shouldHaveSize 9
    result.images[0].height shouldEqual 1754
    result.images[0].width shouldEqual 1240
    result.images[0].source shouldEqual URL("https://scontent.xx.fbcdn.net/hphotos-xtp1/t31.0-8/11709766_723372204440300_7573791609611941912_o.jpg")
  }

  // --- mocks 'n' stuff

  private fun readFileContent(path: String): String {
    val url = javaClass.getResource(path)
    return url.readText(Charsets.UTF_8)
  }

  private fun graphApi() = FacebookGraphApiImpl(createObjectMapper())
}
