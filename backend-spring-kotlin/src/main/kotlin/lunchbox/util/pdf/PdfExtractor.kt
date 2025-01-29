package lunchbox.util.pdf

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.pdfbox.Loader
import org.apache.pdfbox.io.RandomAccessReadBuffer
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL

/**
 * Extrahiert Daten aus PDF-Dateien.
 */
object PdfExtractor {
  private val logger = KotlinLogging.logger {}

  fun extractLines(pdfUrl: URL): List<TextLine> = extract(pdfUrl) { PdfTextGroupStripper().getTextLines(it) }

  fun extractStrings(pdfUrl: URL): List<String> = extractLines(pdfUrl).map { it.toString() }

  fun extractGroups(pdfUrl: URL): List<TextGroup> = extract(pdfUrl) { PdfTextGroupStripper().getTextGroups(it) }

  private fun <T> extract(
    pdfUrl: URL,
    transform: (pdfDoc: PDDocument) -> List<T>,
  ): List<T> {
    var pdfDoc: PDDocument? = null

    try {
      pdfDoc = Loader.loadPDF(RandomAccessReadBuffer(pdfUrl.openStream()))
      if (pdfDoc != null) {
        return transform(pdfDoc)
      }
    } catch (fnf: FileNotFoundException) {
      logger.error(fnf) { "file $pdfUrl not found" }
    } catch (t: IOException) {
      logger.error(t) { "Fehler beim Einlesen von $pdfUrl" }
    } finally {
      pdfDoc?.close()
    }
    return emptyList()
  }
}
