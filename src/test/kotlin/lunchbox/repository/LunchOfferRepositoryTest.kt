package lunchbox.repository

import lunchbox.domain.models.GYROS
import lunchbox.domain.models.SOLJANKA
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LunchOfferRepositoryTest {

  val repo = LunchOfferRepositoryImpl_InMemory()

  @BeforeEach
  fun beforeEach() {
    repo.offers = emptyList()
  }

  @Test
  fun `WHEN findAll  THEN get all`() {
    repo.offers = listOf(GYROS, SOLJANKA)

    val result = repo.findAll()

    assertThat(result).containsExactly(GYROS, SOLJANKA)
  }

  @Test
  fun `WHEN deleteAll  THEN delete all`() {
    repo.offers = listOf(GYROS, SOLJANKA)

    repo.deleteAll()

    assertThat(repo.offers).isEmpty()
  }

  @Test
  fun `WHEN saveAll  THEN save all`() {
    repo.saveAll(listOf(GYROS, SOLJANKA))

    assertThat(repo.offers).containsExactly(GYROS, SOLJANKA)
  }
}
