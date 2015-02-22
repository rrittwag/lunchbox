package info.rori.lunchbox.server.akka.scala.service.feed

import akka.http.marshalling._
import akka.http.marshalling.{ToEntityMarshaller, ToResponseMarshallable}
import akka.http.model.MediaTypes._
import info.rori.lunchbox.server.akka.scala.service.HttpRoute
import akka.http.model.ContentType

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Node

trait FeedRoute
  extends HttpRoute {

  //  import akka.http.marshallers.xml.ScalaXmlSupport
  //  implicit def atomFeedMarshaller = ScalaXmlSupport.nodeSeqMarshaller(`application/atom+xml`)
  implicit def atomFeedMarshaller(implicit ec: ExecutionContext): ToEntityMarshaller[Node] = {
    // different clients accept different types
    val atomFeedTypes = List(`application/atom+xml`, `application/xml`, `text/xml`)
    Marshaller.oneOf(atomFeedTypes.map(ContentType(_)).map(xmlNodeMarshaller): _*)
  }

  def xmlNodeMarshaller(contentType: ContentType)(implicit ec: ExecutionContext): ToEntityMarshaller[Node] =
    Marshaller.StringMarshaller.wrap(contentType) { rootNode: Node =>
      import xml.XML
      val writer = new java.io.StringWriter
      XML.write(writer, scala.xml.Utility.trim(rootNode), "utf-8", true, null)
      writer.toString
    }


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
