package lunchbox.domain.resolvers

import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.GESUNDHEITSZENTRUM
import lunchbox.util.date.DateValidator
import lunchbox.util.date.HolidayUtil
import lunchbox.util.facebook.Attachment
import lunchbox.util.facebook.FacebookGraphApi
import lunchbox.util.facebook.Image
import lunchbox.util.facebook.Post
import lunchbox.util.facebook.Posts
import lunchbox.util.facebook.query
import lunchbox.util.html.HtmlParser
import lunchbox.util.ocr.OcrClient
import lunchbox.util.string.StringParser
import org.joda.money.Money
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

/**
 * Mittagsangebote von Gesundheitszentrum Springpfuhl über deren Facebook-Seite ermitteln.
 */
@Component
class LunchResolverGesundheitszentrum(
  val dateValidator: DateValidator,
  val graphApi: FacebookGraphApi,
  val htmlParser: HtmlParser,
  val ocrClient: OcrClient
) : LunchResolver {

  override val provider = GESUNDHEITSZENTRUM

  override fun resolve(): List<LunchOffer> {
    // Als Folge des Datenskandals um Cambridge Analytica beschränkt Facebook den Zugriff auf
    // seine Graph API. Ab 04.09.2019 können nur noch verifizierte Firmen auf die öffentlichen
    // Posts der Nutzer zugreifen. Blöd für die Lunchbox ...

    // return resolveByGraphApi()

    // ... Na dann müssen wir halt die Webseite parsen. Allerdings ist die Webseite als WebApp
    // mit viel JavaScript-Magie realisiert und rendert nur im Browser vollständig ... oder mit
    // Rendertron.

    return resolveByHtml()
  }

  fun resolveByHtml(): List<LunchOffer> {
    val wochenplaene =
      parseWochenplaeneByHtml(provider.menuUrl)

    return resolveOffersFromWochenplaene(wochenplaene)
  }

  fun parseWochenplaeneByHtml(url: URL): List<Wochenplan> {
    val site = htmlParser.renderAndParse(url)
    val articles = site.select("""div[role="article"]""")

    return articles.mapNotNull { parseWochenplanByHtml(it) }
  }

  fun parseWochenplanByHtml(article: Element): Wochenplan? {
    val monday =
      // im Text steckt das Datum der Woche
      StringParser
        .parseLocalDate(article.text())
        ?.with(DayOfWeek.MONDAY) ?: return null

    val imageLink =
      article
        .selectFirst("a[data-ploi]")
        .attr("data-ploi")
        ?: return null

    return Wochenplan(monday, URL(imageLink))
  }

  private fun resolveByGraphApi(): List<LunchOffer> {
    // von der Facebook-Seite der Kantine die Posts als JSON abfragen (beschränkt auf
    // Text und Anhänge)
    val facebookPosts = graphApi.query<Posts>("181190361991823/posts?fields=message,attachments")
      ?: return emptyList()

    val wochenplaene =
      parseWochenplaeneByGraphApi(facebookPosts.data)
        .mapNotNull { (monday, mittagsplanImageId) ->
          parseImageLink(mittagsplanImageId)?.let { Wochenplan(monday, it) }
        }

    return resolveOffersFromWochenplaene(wochenplaene)
  }

  fun parseWochenplaeneByGraphApi(posts: List<Post>): List<WochenplanWithImageId> {
    val plaene = mutableListOf<WochenplanWithImageId>()

    for (post in posts) {
      val monday =
        // im Text steckt das Datum der Woche
        StringParser
          .parseLocalDate(post.message.replace("\\n", "|"))
          ?.with(DayOfWeek.MONDAY) ?: continue

      val imageAttachment = findImageAttachment(post) ?: continue
      val imageId = imageAttachment.target.id

      plaene += WochenplanWithImageId(monday, imageId)
    }

    return plaene
  }

  private fun findImageAttachment(post: Post): Attachment? {
    // Der 1. Anhang enthält das Bild mit dem Mittagsplan
    val attachment = post.attachments.data.getOrNull(0)
      ?: return null

    // ... außer es gibt SubAttachments. Dann gewinnt das 1. SubAttachment.
    if (attachment.subattachments.data.isNotEmpty())
      return attachment.subattachments.data[0]

    return attachment
  }

  private fun parseImageLink(mittagsplanImageId: String): URL? {
    val facebookImage = graphApi.query<Image>("$mittagsplanImageId?fields=images")
      ?: return null
    return parseImageLink(facebookImage)
  }

  fun parseImageLink(image: Image): URL? {
    // das Bild mit der höchsten Auflösung bringt die besten OCR-Ergebnisse
    val sizedImages = image.images
    val biggestImage = sizedImages.maxBy { it.height }
    return biggestImage?.source
  }

  private fun resolveOffersFromWochenplaene(wochenplaene: List<Wochenplan>): List<LunchOffer> =
    // TODO: parallelize
    wochenplaene
      .distinctBy { it.monday }
      .filter { isWochenplanRelevant(it) }
      .flatMap { resolveOffersFromWochenplan(it) }
      .filterNot { HolidayUtil.isHoliday(it.day, provider.location) }

  fun resolveOffersFromWochenplan(plan: Wochenplan): List<LunchOffer> {
    val ocrText = doOcr(plan.mittagsplanImageUrl)
    return resolveOffersFromText(plan.monday, ocrText)
  }

  fun doOcr(url: URL?): String = url?.let { ocrClient.doOCR(it) } ?: ""

  fun resolveOffersFromText(monday: LocalDate, text: String): List<LunchOffer> =
    groupBySection(text)
      .filterKeys { PdfSection.weekdayValues.contains(it) }
      .flatMap { (section, lines) -> resolveOffersFromSection(section, lines, monday) }

  private fun groupBySection(text: String): Map<PdfSection, List<String>> {
    val result = mutableMapOf<PdfSection, List<String>>()

    var currentSection: PdfSection = PdfSection.HEADER
    var linesForSection = mutableListOf<String>()

    for (line in text.split('\n').map { correctWeekday(it) }) {
      val section = PdfSection.values().find { sec -> line.startsWith(sec.label) }
      if (section != null) {
        result += currentSection to linesForSection
        currentSection = section
        linesForSection = mutableListOf(line.replaceFirst(section.label, "").trim())
      } else
        linesForSection.add(line)
    }

    return result
  }

  private fun resolveOffersFromSection(
    section: PdfSection,
    lines: List<String>,
    monday: LocalDate
  ): List<LunchOffer> {
    val rows = convertToRows(lines)
    val mergedRows = mergeRows(rows)

    return mergedRows.filter { it.price != null }.map {
      LunchOffer(
        0,
        it.name,
        "",
        monday.plusDays(section.order),
        it.price!!,
        emptySet(),
        provider.id
      )
    }
  }

  private fun convertToRows(lines: List<String>): List<OfferRow> {
    val result = mutableListOf<OfferRow>()

    for (rawLine in lines.takeWhile { !isEndingSection(it) }) {
      val line = correctRowName(correctRowPrice(correctRowNumber(rawLine)))

      val lineWithoutFitnessTag =
        line.replace(Regex("^Fitness "), "").trim()

      val lineWithoutNumber =
        lineWithoutFitnessTag.replace(Regex("""^[F\d]\. """), "").trim()

      var lineWithoutPrice = lineWithoutNumber
      var price: Money? = null

      val matchResult = Regex("""(.*) (\d+,\d{2}) €$""").find(lineWithoutNumber)
      if (matchResult != null) {
        val (lineBeforePrice, priceStr) = matchResult.destructured
        lineWithoutPrice = lineBeforePrice
        price = StringParser.parseMoney(priceStr)
      }

      val name = removeZusatzinfos(lineWithoutPrice)
      result += OfferRow(
        name,
        price,
        line != lineWithoutFitnessTag,
        lineWithoutFitnessTag != lineWithoutNumber,
        lineWithoutPrice != name
      )
    }

    return result
  }

  private fun mergeRows(rows: List<OfferRow>): List<OfferRow> {
    val result = mutableListOf<OfferRow>()

    var undone: OfferRow? = null
    for (row in mergeRowsByIncompleteStartEnd(rows)) {
      if (undone == null) {
        undone = row
        continue
      }

      if (!undone.isCompleteOffer()) {
        undone = undone.merge(row)
        continue
      }

      if ((undone.hasZusatzinfos && row.hasZusatzinfos) || row.hasNumberOrFitnessTag()) {
        result += undone
        undone = row
        continue
      }

      undone = undone.merge(row)
    }

    if (undone != null && undone.isCompleteOffer())
      result += undone

    return result
  }

  private fun mergeRowsByIncompleteStartEnd(rows: List<OfferRow>): List<OfferRow> {
    val result = mutableListOf<OfferRow>()

    var index = 0
    while (index + 1 < rows.size) {
      val first = rows[index]
      val second = rows[index + 1]
      if (first.hasIncompleteEnd() || second.hasIncompleteStart()) {
        result += first.merge(second)
        index += 2
      } else {
        result += first
        index += 1
      }
    }
    if (index < rows.size)
      result += rows[index]

    return result
  }

  private fun correctWeekday(text: String) =
    text.trim()
      .replace("Nlittwoch", "Mittwoch")
      .replace("Freng", "Freitag")
      .replace("Freita;", "Freitag")

  private fun isEndingSection(line: String): Boolean =
    line.startsWith("ACHTUNG") ||
      line.startsWith("Wir wünschen") ||
      line.startsWith("Öffnungszeiten") ||
      line.startsWith("Alle Spelsen") ||
      line.contains(" Mit ")

  private fun correctRowNumber(row: String): String {
    val rowWithCorrectedFitness =
      row.trim()
        .replace(Regex("""^FlTNESS """), "Fitness ")

    val rowWithoutFitness =
      rowWithCorrectedFitness
        .replace(Regex("""^FITNESS """), "")

    val correctedRowWithoutFitness =
      rowWithoutFitness.trim()
        .replace(Regex("""^(\d)[.,:;]* """), "$1. ")
        .replace(Regex("""^[fF][.,:;]* """), "F. ")
        .replace("T. ", "1. ")
        .replace(Regex("""^\. """), "1. ")
        .replace(Regex("^[0-9]$"), "") // Nummer ist in eigene Zeile gerutscht

    return if (rowWithoutFitness == rowWithCorrectedFitness)
        correctedRowWithoutFitness
    else
      "Fitness $correctedRowWithoutFitness"
  }

  private fun correctRowPrice(row: String) =
    row.trim()
      .replace(Regex(" 40€$"), " 4,90 €") // Hmm, blöd
      .replace(Regex("9 *€$"), "0 €") // Der Preis ist immer in 10er Cents
      .replace(Regex("""(\d+) ?[.,]? ?(\d{2}) ?[€g]?$"""), "$1,$2 €")
      .trim()

  private fun correctRowName(row: String) =
    row.trim()
      .replace("*", "\"")
      .replace(Regex("[„“”]"), "\"")
      .replace(Regex(",([a-zA-Z])"), ", $1")
      .replace("%!B(MISSING)irne", "1/2 Birne")
      .replace("Scharfes-", "Scharfes ")
      .replace("Gamembert", "Camembert")
      .replace("Ghampignon", "Champignon")
      .replace("Spätze", "Spätzle")
      .replace("2Setzeier", "2 Setzeier")
      .replace("M2 Eier", "4/2 Eier")
      .replace("Gnocei", "Gnocci")
      .replace("Cevapeici", "Cevapcici")
      .replace("!!!", "")
      .replace("SCHNITZELTAG", "Schnitzeltag") // Wieso rumschreien?
      .replace(Regex("""([^s])e-Hackfleisch"""), "$1Käse-Hackfleisch") // fuckin bad OCR
      .trim()

  private fun removeZusatzinfos(name: String) =
    name.trim()
      .replace(Regex(" 3.14 "), " ")
      .replace(Regex(" 2 ?und "), " und ")
      .replace(" kcal", "")
      .replace(Regex(""" \d+ [kK]cal"""), "")
      .replace(Regex(""" [^ ]*[\d!_.\[\]{}|]+[^ ]*$"""), "")
      .replace(Regex(""" [A-Z0-9 ()]+$"""), "")
      .replace(Regex(""" [acgiınulACDGHIJU]{1,5}$"""), "")
      .replace(Regex("""^[acgiınulACDGHIJU]{1,5}$"""), "")
      .trim()

  private fun isWochenplanRelevant(wochenplan: Wochenplan): Boolean =
    dateValidator.isValid(wochenplan.monday)

  enum class PdfSection(
    val label: String,
    val order: Long
  ) {
    HEADER("<<Header>>", 0),
    MONTAG("Montag", 0),
    DIENSTAG("Dienstag", 1),
    MITTWOCH("Mittwoch", 2),
    DONNERSTAG("Donnerstag", 3),
    FREITAG("Freitag", 4),
    FOOTER("Für unseren L", 0);

    companion object {
      val weekdayValues = listOf(MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG)
    }
  }

  data class OfferRow(
    val name: String,
    val price: Money?,
    val hasFitnessTag: Boolean,
    val hasNumber: Boolean,
    val hasZusatzinfos: Boolean
  ) {
    fun merge(otherRow: OfferRow) =
      OfferRow(
        "$name ${otherRow.name}".trim(),
        price ?: otherRow.price,
        hasFitnessTag || otherRow.hasFitnessTag,
        hasNumber || otherRow.hasNumber,
        hasZusatzinfos || otherRow.hasZusatzinfos
      )

    fun hasNumberOrFitnessTag() = hasNumber || hasFitnessTag
    fun isCompleteOffer() =
      name.isNotEmpty() && price != null && !hasIncompleteStart() && !hasIncompleteEnd()

    fun hasIncompleteStart(): Boolean =
      name.matches(Regex("[a-züäö]+ .*")) // beginnt klein geschrieben (und, dazu, mit, ...)

    fun hasIncompleteEnd(): Boolean =
      name.matches(Regex(".* (dazu|mit|und|in|an)")) // endet mit Bindewort
  }

  data class WochenplanWithImageId(
    val monday: LocalDate,
    val mittagsplanImageId: String
  )

  data class Wochenplan(
    val monday: LocalDate,
    val mittagsplanImageUrl: URL
  )
}
