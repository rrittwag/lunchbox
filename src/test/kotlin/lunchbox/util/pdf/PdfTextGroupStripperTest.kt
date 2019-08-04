package lunchbox.util.pdf

import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldHaveSize
import org.apache.pdfbox.text.TextPosition
import org.junit.jupiter.api.Test

class PdfTextGroupStripperTest {

  @Test
  fun `should equip TextGroup with one letter`() {
    val text_a = text(Pos(1.0f, 2.0f), 1.0f, 2.0f, "a")

    val group = TextGroup(listOf(text_a))

    group.toString() shouldBeEqualTo "a"

    group.xMin() shouldEqualTo 1.0f
    group.xMid() shouldEqualTo 1.5f
    group.xMax() shouldEqualTo 2.0f

    group.yMin() shouldEqualTo 2.0f
    group.yMid() shouldEqualTo 3.0f
    group.yMax() shouldEqualTo 4.0f

    group.width() shouldEqualTo 1.0f
    group.height() shouldEqualTo 2.0f

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
    val text_a = text(Pos(1.0f, 2.0f), 1.0f, 2.0f, "a")
    val text_b = text(Pos(2.0f, 2.0f), 1.0f, 2.0f, "b")
    val text_c = text(Pos(3.0f, 2.0f), 1.0f, 2.0f, "c")

    val group = TextGroup(listOf(text_a, text_b, text_c))

    group.toString() shouldBeEqualTo "abc"

    group.xMin() shouldEqualTo 1.0f
    group.xMax() shouldEqualTo 4.0f
    group.xMid() shouldEqualTo 2.5f

    group.yMin() shouldEqualTo 2.0f
    group.yMax() shouldEqualTo 4.0f
    group.yMid() shouldEqualTo 3.0f

    group.width() shouldEqualTo 3.0f
    group.height() shouldEqualTo 2.0f

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

    val text_11 = textGroups.filter { it.toString() == "row 1 column 1" }
    text_11 shouldHaveSize 1

    val text_12 = textGroups.filter { it.toString() == "row 1 column 2" }
    text_12 shouldHaveSize 1

    val text_21 = textGroups.filter { it.toString() == "row 2 column 1" }
    text_21 shouldHaveSize 1

    val text_22 = textGroups.filter { it.toString() == "row 2 column 2" }
    text_22 shouldHaveSize 1

    text_11[0].xIn(text_12[0].xMid()) shouldBe false
    text_11[0].yIn(text_12[0].yMid()) shouldBe true

    text_11[0].xIn(text_21[0].xMid()) shouldBe true
    text_11[0].yIn(text_21[0].yMid()) shouldBe false

    text_11[0].xIn(text_22[0].xMid()) shouldBe false
    text_11[0].yIn(text_22[0].yMid()) shouldBe false
  }

  private fun text(pos: Pos, width: Float, height: Float, char: String): TextPosition {
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
