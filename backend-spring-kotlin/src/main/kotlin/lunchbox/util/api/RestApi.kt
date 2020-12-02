package lunchbox.util.api

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import org.springframework.core.annotation.AliasFor
import org.springframework.web.bind.annotation.RestController

/**
 * Kurzer, sprechender Alias für @RestController.
 */
@Target(CLASS)
@Retention(RUNTIME)
@RestController
annotation class RestApi(

  @get:AliasFor(annotation = RestController::class)
  val value: String = ""

)
