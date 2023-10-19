@file:Suppress("ktlint:standard:max-line-length")

package lunchbox.domain.resolvers

import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.GESUNDHEITSZENTRUM
import lunchbox.domain.resolvers.LunchResolverGesundheitszentrum.Wochenplan
import lunchbox.domain.resolvers.LunchResolverGesundheitszentrum.WochenplanWithImageId
import lunchbox.util.date.DateValidator
import lunchbox.util.facebook.FacebookGraphApi
import lunchbox.util.facebook.Image
import lunchbox.util.facebook.Posts
import lunchbox.util.html.HtmlParser
import lunchbox.util.html.HtmlRenderer
import lunchbox.util.json.createObjectMapper
import lunchbox.util.ocr.OcrClient
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.URL

class LunchResolverGesundheitszentrumTest {
  private val graphApi = mockk<FacebookGraphApi>()
  private val ocrClient = mockk<OcrClient>()
  private val htmlRenderer =
    object : HtmlRenderer {
      override fun render(url: URL): String = url.readText() // example files are pre-rendered
    }
  private val htmlParser = HtmlParser(htmlRenderer)

  private fun resolver() = LunchResolverGesundheitszentrum(DateValidator.alwaysValid(), graphApi, htmlParser, ocrClient)

  private val providerId = GESUNDHEITSZENTRUM.id
  private val objectMapper = createObjectMapper()

  @Nested
  inner class GraphApi {
    @Test
    fun `resolve Wochenplaene for posts of 2015-07-05`() {
      val content = readFileContent("/menus/gesundheitszentrum/graphapi/2015-07-05_posts.json")
      val posts = objectMapper.readValue<Posts>(content)

      val wochenplaene = resolver().parseWochenplaeneByGraphApi(posts.data)

      wochenplaene shouldContain WochenplanWithImageId(date("2015-07-06"), "723372204440300")
      wochenplaene shouldContain WochenplanWithImageId(date("2015-06-22"), "715616615215859")
      wochenplaene shouldContain WochenplanWithImageId(date("2015-06-15"), "712691465508374")
    }

    @Test
    fun `resolve Wochenplaene for posts of 2017-07-24`() {
      val content = readFileContent("/menus/gesundheitszentrum/graphapi/2017-07-24_posts.json")
      val posts = objectMapper.readValue<Posts>(content)

      val wochenplaene = resolver().parseWochenplaeneByGraphApi(posts.data)

      wochenplaene shouldContain WochenplanWithImageId(date("2017-07-24"), "1214480778662771")
      wochenplaene shouldContain WochenplanWithImageId(date("2017-07-17"), "1208020239308825")
      wochenplaene shouldContain WochenplanWithImageId(date("2017-07-10"), "1204499422994240")
    }

    @Test
    fun `resolve double-imaged Wochenplan for posts of 2016-04-25`() {
      val content = readFileContent("/menus/gesundheitszentrum/graphapi/2016-04-25_posts.json")
      val posts = objectMapper.readValue<Posts>(content)

      val wochenplaene = resolver().parseWochenplaeneByGraphApi(posts.data)

      wochenplaene shouldContain WochenplanWithImageId(date("2016-04-25"), "855667961210723")
    }

    @Test
    fun `resolve URL for image of 2015-07-05`() {
      val content = readFileContent("/menus/gesundheitszentrum/graphapi/2015-07-05_image.json")
      val image = objectMapper.readValue<Image>(content)

      val url = resolver().parseImageLink(image)

      url shouldBeEqualTo
        URL(
          "https://scontent.xx.fbcdn.net/hphotos-xtp1/t31.0-8/11709766_723372204440300_7573791609611941912_o.jpg",
        )
    }

    @Test
    fun `resolve URL for image of 2017-07-24`() {
      val content = readFileContent("/menus/gesundheitszentrum/graphapi/2017-07-24_image.json")
      val image = objectMapper.readValue<Image>(content)

      val url = resolver().parseImageLink(image)

      url shouldBeEqualTo
        URL(
          "https://scontent.xx.fbcdn.net/v/t31.0-8/20233053_1214480778662771_9100409891617048289_o.jpg?oh=a50f5058410183e8a5c631e82919f473&oe=5A09D7B9",
        )
    }
  }

