package lunchbox.domain.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LunchProviderTest {
  @Test
  fun `no duplicate labels`() {
    val labels = LunchProvider.values().map { it.label }
    assertThat(labels).doesNotHaveDuplicates()
  }
}
