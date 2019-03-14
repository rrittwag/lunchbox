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
  fun `WHEN findAll  THEN success`() {
    repo.offers = listOf(GYROS, SOLJANKA)

    val result = repo.findAll()

    assertThat(result).containsExactly(GYROS, SOLJANKA)
  }

  @Test
  fun `WHEN findByDay  THEN success`() {
    repo.offers = listOf(GYROS, SOLJANKA)

    val result = repo.findByDay(GYROS.day)

    assertThat(result).hasSize(1)
    assertThat(result).contains(GYROS)
  }

  @Test
  fun `WHEN findById  THEN success`() {
    repo.offers = listOf(GYROS, SOLJANKA)

    val result = repo.findByIdOrNull(GYROS.id)

    assertThat(result).isNotNull
    assertThat(result).isEqualTo(GYROS)
  }

  @Test
  fun `WHEN findById  THEN not found`() {
    val result = repo.findByIdOrNull(GYROS.id)

    assertThat(result).isNull()
  }

  @Test
  fun `WHEN deleteAll  THEN success`() {
    repo.offers = listOf(GYROS, SOLJANKA)

    repo.deleteAll()

    assertThat(repo.offers).isEmpty()
  }

  @Test
  fun `WHEN saveAll  THEN success`() {
    repo.saveAll(listOf(GYROS, SOLJANKA))

    assertThat(repo.offers).containsExactly(GYROS, SOLJANKA)
  }
}
