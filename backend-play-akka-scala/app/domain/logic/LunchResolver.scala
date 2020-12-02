package domain.logic

import domain.models.LunchOffer
import scala.concurrent.Future

trait LunchResolver {
  def resolve: Future[Seq[LunchOffer]]
}

