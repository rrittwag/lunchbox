package domain.util

import java.io.FileNotFoundException
import java.net.URL

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.TextPosition
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class PDFTextGroupStripperSpec extends FlatSpec with Matchers with MockFactory {

  it should "equip TextGroup with one letter" in {
    val text_a = text(Pos(1.0f, 2.0f), 1.0f, 2.0f, "a")

    val group = TextGroup(Seq(text_a))

    group.toString should be ("a")

    group.xMin should be (1.0f)
    group.xMid should be (1.5f)
    group.xMax should be (2.0f)

    group.yMin should be (2.0f)
    group.yMid should be (3.0f)
    group.yMax should be (4.0f)

    group.width should be (1.0f)
    group.height should be (2.0f)

    group.xIn(0.8f) should be (false)
    group.xIn(1.0f) should be (true)
    group.xIn(1.5f) should be (true)
    group.xIn(2.0f) should be (true)
    group.xIn(2.2f) should be (false)

    group.yIn(1.8f) should be (false)
    group.yIn(2.0f) should be (true)
    group.yIn(3.0f) should be (true)
    group.yIn(4.0f) should be (true)
    group.yIn(4.2f) should be (false)
  }

  it should "equip TextGroup with three letters" in {
    val text_a = text(Pos(1.0f, 2.0f), 1.0f, 2.0f, "a")
    val text_b = text(Pos(2.0f, 2.0f), 1.0f, 2.0f, "b")
    val text_c = text(Pos(3.0f, 2.0f), 1.0f, 2.0f, "c")

    val group = TextGroup(Seq(text_a, text_b, text_c))

    group.toString should be ("abc")

    group.xMin should be (1.0f)
    group.xMax should be (4.0f)
    group.xMid should be (2.5f)

    group.yMin should be (2.0f)
    group.yMax should be (4.0f)
    group.yMid should be (3.0f)

    group.width should be (3.0f)
    group.height should be (2.0f)

    group.xIn(0.8f) should be (false)
    group.xIn(1.0f) should be (true)
    group.xIn(2.5f) should be (true)
    group.xIn(4.0f) should be (true)
    group.xIn(4.2f) should be (false)

    group.yIn(1.8f) should be (false)
    group.yIn(2.0f) should be (true)
    group.yIn(3.0f) should be (true)
    group.yIn(4.0f) should be (true)
    group.yIn(4.2f) should be (false)
  }

  it should "extract text groups from simple table" in {
    val url = getClass.getResource("/simple_table.pdf")

    val textGroups = extract(url)

    textGroups should have size 4

    val text_11 = textGroups.filter(_.toString == "row 1 column 1")
    text_11 should have size 1

    val text_12 = textGroups.filter(_.toString == "row 1 column 2")
    text_12 should have size 1

    val text_21 = textGroups.filter(_.toString == "row 2 column 1")
    text_21 should have size 1

    val text_22 = textGroups.filter(_.toString == "row 2 column 2")
    text_22 should have size 1

    text_11.head.xIn(text_12.head.xMid) should be (false)
    text_11.head.yIn(text_12.head.yMid) should be (true)

    text_11.head.xIn(text_21.head.xMid) should be (true)
    text_11.head.yIn(text_21.head.yMid) should be (false)

    text_11.head.xIn(text_22.head.xMid) should be (false)
    text_11.head.yIn(text_22.head.yMid) should be (false)
  }

  private def text(pos: Pos, width: Float, height: Float, char: String): TextPosition = {
    val mockPos = mock[TextPosition]
    (mockPos.getX _).expects().returns(pos.x).anyNumberOfTimes()
    (mockPos.getY _).expects().returns(pos.y).anyNumberOfTimes()
    (mockPos.getWidth _).expects().returns(width).anyNumberOfTimes()
    (mockPos.getHeight _).expects().returns(height).anyNumberOfTimes()
    (mockPos.getCharacter _).expects().returns(char).anyNumberOfTimes()
    mockPos
  }

  private def extract(pdfUrl: URL): Seq[TextGroup] = {
    var optPdfDoc: Option[PDDocument] = None
    var pdfContent = Seq[TextGroup]()

    try {
      optPdfDoc = Some(PDDocument.load(pdfUrl))
      for (pdfDoc <- optPdfDoc) {
        val stripper = new PDFTextGroupStripper
        pdfContent = stripper.getTextGroups(pdfDoc)
      }
    } catch {
      case fnf: FileNotFoundException => System.out.println(s"file $pdfUrl not found")
      case t: Throwable => System.out.println(t.getMessage)
    } finally {
      optPdfDoc.foreach(_.close())
    }
    pdfContent
  }

  case class Pos(x: Float, y: Float)
}
