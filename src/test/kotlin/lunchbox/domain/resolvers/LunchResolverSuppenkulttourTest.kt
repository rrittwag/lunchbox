package lunchbox.domain.resolvers /* ktlint-disable max-line-length no-wildcard-imports */

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SUPPENKULTTOUR
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverSuppenkulttourTest {

  private val htmlParser = HtmlParser(mockk())
  private fun resolver() = LunchResolverSuppenkulttour(DateValidator.alwaysValid(), htmlParser)
  private val providerId = SUPPENKULTTOUR.id

  @Test
  fun `resolve offers for week of 2019-01-14`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-01-14.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 40

    var week = weekOf("2019-01-14")
    for (weekday in week.lunchDays) {
      offers shouldContain LunchOffer(0, "Berliner Kartoffelsuppe: mit Kartoffeln, Wiener, Wurzelgemüse, Majoran", weekday, euro("4.70"), providerId)
      offers shouldContain LunchOffer(0, "Hühnersuppe: Huhn, Porree, Sellerie, Möhren, Pastinaken", weekday, euro("4.70"), providerId)
      offers shouldContain LunchOffer(0, "afrikanischer Erdnusseintopf: Erdnusspaste, Kichererbsen, Kidneybohnen, Lauch, rote Paprika, Weißkohl, Mangosaft", weekday, euro("4.70"), providerId)
    }
    offers shouldContain LunchOffer(0, "Schnüsch: der bekannte Norddeutsche Gemüseeintopf, Kartoffeln, Bohnen, Kohlrabi, Möhren, Sellerie, Sahne, Kräuter", week.monday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Pasta mit „Ossi-Würstchengulasch“: Nudeln mit Tomatensoße, Wiener, sauren Gurken, Letschow", week.tuesday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Blumenkohl-Süßkartoffel-Curry: mit Blumenkohl, Süßkartoffeln, Kichererbsen, roten Linsen, Madras Curry, Kurkuma, Ingwer", week.wednesday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Sweet-Chili-Lemon-Chicken: knackiges Gemüse, Sprossen, mariniertes Huhn, Limette, Zitronengras, Reis", week.thursday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Schmorgurken: mit Rinderhackfleisch, Gurken, Tomatenmark, Sahne, Dill wahlweise + Röstzwiebeln", week.friday, euro("4.70"), providerId)

    week = weekOf("2019-01-21")
    for (weekday in week.lunchDays) {
      offers shouldContain LunchOffer(0, "Grüne Bohnen Eintopf: mit grünen Bohnen, Kartoffeln, Möhren, Kasslerschulter, Bohnenkraut", weekday, euro("4.70"), providerId)
      offers shouldContain LunchOffer(0, "Hackfleischsuppe Mexico: Rinderhackfleisch,Mais, weiße Bohnen, Tomaten", weekday, euro("4.70"), providerId)
      offers shouldContain LunchOffer(0, "Magische Kohlsuppe: Weißkohl, Möhren, Paprika, Sellerie,Tomaten, Petersilie", weekday, euro("4.70"), providerId)
    }
    offers shouldContain LunchOffer(0, "altdeutsche Kartoffelsuppe: pürierte Variante, mit Schinken, Wiener, Möhren, Pastinaken, Lauch, Kartoffeln, Sahne, Muskat", week.monday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Pasta Panna: Nudeln mit Käse-Sahnesoße, Champignons, Kochschinken, Pfirsich", week.tuesday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Tomatensuppe: Fond aus vielen, frischen Tomaten mit Feta, Wildreis & Kräutern", week.wednesday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Karotten-Linsensuppe: mit Karotten, Süßkartoffeln, roten Linsen, Kurkuma, Ingwer, Kokos, Madras Curry", week.thursday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Rote Beete Powertopf: Rote Beete, mageres Rindfleisch, Weißkohl, wahlweise + Meerrettich", week.friday, euro("4.70"), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-02-18`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-02-18.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 40

    var week = weekOf("2019-02-18")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4
    offers shouldContain LunchOffer(0, "Süßkartoffel- Erdnusscremesuppe: mit Süßkartoffeln, Erdnusspaste,Weißkohl, Curry, Ingwer, Kokos", week.monday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Metaxasuppe: marinierte Schweinefiletstreifen, Reis, Paprika, Tomaten in leckerer Metaxasoße - wie beim Lieblingsgriechen", week.thursday, euro("4.70"), providerId)

    week = weekOf("2019-02-25")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4
    offers shouldContain LunchOffer(0, "Birnen-Linsen-DAL: mit roten Linsen, Möhren, Kokosmilch, Birnen, frischem Ingwer, Kurkuma, Curry", week.monday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Kurkuma-Käsesuppe: mit Möhren & Zucchini, Schmelzkäse, Kurkuma", week.monday, euro("4.70"), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-06-10`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-06-10.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2019-06-10")
    offers.filter { it.day == week.monday } shouldHaveSize 0
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4

    offers shouldContain LunchOffer(0, "Pasta „Pollo“: Huhn, Möhren, Zucchini, Cherrytomaten in einer cremigen Tomatensoße, verfeinert mit Olivenöl, Frischkäse, Mozzarella, Basilikum, Thymian", week.tuesday, euro("4.70"), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-08-26`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-08-26.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2019-08-26")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4

    offers shouldContain LunchOffer(0, "Quark-Kartoffelsuppe: cremige Suppe, Kartoffeln, Champignons, Wiener, Brokkoli, Kerbel, Schnittlauch, Quark", week.friday, euro("4.70"), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-09-09`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-09-09.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2019-09-09")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4

    offers shouldContain LunchOffer(0, "Möhren - Käse - Suppe: Käsesuppe, Kurkuma, Juliennegemüsestreifen, wahlweise + mit Huhn", week.monday, euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Soupe au Pistou: Gemüsesuppe aus der Provence - grüne Bohnen, weiße Bohnen, Zucchini, Kartoffel, Lauch, Tomaten, wahlweise + Ruccolapesto", week.monday, euro("4.70"), providerId)
  }
}
