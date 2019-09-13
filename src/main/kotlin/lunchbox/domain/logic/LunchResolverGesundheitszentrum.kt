package lunchbox.domain.logic

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.GESUNDHEITSZENTRUM
import lunchbox.util.facebook.Attachment
import lunchbox.util.facebook.FacebookGraphApi
import lunchbox.util.facebook.Image
import lunchbox.util.facebook.Post
import lunchbox.util.facebook.Posts
import lunchbox.util.facebook.query
import lunchbox.util.ocr.OcrClient
import lunchbox.util.string.StringParser
import org.joda.money.Money
import org.springframework.stereotype.Component
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate

data class Wochenplan(
  val monday: LocalDate,
  val mittagsplanImageId: String
)

/**
 * Mittagsangebote von Gesundheitszentrum Springpfuhl über deren Facebook-Seite ermitteln.
 */
@Component
class LunchResolverGesundheitszentrum(
  val dateValidator: DateValidator,
  val graphApi: FacebookGraphApi,
  val ocrClient: OcrClient
) : LunchResolver {

  override val provider = GESUNDHEITSZENTRUM

  override fun resolve(): List<LunchOffer> {
    // von der Facebook-Seite der Kantine die Posts als JSON abfragen (beschränt auf Text und Anhänge)
    val facebookPosts = graphApi.query<Posts>("181190361991823/posts?fields=message,attachments")
      ?: return emptyList()

    val wochenplaene =
      parseWochenplaene(facebookPosts.data)
        .takeWhile { isWochenplanRelevant(it) }

    return resolveOffersFromWochenplaene(wochenplaene)
  }

  fun parseWochenplaene(posts: List<Post>): List<Wochenplan> {
    val plaene = mutableListOf<Wochenplan>()

    for (post in posts) {
      val monday =
        // im Text steckt das Datum der Woche
        StringParser
          .parseLocalDate(post.message.replace("\\n", "|"))
          ?.with(DayOfWeek.MONDAY) ?: continue

      val imageAttachment = findImageAttachment(post) ?: continue
      val imageId = imageAttachment.target.id

      plaene += Wochenplan(monday, imageId)
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

  fun resolveOffersFromWochenplaene(wochenplaene: List<Wochenplan>): List<LunchOffer> =
    // TODO: parallelize
    wochenplaene.flatMap { resolveOffersFromWochenplan(it) }.distinct()

  fun resolveOffersFromWochenplan(plan: Wochenplan): List<LunchOffer> {
    val facebookImage = graphApi.query<Image>("${plan.mittagsplanImageId}?fields=images")
      ?: return emptyList()

    val ocrText = doOcr(parseUrlOfBiggestImage(facebookImage))
    return resolveOffersFromText(plan.monday, ocrText)
  }

  fun doOcr(url: URL?): String = url?.let { ocrClient.doOCR(it) } ?: ""

  fun parseUrlOfBiggestImage(image: Image): URL? {
    val sizedImages = image.images
    val biggestImage = sizedImages.maxBy { it.height }
    return biggestImage?.source
  }

  fun resolveOffersFromText(monday: LocalDate, text: String): List<LunchOffer> =
    groupBySection(text)
      .filterKeys { PdfSection.weekdayValues.contains(it) }
      .flatMap { (section, lines) -> resolveOffersFromSection(section, lines, monday) }

  private fun groupBySection(text: String): Map<PdfSection, List<String>> {
    val result = mutableMapOf<PdfSection, List<String>>()

    var currentSection: PdfSection = PdfSection.HEADER
    var linesForSection = mutableListOf<String>()

    for (unformattedLine in text.split('\n')) {
      val line = unformattedLine.trim()
        .replace("Nlittwoch", "Mittwoch")
        .replace("Freng", "Freitag")

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
    val rows = resolveOfferRows(lines)
    return rows.filter { it.price != null }.map {
      LunchOffer(0, it.name, monday.plusDays(section.order), it.price!!, provider.id)
    }
  }

  private fun resolveOfferRows(lines: List<String>): List<OfferRow> {
    val result = mutableListOf<OfferRow>()
    var currentRow: OfferRow? = null

    fun mergeRow(newRow: OfferRow) {
      currentRow = currentRow?.merge(newRow) ?: newRow
    }

    val cleanedLines =
      lines
        .takeWhile { !isEndingSection(it) }
        .map { removeUnnecessaryText(correctOcrErrors(it)) }

    for (line in cleanedLines) {
      if (line.matches(Regex("""^[F\d]\.?( .*)?$"""))) {
        if (currentRow != null)
          result += currentRow!!
        currentRow = null
      }

      val correctedLine = line.replaceFirst(Regex("""^[F\d]\.?"""), "")

      val matchResult = Regex("""(.*) (\d+,\d{2})""").find(correctedLine)
      if (matchResult != null) {
        val (name, priceStr) = matchResult.destructured
        mergeRow(OfferRow(cleanName(name), StringParser.parseMoney(priceStr)))
      } else
        mergeRow(OfferRow(cleanName(correctedLine), null))
    }
    if (currentRow != null)
      result += currentRow!!

    return result
  }

  private fun correctOcrErrors(line: String) =
    line.trim()
      .replace("‚", ",")
      .replace(Regex("""^[fF][.,]* """), "F. ")
      .replace(Regex("""^(\d)[.,]+ """), "$1. ")
      .replace(Regex("""^l[.,]+ """), "1. ")
      .replace(Regex("""^(\d)A """), "$1. ")
      .replace(Regex("^Zl "), "2. ")
      .replace(Regex("""L,(\d{2}) ?€?$"""), "4,$1")
      .replace(Regex("""(\d+) ?[.,] ?(\d{2}) ?[€g]?$"""), "$1,$2")
      .replace(Regex("""(\d)(\d{2}) ?[€g]?$"""), "$1,$2")
      .replace(Regex(""" 1,(\d{2})$"""), " 4,$1") // für 1,**€ gibt's nix mehr, OCR-Fehler für 4,**€
      .replace("ﬂ", "fl")
      .replace("ﬁ", "fi")
      .replace("IVi", "M")
      .replace("IVl", "M")
      .replace(Regex("""([a-zA-ZäöüßÄÖÜ])II"""), "$1ll")
      .replace(Regex("""([a-zA-ZäöüßÄÖÜ])I"""), "$1l")
      .replace("artoffei", "artoffel")
      .replace("uiasch", "ulasch")
      .replace("oiikorn", "ollkorn")
      .replace("Kiöße", "Klöße")
      .replace("kcai", "kcal")
      .replace("Müiier", "Müller")
      .replace("oreiie", "orelle")
      .replace("Saiz", "Salz")
      .replace(Regex("([kK]oh)i"), "$1l")
      .replace(Regex("([mM])iich"), "$1ilch")
      .replace("udein", "udeln")
      .replace("zeriassen", "zerlassen")
      .replace("Bitteki", "Bifteki")
      .replace(Regex("([bB])iatt"), "$1latt")
      .replace(Regex("([zZ]wiebe)i"), "$1l")
      .replace("chnitzei", "chnitzel")
      .replace("SCHNlTZ", "SCHNITZ")
      .replace("SCHNITZELTAG", "Schnitzeltag") // Wieso rumschreien?
      .replace("uflauf", "uflauf")
      .replace("utiauf", "uflauf")
      .replace("ufiauf", "uflauf")
      .replace("uflaufmit", "uflauf mit")
      .replace("Fiahm", "Rahm")
      .replace("Fiei", "Rei")
      .replace("Fiührei", "Rührei")
      .replace("Blatispinat", "Blattspinat")
      .replace("‘/2", "1/2")
      .replace("au/3er", "außer")
      .replace(Regex("""auf([A-Z])"""), "auf $1")
      .replace("Fiind", "Rind")
      .replace("Fiotkohl", "Rotkohl")
      .replace("CeVapcici", "Cevapcici")
      .replace(Regex("kc[nu]l"), "kcal")
      .replace(Regex("""([^s])e-Hackfleisch"""), "$1Käse-Hackfleisch") // fuckin bad OCR
      .replace(Regex("""([^J])agerschnitzel""""), "$1\"Jägerschnitzel\"")
      .replace("Matjesmpt ", "Matjestopf ")
      .replace("Reibeküse", "Reibekäse")
      .replace("falisch", "fälisch")
      .replace("lll ", "!!! ")
      .replace(" lll", " !!!")
      .replace("ScharfeseKartoffelePaprikszurry", "Scharfes Kartoffel-Paprika-Curry")
      .replace("Möhren7Zucchiniinntopf", "Möhren-Zucchini-Eintopf")
      .replace("Häl111cl1e11sclu1itzel", "Hähnchenschnitzel")
      .replace("Pfeffelralnnsauce", "Pfefferrahmsauce")

  private fun removeUnnecessaryText(text: String) =
    text.trim()
      .replace(Regex("""^FlTNESS [fF\d]\.* """), "F. ")
      .replace(Regex("""^FlTNESS """), "")
      .replace(Regex(""" ,?[unm]; """), " ")
      .replace(Regex(""" \d+ kcal"""), "")
      .replace(Regex(""" [a-zA-Z]+kcal"""), " ")
      .replace(" kcal", "")
      .replace(Regex(" 2 ?und "), " und ")
      .replace(Regex(" 2 ?mit "), " mit ")
      .replace(" , ", ", ")
      .replace(Regex(" 3.14 "), " ")
      .replace(" 1,14m ", " ")
      .replace(Regex(""" \d+[a-zA-Z]+ """), " ")
      .replace(Regex(""" [a-zA-Z]+\d+ """), " ")
      .replace(Regex(""" [a-zA-Z] """), " ")
      .replace(Regex(""" [A-Z][^ ]+[A-Z] """), " ")
      .replace(Regex(""" \|4 """), " ")
      .replace(Regex(""" [Agl(‘!]{1,5} +(\d,\d\d)$"""), " $1")
      .replace(Regex(""" [A-Z]{2}[^ ]* +(\d,\d\d)$"""), " $1")
      .replace("!!!", "")
      .replace("Amame", "")
      .replace("Acu", "")
      .replace(Regex(""" [^ ]+![^ ]+ """), "")
      .replace(Regex(""" [^ ]+![^ ]+$"""), "")
      .replace(Regex(""" [A-Za-z]+[A-Z0-9!].* +(\d,\d\d)$"""), " $1")
      .replace(Regex(""" [0-9]+ +(\d,\d\d)$"""), " $1")
      .replace(Regex(""" [0-9 |l]+$"""), "").trim() // schlecht OCR-ed Zusatzstoffe

  private fun isEndingSection(line: String): Boolean =
    line.startsWith("ACHTUNG") || line.startsWith("Wir wünschen") ||
      line.startsWith("Öffnungszeiten") || line.startsWith("Alle Spelsen")

  private fun cleanName(text: String) = text.replace(" 2 *$", "").trim()

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
    val price: Money?
  ) {
    fun merge(otherRow: OfferRow): OfferRow {
      val newName = listOf(name, otherRow.name).filter { it.isNotEmpty() }.joinToString(" ")
      return OfferRow(newName, price ?: otherRow.price)
    }

    fun isValid() = name.isNotEmpty() && price != null
  }
}
