package lunchbox.domain.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LunchLocationTest {
  @Test
  fun `no duplicate labels`() {
    val labels = LunchLocation.entries.map { it.label }
    assertThat(labels).doesNotHaveDuplicates()
  }
}
