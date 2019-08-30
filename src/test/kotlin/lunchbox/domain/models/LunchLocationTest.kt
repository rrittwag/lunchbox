package lunchbox.domain.models

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class LunchLocationTest {

  @Test
  fun `no duplicate labels`() {
    val labels = LunchLocation.values().map { it.label }
    Assertions.assertThat(labels).doesNotHaveDuplicates()
  }
}
