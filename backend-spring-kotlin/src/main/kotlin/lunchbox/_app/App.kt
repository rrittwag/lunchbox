package lunchbox // Spring's component & entity resolving works best in root package

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class App

fun main(args: Array<String>) {
  @Suppress("SpreadOperator")
  runApplication<App>(*args)
}
