package lunchbox.util.facebook

import com.fasterxml.jackson.module.kotlin.readValue
import lunchbox.util.json.createObjectMapper
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import java.net.URL

class FacebookGraphApiTest {
  @Test
  fun `deserialize posts json from 2015-07-05`() {
    val contentAsString = readFileContent("/menus/gesundheitszentrum/graphapi/2015-07-05_posts.json")

    val result = createObjectMapper().readValue<Posts>(contentAsString)

    result.data shouldHaveSize 25
    result.data[0].id shouldBeEqualTo "181190361991823_723372204440300"
    result.data[0].message shouldContain "10.07.2015"

    result.data[0].attachments.data[0].media shouldNotBe null
    result.data[0].attachments.data[0].subattachments.data shouldHaveSize 0
    result.data[4].attachments.data[0].media shouldBe null
    result.data[4].attachments.data[0].subattachments.data shouldHaveSize 2
  }

  @Test
  fun `deserialize image json from 2015-07-05`() {
    val contentAsString = readFileContent("/menus/gesundheitszentrum/graphapi/2015-07-05_image.json")

    val result = createObjectMapper().readValue<Image>(contentAsString)

    result.id shouldBeEqualTo "723372204440300"
    result.images shouldHaveSize 9
    result.images[0].height shouldBeEqualTo 1754
    result.images[0].width shouldBeEqualTo 1240
    result.images[0].source shouldBeEqualTo
      URL(
        "https://scontent.xx.fbcdn.net/hphotos-xtp1/t31.0-8/11709766_723372204440300_7573791609611941912_o.jpg",
      )
  }

  // --- mocks 'n' stuff

  private fun readFileContent(path: String): String {
    val url = javaClass.getResource(path)
    return url.readText(Charsets.UTF_8)
  }
}
