package lunchbox.util.date

import java.time.LocalDate
import java.time.Month
import lunchbox.domain.models.LunchLocation
import lunchbox.domain.models.LunchLocation.BERLIN_SPRINGPFUHL
import lunchbox.domain.models.LunchLocation.NEUBRANDENBURG
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Test

class HolidayUtilTest {

  @Test
  fun `25 Dezember ist Feiertag`() {
    for (location in LunchLocation.values())
      HolidayUtil.isHoliday(LocalDate.of(2019, Month.DECEMBER, 25), location) shouldBe true
  }

  @Test
  fun `24 Dezember ist kein Feiertag`() {
    HolidayUtil.isHoliday(LocalDate.of(2019, Month.DECEMBER, 24), NEUBRANDENBURG) shouldBe false
  }

  @Test
  fun `Frauentag ist nur in Berlin Feiertag`() {
    HolidayUtil.isHoliday(LocalDate.of(2019, Month.MARCH, 8), NEUBRANDENBURG) shouldBe false
    HolidayUtil.isHoliday(LocalDate.of(2019, Month.MARCH, 8), BERLIN_SPRINGPFUHL) shouldBe true
  }
}
