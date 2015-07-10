package info.rori.lunchbox.server.akka.scala.domain.logic

import info.rori.lunchbox.server.akka.scala.domain.model.LunchOffer

import scala.concurrent.Future

trait LunchResolver {
  def resolve: Future[Seq[LunchOffer]]
}

