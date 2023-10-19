package lunchbox.util.pdf

import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldHaveSize
import org.apache.pdfbox.text.TextPosition
import org.junit.jupiter.api.Test

class PdfTextGroupStripperTest {
  @Test
  fun `should equip TextGroup with one letter`() {
    val textA = text(Pos(1.0f, 2.0f), 1.0f, 2.0f, "a")

    val group = TextGroup(listOf(textA))

    group.toString() shouldBeEqualTo "a"

    group.xMin() shouldBeEqualTo 1.0f
    group.xMid() shouldBeEqualTo 1.5f
    group.xMax() shouldBeEqualTo 2.0f

    group.yMin() shouldBeEqualTo 2.0f
    group.yMid() shouldBeEqualTo 3.0f
    group.yMax() shouldBeEqualTo 4.0f

    group.width() shouldBeEqualTo 1.0f
    group.height() shouldBeEqualTo 2.0f

    group.xIn(0.8f) shouldBe false
    group.xIn(1.0f) shouldBe true
    group.xIn(1.5f) shouldBe true
    group.xIn(2.0f) shouldBe true
    group.xIn(2.2f) shouldBe false

    group.yIn(1.8f) shouldBe false
    group.yIn(2.0f) shouldBe true
    group.yIn(3.0f) shouldBe true
    group.yIn(4.0f) shouldBe true
    group.yIn(4.2f) shouldBe false
  }

  @Test
  fun `should equip TextGroup with three letters`() {
    val textA = text(Pos(1.0f, 2.0f), 1.0f, 2.0f, "a")
    val textB = text(Pos(2.0f, 2.0f), 1.0f, 2.0f, "b")
    val textC = text(Pos(3.0f, 2.0f), 1.0f, 2.0f, "c")

    val group = TextGroup(listOf(textA, textB, textC))

    group.toString() shouldBeEqualTo "abc"

    group.xMin() shouldBeEqualTo 1.0f
    group.xMax() shouldBeEqualTo 4.0f
    group.xMid() shouldBeEqualTo 2.5f

    group.yMin() shouldBeEqualTo 2.0f
    group.yMax() shouldBeEqualTo 4.0f
    group.yMid() shouldBeEqualTo 3.0f

    group.width() shouldBeEqualTo 3.0f
    group.height() shouldBeEqualTo 2.0f

    group.xIn(0.8f) shouldBe false
    group.xIn(1.0f) shouldBe true
    group.xIn(2.5f) shouldBe true
    group.xIn(4.0f) shouldBe true
    group.xIn(4.2f) shouldBe false

    group.yIn(1.8f) shouldBe false
    group.yIn(2.0f) shouldBe true
    group.yIn(3.0f) shouldBe true
    group.yIn(4.0f) shouldBe true
    group.yIn(4.2f) shouldBe false
  }

  @Test
  fun `extract text groups from simple table`() {
    val url = javaClass.getResource("/simple_table.pdf")

    val textGroups = PdfExtractor.extractGroups(url)

    textGroups shouldHaveSize 4

    val text11 = textGroups.filter { it.toString() == "row 1 column 1" }
    text11 shouldHaveSize 1

    val text12 = textGroups.filter { it.toString() == "row 1 column 2" }
    text12 shouldHaveSize 1

    val text21 = textGroups.filter { it.toString() == "row 2 column 1" }
    text21 shouldHaveSize 1

    val text22 = textGroups.filter { it.toString() == "row 2 column 2" }
    text22 shouldHaveSize 1

    text11[0].xIn(text12[0].xMid()) shouldBe false
    text11[0].yIn(text12[0].yMid()) shouldBe true

    text11[0].xIn(text21[0].xMid()) shouldBe true
    text11[0].yIn(text21[0].yMid()) shouldBe false

    text11[0].xIn(text22[0].xMid()) shouldBe false
    text11[0].yIn(text22[0].yMid()) shouldBe false
  }

  private fun text(
    pos: Pos,
    width: Float,
    height: Float,
    char: String,
  ): TextPosition {
    val mockPos = mockk<TextPosition>()
    every { mockPos.x } returns pos.x
    every { mockPos.y } returns pos.y
    every { mockPos.width } returns width
    every { mockPos.height } returns height
    every { mockPos.toString() } returns char
    return mockPos
  }

  data class Pos(val x: Float, val y: Float)
}
