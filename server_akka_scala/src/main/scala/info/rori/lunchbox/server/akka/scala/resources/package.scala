package info.rori.lunchbox.server.akka.scala

import akka.http.server.Directives._

package object resources {
	def route = api.v1.route ~ feed.route
}