package info.rori.lunchbox.server.akka.scala.domain.logic

import info.rori.lunchbox.server.akka.scala.domain.model.LunchOffer

trait LunchResolver {
  def resolve: Seq[LunchOffer]
}

