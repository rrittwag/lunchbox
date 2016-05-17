package util.feed

import java.net._

import org.joda.time.DateTime


case class AtomFeed(id: URI,
                    title: String,
                    selfLink: URL,
                    updated: DateTime,
                    entries: Seq[AtomFeedEntry]
                   )

case class AtomFeedEntry(id: URI,
                         title: String,
                         author: Author,
                         content: Content,
                         published: DateTime,
                         updated: DateTime
                        )

case class Content(contentType: String,
                   body: String
                  )

case class Author(name: String)
