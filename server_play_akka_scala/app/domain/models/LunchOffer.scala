package domain.models

import org.joda.money.Money
import org.joda.time.LocalDate


case class LunchOffer(
                       id: Id,
                       name: String,
                       day: LocalDate,
                       price: Money,
                       provider: LunchProviderId
                       ) extends Ordered[LunchOffer] {

  import scala.math.Ordered.orderingToOrdered
  def compare(that: LunchOffer): Int = (this.provider, this.day.toDate) compare(that.provider, that.day.toDate)
}