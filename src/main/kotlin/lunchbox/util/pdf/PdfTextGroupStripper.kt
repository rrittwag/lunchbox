package lunchbox.util.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition
import kotlin.math.abs

fun Float.nearby(that: Float): Boolean = abs(this - that) <= 1.0f

class PdfTextGroupStripper : PDFTextStripper() {
  private val textGroups = mutableListOf<TextGroup>()

  override fun processTextPosition(text: TextPosition) {
    super.processTextPosition(text)

    val preText =
      textGroups.find { it.xMax().nearby(text.x) && it.yMin().nearby(text.y) }

    if (preText == null)
      textGroups += TextGroup(listOf(text))
    else
      preText.add(text)
  }

  fun getTextGroups(pDDocument: PDDocument): List<TextGroup> {
    getText(pDDocument)
    return textGroups
  }

  fun getTextLines(pDDocument: PDDocument): List<TextLine> =
    TextLine.toLines(getTextGroups(pDDocument))
}

data class TextGroup(var positions: List<TextPosition>) {
  init {
    validate()
  }

  fun add(position: TextPosition) {
    validate()
    positions += position
  }

  fun xMin(): Float = positions.map { it.x }.min() ?: 0f
  fun xMax(): Float = positions.map { it.x + it.width }.max() ?: 0f
  fun yMin(): Float = positions.map { it.y }.min() ?: 0f
  fun yMax(): Float = positions.map { it.y + it.height }.max() ?: 0f
  fun xMid(): Float = (xMin() + xMax()) / 2
  fun yMid(): Float = (yMin() + yMax()) / 2
  fun width(): Float = xMax() - xMin()
  fun height(): Float = yMax() - yMin()
  fun xIn(xVal: Float): Boolean = xMin() <= xVal && xVal <= xMax()
  fun yIn(yVal: Float): Boolean = yMin() <= yVal && yVal <= yMax()

  override fun toString(): String =
    positions.joinToString("").trim()

  private fun validate() {
    require(positions.isNotEmpty())
    require(positions.all { positions[0].y.nearby(it.y) })
  }
}

data class TextLine(val y: Float, val texts: List<TextGroup>) {
  fun oneTextMatches(regex: Regex): Boolean =
    texts.any { it.toString().matches(regex) }

  fun allTextsMatch(regex: Regex): Boolean =
    texts.all { it.toString().matches(regex) }

  override fun toString(): String =
    texts.joinToString(" ")

  companion object {
    fun toLines(texts: List<TextGroup>): List<TextLine> {
      val textMap = mutableMapOf<Float, List<TextGroup>>()
      for (text in texts) {
        val entry = textMap.entries.find { text.yIn(it.key) }
        if (entry == null)
          textMap[text.yMid()] = listOf(text)
        else
          entry.setValue(entry.value + text)
      }

      return textMap.entries
        .map { e -> TextLine(e.key, e.value.sortedBy { it.xMin() }) }
        .sortedBy { it.y }
    }
  }
}
