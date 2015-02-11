package info.rori.lunchbox.server.akka.scala.domain.model

import org.joda.money.Money
import org.joda.time.LocalDate


case class LunchOffer(
                       id: Id,
                       name: String,
                       day: LocalDate,
                       price: Money,
                       provider: LunchProviderId
                       )
