package lunchbox.util.string

import lunchbox.domain.resolvers.weekOf
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.time.LocalDate

class StringParserTest {

  @Test
  fun `parseMondayOfMostUsedWeek by PDF url`() {
    fun parse(lines: List<String>): LocalDate? = StringParser.parseMondayOfMostUsedWeek(lines)

    val yearNow = LocalDate.now().year

    parse(listOf("16.03.-20.03.2016")) shouldBeEqualTo weekOf("2016-03-20").monday
    parse(listOf("23.03.-27.03.")) shouldBeEqualTo weekOf("$yearNow-03-27").monday
    parse(listOf("16.03.-20.03.")) shouldBeEqualTo weekOf("$yearNow-03-20").monday
    parse(listOf("27.07.-31.07.2015 bla")) shouldBeEqualTo weekOf("2015-07-27").monday
  }
}
