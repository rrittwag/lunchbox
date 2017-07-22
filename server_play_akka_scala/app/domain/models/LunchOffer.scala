package domain.models

import org.joda.money.Money
import java.time.LocalDate
import scala.math.Ordered.orderingToOrdered
import util.PlayDateTimeHelper._

case class LunchOffer(
  id: Id,
  name: String,
  day: LocalDate,
  price: Money,
  provider: LunchProviderId) extends Ordered[LunchOffer] {

  def compare(that: LunchOffer): Int = (this.provider, this.day) compare (that.provider, that.day)
}