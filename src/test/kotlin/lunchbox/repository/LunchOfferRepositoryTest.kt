package lunchbox.repository

import lunchbox.domain.models.GYROS
import lunchbox.domain.models.GYROS_NEXT_DAY
import lunchbox.domain.models.SOLJANKA
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LunchOfferRepositoryTest {

  private val repo = LunchOfferRepositoryImplInMemory()

  @BeforeEach
  fun beforeEach() {
    repo.offers.clear()
  }

  @Nested
  inner class FindAll {

    @Test
    fun success() {
      repo.offers += listOf(GYROS, SOLJANKA)

      val result = repo.findAll()

      result shouldContainSame listOf(GYROS, SOLJANKA)
    }
  }

  @Nested
  inner class FindByDay {

    @Test
    fun success() {
      repo.offers += listOf(GYROS, SOLJANKA)

      val result = repo.findByDay(GYROS.day)

      result shouldContainSame listOf(GYROS)
    }
  }

  @Nested
  inner class FindById {

    @Test
    fun success() {
      repo.offers += listOf(GYROS, SOLJANKA)

      val result = repo.findByIdOrNull(GYROS.id)

      result shouldBe GYROS
    }

    @Test
    fun `not found`() {
      val result = repo.findByIdOrNull(GYROS.id)

      result shouldBe null
    }
  }

  @Nested
  inner class DeleteBefore {

    @Test
    fun success() {
      repo.offers += listOf(GYROS, GYROS_NEXT_DAY)

      repo.deleteBefore(GYROS_NEXT_DAY.day)

      repo.offers shouldContainSame listOf(GYROS_NEXT_DAY)
    }
  }

  @Nested
  inner class DeleteFrom {

    @Test
    fun success() {
      repo.offers += listOf(GYROS, GYROS_NEXT_DAY)

      repo.deleteFrom(GYROS_NEXT_DAY.day, GYROS.provider)

      repo.offers shouldContainSame listOf(GYROS)
    }
  }

  @Nested
  inner class SaveAll {

    @Test
    fun `WHEN save 1 offer  THEN save and assign id`() {
      val result = repo.saveAll(listOf(GYROS))

      result shouldContainSame repo.offers
      repo.offers shouldContainSame listOf(GYROS.copy(id = 1))
    }

    @Test
    fun `WHEN save 2 offers  THEN save and assign unique id`() {
      val result = repo.saveAll(listOf(GYROS, SOLJANKA))

      result shouldContainSame repo.offers
      repo.offers shouldHaveSize 2
      repo.offers shouldContain GYROS.copy(id = 1)
      repo.offers shouldContain SOLJANKA.copy(id = 2)
    }

    @Test
    fun `WHEN save 1 offer while 1 existing  THEN add and assign unique id`() {
      repo.offers += listOf(GYROS.copy(id = 1))

      val result = repo.saveAll(listOf(SOLJANKA))

      result shouldContain SOLJANKA.copy(id = 2)
      repo.offers shouldHaveSize 2
      repo.offers shouldContain GYROS.copy(id = 1)
      repo.offers shouldContain SOLJANKA.copy(id = 2)
    }
  }
}
