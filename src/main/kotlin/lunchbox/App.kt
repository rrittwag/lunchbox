package lunchbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication

@SpringBootApplication
class App

fun main(args: Array<String>) {
  SpringApplication.run(App::class.java, *args)
}