  @Nested
  inner class HtmlParsing {
    @Test
    fun `resolve Wochenplaene for posts of 2019-09-16`() {
      val url = javaClass.getResource("/menus/gesundheitszentrum/html/2019-09-16_posts.html")

      val wochenplaene = resolver().parseWochenplaeneByHtml(url)

      wochenplaene shouldHaveSize 2
      wochenplaene shouldContain
        Wochenplan(
          date("2019-09-16"),
          URL(
            "https://scontent-ber1-1.xx.fbcdn.net/v/t1.0-9/70243813_2176643935779779_1905631810474213376_o.jpg?_nc_cat=106&_nc_oc=AQnOmbEvG5WngTMx4RqIMiGBD4jDftJMUMYi2M5uwa3Nu3QAJUdseNXbSEr1Iejl_Ds&_nc_ht=scontent-ber1-1.xx&oh=e40ce027618fa63a5b7f4971fc02b83d&oe=5E06EF54",
          ),
        )
      wochenplaene shouldContain
        Wochenplan(
          date("2019-09-09"),
          URL(
            "https://scontent-ber1-1.xx.fbcdn.net/v/t1.0-9/69761025_2164731656971007_4787155769139134464_o.jpg?_nc_cat=101&_nc_oc=AQlqnbM_DBfJ-eB1mBQK48kb8M3UWtjmyGo1knDG-9caTgaJAPraVhT6ZuHcff7_5P0&_nc_ht=scontent-ber1-1.xx&oh=77d585b271943105b09845d3ed23c00b&oe=5DF4A077",
          ),
        )
    }

    @Test
    fun `resolve Wochenplaene for posts of 2019-09-23`() {
      val url = javaClass.getResource("/menus/gesundheitszentrum/html/2019-09-23_posts.html")

      val wochenplaene = resolver().parseWochenplaeneByHtml(url)

      wochenplaene shouldHaveSize 2
      wochenplaene shouldContain
        Wochenplan(
          date("2019-09-23"),
          URL(
            "https://scontent-ber1-1.xx.fbcdn.net/v/t1.0-9/70741530_2188809777896528_1685416184234639360_o.jpg?_nc_cat=104&_nc_oc=AQn0voSe6PW2bpXJhANweSiOIyLcrYb0G-NmImMtPJ6Ka4swX6GfSG-Eudtb4LkGCe8&_nc_ht=scontent-ber1-1.xx&oh=be84961d17026c68fc74c1ecf23b2396&oe=5DF929A9",
          ),
        )
      wochenplaene shouldContain
        Wochenplan(
          date("2019-09-16"),
          URL(
            "https://scontent-ber1-1.xx.fbcdn.net/v/t1.0-9/70243813_2176643935779779_1905631810474213376_o.jpg?_nc_cat=106&_nc_oc=AQnlw8UTdf_EHJa-WZ3OfYWP7TyOyahpP-xMOhEj_-x_78veJsYvEmYT68pImua8XLA&_nc_ht=scontent-ber1-1.xx&oh=00559a3cb8d8df7486d84dc607a7c2b0&oe=5E2E7C54",
          ),
        )
    }
  }

