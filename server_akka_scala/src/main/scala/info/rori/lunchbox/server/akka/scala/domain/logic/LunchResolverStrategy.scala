package info.rori.lunchbox.server.akka.scala.domain.logic

import info.rori.lunchbox.server.akka.scala.domain.model.LunchOffer

trait LunchResolverStrategy {
  def resolve: Seq[LunchOffer]
}

