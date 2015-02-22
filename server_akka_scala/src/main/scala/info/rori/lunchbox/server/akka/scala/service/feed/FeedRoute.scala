package info.rori.lunchbox.server.akka.scala.service.feed

import akka.http.marshalling._
import info.rori.lunchbox.server.akka.scala.service.{HttpRoute, HttpXmlConversions}

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Node

object AtomFeedConversion extends HttpXmlConversions {
  implicit def defaultNodeSeqMarshaller(implicit ec: ExecutionContext): ToEntityMarshaller[Node] = atomFeedMarshaller
}

trait FeedRoute
  extends HttpRoute {

  import AtomFeedConversion._

  def feedRoute =
    logRequest(context.system.name) {
      path("feed") {
        get {
          complete {
            Future {
              createLunchOfferAtomFeed
            }
          }
        }
      }
    }

  def createLunchOfferAtomFeed: ToResponseMarshallable =
    <feed xmlns="http://www.w3.org/2005/Atom">
      <author>
        <name>Autor des Weblogs</name>
      </author>
      <title>Titel des Weblogs</title>
      <id>urn:uuid:60a76c80-d399-11d9-b93C-0003939e0af6</id>
      <updated>2003-12-14T10:20:09Z</updated>

      <entry>
        <title>Titel des Weblog-Eintrags</title>
        <link href="http://example.org/2003/12/13/atom-beispiel"/>
        <id>urn:uuid:1225c695-cfb8-4ebb-aaaa-80da344efa6a</id>
        <updated>2003-12-13T18:30:02Z</updated>
        <summary>Zusammenfassung des Weblog-Eintrags</summary>
        <content>Volltext des Weblog-Eintrags</content>
      </entry>
    </feed>

}