  @Test
  fun `resolve offers for week of 2015-06-22`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2015-06-22.jpg.txt")
    val week = weekOf("2015-06-22")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 20
    offers shouldContain LunchOffer(0, "Wirsingkohleintopf", "", week.monday, euro("2.40"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Riesenkrakauer mit Sauerkraut und Salzkartoffeln",
        "",
        week.monday,
        euro("4.40"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Steak \"au four\" mit Buttererbsen und Kroketten",
        "",
        week.monday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Tortellini mit Tomatensauce",
        "",
        week.monday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Germknödel mit Vanillesauce und Mohn",
        "",
        week.tuesday,
        euro("3.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Jägerschnitzel (panierte Jagdwurst) mit Tomatensauce und Nudeln",
        "",
        week.tuesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gefüllter Schweinerücken mit Gemüse und Salzkartoffeln",
        "",
        week.tuesday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Eier-Spinatragout mit Petersilienkartoffeln",
        "",
        week.tuesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Vegetarisches Chili", "", week.wednesday, euro("2.40"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Schmorkohl mit Hackfleisch und Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Paprikahähnchenkeule mit Gemüse und Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schollenfilet mit Gemüsereis",
        "",
        week.wednesday,
        euro("4.80"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Pichelsteiner Gemüseeintopf",
        "",
        week.thursday,
        euro("2.40"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinegeschnetzeltes mit Pilzen dazu Reis",
        "",
        week.thursday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gebratenes Putensteak mit Champignons-Rahmsauce und Spätzle",
        "",
        week.thursday,
        euro("4.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Nudel-Gemüse-Auflauf", "", week.thursday, euro("3.90"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Brühreis", "", week.friday, euro("2.40"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Hühnerfrikassee mit Champignons, Spargel und Reis",
        "",
        week.friday,
        euro("4.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Holzfällersteak mit geschmorten Zwiebeln, Pilzen und Bratkartoffeln",
        "",
        week.friday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kräuterrüherei mit Kartoffelpüree",
        "",
        week.friday,
        euro("3.80"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2015-06-29`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2015-06-29.jpg.txt")
    val week = weekOf("2015-06-29")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 20
    offers shouldContain LunchOffer(0, "Bunter Gemüseeintopf", "", week.monday, euro("2.40"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Hausgemachte Boulette mit Mischgemüse und Kartoffelpüree",
        "",
        week.monday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinekammbraten mit Rotkohl und Klöße",
        "",
        week.monday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Pasta mit Pilzrahmsauce", "", week.monday, euro("3.80"), emptySet(), providerId)

    offers shouldContain
      LunchOffer(
        0,
        "Grießbrei mit roten Früchten",
        "",
        week.tuesday,
        euro("2.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Blutwurst mit Sauerkraut und Salzkartoffeln",
        "",
        week.tuesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinerollbraten mit grünen Bohnen und Salzkartoffeln",
        "",
        week.tuesday,
        euro("4.50"),
        emptySet(),
        providerId,
      ) // OCR erkennt Preis falsch
    offers shouldContain
      LunchOffer(
        0,
        "Pilzragout mit Semmelknödel",
        "",
        week.tuesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Minestrone", "", week.wednesday, euro("2.40"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Gefüllte Paprikaschote mit Tomatensauce und Reis",
        "",
        week.wednesday,
        euro("4.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenbrust mit Wirsingrahmgemüse und Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.70"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pangasiusfilet mit Honig-Senf-Dillrahmsauce dazu Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.60"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Feuertopf (Kidneybohnen, grüne Bohnen, Jagdwurst)",
        "",
        week.thursday,
        euro("2.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Paprikasahnegulasch mit Nudeln",
        "",
        week.thursday,
        euro("4.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rieseneisbein mit Sauerkraut und Salzkartoffeln",
        "",
        week.thursday,
        euro("4.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kartoffel-Tomaten-Zucchini-Auflauf mit Mozzarella überbacken",
        "",
        week.thursday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Porreeeintopf", "", week.friday, euro("2.40"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Hackbraten mit Mischgemüse und Salzkartoffeln",
        "",
        week.friday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Wildgulasch mit Rotkohl und Klöße",
        "",
        week.friday,
        euro("4.50"),
        emptySet(),
        providerId,
      ) // OCR erkennt Preis falsch
    offers shouldContain
      LunchOffer(
        0,
        "Matjestopf mit Bohnensalat und Kräuterkartoffeln",
        "",
        week.friday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for badly OCR-able plan of week of 2015-06-15`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2015-06-15.jpg.txt")
    val week = weekOf("2015-06-15")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 20
    offers shouldContain
      LunchOffer(
        0,
        "Vegetarische Linsensuppe mit Vollkornbrötchen",
        "",
        week.monday,
        euro("3.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Ofenfrischer Leberkäs mit einem Setzei, Zwiebelsauce und Kartoffelpüree",
        "",
        week.monday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinekammsteak mit Rahmchampions, dazu Pommes frites",
        "",
        week.monday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rührei mit Sauce \"Funghi\" und Kartoffelpüree",
        "",
        week.monday,
        euro("3.80"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Milchreis mit Zucker und Zimt",
        "",
        week.tuesday,
        euro("3.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "2 Setzeier (außer Haus Rührei) mit Spinat und Salzkartoffeln",
        "",
        week.tuesday,
        euro("3.80"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gemischtes Gulasch aus Rind und Schwein dazu Nudeln",
        "",
        week.tuesday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pasta mit Blattspinat, Tomaten und Reibekäse (analog)",
        "",
        week.tuesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Gulaschsuppe", "", week.wednesday, euro("3.20"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "2 Bifteki mit Zigeunersauce und Reis",
        "",
        week.wednesday,
        euro("4.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rinderbraten mit Rotkohl und Klößen",
        "",
        week.wednesday,
        euro("4.80"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Forelle \"Müllerin Art\" mit zerlassener Butter und Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.60"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Kohlrabieintopf", "", week.thursday, euro("2.40"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Pfefferfleisch mit Buttergemüse dazu Reis",
        "",
        week.thursday,
        euro("4.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinekammsteak mit Rahmchampions, dazu Pommes frites",
        "",
        week.thursday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kartoffel-Tomaten-Zucchini-Auflauf",
        "",
        week.thursday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Ungarischer Sauerkrauttopf",
        "",
        week.friday,
        euro("2.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Cevapcici mit Zigeunersauce und Reis",
        "",
        week.friday,
        euro("4.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenschnitzel \"Wiener Art\" mit Pfefferrahmsauce, Gemüse und Kroketten",
        "",
        week.friday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Sahnehering mit Bratkartoffeln",
        "",
        week.friday,
        euro("3.70"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for short week of 2015-07-06`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2015-07-06.jpg.txt")
    val week = weekOf("2015-07-06")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 16
    offers shouldContain
      LunchOffer(
        0,
        "Serbischer Bohneneintopf",
        "",
        week.monday,
        euro("2.40"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rostbratwurst mit Sauerkraut und Kartoffelpüree",
        "",
        week.monday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Thüringer Rostbrätl mit geschmorten Zwiebeln und Bratkartoffeln",
        "",
        week.monday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gebackener Camembert mit Preiselbeeren, 1/2 Birne und Baguette",
        "",
        week.monday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Hefeklöße mit Früchten", "", week.tuesday, euro("3.00"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Pasta \"Schuta\" (Hackfleisch und Paprika) auf Nudeln",
        "",
        week.tuesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hausgemachte Rinderroulade mit Rotkohl und Klößen",
        "",
        week.tuesday,
        euro("4.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gefüllte Paprika (vegetarisch) mit Vollkornreis und Salat",
        "",
        week.tuesday,
        euro("4.20"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Gelber Erbseneintopf", "", week.wednesday, euro("2.40"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Königsberger Klopse mit Kapernsauce und Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Lachs mit Makkaroni und Möhren-Sellerie-Salat",
        "",
        week.wednesday,
        euro("4.80"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Mexikanischer Nudelauflauf",
        "",
        week.wednesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Reitersuppe (Hackfleisch, grüne Bohnen, Champignons, Paprika)",
        "",
        week.thursday,
        euro("3.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Chinapfanne mit Reis", "", week.thursday, euro("4.20"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Gemischtes Gulasch aus Rind und Schwein dazu Nudeln",
        "",
        week.thursday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kartoffel-Blumenkohl-Gratin",
        "",
        week.thursday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers.filter { it.day == week.friday } shouldHaveSize 0
  }

  @Test
  fun `resolve offers for week of 2015-07-27`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2015-07-27.jpg.txt")
    val week = weekOf("2015-07-27")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 20

    offers shouldContain
      LunchOffer(
        0,
        "Kartoffelsuppe mit Wiener",
        "",
        week.monday,
        euro("3.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweineleber mit Zwiebelsauce und Kartoffelpüree",
        "",
        week.monday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchen \"Cordon Bleu\" ;Pfefferrahmsauce, Gemüse und Kroketten",
        "",
        week.monday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gefüllte Kartoffeltaschen mit Tomatensauce",
        "",
        week.monday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Germknödel mit Vanillesauce und Mohn",
        "",
        week.tuesday,
        euro("3.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Paprikasahnegulasch mit Nudeln",
        "",
        week.tuesday,
        euro("4.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gefüllter Schweinerücken mit Gemüse und Salzkartoffeln",
        "",
        week.tuesday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Vegetarische Paprikaschote mit Vollkornreis und Salat",
        "",
        week.tuesday,
        euro("4.20"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Gulaschsuppe", "", week.wednesday, euro("3.20"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Schmorkohl mit Hackfleisch und Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pangasius mit Honig-Senf-Dillrahmsauce und Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kartoffel-Brokkoli-Auflauf",
        "",
        week.wednesday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Käse-Lauch-Suppe", "", week.thursday, euro("3.20"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Hühnerfrikassee mit Champignons, Spargel und Reis",
        "",
        week.thursday,
        euro("4.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Steak \"au four\" mit Buttererbsen und Kroketten",
        "",
        week.thursday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pasta mit Blattspinat, Tomaten und Reibekäse",
        "",
        week.thursday,
        euro("4.00"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Fischtopf", "", week.friday, euro("3.20"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Schweinegeschnetzeltes mit Pilzen und Reis",
        "",
        week.friday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Holzfällersteak mit geschmorten Zwiebeln und Bratkartoffeln",
        "",
        week.friday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kräuterrührei mit Kartoffelpüree",
        "",
        week.friday,
        euro("3.80"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2016-04-04`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2016-04-04.jpg.txt")
    val week = weekOf("2016-04-04")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 21

    offers shouldContain
      LunchOffer(
        0,
        "Käse-Hackfleisch-Lauch-Suppe",
        "",
        week.monday,
        euro("3.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "\"Jägerschnitzel\"(panierte Jagdwurst), Tomatensauce und Nudeln",
        "",
        week.monday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenstreifen in Rucolasauce und Tomatenmakkaroni",
        "",
        week.monday,
        euro("4.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bunte Reispfanne mit Gemüse",
        "",
        week.monday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Hausgemachte Kartoffelpuffer mit Apfelmus und Zucker",
        "",
        week.tuesday,
        euro("3.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hackroulade mit Sahnesauce, Gemüse und Salzkartoffeln",
        "",
        week.tuesday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Putengeschnetzeltes mit Champignons dazu Spätzle",
        "",
        week.tuesday,
        euro("4.80"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Frühlingsrolle mit Asiagemüse und Reis",
        "",
        week.tuesday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Brühnudeln", "", week.wednesday, euro("2.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Matjestopf mit Kräuterkartoffeln und Bohnensalat",
        "",
        week.wednesday,
        euro("4.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gebratenes Pangasiusfilet mit Kräutersauce und Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.80"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gebackener Camembert mit 1/2 Birne, Preiselbeeren und Baguette",
        "",
        week.wednesday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Kokos-Curry-Suppe mit Hühnerfleisch",
        "",
        week.thursday,
        euro("3.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinegulasch mit Nudeln",
        "",
        week.thursday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Curryhuhn mit Makkaroni",
        "",
        week.thursday,
        euro("4.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kräuterrührei mit Pilzrahmsauce und Kartoffelpüree",
        "",
        week.thursday,
        euro("3.80"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rieseneisbein mit Erbspüree, Sauerkraut, und Salzkartoffeln",
        "",
        week.thursday,
        euro("5.10"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Chili con Carne", "", week.friday, euro("3.30"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Wurstgulasch mit Nudeln", "", week.friday, euro("4.20"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Tiroler Eiersalat (Mais, Champignons, Spargel) dazu Bratkartoffeln",
        "",
        week.friday,
        euro("3.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinekammbraten mit Rotkohl und Salzkartoffeln",
        "",
        week.friday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2016-04-11`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2016-04-11.jpg.txt")
    val week = weekOf("2016-04-11")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 20

    offers shouldContain
      LunchOffer(
        0,
        "Kokos-Curry-Suppe mit Hühnerfleisch",
        "",
        week.monday,
        euro("2.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schichtkohl mit Hackfleisch und Salzkartoffeln",
        "",
        week.monday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinemedaillons mit Pfeffersauce und Kroketten",
        "",
        week.monday,
        euro("4.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Brokkoli in Käse-Sahne-Sauce und Nudeln",
        "",
        week.monday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Hausgemachte Eierkuchen mit Apfelmus und Zucker",
        "",
        week.tuesday,
        euro("3.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pasta Schuta (Hackfleisch und Paprika) und Nudeln",
        "",
        week.tuesday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kasselerbraten mit Sauerkraut dazu Salzkartoffeln",
        "",
        week.tuesday,
        euro("4.70"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rührei mit Sauce Funghi dazu Kartoffelpüree",
        "",
        week.tuesday,
        euro("3.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Möhren-Zucchini-Eintopf",
        "",
        week.wednesday,
        euro("2.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hausgemachte Boulette mit Champignonrahm dazu Kartoffelpüree",
        "",
        week.wednesday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Seelachs in Eihülle auf Lauch-Tomatengemüse dazu Reis",
        "",
        week.wednesday,
        euro("4.80"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Makkaroni mit Blattspinat, Tomaten und Reibekäse",
        "",
        week.wednesday,
        euro("4.00"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Kartoffelsuppe mit Wiener",
        "",
        week.thursday,
        euro("3.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Blutwurst mit Sauerkraut und Salzkartoffeln",
        "",
        week.thursday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenkeule mit Gemüsereis",
        "",
        week.thursday,
        euro("4.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Spaghetti \"all Arrabiata\" (scharf)",
        "",
        week.thursday,
        euro("3.80"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Bauerntopf (Hackfleisch, Kartoffel, Paprika)",
        "",
        week.friday,
        euro("2.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Böhmisches Bierfleisch mit Semmelknödel",
        "",
        week.friday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinekammsteak mit Letcho und Reis",
        "",
        week.friday,
        euro("3.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Eierfrikassee mit Salzkartoffeln",
        "",
        week.friday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2016-04-18`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2016-04-18.jpg.txt")
    val week = weekOf("2016-04-18")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 20

    offers shouldContain LunchOffer(0, "Hausgemachte Soljanka", "", week.monday, euro("2.90"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Gemischter Gulasch mit Rotkohl und Kartoffeln",
        "",
        week.monday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Nudel-Broccoli-Auflauf", "", week.monday, euro("3.90"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Steak \"au four\" mit Würzfleisch überbacken dazu Gemüse und Pommes Frites",
        "",
        week.monday,
        euro("4.80"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Germknödel mit Vanillesauce und Mohn",
        "",
        week.tuesday,
        euro("3.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Cevapcici mit Zigeunersauce dazu Reis",
        "",
        week.tuesday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Champignonpfanne mit Kartoffelpüree",
        "",
        week.tuesday,
        euro("3.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchen \"Cordon Bleu\" mit Pfeffersauce dazu Gemüse Kroketten",
        "",
        week.tuesday,
        euro("4.80"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Bunter Gemüse Eintopf",
        "",
        week.wednesday,
        euro("2.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gemüseschnitzel mit Sauce Hollandaise und Kartoffelpüree",
        "",
        week.wednesday,
        euro("3.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "2 Rinderbouletten mit Ratatouille dazu Reis",
        "",
        week.wednesday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schollenfilet mit Gemüsereis",
        "",
        week.wednesday,
        euro("4.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Rosenkohleintopf", "", week.thursday, euro("2.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Paprikasahnegulasch mit Nudeln",
        "",
        week.thursday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kartoffel-Zucchinie-Tomaten Auflauf mit Mozzarella überbacken",
        "",
        week.thursday,
        euro("3.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Westfälischer Pfeffer Potthast (Rindfleisch) mit Rote Bete Salat dazu Salzkartoffeln",
        "",
        week.thursday,
        euro("4.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Reitersuppe (Hackfleisch, grüne Bohnen, Chmapignons, Paprika)",
        "",
        week.friday,
        euro("3.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweineleber mit Zwiebelsauce dazu Kartoffelpüree",
        "",
        week.friday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gefüllte Kartoffeltaschen mit Tomatensauce",
        "",
        week.friday,
        euro("3.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchengeschnetzeltes \"Gyros Art\" dazu Tzatziki und Reis",
        "",
        week.friday,
        euro("4.80"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2016-04-25`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2016-04-25.jpg.txt")
    val week = weekOf("2016-04-25")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 18

    offers shouldContain
      LunchOffer(
        0,
        "Schwedische Fischsuppe mit Apfelstückchen",
        "",
        week.monday,
        euro("3.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Chinapfanne mit Geflügelfleisch dazu Reis",
        "",
        week.monday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Maultaschen auf Blattspinat dazu Käsesauce",
        "",
        week.monday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweineschnitzel \"Jäger Art\" mit Champignons und Waldpilzen in Rahm dazu Pommes Frites",
        "",
        week.monday,
        euro("4.70"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Grießbrei mit warmen Schattenmorellen",
        "",
        week.tuesday,
        euro("3.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Schnitzeltag", "", week.tuesday, euro("5.00"), emptySet(), providerId)

    offers shouldContain
      LunchOffer(
        0,
        "Tomatensuppe mit Sahnehäubchen",
        "",
        week.wednesday,
        euro("2.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Sahnehering mit Salzkartoffeln",
        "",
        week.wednesday,
        euro("3.70"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bunte Reispfanne mit Gemüse",
        "",
        week.wednesday,
        euro("3.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinerahmgeschnetzeltes mit Spätzle",
        "",
        week.wednesday,
        euro("4.40"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Kohlrabieintopf", "", week.thursday, euro("2.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Pfefferfleisch mit Buttererbsen und Salzkarttofeln",
        "",
        week.thursday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gnocci mit Pilzrahmsauce und Parmesan",
        "",
        week.thursday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchen mit Mozzarellakruste und Curryreis",
        "",
        week.thursday,
        euro("4.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Brühnudeln", "", week.friday, euro("2.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Klopse \"Napoli\" mit Tomatensauce und Reis",
        "",
        week.friday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Asiatisches Wokgericht mit Cashewkernen dazu Reis",
        "",
        week.friday,
        euro("4.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Forelle \"Müllerin\" mit zerlassener Butter und Petersilienkartoffeln",
        "",
        week.friday,
        euro("4.90"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2016-05-09`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2016-05-09.jpg.txt")
    val week = weekOf("2016-05-09")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 21

    offers shouldContain
      LunchOffer(
        0,
        "Ofenfrischer Leberkäse mit Zwiebelsauce dazu Kartoffelpürree",
        "",
        week.wednesday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rieseneisbein mit Sauerkraut dazu Salzkartoffeln",
        "",
        week.thursday,
        euro("5.10"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2017-07-24`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2017-07-24.jpg.txt")
    val week = weekOf("2017-07-24")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 18

    offers shouldContain
      LunchOffer(
        0,
        "Scharfes Kartoffel-Paprika-Curry",
        "",
        week.tuesday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Gelbe Erbseneintopf", "", week.friday, euro("2.80"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2017-07-31`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2017-07-31.jpg.txt")
    val week = weekOf("2017-07-31")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 19

    offers shouldContain LunchOffer(0, "Möhren-Zucchini-Eintopf", "", week.friday, euro("2.80"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Kasselerkammbraten mit Sauerkraut und Salzkartoffeln",
        "",
        week.friday,
        euro("4.80"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-08-05`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2019-08-05.jpg.txt")
    val week = weekOf("2019-08-05")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 18

    offers shouldContain
      LunchOffer(
        0,
        "Saarländische Kartoffelsuppe",
        "",
        week.monday,
        euro("3.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kräuterrührei mit Pilzrahmsauce und Kartoffelpüree",
        "",
        week.monday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Wurstgulasch mit Nudeln", "", week.monday, euro("4.60"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenbrust \"Melba\" Orangensauce und Kroketten",
        "",
        week.monday,
        euro("5.20"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Milchreis mit Zucker und Zimt",
        "",
        week.tuesday,
        euro("3.10"),
        emptySet(),
        providerId,
      ) // OCR erkennt Preis falsch
    offers shouldContain
      LunchOffer(
        0,
        "Schupfnudeln mit Gorgonzolasauce",
        "",
        week.tuesday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schmorkohl mit Hackfleisch und Salzkartoffeln",
        "",
        week.tuesday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Westfälischer Pfefferpotthast (Rindfleisch) mit Rote Bete dazu Salzkartoffeln",
        "",
        week.tuesday,
        euro("5.20"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Pichelsteiner Gemüseeintopf",
        "",
        week.wednesday,
        euro("3.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pasta mit Tomaten-Sugo, Oliven und Ruccola",
        "",
        week.wednesday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hausgemachte Boulette mit Champignonrahm dazu Kartoffelpüree",
        "",
        week.wednesday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Seelachs in Eihülle auf Lauch-Tomatengemüse dazu Reis",
        "",
        week.wednesday,
        euro("5.00"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Feuertopf (Kidneybohnen, grüne Bohnen, Jagdwurst)",
        "",
        week.thursday,
        euro("3.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kartoffel-Spinat Auflauf",
        "",
        week.thursday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Böhmisches Bierfleisch mit Semmelknödel",
        "",
        week.thursday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "\"Mailänder\" Käseschnitzel (Schweineschnitzel in Käse-Eihülle) mit Tomatensauce und Nudeln",
        "",
        week.thursday,
        euro("5.20"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Karotteneintopf", "", week.friday, euro("3.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Schweinerahmgeschnetzeltes mit Champignons und Spätzle",
        "",
        week.friday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-09-09`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2019-09-09.jpg.txt")
    val week = weekOf("2019-09-09")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 18

    offers shouldContain LunchOffer(0, "Kohlrabieintopf", "", week.monday, euro("3.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Frühlingsrolle mit süß-sauer Sauce dazu Reis",
        "",
        week.monday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Spaghetti \"Carbonara\" mit Schinken",
        "",
        week.monday,
        euro("4.40"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Putenragout \"Provencial\" mit Champignon, Tomaten und Kräuter dazu Reis",
        "",
        week.monday,
        euro("4.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Germknödel mit Vanillesauce und Mohn",
        "",
        week.tuesday,
        euro("3.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Blumenkohl überbacken mit Sc.Hollandaise und Salzkartoffeln",
        "",
        week.tuesday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Paprikasahnegulasch mit Nudeln",
        "",
        week.tuesday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchen mit Mozzarellakruste und Curryreis",
        "",
        week.tuesday,
        euro("5.20"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Brühreis", "", week.wednesday, euro("3.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Gebackener Camembert mit 1/2 Birne, Preiselbeeren und Baguette",
        "",
        week.wednesday,
        euro("4.10"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hausgemachte Boulette mit Champignonrahm dazu Kartoffelpüree",
        "",
        week.wednesday,
        euro("4.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Seelachs gebraten mit Mandelbutter dazu Brokkoli und Salzkartoffeln",
        "",
        week.wednesday,
        euro("5.20"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Käse-Hackfleisch-Lauch-Suppe",
        "",
        week.thursday,
        euro("3.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pasta mit Tomaten-Sugo, Oliven und Ruccola",
        "",
        week.thursday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "2 Currywürste mit Curryketchup und Pommes Frites",
        "",
        week.thursday,
        euro("4.80"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rindergeschnetzeltes \"Art Stroganoff\" mit Reis",
        "",
        week.thursday,
        euro("5.20"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Vegetarischer Linseneintopf mit einem Vollkornbrötchen",
        "",
        week.friday,
        euro("3.40"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Holzfällersteak mit Schmorzwiebeln dazu Bratkartoffeln",
        "",
        week.friday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-09-16`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2019-09-16.jpg.txt")
    val week = weekOf("2019-09-16")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 18

    offers shouldContain LunchOffer(0, "Porreeeintopf", "", week.monday, euro("3.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Allgäuer-Gemüse-Käse-Salat",
        "",
        week.monday,
        euro("4.20"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Lasagne \"Bolognaise\" mit Pizza Mix",
        "",
        week.monday,
        euro("4.70"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Putenbrustbraten mit Buttergemüse und Salzkartoffeln",
        "",
        week.monday,
        euro("5.20"),
        emptySet(),
        providerId,
      )

    offers shouldContain
      LunchOffer(
        0,
        "Milchreis mit Zucker und Zimt",
        "",
        week.tuesday,
        euro("3.40"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kartoffel-Brokkoli-Auflauf mit Feta und Sonnenblumenkernen",
        "",
        week.tuesday,
        euro("4.40"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Wurstgulasch mit Nudeln",
        "",
        week.tuesday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Cevapcici mit Zigeunersauce dazu Reis",
        "",
        week.tuesday,
        euro("4.90"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Weißkohleintopf", "", week.wednesday, euro("3.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Eierfrikasse mit Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Blutwurst mit Sauerkraut und Salzkartoffeln",
        "",
        week.wednesday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Matjestopf mit Kräuterkartoffeln und Bohnensalat",
        "",
        week.wednesday,
        euro("4.60"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Karotteneintopf", "", week.thursday, euro("3.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Gefüllte Kartoffeltaschen mit Käse-Lauchsauce",
        "",
        week.thursday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pfefferfleisch mit Buttererbsen und Reis",
        "",
        week.thursday,
        euro("4.60"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweineschnitzel \"Bolognaiser Art\" mit Hackfleischsauce überbacken dazu Pommes Frites",
        "",
        week.thursday,
        euro("5.20"),
        emptySet(),
        providerId,
      )

    offers shouldContain LunchOffer(0, "Linseneintopf", "", week.friday, euro("3.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "1/2 Hähnchen mit Pommes frites",
        "",
        week.friday,
        euro("4.80"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-09-23`() {
    val text = readFileContent("/menus/gesundheitszentrum/ocr/2019-09-23.jpg.txt")
    val week = weekOf("2019-09-23")

    val offers = resolver().resolveOffersFromText(week.monday, text)

    offers shouldHaveSize 18
    offers shouldContain
      LunchOffer(
        0,
        "4/2 Eier mit Remouladensauce dazu Bratkartoffeln",
        "",
        week.wednesday,
        euro("4.30"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Reitersuppe (Hackfleisch, grüne Bohnen, Champignons, Paprika)",
        "",
        week.thursday,
        euro("3.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Möhren-Zucchini-Eintopf", "", week.friday, euro("3.50"), emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Schweinesteak mit Waldpilzsauce und Rosmarinkartoffeln",
        "",
        week.friday,
        euro("5.20"),
        emptySet(),
        providerId,
      )
  }

  private fun readFileContent(path: String): String {
    val url = javaClass.getResource(path)
    return url.readText(Charsets.UTF_8)
  }
}
