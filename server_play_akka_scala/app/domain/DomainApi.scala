package domain

import javax.inject.{Inject, Singleton}

import akka.actor.{ActorRef, ActorSystem}
import domain.services.{LunchOfferService, LunchProviderService}
import play.api._
import play.api.inject._

/**
 * API für Lunchbox-Domänen-Zugriff. Injected via DI.
 */
trait DomainApi {
  val lunchProviderService: ActorRef
  val lunchOfferService: ActorRef
}

/**
 * Default-Implementierung für Domain-API. Lässt sich in Unit-Tests leicht ersetzen.
 *
 * @param system Akka-System.
 */
@Singleton
class DefaultDomainApi @Inject() (system: ActorSystem) extends DomainApi {
  override val lunchProviderService = system.actorOf(LunchProviderService.props, LunchProviderService.Name)
  override val lunchOfferService = system.actorOf(LunchOfferService.props, LunchOfferService.Name)
}

/**
 * Über dieses Play-Modul werden Domänen-Klassen für das DI angemeldet, insbesondere die DomainApi.
 * Die DSL ist Play-eigen und unabhängig vom DI-Framework, allerdings nicht allzu mächtig.
 */
final class DomainModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration) = {
    Seq(bind[DomainApi].to[DefaultDomainApi].eagerly)
  }
}