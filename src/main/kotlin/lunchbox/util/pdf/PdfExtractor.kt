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

  fun extractLines(pdfUrl: URL): List<TextLine> {
    var pdfDoc: PDDocument? = null
    var pdfContent = emptyList<TextLine>()

    try {
      pdfDoc = PDDocument.load(pdfUrl)
      pdfDoc?.let {
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
    return pdfContent
  }

  fun extractStrings(pdfUrl: URL): List<String> =
    extractLines(pdfUrl).map { it.toString() }
}
