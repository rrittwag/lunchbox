package lunchbox.util.date

import lunchbox.domain.models.Bundesland.BERLIN
import lunchbox.domain.models.Bundesland.MECK_POMM
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.time.LocalDate
import java.time.Year

class FeiertageApiTest {

  private lateinit var mockWebServer: MockWebServer
  private lateinit var api: FeiertageApi

  @BeforeAll
  fun beforeAll() {
    mockWebServer = MockWebServer()
    mockWebServer.start()
    api = FeiertageApiImpl("http://localhost:${mockWebServer.port}")
  }

  @AfterAll
  fun afterAll() {
    mockWebServer.shutdown()
  }

  @Test
  fun `Feiertage aus 2023 in MV`() {
    mockWebServer.enqueue(
      mockResponse(javaClass.getResource("/feiertage/feiertage-2023-mv.json")!!.readText()),
    )

    val result = api.queryFeiertage(setOf(Year.of(2023)), setOf(MECK_POMM))

    result shouldHaveSize 11
    result shouldContain Feiertag(MECK_POMM, date("2023-01-01"), "Neujahrstag")
  }

  @Test
  fun `mehrere Jahre, mehrere Laender`() {
    mockWebServer.dispatcher = object : Dispatcher() {
      override fun dispatch(request: RecordedRequest) = when (request.path) {
        "/api/?jahr=2023&nur_land=MV" -> mockResponse("""{"Neujahrstag":{"datum":"2023-01-01"}}""")
        "/api/?jahr=2023&nur_land=BE" -> mockResponse("""{"Neujahrstag":{"datum":"2023-01-01"}}""")
        "/api/?jahr=2024&nur_land=MV" -> mockResponse("""{"Neujahrstag":{"datum":"2024-01-01"}}""")
        "/api/?jahr=2024&nur_land=BE" -> mockResponse("""{"Neujahrstag":{"datum":"2024-01-01"}}""")
        else -> MockResponse().setResponseCode(404)
      }
    }

    val result = api.queryFeiertage(setOf(Year.of(2023), Year.of(2024)), setOf(MECK_POMM, BERLIN))

    result shouldHaveSize 4
    result shouldContain Feiertag(MECK_POMM, date("2023-01-01"), "Neujahrstag")
    result shouldContain Feiertag(BERLIN, date("2023-01-01"), "Neujahrstag")
    result shouldContain Feiertag(MECK_POMM, date("2024-01-01"), "Neujahrstag")
    result shouldContain Feiertag(BERLIN, date("2024-01-01"), "Neujahrstag")
  }

  private fun date(dateStr: String): LocalDate = LocalDate.parse(dateStr)

  private fun mockResponse(content: String) = MockResponse()
    .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    .setBody(content)
}
