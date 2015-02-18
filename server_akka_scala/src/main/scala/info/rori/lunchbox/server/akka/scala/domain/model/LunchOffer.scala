package info.rori.lunchbox.server.akka.scala.domain.model

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
  def compare(that: LunchOffer): Int = (this.provider, this.price) compare(that.provider, that.price)
}