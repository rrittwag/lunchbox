package lunchbox.domain.logic

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProvider.DAS_KRAUTHOF
import lunchbox.util.html.Html
import lunchbox.util.pdf.PdfTextGroupStripper
import lunchbox.util.pdf.TextLine
import org.apache.pdfbox.pdmodel.PDDocument
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.FileNotFoundException
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class LunchResolverKrauthof(
  val dateValidator: DateValidator
) : LunchResolver {

  private val logger = LoggerFactory.getLogger(javaClass)

  override val provider: LunchProvider = DAS_KRAUTHOF

  override fun resolve(): List<LunchOffer> {
    val pdfLinks = resolvePdfLinks(URL("https://www.daskrauthof.de/karte"))
    return resolveFromPdfs(pdfLinks)
  }

  fun resolvePdfLinks(htmlUrl: URL): List<String> {
    val site = Html.load(htmlUrl)

    val links = site.select("a").map { it.attr("href") }
    return links.filter { it.matches(Regex(""".*/KRAUTHOF.*.pdf""")) }
  }

  fun resolveFromPdfs(pdfPaths: List<String>): List<LunchOffer> =
    // TODO: parallelize
    pdfPaths.map { resolveFromPdf(URL(it)) }.flatten()

  fun resolveFromPdf(pdfUrl: URL): List<LunchOffer> {
    val pdfContent = extractPdfContent(pdfUrl)

    val monday = parseMondayFromContent(pdfContent)
    if (monday == null || !dateValidator.isValid(monday))
      return emptyList()
    return resolveFromPdfContent(pdfContent, monday)
  }

  private fun resolveFromPdfContent(pdfContent: List<String>, monday: LocalDate): List<LunchOffer> {
    val rows = mutableListOf<OfferRow>()
    var currentRow: OfferRow? = null

    fun finishOffer() {
      currentRow?.let { if (it.isValid()) rows.add(it) }
      currentRow = null
    }

    for (line in pdfContent.map { it.trim() }) {
      val matchOffer = Regex("""(.+)(\d+[.,]\d{2}) *€\*""").find(line)
      if (matchOffer != null) {
        val (text, priceString) = matchOffer.destructured
        finishOffer()
        currentRow = OfferRow(parseName(text), parsePrice(priceString))
        continue
      }

      if (line.isEmpty()) {
        finishOffer()
        continue
      }

      val matchLastLine = Regex(""".*inklusive Tagesgetränk.*""").find(line)
      if (matchLastLine != null) {
        finishOffer()
        continue
      }

      val thisRow = OfferRow(parseName(line), null)
      currentRow = currentRow?.merge(thisRow) ?: thisRow
    }

    val mondayOffers = rows.map { row ->
      LunchOffer(0, row.name, monday, row.price!!, provider.id)
    }

    // an allen Wochentagen gibt es das selbe Angebot
    return LongRange(0, 4).flatMap { weekdayLong ->
      mondayOffers.map { it.copy(day = monday.plusDays(weekdayLong)) }
    }
  }

  private fun extractPdfContent(pdfUrl: URL): List<String> {
    var pdfDoc: PDDocument? = null
    var pdfContent = emptyList<TextLine>()

    try {
      pdfDoc = PDDocument.load(pdfUrl)
      if (pdfDoc != null) {
        val stripper = PdfTextGroupStripper()
        pdfContent = stripper.getTextLines(pdfDoc)
      }
    } catch (fnf: FileNotFoundException) {
      logger.error("file $pdfUrl not found")
    } catch (t: Throwable) {
      logger.error("Fehler beim Einlesen von $pdfUrl", t)
    } finally {
      pdfDoc?.close()
    }
    return pdfContent.map { it.toString() }
  }

  private fun parseMondayFromContent(lines: List<String>): LocalDate? {
    // alle Datumse aus PDF ermitteln
    val days = lines.mapNotNull { parseDay(it) }
    val mondays = days.map { it.with(DayOfWeek.MONDAY) }

    // den Montag der am häufigsten verwendeten Woche zurückgeben
    return mondays
      .groupBy { it }
      .maxBy { it.value.size }
      ?.key
  }

  private fun parseDay(dayString: String): LocalDate? {
    Regex(""".*(\d{2}.\d{2}.\d{4}).*""").find(dayString)?.let {
      val (dateString) = it.destructured
      return parseLocalDate(dateString, "dd.MM.yyyy")
    }

    Regex(""".*(\d{2}.\d{2}.\d{2}).*""").find(dayString)?.let {
      val (dateString) = it.destructured
      return parseLocalDate(dateString, "dd.MM.yy")
    }

    Regex(""".*(\d{2}.\d{2}).*""").find(dayString)?.let {
      val (dateString) = it.destructured
      val yearToday = LocalDate.now().year
      val year =
        if (LocalDate.now().monthValue == 12 && dateString.endsWith("01"))
          yearToday + 1
        else
          yearToday
      return parseLocalDate("$dateString.$year", "dd.MM.yyyy")
    }

    return null
  }

  private fun parseLocalDate(dateString: String, dateFormat: String): LocalDate? =
    try {
      LocalDate.from(DateTimeFormatter.ofPattern(dateFormat).parse(dateString))
    } catch (exc: Throwable) {
      null
    }

  private fun parseName(text: String): String =
    text.trim()
      .replace("  ", " ")
      .replace("–", "-")
      .replace(Regex(""" *[│|] *"""), ", ")
      .replace(" I ", ", ")
      .replace(" *,", ",")
      .trim()

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   *
   * @param priceString String im Format "*0,00*"
   * @return
   */
  private fun parsePrice(priceString: String): Money? {
    val regex = Regex(""".*(\d+)[.,](\d{2}).*""")
    val matchResult = regex.find(priceString)
      ?: return null
    val (major, minor) = matchResult.destructured
    return Money.ofMinor(CurrencyUnit.EUR, major.toLong() * 100 + minor.toLong())
  }

  data class OfferRow(
    val name: String,
    val price: Money?
  ) {
    fun merge(otherRow: OfferRow): OfferRow {
      val newName = listOf(name, otherRow.name).filter { it.isNotEmpty() }.joinToString(" ")
      return OfferRow(newName, price ?: otherRow.price)
    }

    fun isValid() = name.isNotEmpty() && price != null
  }
}
