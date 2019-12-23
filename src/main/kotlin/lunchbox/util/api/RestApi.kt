package lunchbox.util.api

import org.springframework.web.bind.annotation.RestController

/**
 * Kurzer Alias für den irritierend benannten @RestController.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@RestController
annotation class RestApi
