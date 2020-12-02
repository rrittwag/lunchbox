package domain.util

import java.net.URL
import scala.xml.XML
import org.xml.sax.InputSource
import scala.xml.parsing.NoBindingFactoryAdapter
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import java.net.HttpURLConnection
import scala.xml.Node

object Html {
  lazy val adapter = new NoBindingFactoryAdapter
  lazy val parser = (new SAXFactoryImpl).newSAXParser

  def load(url: URL): Node = {
    val source = new InputSource(url.toString)
    adapter.loadXML(source, parser)
  }

  def load(url: URL, encoding: String): Node = {
    val source = new InputSource(url.toString)
    source.setEncoding(encoding)
    adapter.loadXML(source, parser)
  }

  def hasId(id: String): Node => Boolean =
    node => (node \@ "id") == id

  def hasClass(className: String): Node => Boolean =
    node => (node \@ "class").split(" ").contains(className)
}