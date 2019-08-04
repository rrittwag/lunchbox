package lunchbox.util.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.slf4j.LoggerFactory
import java.io.FileNotFoundException
import java.net.URL

/**
 * User: robbel
 * Date: 2019-08-05
 */
object PdfExtractor {

  private val logger = LoggerFactory.getLogger(javaClass)

  fun extractLines(pdfUrl: URL): List<TextLine> =
    extract(pdfUrl) {
      val stripper = PdfTextGroupStripper()
      stripper.getTextLines(it)
    }

  fun extractStrings(pdfUrl: URL): List<String> =
    extractLines(pdfUrl).map { it.toString() }

  fun extractGroups(pdfUrl: URL): List<TextGroup> =
    extract(pdfUrl) {
      val stripper = PdfTextGroupStripper()
      stripper.getTextGroups(it)
    }

  private fun <T> extract(pdfUrl: URL, transform: (pdfDoc: PDDocument) -> List<T>): List<T> {
    var pdfDoc: PDDocument? = null

    try {
      pdfDoc = PDDocument.load(pdfUrl.openStream())
      pdfDoc?.let {
        return transform(it)
      }
    } catch (fnf: FileNotFoundException) {
      logger.error("file $pdfUrl not found")
    } catch (t: Throwable) {
      logger.error("Fehler beim Einlesen von $pdfUrl", t)
    } finally {
      pdfDoc?.close()
    }
    return emptyList()
  }
}
