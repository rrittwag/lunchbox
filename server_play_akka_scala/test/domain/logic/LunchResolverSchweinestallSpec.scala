package domain.logic

import java.time.LocalDate

import domain.models.{LunchOffer, LunchProvider}
import org.joda.money.Money
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class LunchResolverSchweinestallSpec extends FlatSpec with Matchers with MockFactory {

  it should "resolve offers for week of 2015-02-09" in {
    val url = getClass.getResource("/mittagsplaene/schweinestall_2015-02-09.html")

    val offers = resolver.resolve(url)

    offers should have size 5
    offers should contain(LunchOffer(0, "Goldmakrelenfilet auf Gurken-Dillsauce mit Salzkartoffeln", date("2015-02-09"), euro("5.80"), Id))
    offers should contain(LunchOffer(0, "Pilzgulasch mit Salzkartoffeln", date("2015-02-10"), euro("4.80"), Id))
    offers should contain(LunchOffer(0, "Paniertes Hähnchenbrustfilet gefüllt mit Kräuterbutter, dazu Erbsen und Kartoffelpüree", date("2015-02-11"), euro("5.80"), Id))
    offers should contain(LunchOffer(0, "Kasselersteak mit Ananas und Käse überbacken, dazu Buttermöhren und Pommes frites", date("2015-02-12"), euro("5.80"), Id))
    offers should contain(LunchOffer(0, "Spaghetti \u0084Bolognese\u0093 mit Parmesan", date("2015-02-13"), euro("4.80"), Id))
  }

  private def resolver = {
    val validatorStub = stub[DateValidator]
    (validatorStub.isValid _).when(*).returning(true)
    new LunchResolverSchweinestall(validatorStub)
  }

  private val Id = LunchProvider.SCHWEINESTALL.id

  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)

  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")
}