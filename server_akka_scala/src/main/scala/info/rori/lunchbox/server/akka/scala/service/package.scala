package info.rori.lunchbox.server.akka.scala

import akka.http.server.Directives._

package object service {
	def httpRoute = api.v1.httpRoute ~ feed.httpRoute
}
