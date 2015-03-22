package info.rori.lunchbox.server.akka.scala.domain.logic

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.{TextPosition, PDFTextStripper}
import org.scalactic.Tolerance._
import org.scalactic.TripleEquals._

class PDFTextGroupStripper extends PDFTextStripper {
  var textGroups = Seq[TextGroup]()

  override def processTextPosition(text: TextPosition): Unit = {
    super.processTextPosition(text)

    textGroups.find(e => (e.xMax === text.getX +- 0.1f) && (e.yMin === text.getY +- 0.1f)) match {
      case Some(e) => e.add(text)
      case None => textGroups :+= TextGroup(text :: Nil)
    }
  }

  def getTextGroups(pDDocument: PDDocument): Seq[TextGroup] = {
    getText(pDDocument)
    textGroups
  }
}

case class TextGroup(var positions: Seq[TextPosition]) {
  validate()

  def add(position: TextPosition): Unit = {
    validate()
    positions :+= position
  }

  def xMin: Float = positions.map(e => e.getX).min

  def xMax: Float = positions.map(e => e.getX + e.getWidth).max

  def yMin: Float = positions.map(e => e.getY).min

  def yMax: Float = positions.map(e => e.getY + e.getHeight).max

  def xMid: Float = (xMin + xMax) / 2

  def yMid: Float = (yMin + yMax) / 2

  def width: Float = xMax - xMin

  def height: Float = yMax - yMin

  def xIn(xVal: Float): Boolean = xMin <= xVal && xVal <= xMax

  def yIn(yVal: Float): Boolean = yMin <= yVal && yVal <= yMax

  override def toString: String = positions.map(_.getCharacter).mkString("").trim

  private def validate(): Unit = {
    require(positions.nonEmpty)
    require(positions.forall(positions.head.getY === _.getY +- 0.1f))
    require(positions.forall(positions.head.getHeight === _.getHeight +- 0.1f))
  }
}