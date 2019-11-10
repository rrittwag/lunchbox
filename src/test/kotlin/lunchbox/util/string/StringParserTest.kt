package lunchbox.util.string

import java.time.LocalDate
import lunchbox.domain.resolvers.weekOf
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class StringParserTest {

  @Test
  fun `parseMondayOfMostUsedWeek by PDF url`() {
    fun parse(lines: List<String>): LocalDate? = StringParser.parseMondayOfMostUsedWeek(lines)

    val yearNow = LocalDate.now().year

    parse(listOf("16.03.-20.03.2016")) shouldEqual weekOf("2016-03-20").monday
    parse(listOf("23.03.-27.03.")) shouldEqual weekOf("$yearNow-03-27").monday
    parse(listOf("16.03.-20.03.")) shouldEqual weekOf("$yearNow-03-20").monday
    parse(listOf("27.07.-31.07.2015 bla")) shouldEqual weekOf("2015-07-27").monday
  }
}
